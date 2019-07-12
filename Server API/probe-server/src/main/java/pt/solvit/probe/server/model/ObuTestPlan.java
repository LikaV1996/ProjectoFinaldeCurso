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
import pt.solvit.probe.server.model.properties.ObuTestPlanProperties;
import pt.solvit.probe.server.model.enums.CancelState;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
public class ObuTestPlan {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("obuId")
    private Long obuId;

    @JsonIgnore
    @JsonProperty("testPlanId")
    private Long testPlanId;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("cancelState")
    private CancelState cancelState;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("stateList")
    private List<TestPlanState> stateList;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("testPlan")
    private TestPlan testPlan;

    public ObuTestPlan(Long obuId, Long testPlanId, CancelState cancelState, List<TestPlanState> stateList, TestPlan testPlan) {
        this.obuId = obuId;
        this.testPlanId = testPlanId;
        this.cancelState = cancelState;
        this.stateList = stateList;
        this.testPlan = testPlan;
    }

    public ObuTestPlan(long obuId, long testPlanId) {
        this.obuId = obuId;
        this.testPlanId = testPlanId;
        this.cancelState = CancelState.NONE;
    }

    public long getObuId() {
        return obuId;
    }

    @JsonIgnore
    public long getTestPlanId() {
        return testPlanId;
    }

    public List<TestPlanState> getStateList() {
        return stateList;
    }

    public void setStateList(List<TestPlanState> stateList) {
        this.stateList = stateList;
    }

    public CancelState getCancelState() {
        return cancelState;
    }

    public void setCancelState(CancelState cancelState) {
        this.cancelState = cancelState;
    }

    public TestPlan getTestPlan() {
        return testPlan;
    }

    public void setTestPlan(TestPlan testPlan) {
        this.testPlan = testPlan;
    }

    @JsonIgnore
    public String getPropertiesString() {
        return GSON.toJson(new ObuTestPlanProperties(this));
    }
}
