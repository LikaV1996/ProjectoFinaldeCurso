package com.isel.project.MQSCF.controllers

import com.isel.project.MQSCF.model.Probeuser
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController{

    @GetMapping("/")
    fun blog(users: Probeuser): String{
        //model["title"] = "Blog" //equivalente a model.addAttribute("title", "Blog")
        return users.getUsers().toString()
    }

}

//CLASSE TESTE - nao faz nada!