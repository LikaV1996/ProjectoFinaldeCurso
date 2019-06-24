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
import pt.solvit.probe.server.config.AppConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface IDatabaseController {

    @ApiOperation(value = "Reset database", tags = {"Database",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The database was successfully reset."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = AppConfiguration.URL_RESET_DB, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> reset(HttpServletRequest request);

    @ApiOperation(value = "Factory reset database", tags = {"Database",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The database was successfully factory reset."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = AppConfiguration.URL_FACTORY_RESET_DB, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> factoryReset(HttpServletRequest request);
}
