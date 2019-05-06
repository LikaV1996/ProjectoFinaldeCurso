/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import pt.solvit.probe.server.repository.model.UserDao;

import java.util.List;

/**
 *
 * @author AnaRita
 */
public interface IAuthenticationRepository {

    public UserDao findById(long id);

    public UserDao findByNameAndPassword(String username, String password);
}
