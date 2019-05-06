/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import java.util.List;
import pt.solvit.probe.server.model.Component;
import pt.solvit.probe.server.model.Hardware;

/**
 *
 * @author AnaRita
 */
public class HardwareProperties {

    private List<Component> components;

    public HardwareProperties(Hardware hardware) {
        this.components = hardware.getComponents();
    }

    public List<Component> getComponents() {
        return components;
    }
}
