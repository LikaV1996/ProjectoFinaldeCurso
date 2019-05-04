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
import pt.solvit.probe.server.controller.model.input.config.InputConfig;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.ObuConfig;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface IConfigurationController {

    @ApiOperation(value = "Returns partial representation of all configurations", tags = {"Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of configurations."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_CONFIG, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Config>> getAllConfigs(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization);

    @ApiOperation(value = "Creates a configuration", tags = {"Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "Configuration was created successfully. To get the created configuration, make a HTTP GET request to the URI provided in header location of this response."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_CONFIG, 
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> createConfig(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestBody InputConfig body);

    @ApiOperation(value = "Returns a configuration", tags = {"Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of configuration."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_CONFIG_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Config> getConfig(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("config-id") long configId);

    @ApiOperation(value = "Deletes a configuration", tags = {"Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The configuration was successfully deleted."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_CONFIG_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteConfig(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("config-id") long configId);

    @ApiOperation(value = "Returns partial representation of all configurations associated with an obu", tags = {"Obu Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of obu configurations."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_CONFIG,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuConfig>> getAllConfigsFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Removes all configurations associated to an obu", tags = {"Obu Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "All configurations were successfully removed from obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_OBU_CONFIG,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> removeAllConfigsFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Adds a configuration to an obu", tags = {"Obu Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The configuration was successfully added to obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_OBU_CONFIG_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> addConfigToObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId);

    @ApiOperation(value = "Returns a configuration from an obu", tags = {"Obu Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of obu configuration."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_CONFIG_ID
    )
    public ResponseEntity<ObuConfig> getConfigFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId);

    @ApiOperation(value = "Cancel configuration from an obu", tags = {"Obu Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The configuration was successfully canceled from obu."),
                @ApiResponse(code = 202, message = "A cancel request will be sent to obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH, 
            value = AppConfiguration.URL_OBU_CONFIG_ID_CANCEL,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> cancelConfigFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId);

    @ApiOperation(value = "Removes configuration from an obu", tags = {"Obu Configuration",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The configuration was successfully removed from obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_OBU_CONFIG_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> removeConfigFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId);
}