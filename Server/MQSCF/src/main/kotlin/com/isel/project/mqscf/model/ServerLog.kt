package com.isel.project.mqscf.model

import com.isel.project.mqscf.dao.ServerLogDao
import com.isel.project.mqscf.data.DataSrc
import com.isel.project.mqscf.utils.JsonProblemException
import org.springframework.stereotype.Component
import java.util.ArrayList
import javax.servlet.http.HttpServletRequest

@Component
class ServerLog(private val db : DataSrc) {

    //Query's List
    private val getAllServerLogs = "SELECT * FROM serverlog"
    private val createServerLog = "INSERT INTO serverlog (log_date, access_path, access_type, access_user, response_date, status, detail)" +
                                            "VALUES (CURRENT_TIMESTAMP,"+"?,"  +  "USER,"    +    "?,"   +     "?,"     +  "?," +  "?)"
                                        //"VALUES (current_timestamp, '/api/v1/testpath', 'USER', 'tester', null, 'acess', 'test acess, DB inserted log')"


    fun getAllServerLogs(): ArrayList<ServerLogDao> =
            db.connection.prepareStatement(getAllServerLogs)
                    .let {
                        try {
                            it.executeQuery()
                        } finally {
                            it.connection.close()
                        }
                    }
                    .let {
                        val res = ArrayList<ServerLogDao>()
                        while (it.next())
                            res.add(ServerLogDao(it))
                        return res
                    }

    fun createServerLog(request: HttpServletRequest, logStatus: String, logMsg: String) =
        db.connection.prepareStatement(createServerLog)
                .also {
                    val userName = request.getAttribute("userName") as String
                    it.setString(1, request.requestURL.toString())
                    //it.setString(2, "USER")
                    it.setString(2, userName)
                    it.setObject(3,null)    //TODO check this
                    it.setString(4, logStatus)  //TODO what is this status?
                    it.setString(4, logMsg)
                }
                .let {
                    try {
                        it.execute()
                    }finally {
                        it.connection.close()
                    }
                }


}
