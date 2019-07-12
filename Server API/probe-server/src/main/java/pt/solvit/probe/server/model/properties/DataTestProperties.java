/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.properties;

import pt.solvit.probe.server.model.Test;

/**
 *
 * @author AnaRita
 */
public class DataTestProperties {

    private String[] destination;
    private String message;

    public DataTestProperties(Test test) {
        this.destination = test.getDestination();
        this.message = test.getMessage();
    }

    public String[] getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }
}
