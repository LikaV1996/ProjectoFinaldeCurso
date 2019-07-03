/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IConfigRepository;
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.repository.api.IServerLogRepository;
import pt.solvit.probe.server.repository.api.ISetupRepository;
import pt.solvit.probe.server.repository.api.ITestPlanRepository;
import pt.solvit.probe.server.service.api.IDatabaseService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.repository.api.IUserRepository;
import pt.solvit.probe.server.repository.api.IHardwareRepository;
import pt.solvit.probe.server.service.exception.impl.PermissionException;

/**
 *
 * @author AnaRita
 */
@Service
public class DatabaseService implements IDatabaseService {

    private static final Logger LOGGER = Logger.getLogger(DatabaseService.class.getName());

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private IUserService userService;
    @Autowired
    private IHardwareRepository hardwareRepository;
    @Autowired
    private IObuRepository obuRepository;
    @Autowired
    private IConfigRepository configRepository;
    @Autowired
    private ITestPlanRepository testPlanRepository;
    @Autowired
    private ISetupRepository setupRepository;
    @Autowired
    private IServerLogRepository serverLogRepository;
    @Autowired
    private IUserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetDb(User user) {
        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Reseting database");
        obuRepository.deleteAll();
        hardwareRepository.deleteAll();
        configRepository.deleteAll();
        testPlanRepository.deleteAll();
        setupRepository.deleteAll();
        serverLogRepository.deleteAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void factoryResetDb(User user) {
        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Factory reseting database");
        resetDb(user);
        userRepository.deleteAll();
    }
}
