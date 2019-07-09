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
import pt.solvit.probe.server.controller.model.input.InputHardware;
import pt.solvit.probe.server.model.Hardware;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface IHardwareController {

    @ApiOperation(value = "Returns all hardware", tags = {"Hardware",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of hardware."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_HARDWARE, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Hardware>> getAllHardwares(HttpServletRequest request);

    @ApiOperation(value = "Creates a hardware", tags = {"Hardware",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "Hardware was created successfully. To get the created configuration, make a HTTP GET request to the URI provided in header location of this response."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = AppConfiguration.URL_HARDWARE, 
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Hardware> createHardware(HttpServletRequest request, @RequestBody InputHardware body);

    @ApiOperation(value = "Returns a hardware", tags = {"Hardware",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of hardware."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_HARDWARE_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Hardware> getHardware(HttpServletRequest request, @PathVariable("hardware-id") long hardwareId);

    @ApiOperation(value = "Deletes a hardware", tags = {"Hardware",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The hardware was successfully deleted."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_HARDWARE_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteHardware(HttpServletRequest request, @PathVariable("hardware-id") long hardwareId);


    @ApiOperation(value = "Updates a hardware", tags = {"Hardware",})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The hardware was successfully updated."),
                    @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                    @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            value = AppConfiguration.URL_HARDWARE_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Hardware> updateHardware(HttpServletRequest request, @RequestBody InputHardware body, @PathVariable("hardware-id") long hardwareId);

}
