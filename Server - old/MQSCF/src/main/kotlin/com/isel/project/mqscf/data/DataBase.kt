package com.isel.project.mqscf.data

import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager

@Service
class DataBase(): DataSrc{

    val connectionString = System.getenv("CONNSTR")
    val password = System.getenv("DBPASSWORD")
    val userName = System.getenv("DBUSER")

    private fun createConnection(): Connection =
            Class.forName("org.postgresql.Driver")
                    .let {
                        DriverManager.getConnection(
                                connectionString,
                                userName,
                                password
                        )
                    }


    override val connection : Connection
        get() = createConnection()
}