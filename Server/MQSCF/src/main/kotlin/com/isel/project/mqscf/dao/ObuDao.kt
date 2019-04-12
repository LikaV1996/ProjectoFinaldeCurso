package com.isel.project.mqscf.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet

class ObuDao @JsonCreator constructor (result: ResultSet) {

    val id: Int = result.getInt("id")
    var hardware_id: Int = result.getInt("hardware_id")
    var obu_state: String = result.getString("obu_state")
    var current_config_id: Int? = result.getInt("current_config_id")
    var current_test_plan_id: Int? = result.getInt("current_test_plan_id")
    var obu_name: String = result.getString("obu_name")
    var obu_password: String = result.getString("obu_password") //Possivelmente não ficará aqui!
    var properties: String? = /*ObjectMapper*/(result.getString("properties"))   //verificar campo properties
    var creator: String = result.getString("creator")
    var creation_date: String = result.getString("creation_date")


}


