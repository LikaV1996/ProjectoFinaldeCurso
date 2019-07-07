/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.logfiles.TestLog;
import pt.solvit.probe.server.model.logfiles.SysLog;

/**
 *
 * @author AnaRita
 */
public interface IFileService {

    public List<TestLog> getAllObuTestLogs(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit, User loggedInUser);
    public List<SysLog> getAllObuSysLogs(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit, User loggedInUser);

    public long addTestLogToObu(ObuFile obuFile);
    public long addSysLogToObu(ObuFile obuFile);

    public long getAllObuTestLogsEntries(long obuId, String filename, User loggedInUser);
    public long getAllObuSysLogsEntries(long obuId, String filename, User loggedInUser);

    public TestLog getObuTestLog(long obuId, long testLogId, User loggedInUser);

    public SysLog getObuSysLog(long obuId, long sysLogId, User loggedInUser);
}
