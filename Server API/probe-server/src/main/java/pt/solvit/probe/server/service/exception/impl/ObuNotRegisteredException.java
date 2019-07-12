/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.exception.impl;

import pt.solvit.probe.server.service.exception.DomainException;

/**
 *
 * @author AnaRita
 */
public class ObuNotRegisteredException extends DomainException {

    public ObuNotRegisteredException() {
        super("Invalid operation.", "The operation can not be done because the obu is not registered.");
    }
}
