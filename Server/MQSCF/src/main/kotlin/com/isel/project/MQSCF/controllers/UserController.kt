package com.isel.project.MQSCF.controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.MQSCF.config.ProtectedRoute
import com.isel.project.MQSCF.dao.ProbeuserDao
import com.isel.project.MQSCF.model.Probeuser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


data class UserFromBody @JsonCreator constructor( //Class for receiving body information about a new user
        val username: String?,
        val password: String?,
        val user_profile: String?,
        val properties: String?,
        val creator: String?,
        val suspended: Boolean? //TODO is it String???
)



//getting all users
data class ResponseGETUsers @JsonCreator constructor(val total : Int, val users : List<ProbeuserDao>)
//getting a user by it's ID
data class ResponseGETUserByID @JsonCreator constructor(val user : ProbeuserDao)
//creating a user
data class ResponsePOSTUser @JsonCreator constructor(val id : Int)
//updating a user
data class ResponsePUTUser @JsonCreator constructor(val user : ProbeuserDao)

@RestController
@RequestMapping(value = ["api/v1/"])
//@ProtectedRoute
class UserController(val user: Probeuser){

    @GetMapping(path = ["/users"])
    fun getUsers() : ResponseEntity<ResponseGETUsers> =
            user.getUsers()
                    .let {
                        ResponseEntity.ok().body(ResponseGETUsers(it.count(), it))
                    }


    @GetMapping(path = ["/user/{id}"])
    fun getUserByID(@PathVariable("id") id: Int) : ResponseEntity<ResponseGETUserByID> =
            user.getUserByID(id)
                    .let {
                        ResponseEntity.ok().body(ResponseGETUserByID(it))
                    }


    @PostMapping(path = ["/user"])  //TODO return user or id?
    fun createUser(@RequestBody value: UserFromBody) : ResponseEntity<ResponsePOSTUser> = //Create user
            user.createUser(
                value.username?:"",
                value.password?:"",
                value.user_profile?:"",
                "",
                value.creator?:"",
                value.suspended?:false
            ).let {

                return ResponseEntity.status(201).body(ResponsePOSTUser(it))
            }


    @PutMapping(path = ["/user/{id}"])
    fun suspendUser(@PathVariable("id") id: Int) : ResponseEntity<ResponsePUTUser> =
            user.suspendUser(id)
                    .let {
                        ResponseEntity.ok().body(ResponsePUTUser(it))
                    }




}