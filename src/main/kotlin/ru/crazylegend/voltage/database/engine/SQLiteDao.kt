package ru.crazylegend.voltage.database.engine

import ru.crazylegend.voltage.database.AsyncAutoClosableDao
import java.io.File

class SQLiteDao<T, ID>(
    file: File,
    tableClass: Class<T>
) : AsyncAutoClosableDao<T, ID>(
    "jdbc:h2:${file.absoluteFile}", tableClass
)