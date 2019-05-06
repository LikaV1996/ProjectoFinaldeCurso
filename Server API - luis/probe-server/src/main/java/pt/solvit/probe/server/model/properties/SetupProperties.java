/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.controller.model.input.testplan.Scanning;
import pt.solvit.probe.server.model.enums.ModemType;

/**
 *
 * @author AnaRita
 */
public class SetupProperties {

    private ModemType modemType;
    private Scanning scanning;

    public SetupProperties(Setup setup) {
        this.modemType = setup.getModemType();
        this.scanning = setup.getScanning();
    }

    public ModemType getModemType() {
        return modemType;
    }

    public Scanning getScanning() {
        return scanning;
    }
}
