/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.model.enums.ObuState;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.model.ObuDao;
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.service.api.IObuService;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.EntityOwnershipException;
import pt.solvit.probe.server.service.exception.impl.ObuActiveException;
import pt.solvit.probe.server.service.exception.impl.PermissionException;

/**
 *
 * @author AnaRita
 */
@Service
public class ObuService implements IObuService {

    private static final Logger LOGGER = Logger.getLogger(ObuService.class.getName());

    @Autowired
    private IUserService userService;
    @Autowired
    private IObuRepository obuRepository;

    @Override
    public long createObu(Obu obu) {
        LOGGER.log(Level.INFO, "Creating new obu");
        return obuRepository.add(Obu.transformToObuDao(obu));
    }

    @Override
    public Obu getObu(long obuId) {
        LOGGER.log(Level.INFO, "Finding obu {0}", obuId);
        ObuDao obuDao = obuRepository.findById(obuId);
        return ObuDao.transformToObu(obuDao);
    }

    @Override
    public List<Obu> getObusWithHardware(long hardwareId) {
        LOGGER.log(Level.INFO, "Finding obus with hardware {0}", hardwareId);
        List<ObuDao> obuDaoList = obuRepository.findByHardwareId(hardwareId);
        return ServiceUtil.map(obuDaoList, ObuDao::transformToObu);
    }

    @Override
    public List<Obu> getAllObus() {
        LOGGER.log(Level.INFO, "Finding all obus");
        List<ObuDao> obuDaoList = obuRepository.findAll();
        return ServiceUtil.map(obuDaoList, ObuDao::transformToObu);
    }

    @Override
    public void updateObu(Obu obu) {
        obuRepository.update(Obu.transformToObuDao(obu));
    }

    @Override
    public void deleteObu(long obuId, User user) {
        LOGGER.log(Level.INFO, "Checking if obu {0} exists", obuId);
        ObuDao obuDao = obuRepository.findById(obuId);

        try {
            userService.checkLoggedInUserPermissions(user, UserProfile.SUPER_USER);
        } catch (PermissionException e) {
            userOwnsObu(obuDao, user);
        }

        if (!obuDao.getObuState().equals(ObuState.READY.name())) {
            throw new ObuActiveException();
        }

        LOGGER.log(Level.INFO, "Deleting obu {0}", obuId);
        obuRepository.deleteById(obuId);
    }

    private void userOwnsObu(ObuDao obuDao, User user) {
        LOGGER.log(Level.INFO, "Checking if user {0} owns obu {1}", new String[]{user.getUserName(), String.valueOf(obuDao.getId())});
        if (!obuDao.getCreator().equals(user.getUserName())) {
            throw new EntityOwnershipException(EntityType.OBU);
        }
    }




}
