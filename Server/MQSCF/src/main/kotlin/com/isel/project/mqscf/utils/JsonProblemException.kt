package com.isel.project.mqscf.utils

import com.isel.project.mqscf.config.InvalidParamMessage
import java.lang.Exception

class JsonProblemException(
        detail: String?,
         type: String?,
         title: String?,
         val status : Int?,
         instance : String?,
         invalid_params :Array<InvalidParamMessage>?
 ) : Exception(){
  val map = HashMap<String,Any>()
  init {
   detail?.let {
    map.put("detail",it)
   }
   type?.let {
    map.put("type", it)
   }
   title?.let {
    map.put("title", it)
   }
   status?.let {
    map.put("status", it)
   }
   instance?.let {
    map.put("instance", it)
   }
   //if(!invalid_params.isNullOrEmpty())
    //map.put("invalid-params",invalid_params)
  }

 }