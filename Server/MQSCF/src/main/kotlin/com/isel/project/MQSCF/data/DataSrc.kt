package com.isel.project.MQSCF.data

import java.sql.Connection

interface DataSrc {
    val connection:Connection
}