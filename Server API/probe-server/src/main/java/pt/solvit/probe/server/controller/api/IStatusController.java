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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.ObuStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author AnaRita
 */
public interface IStatusController {

    @ApiOperation(value = "Returns obu status", tags = {"Obu", "Status",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of obu status."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = AppConfiguration.URL_OBU_STATUS,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuStatus>> getObuStatus(
            HttpServletRequest request,
            @PathVariable("obu-id") long obuId,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateStr,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateStr
    );
}
