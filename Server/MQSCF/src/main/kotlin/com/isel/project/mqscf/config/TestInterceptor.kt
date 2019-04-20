package com.isel.project.mqscf.config

import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.model.Probeuser
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TestInterceptor(val user : Probeuser) : HandlerInterceptor {


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userID = request.getAttribute("userIDForTests")
        if(handler is HandlerMethod) {
            if(userID != null) {
                val probeuser = user.getUserByID( (userID as Int) )
                checkUserProfile(probeuser, handler)
            }
            /*
            val authHeader = request.getHeader("Authorization")?.split(" ")
            if (authHeader?.get(0) != "Basic") {
                throw JsonProblemException("You must provide a token to access that resource","token-error","no token",401,null,null)
            }
            val userID = verify(authHeader?.get(1))
            request.setAttribute("userID",userID)
            */
        }

        return true
    }

    private val user_profiles = arrayListOf("NORMAL_USER","SUPER_USER","ADMIN")
    fun checkUserProfile(probeuser: ProbeuserDao, handler: HandlerMethod) =
            probeuser
                .let {
                    if( ! user_profiles.contains(it.user_profile) )
                        throw Exception("User_Profile does not exist")  //API error
                    val lol = handler.method.annotations
                    if( ! (handler.method.isAnnotationPresent(AdminRoute::class.java) && it.user_profile == user_profiles[2]) ||
                           ! (handler.method.isAnnotationPresent(SuperUserRoute::class.java) && it.user_profile == user_profiles[1]) )
                        throw JsonProblemException("User does not have enough clearance to check this resource","user_profile-error","User low clearance",401,null, null)
                }

}