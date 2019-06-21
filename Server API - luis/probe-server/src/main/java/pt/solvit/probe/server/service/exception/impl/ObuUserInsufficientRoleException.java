/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.exception.impl;

import pt.solvit.probe.server.service.exception.DomainException;


public class ObuUserInsufficientRoleException extends DomainException {

    public ObuUserInsufficientRoleException() {
        super("Invalid operation.", "User is only viewer of OBU so you cannnot edit.");
    }
}
