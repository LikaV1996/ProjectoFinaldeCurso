/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IFilesController;
import pt.solvit.probe.server.controller.model.output.OutputSysLog;
import pt.solvit.probe.server.controller.model.output.OutputTestLog;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.logfiles.TestLog;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.logfiles.SysLog;
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

    @Override
    public ResponseEntity<OutputTestLog> getAllObuTestLogs(
            HttpServletRequest request,
            @PathVariable("obu-id") long obuId,
            @RequestParam(value = "order", required = false) Boolean ascending,
            @RequestParam(value = "filename", required = false) String filename,
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "limit", required = false) Integer pageLimit
    ) {

        User user = (User) request.getAttribute("user");


        boolean asc = ascending == null ? false : ascending;
        String file_name = filename != null && filename.equals("") ? null : filename;


        long testLogEntries = obuFilesService.getAllObuTestLogsEntries(obuId, file_name, user);

        List<TestLog> testLogList = obuFilesService.getAllObuTestLogs(obuId, asc, file_name, pageNumber, pageLimit, user);

        return ResponseEntity.ok(new OutputTestLog(testLogEntries, testLogList.size(), testLogList));


        //List<ObuFile> obuFileList = ServiceUtil.map(testLogList, this::transformToObuFile);
        //return ResponseEntity.ok(obuFileList);
    }

    @Override
    public ResponseEntity<String> getObuTestLog(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-log-id") long testLogId) {

        User user = (User) request.getAttribute("user");

        TestLog testLog = obuFilesService.getObuTestLog(obuId, testLogId, user);
        //Unzip test log data
        //String testLogStr = FilesUtils.unzipBytes(testLog.getLogData());
        String testLogStr = "";


        return ResponseEntity.ok(testLogStr);
    }

    @Override
    public ResponseEntity<OutputSysLog> getAllObuSysLogs(
            HttpServletRequest request,
            @PathVariable("obu-id") long obuId,
            @RequestParam(value = "order", required = false) Boolean ascending,
            @RequestParam(value = "filename", required = false) String filename,
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "limit", required = false) Integer pageLimit
    ) {

        User user = (User) request.getAttribute("user");


        boolean asc = ascending == null ? false : ascending;
        String file_name = filename != null && filename.equals("") ? null : filename;


        long sysLogEntries = obuFilesService.getAllObuSysLogsEntries(obuId, file_name, user);

        List<SysLog> sysLogList = obuFilesService.getAllObuSysLogs(obuId, asc, file_name, pageNumber, pageLimit, user);

        return ResponseEntity.ok(new OutputSysLog(sysLogEntries, sysLogList.size(), sysLogList));


        //List<ObuFile> obuFileList = ServiceUtil.map(sysLogList, this::transformToObuFile);
        //return ResponseEntity.ok(obuFileList);
    }

    @Override
    public ResponseEntity<String> getObuSysLog(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("sys-log-id") long sysLogId) {

        User user = (User) request.getAttribute("user");

        SysLog sysLog = obuFilesService.getObuSysLog(obuId, sysLogId, user);
        //Unzip system log data
        //String sysLogStr = FilesUtils.unzipBytes(sysLog.getLogData());
        String sysLogStr = "";


        return ResponseEntity.ok(sysLogStr);
    }

    private ObuFile transformToObuFile(TestLog testLog) {
        return (ObuFile) testLog;
    }

    private ObuFile transformToObuFile(SysLog sysLog) {
        return (ObuFile) sysLog;
    }
}
