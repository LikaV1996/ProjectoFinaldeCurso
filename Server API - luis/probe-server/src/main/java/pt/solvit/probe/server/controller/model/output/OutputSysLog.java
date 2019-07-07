package pt.solvit.probe.server.controller.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.solvit.probe.server.model.logfiles.SysLog;

import java.util.List;

public class OutputSysLog {

    @JsonProperty("fullCount")
    private long fullCount;

    @JsonProperty("listCount")
    private int listCount;

    @JsonProperty("sysLogList")
    private List<SysLog> sysLogList;

    public OutputSysLog(long fullCount, int listCount, List<SysLog> sysLogList){
        this.fullCount = fullCount;
        this.listCount = listCount;
        this.sysLogList = sysLogList;
    }

    public long getFullCount() {
        return fullCount;
    }

    public int getListCount() {
        return listCount;
    }

    public List<SysLog> getSysLogList() {
        return sysLogList;
    }


    public void setFullCount(long fullCount) {
        this.fullCount = fullCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public void setSysLogList(List<SysLog> sysLogList) {
        this.sysLogList = sysLogList;
    }
}
