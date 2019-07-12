/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.logfiles;

import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.enums.FileType;

/**
 *
 * @author AnaRita
 */
public class SysLog extends ObuFile {

    private List<LogData> logData;

    public SysLog(Long id, long obuId, String fileName, LocalDateTime closeDate, LocalDateTime uploadDate, byte[] fileData, List<LogData> logData) {
        super(id, obuId, fileName, FileType.SYS_LOG, closeDate, uploadDate, fileData);
        this.logData = logData;
    }

    public List<LogData> getLogData() {
        return logData;
    }

    public void setLogData(List<LogData> logData) {
        this.logData = logData;
    }
}
