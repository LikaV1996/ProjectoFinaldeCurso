package com.isel.project.mqscf.model

import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.data.DataSrc
import com.isel.project.mqscf.config.InvalidParamMessage
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import java.sql.SQLException
import java.sql.Types
import java.util.*


@Component
class Probeuser(private val db : DataSrc) {

    //Query's List

    //private val userInsertFields = "user_name, user_password, user_profile, properties, creator, creation_date, suspended"
    private val userFields = "id, user_name, user_password, user_profile, properties, creator, creation_date, modifier, modified_date, suspended"
    private val viewUserFields = "user_level"
    private val selectAllViewUserFields = "SELECT $userFields, $viewUserFields  FROM view_probeuser"
    //private val selectAllUserFields = "SELECT $userFields FROM probeuser"

    private val authenticateUserQuery = "$selectAllViewUserFields WHERE user_name = ? AND user_password = ?"
    private val getUsersQuery = selectAllViewUserFields
    private val getUserByIDQuery = "$selectAllViewUserFields WHERE id = ?"
    private val getUserByNameQuery = "$selectAllViewUserFields WHERE user_name = ?"
    private val createUserQuery = "INSERT INTO ProbeUser (user_name, user_password, user_profile, properties, creator, creation_date, suspended) " +
                                                "VALUES (?,"            +"?,"      +"'NORMAL_USER',"+"?::json,"+ "?," +"CURRENT_TIMESTAMP,"+"?) returning *"
    private val suspensionUserQuery = "UPDATE probeuser SET suspended = ?, modifier = ?, modified_date = CURRENT_TIMESTAMP WHERE id = ? returning id"
    private val changeRoleUserQuery = "UPDATE probeuser SET user_profile = ?, modifier = ?, modified_date = CURRENT_TIMESTAMP WHERE id = ? returning id"


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
                    /*
                    .also {
                        if(it.suspended)
                            throw JsonProblemException("User is suspended and can not login","user-suspended","User is suspended",403,null, null)
                    }
                    */
                    .let{
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
            db.connection.prepareStatement(getUserByIDQuery)
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

    fun getUserByName(user_name: String): ProbeuserDao =
            db.connection.prepareStatement(getUserByNameQuery)
                    .also {
                        it.setString(1,user_name)
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
                            throw JsonProblemException("User with user_name $user_name not found",null,"User doesn't exist",400,null, null)
                    }


    //TODO not tested
    fun getUserByParam(param: String): ProbeuserDao =
        try {
            //id case
            val id = param.toInt()
            db.connection.prepareStatement(getUserByIDQuery)
                    .also {
                        it.setInt(1,id)
                    }
        }
        catch (e: NumberFormatException) {
            // username case
            val user_name = param
            db.connection.prepareStatement(getUserByNameQuery)
                    .also {
                        it.setString(1, user_name)
                    }
        }
        catch (e: Exception){ throw e }
                .let {
                    try {
                        it.executeQuery()
                    } finally {
                        it.connection.close()
                    }
                }
                .let {
                    if (it.next())
                        ProbeuserDao(it)
                    else
                        throw JsonProblemException("User not found", null, "User doesn't exist", 400, null, null)
                }




    //TODO check how to insert nulls into postgreSQL
    fun createUser(user_name: String, user_password: String,/* user_profile: String,*/ properties: String?, creator: String, suspended: Boolean?) : ProbeuserDao =
            db.connection.prepareStatement(createUserQuery)
                    .also {
                        it.setString(1,user_name)
                        it.setString(2,user_password)
                        //it.setString(3,user_profile)
                        if (properties.isNullOrEmpty()) it.setObject(3,null) else it.setString(4,properties)
                        it.setString(4,creator)
                        //it.setDate(5,)    //current date
                        it.setBoolean(5, suspended != null && suspended)
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
                        ProbeuserDao(it)
                    }


    fun suspendUser(id: Int, modifierID: Int): ProbeuserDao {
        val user = getUserByID(id)
        val modifier = getUserByID(modifierID)

        if(user.user_level!! >= modifier.user_level!!)
            throw JsonProblemException("User cannot be suspended because he has a higher profile/role",null,"lower-role-error",403,null, null)

        db.connection.prepareStatement(suspensionUserQuery)
                .also {
                    it.setBoolean(1, !user.suspended)  //toggle
                    it.setString(2, modifier.user_name)

                    it.setInt(3, user.id)
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

                    throw JsonProblemException(/*"User with id $id not found"*/"Something went wrong",null,/*"User doesn't exist"*/"",400,null, null)
                }
    }



    fun changeRoleUser(id: Int, modifierID: Int): ProbeuserDao {
        val user = getUserByID(id)
        val modifier = getUserByID(modifierID)

        if(user.user_level!! >= modifier.user_level!! )
            throw JsonProblemException("User cannot have a role change because he has a higher profile/role",null,"lower-role-error",403,null, null)

        db.connection.prepareStatement(changeRoleUserQuery)
                .also {
                    it.setString(1, if(user.user_level == 0) "SUPER_USER" else "NORMAL_USER" )  //toggle
                    it.setString(2, modifier.user_name)

                    it.setInt(3, user.id)
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

                    throw JsonProblemException(/*"User with id $id not found"*/"Something went wrong",null,/*"User doesn't exist"*/"",400,null, null)
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
