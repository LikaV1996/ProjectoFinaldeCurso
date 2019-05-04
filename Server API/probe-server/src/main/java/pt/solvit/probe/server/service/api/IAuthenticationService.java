/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import pt.solvit.probe.server.model.Login;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;

import java.util.List;

public interface IAuthenticationService {

    public Login login(String username, String password);

    public User getLoggedInUser(long userID);

    public User getAuthenticatedUser(String username, String password);
}
