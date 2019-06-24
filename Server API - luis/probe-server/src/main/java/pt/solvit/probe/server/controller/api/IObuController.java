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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.InputObu;
import pt.solvit.probe.server.controller.model.input.InputObuFlags;
import pt.solvit.probe.server.model.Obu;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface IObuController {

    @ApiOperation(value = "Returns all obus", tags = {"Obu",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of obus."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Obu>> getAllObus(HttpServletRequest request);

    @ApiOperation(value = "Creates a obu", tags = {"Obu",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "Obu was created successfully. To get the created obu, make a HTTP GET request to the URI provided in header location of this response."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_OBU, 
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Obu> createObu(HttpServletRequest request, @RequestBody InputObu body);

    @ApiOperation(value = "Returns a obu", tags = {"Obu",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Obu> getObuByID(HttpServletRequest request, @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Updates obu", tags = {"Obu",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The obu was successfully updated."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            value = AppConfiguration.URL_OBU_ID, 
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Obu> updateObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @RequestBody InputObu body);

    @ApiOperation(value = "Deletes a obu", tags = {"Obu",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The obu was successfully deleted."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_OBU_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteObu(HttpServletRequest request, @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Updates obu flags", tags = {"Obu",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The obu flags were successfully updated."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_OBU_FLAGS, 
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> updateObuFlags(HttpServletRequest request, @PathVariable("obu-id") long obuId, @RequestBody InputObuFlags body);
}
