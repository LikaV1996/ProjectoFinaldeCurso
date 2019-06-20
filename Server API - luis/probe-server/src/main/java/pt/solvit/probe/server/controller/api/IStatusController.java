/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.api;

import com.mapbox.services.commons.geojson.LineString;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.ObuStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @author AnaRita
 */
public interface IStatusController {

    @ApiOperation(value = "Returns obu position", tags = {"Obu Status",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of obu position."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_OBU_POSITION, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuStatus>> getObuPosition(HttpServletRequest request, @PathVariable("obu-id") long obuId);
}
