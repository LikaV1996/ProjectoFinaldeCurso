package com.isel.project.mqscf.controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.mqscf.config.AdminRoute
import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.model.Probeuser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


data class CreateUserFromBody @JsonCreator constructor( //Class for receiving body information about a new user
        val user_name: String?,
        val user_password: String?,
        val user_profile: String?,
        val properties: String?,
        val creator: String?,
        val suspended: Boolean? //TODO is it String???
)



//getting all user
data class ResponseGETUsers @JsonCreator constructor(val total : Int, val users : List<ProbeuserDao>)
//getting a user by it's ID
//data class ResponseGETUserByID @JsonCreator constructor(val user : ProbeuserDao)
//getting a user by it's Name
//data class ResponseGETUserByName @JsonCreator constructor(val user : ProbeuserDao)
//getting a user by it's ID or Name
data class ResponseGETUserByParam @JsonCreator constructor(val user : ProbeuserDao)
//creating a user
data class ResponsePOSTUser @JsonCreator constructor(val id : Int)
//updating a user
data class ResponsePUTUser @JsonCreator constructor(val user : ProbeuserDao)


@RestController
@RequestMapping(value = ["api/v1/"])
class UserController(val user: Probeuser){


    @GetMapping(path = ["/users"])
    fun getUsers() : ResponseEntity<ResponseGETUsers> =
            user.getUsers()
                    .let {
                        ResponseEntity.ok().body(ResponseGETUsers(it.count(),it))
                    }



    /*
    @GetMapping(path = ["/user/{id}"])
    fun getUserByID(@PathVariable("id") id: Int) : ResponseEntity<ResponseGETUserByID> =
            user.getUserByID(id)
                    .let {
                        ResponseEntity.ok().body(ResponseGETUserByID(it))
                    }

    @GetMapping(path = ["/user/{username}"])
    fun getUserByName(@PathVariable("username") user_name: String) : ResponseEntity<ResponseGETUserByName> =
            user.getLoggedInUser(user_name)
                    .let {
                        ResponseEntity.ok().body(ResponseGETUserByName(it))
                    }
    */

    @GetMapping(path = ["/user/{param}"])   //get user by ID or user_name (used afterlogging in)
    fun getUserByParam(@PathVariable("param") param: String) : ResponseEntity<ResponseGETUserByParam> {
        var ret : ProbeuserDao
        try {
            val id = param.toInt()
            // id case
            user.getUserByID(id)
                    .let {
                        ret = it
                    }
        } catch (e: NumberFormatException) {
            // username case
            val user_name = param
            user.getUserByName(user_name)
                    .let {
                        ret = it
                    }
        }
        catch (e: Exception){ throw e }

        return ResponseEntity.ok().body(ResponseGETUserByParam(ret))
    }



    @PostMapping(path = ["/users"])  //TODO return user or id ???
    fun createUser(@RequestBody value: CreateUserFromBody) : ResponseEntity<ResponsePOSTUser> = //Create user
            user.createUser(
                value.user_name?:"",
                value.user_password?:"",
                value.user_profile?:"",
                value.properties?:"",
                value.creator?:"",
                value.suspended?:false
            ).let {

                return ResponseEntity.status(200).body(ResponsePOSTUser(it))
            }


    @PutMapping(path = ["/user/{id}/suspend"])
    fun suspendUser(@PathVariable("id") id: Int) : ResponseEntity<ResponsePUTUser> =
            user.suspendUser(id)
                    .let {
                        ResponseEntity.ok().body(ResponsePUTUser(it))
                    }




}