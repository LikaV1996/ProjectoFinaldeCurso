/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.ObuFile;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface IFilesController {

    @ApiOperation(value = "Returns all obu test logs", tags = {"Files",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The obu with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_TESTLOG, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuFile>> getAllObuTestLogs(HttpServletRequest request, @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Returns obu test log", tags = {"Files",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The obu or test log with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_TESTLOG_ID
            //produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<String> getObuTestLog(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-log-id") long testLogId);

    @ApiOperation(value = "Returns all obu system logs", tags = {"Files",})
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_SYSLOG, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiResponses(
            value = {
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The obu with the requested id was not found.")
            }
    )
    public ResponseEntity<List<ObuFile>> getAllObuSysLogs(HttpServletRequest request, @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Returns obu system log", tags = {"Files",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The obu or system log with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_SYSLOG_ID
            //produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<String> getObuSysLog(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("sys-log-id") long sysLogId);
}
