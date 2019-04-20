package com.isel.project.mqscf.controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.mqscf.dao.OBUDao
import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.model.OBU
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


data class CreateOBUFromBody @JsonCreator constructor( //Class for receiving body information about a new obu
        val hardware_id: Int?,
        val obu_state: String?,
        val current_config_id: Int?,
        val current_test_plan_id: Int?,
        val obu_name: String?,
        val obu_password: String?,
        val properties: String?,
        val creator: String?
)



//getting all OBUs
data class ResponseGETOBUs @JsonCreator constructor(val total : Int, val obus : List<OBUDao>)
//getting a OBU by it's ID
data class ResponseGETOBUByID @JsonCreator constructor(val obu : OBUDao)
//creating a OBU
data class ResponsePOSTOBU @JsonCreator constructor(val id : Int)
//updating a OBU
data class ResponsePUTOBU @JsonCreator constructor(val obu : OBUDao)


@RestController
@RequestMapping(value = ["api/v1/"])
class OBUController(val obu: OBU){


    @GetMapping(path = ["/obus"])
    fun getOBUs() : ResponseEntity<ResponseGETOBUs> =
            obu.getOBUs()
                    .let {
                        ResponseEntity.ok().body(ResponseGETOBUs(it.count(),it))
                    }


    @GetMapping(path = ["/obu/{id}"])
    fun getOBUByID(@PathVariable("id") id: Int) : ResponseEntity<ResponseGETOBUByID> =
            obu.getOBUByID(id)
                    .let {
                        ResponseEntity.ok().body(ResponseGETOBUByID(it))
                    }

    //TODO create OBU
/*
    @PostMapping(path = ["/obus"])
    fun createOBU(@RequestBody value: CreateOBUFromBody) : ResponseEntity<ResponsePOSTOBU> = //Create OBU
            obu.createOBU(
                //value.user_name?:"",
                //value.user_password?:"",
                //value.user_profile?:"",
                //value.properties?:"",
                //value.creator?:"",
                //value.suspended?:false
            ).let {

                return ResponseEntity.status(200).body(ResponsePOSTUser(it))
            }
*/

}