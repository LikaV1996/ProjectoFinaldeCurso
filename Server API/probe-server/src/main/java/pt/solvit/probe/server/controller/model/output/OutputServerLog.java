package pt.solvit.probe.server.controller.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.solvit.probe.server.model.ServerLog;

import java.util.List;

public class OutputServerLog {

    @JsonProperty("fullCount")
    private long fullCount;

    @JsonProperty("listCount")
    private int listCount;

    @JsonProperty("serverLogList")
    private List<ServerLog> serverLogs;

    public OutputServerLog(long fullCount, int listCount, List<ServerLog> serverLogs){
        this.fullCount = fullCount;
        this.listCount = listCount;
        this.serverLogs = serverLogs;
    }

    public long getFullCount() {
        return fullCount;
    }

    public int getListCount() {
        return listCount;
    }

    public List<ServerLog> getServerLogs() {
        return serverLogs;
    }


    public void setFullCount(long fullCount) {
        this.fullCount = fullCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public void setServerLogs(List<ServerLog> serverLogs) {
        this.serverLogs = serverLogs;
    }
}
