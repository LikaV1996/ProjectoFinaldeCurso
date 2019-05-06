/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.enums;

/**
 *
 * @author AnaRita
 */
public enum ComponentType {
    MODEM, GPS, LEDS, POWER_SUPPLY, MOTHERBOARD, COMPUTER, POWER_BOARD;

    public static String getAllowableValues() {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (ComponentType curComponentType : values()) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(curComponentType.name());
            i++;
        }
        return sb.toString();
    }
}
