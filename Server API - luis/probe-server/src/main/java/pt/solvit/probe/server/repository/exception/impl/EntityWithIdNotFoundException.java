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
public class EntityWithIdNotFoundException extends RepositoryException {

    public EntityWithIdNotFoundException(EntityType entity) {
        super("The " + entity.getName() + " was not found.", "The " + entity.getName() + " with the requested id was not found.");
    }

    public EntityWithIdNotFoundException(EntityType entity1, EntityType entity2){
        super("The " + entity1.getName() + " or " + entity2.getName() + " was not found.", "The " + entity1.getName() + " or " + entity2.getName() + " with the requested id was not found.");
    }

}
