/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.exception.impl;

import pt.solvit.probe.server.repository.exception.RepositoryException;

/**
 *
 * @author AnaRita
 */
public class ObuUserAlreadyExistsException extends RepositoryException {

    public ObuUserAlreadyExistsException() {
        super("Invalid body.", "There is already an obu_user registry with those IDs.");
    }
}
