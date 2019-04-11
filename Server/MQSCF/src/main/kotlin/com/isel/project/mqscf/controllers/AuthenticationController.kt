package com.isel.project.mqscf.controllers

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.mqscf.config.InvalidParamMessage
import com.isel.project.mqscf.model.Probeuser
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class LoginUserFromBody @JsonCreator constructor(val user_name: String?, val user_password: String?)

data class ResponsePOSTUserLogin @JsonCreator constructor(val token :String)

@RestController
@RequestMapping(value = ["api/v1/"])
class AuthenticationController(val user: Probeuser){


    @PostMapping(path = ["/login"])
    fun login(@RequestBody value: LoginUserFromBody) : ResponseEntity<ResponsePOSTUserLogin> {

        val invalidParams = ArrayList<InvalidParamMessage>()
        if (value.user_name.isNullOrEmpty())
            invalidParams.add(InvalidParamMessage("username", "username can't be null"))
        if (value.user_password.isNullOrEmpty())
            invalidParams.add(InvalidParamMessage("password", "password can't be null"))

        if (!invalidParams.isEmpty())
            throw JsonProblemException("the Credentials passed are not valid to login", "login-error", "Empty credentials", 400, null, invalidParams.toTypedArray())
        else
            user.login(value.user_name!!, value.user_password!!)
                    .let {
                        return ResponseEntity.status(200).body(ResponsePOSTUserLogin(it))
                    }
    }

    /*
    @PostMapping(path = ["/logout"])
    fun logout()
    */

}
