/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface IDatabaseService {

    public void resetDb(User user);

    public void factoryResetDb(User user);
}
