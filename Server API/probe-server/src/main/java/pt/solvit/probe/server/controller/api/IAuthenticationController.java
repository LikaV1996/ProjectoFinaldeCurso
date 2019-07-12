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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.model.input.InputLogin;
import pt.solvit.probe.server.model.Login;
import pt.solvit.probe.server.model.User;

import javax.servlet.http.HttpServletRequest;

//@RequestMapping(path = {"/backoffice"})
public interface IAuthenticationController {

    @ApiOperation(value = "Returns login token", tags = {"Login",})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A list with partial representation of users."),
                    @ApiResponse(code = 400, message = "There was an error with the credentials.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = AppConfiguration.URL_LOGIN,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Login> getLoginToken(HttpServletRequest request, @RequestBody InputLogin body);



    @ApiOperation(value = "Returns a user", tags = {"Login",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of user."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_GET_LOGGEDIN_USER,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> getLoggedInUser(HttpServletRequest request);

}
