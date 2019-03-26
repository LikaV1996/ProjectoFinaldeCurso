package com.isel.project.MQSCF.Data

import java.sql.Connection

interface DataSrc {
    val connection:Connection
}