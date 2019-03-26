package com.isel.project.MQSCF.DAO

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet

class ProbeuserDAO @JsonCreator constructor (result: ResultSet){

    val id:Int = result.getInt("id")
    val userName:String = result.getString("user_name")
    val userProfile = result.getString("user_profile")

}