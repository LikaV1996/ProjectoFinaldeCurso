/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;


import com.mapbox.services.commons.models.Position;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import pt.solvit.probe.server.model.Location;
import pt.solvit.probe.server.model.ObuStatus;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IStatusController;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.api.IObuStatusService;

import javax.servlet.http.HttpServletRequest;


@RestController
public class StatusController implements IStatusController {

    @Autowired
    private IObuStatusService obuStatusService;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<List<ObuStatus>> getObuStatus(
            HttpServletRequest request,
            @PathVariable("obu-id") long obuId,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateLDT,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateLDT
        ) {

        User user = (User) request.getAttribute("user");

        List<ObuStatus> obuStatusList = obuStatusService.getAllObuStatus(obuId, endDateLDT, startDateLDT, user);

        return ResponseEntity.ok().body(obuStatusList);


        /*
        List<Location> locationList = obuStatusService.getAllObuLocations(obuId);
        LineString ls = LineString.fromCoordinates(transformToPosition(locationList));

        return ResponseEntity.ok().body(ls);
        */
    }

    private List<Position> transformToPosition(List<Location> locationList) {
        List<Position> positionList = new ArrayList();
        for (Location curLocation : locationList) {
            positionList.add(Position.fromCoordinates(curLocation.getLon(), curLocation.getLat()));
        }
        return positionList;
    }


/*
    private LocalDateTime parseStringToLocalDateTime(String datetime) {
        if (datetime.split("T").length > 1 || datetime.split(" ").length > 1)


        return ;
    }
    */
}
