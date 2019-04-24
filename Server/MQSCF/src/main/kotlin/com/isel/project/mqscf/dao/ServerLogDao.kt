package com.isel.project.mqscf.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet
import java.sql.SQLException

class ServerLogDao @JsonCreator constructor (result: ResultSet) {

    val id: Int = result.getInt("id")
    val log_date: String = result.getString("log_date")
    val access_path: String = result.getString("access_path")
    val access_type: String = result.getString("access_type")
    val access_user: String = result.getString("access_user")
    val response_date: String? = result.getString("response_date")
    val status: String = result.getString("status")
    val detail: String = result.getString("detail")

}