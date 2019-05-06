/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.enums.FileType;

/**
 *
 * @author AnaRita
 */
public class TestLog extends ObuFile {

    @JsonProperty("testPlanId")
    private Long testPlanId;
    @JsonProperty("setupId")
    private Long setupId;

    @JsonProperty("data")
    private List<LogData> logData;

    public TestLog(Long id, long obuId, String fileName, LocalDateTime closeDate, LocalDateTime uploadDate, byte[] fileData, long testPlanId, long setupId, List<LogData> logData) {
        super(id, obuId, fileName, FileType.TEST_LOG, closeDate, uploadDate, fileData);
        this.testPlanId = testPlanId;
        this.setupId = setupId;
        this.logData = logData;
    }

    public Long getTestPlanId() {
        return testPlanId;
    }

    public Long getSetupId() {
        return setupId;
    }

    public List<LogData> getLogData() {
        return logData;
    }

    public void setLogData(List<LogData> logData) {
        this.logData = logData;
    }
}
