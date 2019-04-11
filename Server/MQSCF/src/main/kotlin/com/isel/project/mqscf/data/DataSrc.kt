package com.isel.project.mqscf.data

import java.sql.Connection

interface DataSrc {
    val connection:Connection
}