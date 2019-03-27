package com.isel.project.MQSCF.Controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.MQSCF.DAO.ProbeuserDAO
import com.isel.project.MQSCF.Model.Probeuser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


data class UserFromBody @JsonCreator constructor( //Class for receiving body information about a new user
        val username: String?,
        val password: String?,
        val user_profile: String?
)

data class UserCreateResponse @JsonCreator constructor( //Class for sending id from new user
        val id: Int
)

@RestController
@RequestMapping(value = ["/api/v1/"])
class UsersController (val users : Probeuser){

    @GetMapping(path = ["/users"])
    fun getAll(): ResponseEntity<Any> { //Get all users
        return ResponseEntity.ok(users.getUsers())
    }

    @PostMapping(path = ["/user"])
    fun createUser(@RequestBody value: UserFromBody) : ResponseEntity<Any>{ //Create user

        val createdUserId: Int = users.createUser(
                value.username?:"",
                value.password?:"",
                value.user_profile?:""
        )

        return ResponseEntity.status(201).body(UserCreateResponse(createdUserId))
    }


}