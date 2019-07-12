/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.ObuUser;
import pt.solvit.probe.server.model.User;

import java.util.List;

/**
 *
 * @author AnaRita
 */
public interface IObuUserService {

    public long createObuUserRegistry(ObuUser obu, User loggedInUser);

    //public ObuUser getObuUserByID(long obuID, long userID, User loggedInUser);

    public List<ObuUser> getAllObuUserByUserID(long userID, User loggedInUser);

    //public List<ObuUser> getAllObuUserByObuID(long obuID, User loggedInUser);

    public List<ObuUser> getAllObuUser(User loggedInUser);

    public void updateObuUserRole(ObuUser obuUser, User loggedInUser);

    public void deleteObuUser(long obuID, long userID, User loggedInUser);

}
