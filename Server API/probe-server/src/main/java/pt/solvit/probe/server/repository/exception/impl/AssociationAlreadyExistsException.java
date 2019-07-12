/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.exception.impl;

import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.repository.exception.RepositoryException;

/**
 *
 * @author AnaRita
 */
public class AssociationAlreadyExistsException extends RepositoryException {

    public AssociationAlreadyExistsException(EntityType entity1, EntityType entity2) {
        super("Invalid operation.", "The " + entity1.getName() + " is already associated to " + entity2.getName() + ".");
    }
}
