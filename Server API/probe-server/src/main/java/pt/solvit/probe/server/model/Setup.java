/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.testplan.Scanning;
import pt.solvit.probe.server.model.properties.SetupProperties;
import pt.solvit.probe.server.model.enums.ModemType;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Setup", description = "Setup data tranfer object")
public class Setup extends CreatorModel {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("setupName")
    private String setupName;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("modemType")
    private ModemType modemType;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("scanning")
    private Scanning scanning;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("tests")
    private List<Test> tests;

    public Setup(Long id, String setupName, ModemType modemType, Scanning scanning, List<Test> tests, String creator, LocalDateTime creationDate, String modifier, LocalDateTime modifiedDate) {
        super(creator, creationDate, modifier, modifiedDate);
        this.id = id;
        this.setupName = setupName;
        this.modemType = modemType;
        this.scanning = scanning;
        this.tests = tests;
    }

    @ApiModelProperty(required = true, value = "Setup identifier")
    public Long getId() {
        return id;
    }

    public String getSetupName() {
        return setupName;
    }

    public void setSetupName(String setupName) {
        this.setupName = setupName;
    }

    @ApiModelProperty(required = true, value = "Modem type", allowableValues = "PLMN, GSMR")
    public ModemType getModemType() {
        return modemType;
    }

    public void setModemType(ModemType modemType) {
        this.modemType = modemType;
    }

    @ApiModelProperty(value = "Scanning properties")
    public Scanning getScanning() {
        return scanning;
    }

    public void setScanning(Scanning scanning) {
        this.scanning = scanning;
    }

    @ApiModelProperty(value = "Test list")
    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    @JsonIgnore
    public String getPropertiesString() {
        return GSON.toJson(new SetupProperties(this));
    }
}
