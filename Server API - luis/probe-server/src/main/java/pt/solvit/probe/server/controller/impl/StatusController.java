/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;


import com.mapbox.services.commons.models.Position;
import java.util.ArrayList;
import pt.solvit.probe.server.model.Location;
import pt.solvit.probe.server.model.ObuStatus;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IStatusController;
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
    public ResponseEntity<List<ObuStatus>> getObuPosition(HttpServletRequest request, @PathVariable("obu-id") long obuId) {


        List<ObuStatus> obuStatusList = obuStatusService.getAllObuStatus(obuId);

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
}
