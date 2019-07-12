/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import pt.solvit.probe.server.repository.model.ObuDao;
import pt.solvit.probe.server.repository.model.ObuUserDao;

import java.util.List;


public interface IObuUserRepository {

    public ObuUserDao findById(long obuID, long userID);

    public List<ObuUserDao> findAll();

    public List<ObuUserDao> findAllByUserId(long userID);

    public List<ObuUserDao> findAllByObuId(long userID);

    public long add(ObuUserDao obuUserDao);

    /*
    public int deleteByUserId(long userID);

    public int deleteByObuId(long obuID);
    */

    public int deleteById(long obuID, long userID);

    //public int deleteAll();

    public void updateRole(ObuUserDao obuUserDao);

}
