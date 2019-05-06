/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.Obu;
import java.util.List;
import pt.solvit.probe.server.controller.model.input.SimCard;
import pt.solvit.probe.server.model.Config;

/**
 *
 * @author AnaRita
 */
public class ObuProperties {

    private Config factoryConfig;
    private List<SimCard> sims;
    private boolean authenticate = true;
    private boolean uploadRequest = false;
    private boolean clearAlarmsRequest = false;
    private boolean resetRequest = false;
    private boolean shutdownRequest = false;

    public ObuProperties(Obu obu) {
        this.factoryConfig = obu.getFactoryConfig();
        this.sims = obu.getSims();
        this.authenticate = obu.isAuthenticate();
        this.uploadRequest = obu.isUploadRequest();
        this.clearAlarmsRequest = obu.isClearAlarmsRequest();
        this.resetRequest = obu.isResetRequest();
        this.shutdownRequest = obu.isShutdownRequest();
    }

    public Config getFactoryConfig() {
        return factoryConfig;
    }

    public List<SimCard> getSims() {
        return sims;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public boolean isUploadRequest() {
        return uploadRequest;
    }

    public boolean isClearAlarmsRequest() {
        return clearAlarmsRequest;
    }

    public boolean isResetRequest() {
        return resetRequest;
    }

    public boolean isShutdownRequest() {
        return shutdownRequest;
    }
}
