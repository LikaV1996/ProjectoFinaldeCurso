package com.isel.project.MQSCF.Model

import com.isel.project.MQSCF.DAO.ProbeuserDAO
import com.isel.project.MQSCF.Data.DataSrc
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.sql.SQLException
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Component
class Probeuser(private val db : DataSrc){

    //Query's List
    private val getUsersQuery= "SELECT * FROM probeuser"
    private val createUserQuery = "INSERT INTO ProbeUser (user_name, user_password, user_profile, creator, creation_date) " +
            "VALUES (?,?,?,?,CURRENT_TIMESTAMP) returning id"


    fun getUsers(): ArrayList<ProbeuserDAO> {

        val statement = db.connection.prepareStatement(getUsersQuery)
        try {
            val resSet: ResultSet = statement.executeQuery()
            val res: ArrayList<ProbeuserDAO> = ArrayList<ProbeuserDAO>()
            while (resSet.next())
                res.add(ProbeuserDAO(resSet))
            return res
        } finally {
            statement.connection.close()
        }
    }


    fun createUser(username: String, password: String, user_profile: String) : Int{

        val statement = db.connection.prepareStatement(createUserQuery)
        statement.setString(1,username)
        statement.setString(2,password)
        statement.setString(3,user_profile)
        statement.setString(4,"ADMIN")

        try {
            val statement: ResultSet = statement.executeQuery()
            if(statement.next())
                return statement.getInt("id")
            return -1
        }catch (err: SQLException){
            listOf("duplicate key", "user_name", "already exists")
                    .parallelStream()
                    .allMatch { err.message?.contains(it)?: false }
                    .let {
                        if(it){
                            //return ResponseEntity.status(400).//body("Username already exists") -
                            //EM FALTA - O status code tem de serr 400 !!!! e está 500!
                            throw IllegalArgumentException("Username already exists")
                        }
                        else
                            throw Exception("An error as occured")
                    }
        } finally {
            statement.connection.close()
        }

    }




}
