/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.logfiles.TestLog;
import pt.solvit.probe.server.model.logfiles.SysLog;

/**
 *
 * @author AnaRita
 */
public interface IFileService {

    public List<TestLog> getAllObuTestLogs(long obuId);

    public List<SysLog> getAllObuSysLogs(long obuId);

    public long addTestLogToObu(ObuFile obuFile);

    public long addSysLogToObu(ObuFile obuFile);

    public TestLog getObuTestLog(long obuId, long testLogId);

    public SysLog getObuSysLog(long obuId, long sysLogId);
}
