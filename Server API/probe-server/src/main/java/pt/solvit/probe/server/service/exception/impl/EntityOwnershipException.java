/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.exception.impl;

import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.service.exception.DomainException;

/**
 *
 * @author AnaRita
 */
public class EntityOwnershipException extends DomainException {

    public EntityOwnershipException(EntityType entity) {
        super("Invalid operation.", "The " + entity.getName() + " is not owned by this user.");
    }
}
