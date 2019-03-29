package com.isel.project.MQSCF.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet

class UserDao @JsonCreator constructor (result: ResultSet){

    val id:Int = result.getInt("id")
    val username:String = result.getString("username")
}

