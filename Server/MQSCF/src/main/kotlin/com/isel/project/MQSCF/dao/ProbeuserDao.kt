package com.isel.project.MQSCF.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet

class ProbeuserDao @JsonCreator constructor (result: ResultSet) {

    val id: Int = result.getInt("id")
    var user_name: String = result.getString("user_name")
    var user_profile: String = result.getString("user_profile")
    var properties: String? = /*ObjectMapper*/(result.getString("properties"))   //verificar campo properties
    var creator: String = result.getString("creator")
    var creation_date: String = result.getString("creation_date")
    var suspended: Boolean = result.getBoolean("suspended")

}