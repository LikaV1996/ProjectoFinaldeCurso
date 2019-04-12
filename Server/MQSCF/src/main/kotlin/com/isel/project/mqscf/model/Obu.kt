package com.isel.project.mqscf.model

import com.isel.project.mqscf.dao.ObuDao
import com.isel.project.mqscf.dao.ProbeuserDao
import com.isel.project.mqscf.data.DataSrc
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class Obu(private val db : DataSrc) {

    //Query's List
    private val getObusQuery = "SELECT * FROM obu"
    private val getObuByIdQuery = "Select * FROM obu where id = ?"

    fun getObus(): ArrayList<ObuDao> =
            db.connection.prepareStatement(getObusQuery)
                    .let {
                        try {
                            it.executeQuery()
                        } finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        val res = ArrayList<ObuDao>()
                        while (it.next())
                            res.add(ObuDao(it))
                        return res
                    }

    fun getObuByID(id: Int) : ObuDao =
            db.connection.prepareStatement(getObuByIdQuery)
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
                            ObuDao(it)
                        else
                            throw JsonProblemException("User with id $id not found",null,"User doesn't exist",400,null, null)
                    }

}
