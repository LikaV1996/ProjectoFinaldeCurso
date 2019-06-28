/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.model.Location;
import pt.solvit.probe.server.model.ObuStatus;

/**
 *
 * @author AnaRita
 */
public interface IObuStatusService {

    public List<ObuStatus> getAllObuStatus(long obuId, LocalDateTime endDateLDT, LocalDateTime startDateLDT);

    public List<Location> getAllObuLocations(long obuId);

    public ObuStatus getLastObuStatus(long obuId);
}
