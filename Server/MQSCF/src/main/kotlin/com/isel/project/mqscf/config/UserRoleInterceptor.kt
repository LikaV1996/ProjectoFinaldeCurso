package com.isel.project.mqscf.config

import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.model.Probeuser
import com.isel.project.mqscf.model.ServerLog
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserRoleInterceptor(/*val user: Probeuser , val serverlog : ServerLog*/) : HandlerInterceptor {


    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: java.lang.Exception?) {
        if(handler is HandlerMethod) {
            //serverlog.createServerLog(request.requestURL.toString(), "something something bla bla")
            val resp = response
        }
    }


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            val userID = request.getAttribute("userID")
            if(handler is HandlerMethod) {
                val curUserProfile = user_profiles.indexOf( request.getAttribute("userProfile") as String )

                //check if method is reserved to admins or superusers
                if( (handler.hasMethodAnnotation(SuperUserRoute::class.java) && curUserProfile < 1) || //super_user and above
                        (handler.hasMethodAnnotation(AdminRoute::class.java) && curUserProfile < 2)){  //admin (and above)
                    throw JsonProblemException("User does not have enough clearance to use this resource","user_profile-error","User low clearance",403,null, null)
                }


                //canGetUserByID(curUserProfile, request)
            }

        }catch (e: Exception){
            throw e
        }

        return true
    }

    //--------------------------------------     0             1          2
    private val user_profiles = arrayListOf("NORMAL_USER","SUPER_USER","ADMIN")



    fun canGetUserByID(userProfile: Int, req: HttpServletRequest) : Boolean {   //checks if the user can make the request (normal_users can get themselves, but not others)
        if(userProfile <= 0) {   //only normal_users

            if (req.method == "GET" && req.requestURL.toString().contains("/api/v1/user/")) {   //getUserByParam

                val paramMap = req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, String>
                val param = paramMap.get("param")!!

                try {
                    val id = param.toInt()   // id case

                    if(req.getAttribute("userID") != id)
                        throw JsonProblemException("User can only get self", "get-normal-user-error", "Not allowed to get resource", 403, null, null)

                } catch (e: NumberFormatException) {
                    val username = param    // username case

                    if (req.getAttribute("userName") != username)
                        throw JsonProblemException("User can only get self", "get-normal-user-error", "Not allowed to get resource", 403, null, null)

                } catch (e: Exception) {
                    throw e
                }
            }
        }

        return true
    }

}