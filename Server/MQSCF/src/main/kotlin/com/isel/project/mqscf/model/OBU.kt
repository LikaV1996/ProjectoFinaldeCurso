package com.isel.project.mqscf.model

import com.isel.project.mqscf.dao.OBUDao
import com.isel.project.mqscf.data.DataSrc
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class OBU(private val db : DataSrc) {

    //Query's List
    private val getOBUsQuery = "SELECT * FROM obu"
    private val getOBUByIdQuery = "SELECT * FROM obu WHERE id = ?"

    fun getOBUs(): ArrayList<OBUDao> =
            db.connection.prepareStatement(getOBUsQuery)
                    .let {
                        try {
                            it.executeQuery()
                        } finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        val res = ArrayList<OBUDao>()
                        while (it.next())
                            res.add(OBUDao(it))
                        return res
                    }

    fun getOBUByID(id: Int) : OBUDao =
            db.connection.prepareStatement(getOBUByIdQuery)
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
                            OBUDao(it)
                        else
                            throw JsonProblemException("OBU with id $id not found",null,"OBU doesn't exist",400,null, null)
                    }

}
