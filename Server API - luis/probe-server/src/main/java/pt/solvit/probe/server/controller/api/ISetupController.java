/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.testplan.InputSetup;
import pt.solvit.probe.server.model.Setup;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface ISetupController {

    @ApiOperation(value = "Returns all setups", tags = {"Setup",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of setups."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_SETUP, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Setup>> getAllSetups(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization);

    @ApiOperation(value = "Creates a setup", tags = {"Setup",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "Setup was created successfully. To get the created configuration, make a HTTP GET request to the URI provided in header location of this response."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = AppConfiguration.URL_SETUP, 
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> createSetup(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestBody InputSetup body);

    @ApiOperation(value = "Returns a setup", tags = {"Setup",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of setup."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_SETUP_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Setup> getSetup(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("setup-id") long setupId);

    @ApiOperation(value = "Updates a setup", tags = {"Setup",})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Setup was updated successfully."),
                    @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            value = AppConfiguration.URL_SETUP,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Setup> updateSetup(HttpServletRequest request, @PathVariable("setup-id") long setupId, @RequestBody InputSetup body);

    @ApiOperation(value = "Deletes a setup", tags = {"Setup",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The setup was successfully deleted."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_SETUP_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteSetup(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("setup-id") long setupId);

}
