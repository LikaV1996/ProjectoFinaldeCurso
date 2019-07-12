/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.UserDao;

/**
 *
 * @author AnaRita
 */
public interface IUserRepository {

    public UserDao findById(long id);

    public UserDao findByName(String userName);

    public List<UserDao> findAll();

    public long add(UserDao userDao);

    public int deleteById(long id);

    public int deleteAll();

    public int updateByID(UserDao userDao);
}
