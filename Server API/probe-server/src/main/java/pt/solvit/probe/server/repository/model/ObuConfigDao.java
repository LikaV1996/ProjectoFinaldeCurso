/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.model;

/**
 *
 * @author AnaRita
 */
public class ObuConfigDao {

    private long obuId;
    private long configId;
    private String properties;

    public ObuConfigDao() {
    }

    public ObuConfigDao(long obuId, long configId, String properties) {
        this.obuId = obuId;
        this.configId = configId;
        this.properties = properties;
    }

    public long getObuId() {
        return obuId;
    }

    public long getConfigId() {
        return configId;
    }

    public String getProperties() {
        return properties;
    }

    public void setObuId(long obuId) {
        this.obuId = obuId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
