package com.isel.project.mqscf.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet

class ProbeuserDao @JsonCreator constructor (result: ResultSet) {

    val id: Int = result.getInt("id")
    var user_name: String = result.getString("user_name")
    var user_profile: String = result.getString("user_profile")
    var properties: String? = /*ObjectMapper*/(result.getString("properties"))   //verificar campo properties
    var creator: String = result.getString("creator")
    var creation_date: String = result.getString("creation_date")

    var user_level: Int = result.getInt("user_level")
    var suspended: Boolean = result.getBoolean("suspended")
    var modifier: String? = result.getString("modifier")
    var modified_date: String? = result.getString("modified_date")

}