package com.isel.project.mqscf.config

import com.isel.project.mqscf.model.Probeuser
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor(val user : Probeuser) : HandlerInterceptor {


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod /*&& handler.method.declaringClass.isAnnotationPresent(ProtectedRoute::class.java)*/) {
            val authHeader = request.getHeader("Authorization")?.split(" ")
            if (authHeader?.get(0) != "Basic") {
                throw JsonProblemException("You must provide a token to access that resource","token-error","no token",401,null,null)
            }
            val user = verify(authHeader?.get(1))
            request.setAttribute("userID",user.id)
            request.setAttribute("userName",user.user_name)
            //request.setAttribute("userProfile",user.user_profile)
        }

        return true
    }

    fun verify(token : String) =
            token
                .let {
                    String(Base64.getDecoder().decode(it)).split(":")
                }
                .also {
                    if(it.size != 2)
                        throw JsonProblemException("Token provided is not valid, Authentication must be Basic","token-error","Invalid token",401,null,null)
                }
                .let {
                    user.getAuthenticatedUser(it[0],it[1])
                }
                .also {
                    if(it.suspended)
                        throw JsonProblemException("User is suspended and cannot access any resources","user-suspended","User suspended",403,null,null)
                }


}