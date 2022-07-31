package ru.crazylegend.voltage.database

import com.google.mu.function.CheckedConsumer
import com.google.mu.function.CheckedFunction
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.util.concurrent.CompletableFuture

open class AsyncAutoClosableDao<T, ID>(
    private val databaseUrl: String, private val tableClass: Class<T>
) : IAsyncAutoClosableDao<T, ID> {


    override fun createTable(): CompletableFuture<Int> = CompletableFuture.supplyAsync {
        applyAutoCloseableConnection { TableUtils.createTableIfNotExists(it, tableClass) }
    }

    override fun createDao(): CompletableFuture<Dao<T, ID>> = CompletableFuture.supplyAsync {
        applyAutoCloseableConnection { DaoManager.createDao(it, tableClass) }
    }

    override fun runDaoQuery(consumerDaoQuery: CheckedConsumer<Dao<T, ID>, Exception>): CompletableFuture<Void> =
        createDao()
            .thenAccept { dao -> consumerDaoQuery.accept(dao) }

    override fun <R> applyDaoQuery(functionDaoQuery: CheckedFunction<Dao<T, ID>, R, Exception>): CompletableFuture<R> =
        createDao()
            .thenApply { dao -> functionDaoQuery.apply(dao) }

    override fun runAutoCloseableConnection(consumerConnection: CheckedConsumer<ConnectionSource, Exception>) {
        applyAutoCloseableConnection { consumerConnection.accept(it) }
    }

    override fun <R> applyAutoCloseableConnection(functionConnection: CheckedFunction<ConnectionSource, R, Exception>): R {
        val exception: Throwable
        var connectionSource: ConnectionSource? = null

        try {
            connectionSource = getConnection()
            return functionConnection.apply(connectionSource)
        } catch (throwable: Throwable) {
            exception = throwable
        } finally {
            try {
                connectionSource?.close()
            } catch (exception: Exception) {
                throw RuntimeException(exception)
            }
        }
        throw RuntimeException(exception)
    }

    private fun getConnection(): ConnectionSource = JdbcConnectionSource(databaseUrl)
}