package com.isel.project.MQSCF.Model

import com.isel.project.MQSCF.DAO.ProbeuserDAO
import com.isel.project.MQSCF.Data.DataSrc
import org.springframework.stereotype.Component


@Component
class Probeuser(private val db : DataSrc){

    private val getUsersQuery= "SELECT * FROM probeuser"


    fun getUsers(): ArrayList<ProbeuserDAO> =
            db.connection.prepareStatement(getUsersQuery)
                    .let {
                        try {
                            it.executeQuery()
                        }finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        val res = ArrayList<ProbeuserDAO>()
                        while(it.next())
                            res.add(ProbeuserDAO(it))
                        return res
                    }

}