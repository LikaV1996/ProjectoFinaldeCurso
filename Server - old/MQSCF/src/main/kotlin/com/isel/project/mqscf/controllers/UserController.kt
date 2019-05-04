package com.isel.project.mqscf.controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.mqscf.config.AdminRoute
import com.isel.project.mqscf.config.InvalidParamMessage
import com.isel.project.mqscf.config.SuperUserRoute
import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.model.Probeuser
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


data class CreateUserFromBody @JsonCreator constructor( //Class for receiving body information about a new user
        val user_name: String?,
        val user_password: String?,
        val user_profile: String?,
        val properties: String?,
        val creator: String?,
        val suspended: Boolean?
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
data class ResponsePOSTUser @JsonCreator constructor(val user : ProbeuserDao)
//updating a user
data class ResponsePUTUser @JsonCreator constructor(val user : ProbeuserDao)


@RestController
@RequestMapping(value = ["api/v1/"])
class UserController(val user: Probeuser){


    @SuperUserRoute
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
    fun getUserByParam(request: HttpServletRequest, @PathVariable("param") param: String) : ResponseEntity<ResponseGETUserByParam> =
            try {

                val id = param.toInt()

                // id case
                if (request.getAttribute("userProfile") == "NORMAL_USER" && request.getAttribute("userID") != id)
                    throw JsonProblemException("User can only get self", "get-normal-user-error", "Not allowed to get resource", 403, null, null)

                user.getUserByID(id)
                        .let {
                            return ResponseEntity.ok().body(ResponseGETUserByParam(it))
                        }

            } catch (e: NumberFormatException) {

                val username = param
                // username case
                if (request.getAttribute("userProfile") == "NORMAL_USER" && request.getAttribute("userName") != username)
                    throw JsonProblemException("User can only get self", "get-normal-user-error", "Not allowed to get resource", 403, null, null)

                user.getUserByName(username)
                        .let {
                            return ResponseEntity.ok().body(ResponseGETUserByParam(it))
                        }

            } catch (e: Exception) { throw e }


    @AdminRoute
    @PostMapping(path = ["/users"])
    fun createUser(request: HttpServletRequest, @RequestBody value: CreateUserFromBody) : ResponseEntity<ResponsePOSTUser> { //Create user
        val invalidParams = ArrayList<InvalidParamMessage>()
        if (value.user_name.isNullOrEmpty())
            invalidParams.add(InvalidParamMessage("user_name", "user_name can't be null"))
        if (value.user_password.isNullOrEmpty())
            invalidParams.add(InvalidParamMessage("user_password", "user_password can't be null"))
        /*
        if (value.user_profile.isNullOrEmpty())
            invalidParams.add(InvalidParamMessage("user_profile", "user_profile can't be null"))
        if (value.creator.isNullOrEmpty())
            invalidParams.add(InvalidParamMessage("creator", "creator can't be null"))
        if (value.suspended == null)
            invalidParams.add(InvalidParamMessage("suspended", "suspended can't be null"))
        */

        if (!invalidParams.isEmpty())
            throw JsonProblemException("The body passed is missing values", "create-user-error", "Empty body/Missing params", 400, null, invalidParams.toTypedArray())
        else {
            val userName = request.getAttribute("userName") as String

            user.createUser(
                    value.user_name!!,
                    value.user_password!!,
                    //value.user_profile!!,
                    value.properties,
                    userName,
                    value.suspended ?: false
            ).let {

                return ResponseEntity.status(200).body(ResponsePOSTUser(it))
            }
        }
    }


    @SuperUserRoute
    @PutMapping(path = ["/user/{id}/suspend"])
    fun suspendUser(request: HttpServletRequest, @PathVariable("id") id: Int) : ResponseEntity<ResponsePUTUser> =
            request.getAttribute("userID")
                    .let { it as Int }
                    .let {
                        user.suspendUser(id, modifierID = it)
                    }
                    .let {
                        return ResponseEntity.ok().body(ResponsePUTUser(it))
                    }


    @AdminRoute
    @PutMapping(path = ["/user/{id}/change-role"])  //toggle (for now at least) (Admin can only be achieved through DB insert)
    fun changeRoleUser(request: HttpServletRequest, @PathVariable("id") id: Int) : ResponseEntity<ResponsePUTUser> =
            request.getAttribute("userID")
                    .let { it as Int }
                    .let {
                        user.changeRoleUser(id, modifierID = it)
                    }
                    .let {
                        return ResponseEntity.ok().body(ResponsePUTUser(it))
                    }

}