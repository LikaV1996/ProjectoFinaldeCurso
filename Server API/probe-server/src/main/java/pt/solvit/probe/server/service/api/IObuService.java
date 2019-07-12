/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;

import pt.solvit.probe.server.controller.model.input.InputObu;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface IObuService {

    public long createObu(Obu obu, User loggedInUser);

    //public Obu getObuByID(long obuId);
    public Obu getObuByID(long obuId, User loggedInUser);

    public List<Obu> getObusWithHardware(long hardwareId);

    public List<Obu> getAllObus(User loggedInUser);

    public void updateObu(Obu obu, User loggedInUser);

    public void deleteObu(long obuId, User loggedInUser);
}
