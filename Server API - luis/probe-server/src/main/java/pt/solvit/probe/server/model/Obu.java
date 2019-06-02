/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;

import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.InputObu;
import pt.solvit.probe.server.controller.model.input.SimCard;
import pt.solvit.probe.server.model.enums.ObuState;
import pt.solvit.probe.server.model.properties.ObuProperties;
import pt.solvit.probe.server.repository.model.ObuDao;

import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Obu", description = "Obu data tranfer object")
public class Obu extends CreatorModel {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("hardwareId")
    private long hardwareId;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("obuState")
    private ObuState obuState;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("obuName")
    private String obuName;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("obuPassword")
    private String obuPassword;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("sims")
    private List<SimCard> sims;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("currentConfigId")
    private Long currentConfigId = null;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("currentTestPlanId")
    private Long currentTestPlanId = null;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("factoryConfig")
    private Config factoryConfig;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("authenticate")
    private boolean authenticate = true;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("uploadRequest")
    private boolean uploadRequest = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("clearAlarmsRequest")
    private boolean clearAlarmsRequest = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("resetRequest")
    private boolean resetRequest = false;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("shutdownRequest")
    private boolean shutdownRequest = false;

    public Obu(Long id, long hardwareId, ObuState obuState, String obuName, String obuPassword, List<SimCard> sims,
            Long currentConfigId, Long currentTestPlanId, Config factoryConfig,
            boolean authenticate, boolean uploadRequest, boolean clearAlarmsRequest, boolean resetRequest, boolean shutdownRequest,
            String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.hardwareId = hardwareId;
        this.obuState = obuState;
        this.obuName = obuName;
        this.obuPassword = obuPassword;
        this.sims = sims;

        this.currentConfigId = currentConfigId;
        this.currentTestPlanId = currentTestPlanId;
        this.factoryConfig = factoryConfig;

        this.authenticate = authenticate;
        this.uploadRequest = uploadRequest;
        this.clearAlarmsRequest = clearAlarmsRequest;
        this.resetRequest = resetRequest;
        this.shutdownRequest = shutdownRequest;
    }

    public Obu(Long id, long hardwareId, ObuState obuState, String obuName, String obuPassword, List<SimCard> sims,
               String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.hardwareId = hardwareId;
        this.obuState = obuState;
        this.obuName = obuName;
        this.obuPassword = obuPassword;
        this.sims = sims;
    }

    public Long getId() {
        return id;
    }

    public long getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(long hardwareId) {
        this.hardwareId = hardwareId;
    }

    public ObuState getObuState() {
        return obuState;
    }

    public void setObuState(ObuState obuState) {
        this.obuState = obuState;
    }

    public String getObuName() {
        return obuName;
    }

    public void setObuName(String obuName) {
        this.obuName = obuName;
    }

    public String getObuPassword() {
        return obuPassword;
    }

    public List<SimCard> getSims() {
        return sims;
    }

    public void setSims(List<SimCard> sims) {
        this.sims = sims;
    }

    public void setSimList(List<SimCard> sims) {
        this.sims = sims;
    }

    public Long getCurrentConfigId() {
        return currentConfigId;
    }

    public void setCurrentConfigId(Long currentConfigId) {
        this.currentConfigId = currentConfigId;
    }

    public Long getCurrentTestPlanId() {
        return currentTestPlanId;
    }

    public void setCurrentTestPlanId(Long currentTestPlanId) {
        this.currentTestPlanId = currentTestPlanId;
    }

    public Config getFactoryConfig() {
        return factoryConfig;
    }

    public void setFactoryConfig(Config factoryConfig) {
        this.factoryConfig = factoryConfig;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    public boolean isUploadRequest() {
        return uploadRequest;
    }

    public void setUploadRequest(boolean uploadRequest) {
        this.uploadRequest = uploadRequest;
    }

    public boolean isClearAlarmsRequest() {
        return clearAlarmsRequest;
    }

    public void setClearAlarmsRequest(boolean clearAlarmsRequest) {
        this.clearAlarmsRequest = clearAlarmsRequest;
    }

    public boolean isResetRequest() {
        return resetRequest;
    }

    public Obu setResetRequest(boolean resetRequest) {
        this.resetRequest = resetRequest;
        return this;
    }

    public boolean isShutdownRequest() {
        return shutdownRequest;
    }

    public Obu setShutdownRequest(boolean shutdownRequest) {
        this.shutdownRequest = shutdownRequest;
        return this;
    }

    @JsonIgnore
    public String getPropertiesString() {
        return GSON.toJson(new ObuProperties(this));
    }

    public static ObuDao transformToObuDao(Obu obu) {
        return new ObuDao(obu.getId(), obu.getHardwareId(), obu.getObuState().name(),
                obu.getCurrentConfigId(), obu.getCurrentTestPlanId(), obu.getObuName(), obu.getObuPassword(),
                obu.getPropertiesString(), obu.getCreator(), Timestamp.valueOf(obu.getCreationLocalDateTime()),
                obu.getModifier(), obu.getModifiedLocalDateTime()!= null ? Timestamp.valueOf(obu.getModifiedLocalDateTime()) : null);
    }

    public static Obu makeObuFromInput(InputObu inputObu, String creator/*, Hardware hardware*/) {
        long hardwareId = inputObu.getHardwareId();
        //String obuName = hardware.getSerialNumber();
        String obuPassword = generateObuPassword();
            //inputObu.getObuPassword();
        return new Obu(null, hardwareId, ObuState.READY, inputObu.getObuName(), obuPassword, inputObu.getSims(),
                creator, LocalDateTime.now(), null, null);
    }

    private static String generateObuPassword() {
        String generatedPassword = ControllerUtil.getAlphaNumeric(6);
        return new String(Base64.getEncoder().encode(generatedPassword.getBytes()), StandardCharsets.UTF_8);
    }
}
