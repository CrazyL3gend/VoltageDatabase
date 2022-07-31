package ru.crazylegend.voltage.database

import com.google.mu.function.CheckedConsumer
import com.google.mu.function.CheckedFunction
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.support.ConnectionSource
import java.sql.SQLException
import java.util.concurrent.CompletableFuture

interface IAsyncAutoClosableDao<T, ID> {

    fun executeRawQuery(sqlString: String): CompletableFuture<Void> =
        createDao().thenAcceptAsync { dao ->
            try {
                dao.executeRawNoArgs(sqlString)
            } catch (exception: SQLException) {
                throw RuntimeException(exception)
            }
        }

    fun createTable(): CompletableFuture<Int>

    fun createDao(): CompletableFuture<Dao<T, ID>>

    fun runDaoQuery(consumerDaoQuery: CheckedConsumer<Dao<T, ID>, Exception>): CompletableFuture<Void>

    fun <R> applyDaoQuery(functionDaoQuery: CheckedFunction<Dao<T, ID>, R, Exception>): CompletableFuture<R>

    fun runAutoCloseableConnection(consumerConnection: CheckedConsumer<ConnectionSource, Exception>)

    fun <R> applyAutoCloseableConnection(functionConnection: CheckedFunction<ConnectionSource, R, Exception>): R
}