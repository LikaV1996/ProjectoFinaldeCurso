/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.ObuConfig;
import java.util.List;
import pt.solvit.probe.server.model.ConfigState;
import pt.solvit.probe.server.model.enums.CancelState;

/**
 *
 * @author AnaRita
 */
public class ObuConfigProperties {

    private CancelState cancelState;
    private List<ConfigState> stateList;

    public ObuConfigProperties(ObuConfig obuConfig) {
        this.cancelState = obuConfig.getCancelState();
        this.stateList = obuConfig.getStateList();
    }

    public CancelState getCancelState() {
        return cancelState;
    }

    public List<ConfigState> getStateList() {
        return stateList;
    }
}
