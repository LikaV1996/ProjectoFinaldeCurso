/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IFilesController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.logfiles.TestLog;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.logfiles.SysLog;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import pt.solvit.probe.server.service.api.IFileService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FilesController implements IFilesController {

    @Autowired
    private IFileService obuFilesService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<ObuFile>> getAllObuTestLogs(HttpServletRequest request,
                                                           @RequestHeader(value = "Authorization", required = true) String authorization,
                                                           @PathVariable("obu-id") long obuId) {

        //User user = (User) request.getAttribute("user");

        List<TestLog> testLogList = obuFilesService.getAllObuTestLogs(obuId);

        List<ObuFile> obuFileList = ServiceUtil.map(testLogList, this::transformToObuFile);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_TESTLOG, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuFileList);
    }

    @Override
    public ResponseEntity<String> getObuTestLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("test-log-id") long testLogId) {

        //User user = userService.checkUserCredentials(authorization);

        TestLog testLog = obuFilesService.getObuTestLog(obuId, testLogId);
        //Unzip test log data
        //String testLogStr = FilesUtils.unzipBytes(testLog.getLogData());
        String testLogStr = "";

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_TESTLOG_ID, obuId, testLogId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(testLogStr);
    }

    @Override
    public ResponseEntity<List<ObuFile>> getAllObuSysLogs(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        //User user = userService.checkUserCredentials(authorization);

        List<SysLog> sysLogList = obuFilesService.getAllObuSysLogs(obuId);

        List<ObuFile> obuFileList = ServiceUtil.map(sysLogList, this::transformToObuFile);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_SYSLOG, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuFileList);
    }

    @Override
    public ResponseEntity<String> getObuSysLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("sys-log-id") long sysLogId) {

        //User user = userService.checkUserCredentials(authorization);

        SysLog sysLog = obuFilesService.getObuSysLog(obuId, sysLogId);
        //Unzip system log data
        //String sysLogStr = FilesUtils.unzipBytes(sysLog.getLogData());
        String sysLogStr = "";

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_SYSLOG_ID, obuId, sysLogId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(sysLogStr);
    }

    private ObuFile transformToObuFile(TestLog testLog) {
        return (ObuFile) testLog;
    }

    private ObuFile transformToObuFile(SysLog sysLog) {
        return (ObuFile) sysLog;
    }
}
