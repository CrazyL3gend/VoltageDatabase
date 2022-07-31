package ru.crazylegend.voltage.database.engine

import ru.crazylegend.voltage.database.AsyncAutoClosableDao

class MySQLDao<T, ID>(
    url: String,
    tableClass: Class<T>
) : AsyncAutoClosableDao<T, ID>(
    "jdbc:mysql://$url", tableClass
)