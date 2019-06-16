/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import com.google.common.reflect.TypeToken;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.controller.model.input.controlconnection.NetworkInterface;
import pt.solvit.probe.server.model.Alarms;
import pt.solvit.probe.server.model.Location;
import pt.solvit.probe.server.model.ObuStatus;
import pt.solvit.probe.server.model.Storage;
import pt.solvit.probe.server.model.properties.LocationProperties;
import pt.solvit.probe.server.repository.model.ObuStatusDao;
import static pt.solvit.probe.server.util.ServerUtil.GSON;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import pt.solvit.probe.server.repository.api.IObuStatusRepository;
import pt.solvit.probe.server.service.api.IObuStatusService;

/**
 *
 * @author AnaRita
 */
@Service
public class ObuStatusService implements IObuStatusService {

    private static final Logger LOGGER = Logger.getLogger(ObuStatusService.class.getName());
    @Autowired
    private IObuStatusRepository obuStatusRepository;

    @Override
    public List<ObuStatus> getAllObuStatus(long obuId) {
        LOGGER.log(Level.INFO, "Finding all status from obu {0}", obuId);
        List<ObuStatusDao> obuStatusDaoList = obuStatusRepository.findByObuId(obuId);
        return ServiceUtil.map(obuStatusDaoList, this::transformToObuStatus);
    }

    @Override
    public List<Location> getAllObuLocations(long obuId) {
        LOGGER.log(Level.INFO, "Finding all status from obu {0}", obuId);
        List<ObuStatusDao> obuStatusDaoList = obuStatusRepository.findByObuId(obuId);
        List<ObuStatusDao> obuStatusDaoList_valid = new ArrayList();
        for (ObuStatusDao curObuStatusDao: obuStatusDaoList){
            if ( !(curObuStatusDao.getLat()==0.0 || curObuStatusDao.getLat()==null) ){
                obuStatusDaoList_valid.add(curObuStatusDao);
            }
        }
        return ServiceUtil.map(obuStatusDaoList_valid, this::transformToLocation);
    }

    @Override
    public ObuStatus getLastObuStatus(long obuId) {
        LOGGER.log(Level.INFO, "Finding last status from obu {0}", obuId);
        ObuStatusDao obuStatusDao = obuStatusRepository.findLastByObuId(obuId);
        return transformToObuStatus(obuStatusDao);
    }

    private ObuStatusDao transformToObuStatusDao(ObuStatus obuStatus, long obuId) {
        return new ObuStatusDao(null, obuId, Timestamp.valueOf(obuStatus.getLocalDateTime()), obuStatus.getLocation().getLat(), obuStatus.getLocation().getLon(),
                obuStatus.getLocation().getGroundSpeed(), obuStatus.getLocation().getLocationPropertiesString(),
                obuStatus.getStorage().getUsable(), obuStatus.getStorage().getFree(), obuStatus.getAlarms().getCritical(),
                obuStatus.getAlarms().getMajor(), obuStatus.getAlarms().getWarning(), 0.0, obuStatus.getNetworkInterfacesString());
    }

    private ObuStatus transformToObuStatus(ObuStatusDao obuStatusDao) {
        Location location = transformToLocation(obuStatusDao);

        Storage storage = new Storage(obuStatusDao.getFreeStorage(), obuStatusDao.getUsableStorage());

        Alarms alarms = new Alarms(obuStatusDao.getCriticalAlarms(), obuStatusDao.getMajorAlarms(), obuStatusDao.getWarningAlarms());

        List<NetworkInterface> networkInterfaces = GSON.fromJson(obuStatusDao.getNetworkInterfaces(), new TypeToken<List<NetworkInterface>>() {}.getType());

        Float temperature = (float)obuStatusDao.getTemperature();

        return new ObuStatus(obuStatusDao.getStatusDate().toLocalDateTime(), temperature, location, storage, alarms, networkInterfaces);
    }

    private Location transformToLocation(ObuStatusDao obuStatusDao) {
        LocationProperties locationProperties = GSON.fromJson(obuStatusDao.getLocationProperties(), LocationProperties.class);

        return locationProperties == null ?
                new Location(
                        obuStatusDao.getStatusDate().toString(), obuStatusDao.getLat(), obuStatusDao.getLon(),
                        null, null, obuStatusDao.getSpeed(),
                        null, null
                )
                :
                new Location(
                        locationProperties.getDate(), obuStatusDao.getLat(), obuStatusDao.getLon(),
                        locationProperties.getHeightAboveEllipsoid(), locationProperties.getHeightAboveMSL(), obuStatusDao.getSpeed(),
                        locationProperties.getHeading(), locationProperties.getGpsFix()
                );
    }
}
