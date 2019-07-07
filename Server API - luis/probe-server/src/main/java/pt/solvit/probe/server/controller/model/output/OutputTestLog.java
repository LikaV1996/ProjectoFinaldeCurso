package pt.solvit.probe.server.controller.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.solvit.probe.server.model.logfiles.TestLog;

import java.util.List;

public class OutputTestLog {

    @JsonProperty("fullCount")
    private long fullCount;

    @JsonProperty("listCount")
    private int listCount;

    @JsonProperty("testLogList")
    private List<TestLog> testLogList;

    public OutputTestLog(long fullCount, int listCount, List<TestLog> testLogList){
        this.fullCount = fullCount;
        this.listCount = listCount;
        this.testLogList = testLogList;
    }

    public long getFullCount() {
        return fullCount;
    }

    public int getListCount() {
        return listCount;
    }

    public List<TestLog> getTestLogList() {
        return testLogList;
    }


    public void setFullCount(long fullCount) {
        this.fullCount = fullCount;
    }

    public void setListCount(int listCount) {
        this.listCount = listCount;
    }

    public void setTestLogList(List<TestLog> testLogList) {
        this.testLogList = testLogList;
    }
}
