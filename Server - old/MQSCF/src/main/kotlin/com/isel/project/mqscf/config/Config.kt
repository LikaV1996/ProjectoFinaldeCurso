package com.isel.project.mqscf.config

import com.fasterxml.jackson.annotation.JsonCreator
import com.isel.project.mqscf.utils.JsonProblemException
import com.isel.project.mqscf.model.Probeuser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebMvc
@ControllerAdvice

class Config : WebMvcConfigurer {

    @Autowired
    lateinit var user : Probeuser

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthInterceptor(user))
                .excludePathPatterns("/api/v1/login")

        registry.addInterceptor(UserRoleInterceptor())
                .excludePathPatterns("/api/v1/login")
                //.addPathPatterns("/api/v1/user/{param}")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedMethods("*")
    }

    @ExceptionHandler
    fun catchAllError(err : JsonProblemException) =
            sendError(err.map,err.status)


    @ExceptionHandler
    fun unknownError(e: Exception){
        return JsonProblemException("An error occurred on the server side",null,"Unknown error",500,null,null).let { sendError(it.map,it.status!!) }
    }


    fun sendError( map :Map<String,Any>, status: Int?) =
            ResponseEntity
                    .status(status!!)
                    .contentType(MediaType.parseMediaType("application/problem+json"))
                    .body(map)

}



data class InvalidParamMessage @JsonCreator constructor(val name : String, val reason: String)