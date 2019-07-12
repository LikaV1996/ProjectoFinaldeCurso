/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.model.output.OutputServerLog;
import pt.solvit.probe.server.model.ServerLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author AnaRita
 */
public interface IServerLogController {

    @ApiOperation(value = "Returns server log", tags = {"Logs", "Server"})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list of server log."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_SERVER_LOG
            //produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<OutputServerLog> getServerLogs(
            HttpServletRequest request,
            @RequestParam(value = "order", required = false) Boolean ascending,
            @RequestParam(value = "accessType", required = false) String accessType,
            @RequestParam(value = "accessor", required = false) String accessor,
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "limit", required = false) Integer pageLimit
    );

    @ApiOperation(value = "Returns obu server log", tags = {"Server Log",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A formatted list of obu server log."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_SERVER_LOG_OBU
            //produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<String> getObuServerLogs(HttpServletRequest request);

    @ApiOperation(value = "Returns users server log", tags = {"Server Log",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A formatted list of user server log."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_SERVER_LOG_USER
            //produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<String> getUserServerLogs(HttpServletRequest request);

    @ApiOperation(value = "Clear server log", tags = {"Server Log",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Server log was successfully deleted."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_SERVER_LOG, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> clearServerLog(HttpServletRequest request);
}
