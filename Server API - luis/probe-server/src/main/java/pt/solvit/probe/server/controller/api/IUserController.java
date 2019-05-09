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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
//@RequestMapping(path = {"/backoffice"})
public interface IUserController {

    @ApiOperation(value = "Returns all users", tags = {"User",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of users."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_GET_USERS,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request);

    @ApiOperation(value = "Returns a user", tags = {"User",})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Representation of user."),
                    @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                    @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_GET_USER_BY_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> getUser(HttpServletRequest request, @PathVariable("user-id") long userId);

    @ApiOperation(value = "Creates a user", tags = {"User",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "User was created successfully. To get the created configuration, make a HTTP GET request to the URI provided in header location of this response."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_CREATE_USER,
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<User> createUser(HttpServletRequest request, @RequestBody InputUser body);

    @ApiOperation(value = "Deletes a user", tags = {"User",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The user was successfully deleted."),
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The test plan with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = AppConfiguration.URL_GET_USER_BY_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteUser(HttpServletRequest request, @PathVariable("user-id") long userId);

}
