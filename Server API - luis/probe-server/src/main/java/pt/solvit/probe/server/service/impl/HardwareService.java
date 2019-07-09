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
import org.springframework.transaction.annotation.Transactional;
import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.model.HardwareDao;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import pt.solvit.probe.server.service.api.IHardwareService;
import pt.solvit.probe.server.repository.api.IHardwareRepository;
import pt.solvit.probe.server.service.api.IObuService;
import pt.solvit.probe.server.service.exception.impl.EntityOnUseException;

/**
 *
 * @author AnaRita
 */
@Service
public class HardwareService implements IHardwareService {

    private static final Logger LOGGER = Logger.getLogger(HardwareService.class.getName());

    @Autowired
    private IObuService obuService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IHardwareRepository hardwareRepository;

    @Override
    public long createHardware(Hardware hardware, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Creating new hardware");
        return hardwareRepository.add(Hardware.transformToHardwareDao(hardware));
    }

    @Override
    public Hardware getHardware(long hardwareId, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding hardware {0}", hardwareId);
        return HardwareDao.transformToHardware(hardwareRepository.findById(hardwareId));
    }

    @Override
    public Hardware getHardware(String serialNumber, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding hardware {0}", serialNumber);
        return HardwareDao.transformToHardware(hardwareRepository.findBySerialNumber(serialNumber));
    }

    @Override
    public List<Hardware> getAllHardware(User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding all hardware");
        List<HardwareDao> hardwareDaoList = hardwareRepository.findAll();
        return ServiceUtil.map(hardwareDaoList, HardwareDao::transformToHardware);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteHardware(long hardwareId, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Checking if hardware {0} exists", hardwareId);
        HardwareDao hardwareDao = hardwareRepository.findById(hardwareId);

        verifyHardwareOnUseCondition(hardwareId);

        LOGGER.log(Level.INFO, "Deleting hardware {0}", hardwareId);
        hardwareRepository.deleteById(hardwareId);
    }

    @Override
    public long updateHardware(Hardware hardware, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Updating hardware");
        return hardwareRepository.updateByID(Hardware.transformToHardwareDao(hardware));
    }

    private void verifyHardwareOnUseCondition(long hardwareId) {
        LOGGER.log(Level.INFO, "Checking if hardware is associated to any obu");
        List<Obu> obuList = obuService.getObusWithHardware(hardwareId);
        if (!obuList.isEmpty()) {
            throw new EntityOnUseException(EntityType.HARDWARE);
        }
    }



    private void checkUserPermissions(User loggedInUser) {
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();
    }

}
