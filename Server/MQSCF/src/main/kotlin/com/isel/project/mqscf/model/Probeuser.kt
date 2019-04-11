package com.isel.project.mqscf.model

import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.data.DataSrc
import com.isel.project.mqscf.config.InvalidParamMessage
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import java.sql.SQLException
import java.util.*


@Component
class Probeuser(private val db : DataSrc) {

    //Query's List

    private val userPKFields = "id"
    private val userFields = "user_name, user_password, user_profile, properties, creator, creation_date, suspended"
    private val allUserFields = "$userPKFields , $userFields"
    private val selectAllUserFields = "SELECT $allUserFields FROM probeuser"

    private val authenticateUserQuery = "$selectAllUserFields WHERE user_name = ? AND user_password = ?"
    private val getUsersQuery = selectAllUserFields
    private val getUserQuery = "$selectAllUserFields WHERE id = ?"
    private val createUserQuery = "INSERT INTO ProbeUser ($userFields) VALUES (?,?,?,?::json,?,CURRENT_TIMESTAMP,?) returning id"
    private val suspensionUserQuery = "UPDATE probeuser SET suspended = ? WHERE id = ? returning id"

    //private val authenticateUserQuery = "$selectAllUserFields where user_name = ? AND user_password = ?"

    fun getAuthenticatedUser(user_name: String, user_password: String): ProbeuserDao =
            db.connection.prepareStatement(authenticateUserQuery)
                    .also {
                        it.setString(1,user_name)
                        it.setString(2,user_password)
                    }
                    .let {
                        try {
                            it.executeQuery()
                        }finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        if(it.next())
                            ProbeuserDao(it)
                        else
                            throw JsonProblemException("The credentials passed do not correspond to any user, try to login again","credentials-error","User not valid",400,null, null)
                    }


    fun login(user_name: String, user_password: String) =
            getAuthenticatedUser(user_name, user_password)
                    .let {
                        "$user_name:$user_password".toByteArray()
                                .let {
                                    Base64.getEncoder().encodeToString(it)!!
                                }
                    }



    fun getUsers(): ArrayList<ProbeuserDao> =
            db.connection.prepareStatement(getUsersQuery)
                    .let {
                        try {
                            it.executeQuery()
                        } finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        val res = ArrayList<ProbeuserDao>()
                        while (it.next())
                            res.add(ProbeuserDao(it))
                        return res
                    }


    fun getUserByID(id: Int) : ProbeuserDao =
            db.connection.prepareStatement(getUserQuery)
                    .also {
                        it.setInt(1,id)
                    }
                    .let {
                        try {
                            it.executeQuery()
                        }finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        if(it.next())
                            ProbeuserDao(it)
                        else
                            throw JsonProblemException("User with id $id not found",null,"User doesn't exist",400,null, null)
                    }


    fun createUser(user_name: String, user_password: String, user_profile: String, properties: String, creator: String, suspended: Boolean?) : Int =
            db.connection.prepareStatement(createUserQuery)
                    .also {
                        it.setString(1,user_name)
                        it.setString(2,user_password)
                        it.setString(3,user_profile)
                        it.setString(4,properties)
                        it.setString(5,creator)
                        //it.setDate(6,)    //current date
                        it.setBoolean(6, if(suspended != null && suspended) true else false)
                    }
                    .let {
                        try {
                            it.executeQuery()
                            //sign(user_name,user_password)
                        }catch (err: SQLException) {
                            listOf("duplicate key", "user_name", "already exists")
                                    .parallelStream()
                                    .allMatch { err.message?.contains(it)?: false }
                                    .let {
                                        if(it){
                                            ArrayList<InvalidParamMessage>()
                                                    .also { it.add(InvalidParamMessage("username","username must be unique")) }
                                                    .let {
                                                        throw JsonProblemException("Username must be unique",null,"Registration failed",400,null, it.toTypedArray())
                                                    }
                                        }
                                        else
                                            throw err
                                    }
                        }finally {
                            it.connection.close()
                        }
                    }.let {
                        it.next()
                        return it.getInt("id")
                    }


    fun suspendUser(id: Int): ProbeuserDao {
        val user = getUserByID(id)
        db.connection.prepareStatement(suspensionUserQuery)
                .also {
                    it.setBoolean(1, if (!user.suspended) true else false)  //toggle
                    it.setInt(2, user.id)
                }.let {
                    try {
                        it.executeQuery()
                    } finally {
                        it.connection.close()
                    }
                }.let {
                    if (it.next()){
                        user.suspended = !user.suspended
                        return user
                    }

                    throw JsonProblemException("User with id $id not found",null,"User doesn't exist",400,null, null)
                }
    }







/*

    fun getAuthenticatedUser(userName: String,password: String): UserDao =
            db.connection.prepareStatement(authenticateUserQuery)
                    .also {
                        it.setString(1,userName)
                        it.setString(2,password)
                    }
                    .let {
                        try {
                            it.executeQuery()
                        }finally {
                            it.connection.close()
                        }
                    }
                    .also {
                        if(!it.next())
                            throw JsonProblemException("The credentials passed do not correspond to any user, try to login again",null,"User not valid",400,null, null)
                    }
                    .let {
                        UserDao(it)
                    }



    fun login(username: String,password: String) =
            db.connection
                    .prepareStatement(authenticateUserQuery)
                    .also {
                        it.setString(1,username)
                        it.setString(2,password)
                    }
                    .let {
                        try {
                            it.executeQuery()
                        }finally {
                            it.connection.close()
                        }
                    }
                    .also {
                        if(!it.next())
                            throw JsonProblemException("the Credentials passed are not valid, can't login",null,"Auth failed",400,null,null)
                    }
                    .let {
                        ProbeuserDao(it)
                    }
                    .let {
                        sign(username,password)

                    }

    private fun sign(username:String, password: String) =
            "$username:$password".toByteArray()
                    .let {
                        Base64.getEncoder().encodeToString(it)!!
                    }
*/

}
