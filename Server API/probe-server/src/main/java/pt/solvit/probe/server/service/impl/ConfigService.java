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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.ObuConfig;
import pt.solvit.probe.server.model.properties.ObuConfigProperties;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.repository.model.ConfigDao;
import pt.solvit.probe.server.repository.model.ObuConfigDao;
import pt.solvit.probe.server.model.enums.CancelState;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IConfigRepository;
import pt.solvit.probe.server.repository.api.IObuConfigRepository;
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.repository.exception.impl.AssociationAlreadyExistsException;
import pt.solvit.probe.server.service.api.IConfigService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.EntityOnUseException;
import pt.solvit.probe.server.service.exception.impl.EntityOwnershipException;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@Service
public class ConfigService implements IConfigService {

    private static final Logger LOGGER = Logger.getLogger(ConfigService.class.getName());

    @Autowired
    private IUserService userService;
    @Autowired
    private IConfigRepository configRepository;
    @Autowired
    private IObuRepository obuRepository;
    @Autowired
    private IObuConfigRepository obuConfigRepository;

    @Override
    public long createConfig(Config config, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Creating new configuration");
        return configRepository.add(Config.transformToConfigDao(config));
    }

    @Override
    public Config getConfig(long configId) {
        LOGGER.log(Level.INFO, "Finding configuration {0}", configId);
        ConfigDao configDao = configRepository.findById(configId);
        return ConfigDao.transformToConfig(configDao);
    }

    @Override
    public List<Config> getAllConfigs() {
        LOGGER.log(Level.INFO, "Finding all configurations");
        List<ConfigDao> configDaoList = configRepository.findAll();
        return ServiceUtil.map(configDaoList, ConfigDao::transformToConfig);
    }

    @Override
    public void deleteConfig(long configId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if configuration {0} exists", configId);
        ConfigDao configDao = configRepository.findById(configId);

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {    //not admin
            if ( userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) ) { //but is super_user
                userOwnsConfig(configDao, loggedInUser);
            }
            else throw new PermissionException();
        }

        verifyConfigOnUseCondition(configId, loggedInUser);

        LOGGER.log(Level.INFO, "Deleting configuration {0}", configId);
        configRepository.deleteById(configId);
    }

    @Override
    public void updateConfig(Config config, User loggedInUser) {
        LOGGER.log(Level.INFO, "Updating configuration {0}", config.getId());

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {    //not admin
            if ( userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) ) { //but is super_user
                userOwnsConfig(Config.transformToConfigDao(config), loggedInUser);
            }
            else throw new PermissionException();
        }


        configRepository.update(Config.transformToConfigDao(config));
    }

    @Override
    public ObuConfig getObuConfig(long obuId, long configId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding configuration {0} from obu {1}", new String[]{String.valueOf(configId), String.valueOf(obuId)});

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        ObuConfigDao obuConfigDao = obuConfigRepository.findById(obuId, configId, userID);
        return transformToObuConfig(obuConfigDao);
    }

    @Override
    public List<ObuConfig> getAllObuConfigs(long obuId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding all configurations from obu {0}", obuId);

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        List<ObuConfigDao> obuConfigDaoList = obuConfigRepository.findByObuId(obuId, userID);
        return ServiceUtil.map(obuConfigDaoList, this::transformToObuConfig);
    }

    @Override
    public void addConfigToObu(long obuId, long configId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if obu {0} exists", obuId);

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        obuRepository.findById(obuId, null);

        LOGGER.log(Level.INFO, "Checking if configuration {0} exists", configId);
        configRepository.findById(configId);

        LOGGER.log(Level.INFO, "Adding configuration {0} to obu {1}", new String[]{String.valueOf(configId), String.valueOf(obuId)});
        ObuConfigDao obuConfigDao = transformToObuConfigDao(new ObuConfig(obuId, configId));
        try {
            obuConfigRepository.addConfigToObu(obuConfigDao);
        } catch (DuplicateKeyException ex) {
            throw new AssociationAlreadyExistsException(EntityType.CONFIG, EntityType.OBU);
        }
    }

    @Override
    public boolean cancelConfigFromObu(long obuId, long configId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        ObuConfig obuConfig = getObuConfig(obuId, configId, loggedInUser);

        LOGGER.log(Level.INFO, "Canceling configuration {0} from obu {1}", new String[]{String.valueOf(configId), String.valueOf(obuId)});
        if (obuConfig.getStateList() == null) { //If configuration was not downloaded >> Canceled
            obuConfig.setCancelState(CancelState.CANCELED);
            updateObuConfig(obuConfig);
            LOGGER.log(Level.INFO, "Configuration successfully canceled from obu");

            return true;
        } else { //If configuration was already downloaded >> Cancel request
            obuConfig.setCancelState(CancelState.CANCEL_REQUEST);
            updateObuConfig(obuConfig);
            LOGGER.log(Level.INFO, "Cancel request will be sent to obu");

            return false;
        }
    }

    @Override
    public void removeConfigFromObu(long obuId, long configId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        ObuConfig obuConfig = getObuConfig(obuId, configId, loggedInUser);

        LOGGER.log(Level.INFO, "Removing configuration {0} from obu {1}", new String[]{String.valueOf(configId), String.valueOf(obuId)});
        if (obuConfig.getStateList() != null) { //If configuration was already downloaded
            throw new BadRequestException("Invalid operation.", "Configuration has already been downloaded by obu.", "/probs/config-already-downloaded", "about:blank");
        }
        obuConfigRepository.removeConfigFromObu(obuId, configId);
    }

    @Override
    public void removeAllConfigsFromObu(long obuId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Removing all configurations from obu {0}", obuId);
        obuConfigRepository.removeAllConfigsFromObu(obuId);
    }








    private void updateObuConfig(ObuConfig obuConfig) {
        LOGGER.log(Level.FINE, "Updating ObuConfig");
        obuConfigRepository.update(transformToObuConfigDao(obuConfig));
    }

    private void verifyConfigOnUseCondition(long configId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if configuration {0} is associated to any obu", configId);

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        List<ObuConfigDao> obuConfigDaoList = obuConfigRepository.findByConfigId(configId, userID);
        if (!obuConfigDaoList.isEmpty()) {
            throw new EntityOnUseException(EntityType.CONFIG);
        }
    }


    private void userOwnsConfig(ConfigDao configDao, User user) {
        LOGGER.log(Level.INFO, "Checking if user {0} owns configuration {1}", new String[]{user.getUserName(), String.valueOf(configDao.getId())});
        if (!configDao.getCreator().equals(user.getUserName())) {
            throw new EntityOwnershipException(EntityType.CONFIG);
        }
    }




    private ObuConfigDao transformToObuConfigDao(ObuConfig obuConfig) {
        return new ObuConfigDao(obuConfig.getObuId(), obuConfig.getConfigId(), obuConfig.getPropertiesString());
    }

    private ObuConfig transformToObuConfig(ObuConfigDao obuConfigDao) {
        Config config = getConfig(obuConfigDao.getConfigId());
        ObuConfigProperties properties = GSON.fromJson(obuConfigDao.getProperties(), ObuConfigProperties.class);

        return new ObuConfig(obuConfigDao.getObuId(), obuConfigDao.getConfigId(), properties.getCancelState(), properties.getStateList(), config);
    }

    private void checkUserPermissions(User loggedInUser) {
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();
    }

    private Long getUserIdIfNotAdmin(User loggedInUser){
        return userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ? null : loggedInUser.getId();
    }
}
