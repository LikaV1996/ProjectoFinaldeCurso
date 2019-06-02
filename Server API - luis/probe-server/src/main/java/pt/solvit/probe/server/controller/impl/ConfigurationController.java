/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IConfigurationController;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.config.InputConfig;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.ObuConfig;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IConfigService;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConfigurationController implements IConfigurationController {

    @Autowired
    private IConfigService configService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<Config>> getAllConfigs(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<Config> configList = configService.getAllConfigs();

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_CONFIG);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(configList);
    }

    @Override
    public ResponseEntity<Config> createConfig(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
                                             @RequestBody InputConfig body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Config config = Config.makeConfigFromInput(body, user.getUserName());
        long configId = configService.createConfig(config);

        config = configService.getConfig(configId);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_CONFIG_ID, configId);

        return ResponseEntity.created(createdURI).body(config);
    }

    @Override
    public ResponseEntity<Config> getConfig(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("config-id") long configId) {

        //User user = userService.checkUserCredentials(authorization);

        Config config = configService.getConfig(configId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_CONFIG_ID, configId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(config);
    }

    @Override
    public ResponseEntity<Void> deleteConfig(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("config-id") long configId) {

        User user = (User) request.getAttribute("user");

        configService.deleteConfig(configId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_CONFIG_ID, configId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ObuConfig>> getAllConfigsFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        //User user = userService.checkUserCredentials(authorization);

        List<ObuConfig> obuConfigList = configService.getAllObuConfigs(obuId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_CONFIG, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuConfigList);
    }

    @Override
    public ResponseEntity<Void> removeAllConfigsFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        //User user = userService.checkUserCredentials(authorization);

        configService.removeAllConfigsFromObu(obuId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_OBU_CONFIG, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addConfigToObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        //User user = userService.checkUserCredentials(authorization);

        configService.addConfigToObu(obuId, configId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.POST, HttpStatus.OK, AppConfiguration.URL_OBU_CONFIG_ID, obuId, configId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ObuConfig> getConfigFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        //User user = userService.checkUserCredentials(authorization);

        ObuConfig obuConfig = configService.getObuConfig(obuId, configId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_CONFIG_ID, obuId, configId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuConfig);
    }

    @Override
    public ResponseEntity<Void> cancelConfigFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        User user = (User) request.getAttribute("user");

        boolean result = configService.cancelConfigFromObu(obuId, configId);

        if (!result){
            //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.PATCH, HttpStatus.ACCEPTED, AppConfiguration.URL_OBU_CONFIG_ID_CANCEL, obuId, configId);
            //serverLogService.createServerLog(serverLog);

            return ResponseEntity.accepted().build();
        }
        
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.PATCH, HttpStatus.OK, AppConfiguration.URL_OBU_CONFIG_ID_CANCEL, obuId, configId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeConfigFromObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        //User user = userService.checkUserCredentials(authorization);

        configService.removeConfigFromObu(obuId, configId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user.getUserName(), RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_OBU_CONFIG_ID, obuId, configId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }
}
