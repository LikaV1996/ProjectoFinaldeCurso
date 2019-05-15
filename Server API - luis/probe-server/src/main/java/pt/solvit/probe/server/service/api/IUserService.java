/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;

import pt.solvit.probe.server.controller.model.input.InputUser;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;

/**
 *
 * @author AnaRita
 */
public interface IUserService {

    public long createUser(InputUser input, User loggedInUser);

    public void updateUser(User userToUpdate, InputUser input, User loggedInUser);

    public User getUser(long userId, User loggedInUser);

    public List<User> getAllUsers(User loggedInUser);

    public void deleteUser(long userId, User loggedInUser);

    public void suspendUser(User userToSuspend, User loggedInUser);

    //public User checkUserCredentials(String authorization);

    public void checkLoggedInUserPermissions(User loggedInUser, UserProfile requiredProfile);

    public void checkLoggedInUserPermissionsHigherThanUser(User loggedInUser, User user);
}
