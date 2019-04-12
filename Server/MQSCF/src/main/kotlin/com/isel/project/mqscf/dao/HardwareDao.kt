package com.isel.project.mqscf.dao

import com.fasterxml.jackson.annotation.JsonCreator
import java.sql.ResultSet

class HardwareDao @JsonCreator constructor (result: ResultSet) {

    val id: Int = result.getInt("id")
    var serial_number: String = result.getString("serial_number")
    var properties: String? = /*ObjectMapper*/(result.getString("properties"))
    var creator: String = result.getString("creator")
    var creation_date: String = result.getString("creation_date")

}
