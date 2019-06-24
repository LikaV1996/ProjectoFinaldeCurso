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
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IConfigurationController;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.config.InputConfig;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.ObuConfig;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IConfigService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ConfigurationController implements IConfigurationController {

    @Autowired
    private IConfigService configService;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<List<Config>> getAllConfigs(HttpServletRequest request) {


        List<Config> configList = configService.getAllConfigs();

        return ResponseEntity.ok(configList);
    }

    @Override
    public ResponseEntity<Config> createConfig(HttpServletRequest request, @RequestBody InputConfig body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Config config = Config.makeConfigFromInput(body, user.getUserName());
        long configId = configService.createConfig(config);

        config = configService.getConfig(configId);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_CONFIG_ID, configId);

        return ResponseEntity.created(createdURI).body(config);
    }

    @Override
    public ResponseEntity<Config> getConfig(HttpServletRequest request, @PathVariable("config-id") long configId) {

        Config config = configService.getConfig(configId);

        return ResponseEntity.ok(config);
    }

    @Override
    public ResponseEntity<Config> updateConfig(HttpServletRequest request, long configId, InputConfig body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Config config = configService.getConfig(configId);

        updateConfig(body, config, user.getUserName());

        configService.updateConfig(config);

        config = configService.getConfig(configId);

        return ResponseEntity.ok().body(config);
    }

    @Override
    public ResponseEntity<Void> deleteConfig(HttpServletRequest request, @PathVariable("config-id") long configId) {

        User user = (User) request.getAttribute("user");

        configService.deleteConfig(configId, user);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ObuConfig>> getAllConfigsFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId) {


        List<ObuConfig> obuConfigList = configService.getAllObuConfigs(obuId);


        return ResponseEntity.ok(obuConfigList);
    }

    @Override
    public ResponseEntity<Void> removeAllConfigsFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId) {

        configService.removeAllConfigsFromObu(obuId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addConfigToObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        //TODO which users can add configs? EDITORS? VIEWERS?
        configService.addConfigToObu(obuId, configId);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ObuConfig> getConfigFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {


        ObuConfig obuConfig = configService.getObuConfig(obuId, configId);


        return ResponseEntity.ok(obuConfig);
    }

    @Override
    public ResponseEntity<Void> cancelConfigFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        User user = (User) request.getAttribute("user");

        boolean result = configService.cancelConfigFromObu(obuId, configId);


        return !result ? ResponseEntity.accepted().build() : ResponseEntity.ok().build();

    }

    @Override
    public ResponseEntity<Void> removeConfigFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("config-id") long configId) {

        configService.removeConfigFromObu(obuId, configId);

        return ResponseEntity.ok().build();
    }


    private void updateConfig(InputConfig inputConfig, Config config, String modifier){
        config.setActivationDate( inputConfig.getActivationLocalDateTime() );
        config.setConfigName( inputConfig.getConfigName() );
        config.setTestPlan( inputConfig.getTestPlan() );
        config.setArchive( inputConfig.getArchive() );
        config.setControlConnection( inputConfig.getControlConnection() );
        config.setCore( inputConfig.getCore() );
        config.setData( inputConfig.getData() );
        config.setDownload( inputConfig.getDownload() );
        config.setScanning( inputConfig.getScanning() );
        config.setServer( inputConfig.getServer() );
        config.setUpload( inputConfig.getUpload() );
        config.setVoice( inputConfig.getVoice() );

        config.setModifier(modifier);
    }
}
