/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.properties.ObuConfigProperties;
import pt.solvit.probe.server.model.enums.CancelState;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
public class ObuConfig {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("obuId")
    private Long obuId;

    @JsonIgnore
    private Long configId;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("cancelState")
    private CancelState cancelState;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("stateList")
    private List<ConfigState> stateList;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("config")
    private Config config;

    public ObuConfig(Long obuId, Long configId, CancelState cancelState, List<ConfigState> stateList, Config config) {
        this.obuId = obuId;
        this.configId = configId;
        this.cancelState = cancelState;
        this.stateList = stateList;
        this.config = config;
    }

    public ObuConfig(long obuId, long configId) {
        this.obuId = obuId;
        this.configId = configId;
        this.cancelState = CancelState.NONE;
    }

    public long getObuId() {
        return obuId;
    }

    @JsonIgnore
    public long getConfigId() {
        return configId;
    }

    public List<ConfigState> getStateList() {
        return stateList;
    }

    public void setStateList(List<ConfigState> stateList) {
        this.stateList = stateList;
    }

    public CancelState getCancelState() {
        return cancelState;
    }

    public void setCancelState(CancelState cancelState) {
        this.cancelState = cancelState;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getPropertiesString() {
        return GSON.toJson(new ObuConfigProperties(this));
    }
}
