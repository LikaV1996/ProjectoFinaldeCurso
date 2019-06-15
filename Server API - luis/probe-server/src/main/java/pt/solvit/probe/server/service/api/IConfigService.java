/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.ObuConfig;
import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface IConfigService {

    public long createConfig(Config config);

    public Config getConfig(long configId);

    public List<Config> getAllConfigs();

    public void deleteConfig(long configId, User user);

    public void updateConfig(Config config);

    //Obu Configuration
    public ObuConfig getObuConfig(long obuId, long configId);

    public List<ObuConfig> getAllObuConfigs(long obuId);

    public void addConfigToObu(long obuId, long configId);

    public boolean cancelConfigFromObu(long obuId, long configId);

    public void removeConfigFromObu(long obuId, long configId);

    public void removeAllConfigsFromObu(long obuId);
}
