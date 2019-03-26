package com.isel.project.MQSCF.Controllers

import com.isel.project.MQSCF.Model.Probeuser
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.ui.Model
import org.springframework.ui.set

@Controller
class HtmlController{

    @GetMapping("/")
    fun blog(users: Probeuser): String{
        //model["title"] = "Blog" //equivalente a model.addAttribute("title", "Blog")
        return users.getUsers().toString()
    }

}