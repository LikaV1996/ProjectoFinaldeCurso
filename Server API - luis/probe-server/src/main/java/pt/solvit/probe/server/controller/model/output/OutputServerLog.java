package pt.solvit.probe.server.controller.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.solvit.probe.server.model.ServerLog;

import java.util.List;

public class OutputServerLog {

    @JsonProperty("listCount")
    private int listCount;

    @JsonProperty("serverLogList")
    private List<ServerLog> serverLogs;

    public OutputServerLog(int listCount, List<ServerLog> serverLogs){
        this.listCount = listCount;
        this.serverLogs = serverLogs;
    }


    public int getListCount() {
        return listCount;
    }

    public List<ServerLog> getServerLogs() {
        return serverLogs;
    }


    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public void setServerLogs(List<ServerLog> serverLogs) {
        this.serverLogs = serverLogs;
    }
}
