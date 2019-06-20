/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.exception.impl;

import pt.solvit.probe.server.service.exception.DomainException;


public class ObuHasTestPlanException extends DomainException {

    public ObuHasTestPlanException() {
        super("Invalid operation.", "The operation can not be done because the obu has test plans associated.");
    }
}
