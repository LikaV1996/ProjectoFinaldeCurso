/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;

/**
 *
 * @author AnaRita
 */
public interface IUserService {

    public long createUser(User user);

    public User getUser(long userId);

    public List<User> getAllUsers();

    public void deleteUser(long userId, User loggedInUser);

    public void suspendUser(User suspendedUser, User loggedInUser);

    //public User checkUserCredentials(String authorization);

    public void checkUserPermissionsForUpdate(User user, User loggedInUser);
    public void checkUserPermissions(User loggedInUser, UserProfile requiredProfile);
}
