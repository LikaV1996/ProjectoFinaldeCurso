package com.isel.project.mqscf.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet
import java.sql.SQLException

class ProbeuserDao @JsonCreator constructor (result: ResultSet) {

    val id: Int = result.getInt("id")
    var user_name: String = result.getString("user_name")
    var user_profile: String = result.getString("user_profile")
    var properties: String? = /*ObjectMapper*/(result.getString("properties"))   //verificar campo properties
    var creator: String = result.getString("creator")
    var creation_date: String = result.getString("creation_date")

    var suspended: Boolean = result.getBoolean("suspended")
    var modifier: String? = result.getString("modifier")
    var modified_date: String? = result.getString("modified_date")

    var user_level: Int? =
            try{
                result.getInt("user_level")
            }catch (e : SQLException){
                listOf("column name", "user_level", "not found")
                        .parallelStream()
                        .allMatch { e.message?.contains(it)?: false }
                        .let {
                            if(it) { null }
                            else { throw e }
                        }
            }

}