package com.isel.project.MQSCF.Controllers

import com.isel.project.MQSCF.Model.Probeuser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/"])
class simplecontroller (val users : Probeuser){

    @GetMapping(path = ["/greetings"])
    fun xpto() = ResponseEntity.ok(users.getUsers())

}