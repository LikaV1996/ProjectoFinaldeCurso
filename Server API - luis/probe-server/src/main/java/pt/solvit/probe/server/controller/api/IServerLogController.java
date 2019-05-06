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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface IServerLogController {

    @ApiOperation(value = "Returns server log", tags = {"Server Log",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A formatted list of server log."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_SERVER_LOG
            //produces = {MediaType.TEXT_PLAIN_VALUE}
    )
    public ResponseEntity<String> getServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization);

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
    public ResponseEntity<String> getObuServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization);

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
    public ResponseEntity<String> getUserServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization);

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
    public ResponseEntity<Void> clearServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization);
}
