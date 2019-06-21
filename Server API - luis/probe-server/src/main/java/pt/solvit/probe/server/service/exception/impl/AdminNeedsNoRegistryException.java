/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.exception.impl;

import pt.solvit.probe.server.service.exception.DomainException;


public class AdminNeedsNoRegistryException extends DomainException {

    public AdminNeedsNoRegistryException(String admin_username) {
        super("Invalid operation.", "The user " + admin_username + " is an Admin and can already edit and view all OBUs.");
    }
}
