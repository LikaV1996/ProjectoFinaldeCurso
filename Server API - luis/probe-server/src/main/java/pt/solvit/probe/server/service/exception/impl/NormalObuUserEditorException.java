/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.exception.impl;

import pt.solvit.probe.server.service.exception.DomainException;


public class NormalObuUserEditorException extends DomainException {

    public NormalObuUserEditorException(String username) {
        super("Invalid operation.", "The user " + username + " is a normal user and cannot be an editor of any OBUs.");
    }
}
