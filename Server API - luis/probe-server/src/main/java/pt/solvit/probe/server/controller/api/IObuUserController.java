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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.model.input.InputObuUser;
import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.ObuUser;
import pt.solvit.probe.server.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


//@RequestMapping(path = {"/backoffice"})
public interface IObuUserController {

    @ApiOperation(value = "Returns all obu_user registries", tags = {"User", "Obu"})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with obu_users."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_GET_OBU_USERS_BY_USER_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuUser>> getAllObuUserRegistries(HttpServletRequest request,  @PathVariable("user-id") long userId);

    @ApiOperation(value = "Returns a obu_user registry", tags = {"User", "Obu"})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Representation of obu_user."),
                    @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                    @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_GET_OBU_USERS,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuUser>> getObuUserRegistries(HttpServletRequest request);

    @ApiOperation(value = "Creates a obu_user registry", tags = {"User", "Obu"})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "Obu_user was registered successfully."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_CREATE_OBU_USER,
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ObuUser> createObuUserRegistry(HttpServletRequest request, @RequestBody InputObuUser body);


    @ApiOperation(value = "Updates a obu_user", tags = {"User", "Obu"})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The Obu_user registry was successfully updated."),
                    @ApiResponse(code = 401, message = "There was an error with authentication."),
                    @ApiResponse(code = 404, message = "The user with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            value = AppConfiguration.URL_UPDATE_OBU_USER,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ObuUser> updateObuUserRegistry(HttpServletRequest request, @PathVariable("user-id") long userId, @PathVariable("obu-id") long obuId, @RequestBody InputObuUser body);


    @ApiOperation(value = "Deletes a obu_user registry", tags = {"User", "Obu"})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The user was successfully deleted."),
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The user with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = AppConfiguration.URL_DELETE_OBU_USER,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteObuUserRegistry(HttpServletRequest request, @PathVariable("user-id") long userId, @PathVariable("obu-id") long obuId);

}
