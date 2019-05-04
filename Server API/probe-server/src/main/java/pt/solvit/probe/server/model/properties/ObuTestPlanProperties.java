/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.TestPlanState;
import pt.solvit.probe.server.model.ObuTestPlan;
import java.util.List;
import pt.solvit.probe.server.model.enums.CancelState;

/**
 *
 * @author AnaRita
 */
public class ObuTestPlanProperties {

    private CancelState cancelState;
    private List<TestPlanState> stateList;

    public ObuTestPlanProperties(ObuTestPlan obuTestPlan) {
        this.cancelState = obuTestPlan.getCancelState();
        this.stateList = obuTestPlan.getStateList();
    }

    public CancelState getCancelState() {
        return cancelState;
    }

    public List<TestPlanState> getStateList() {
        return stateList;
    }
}
