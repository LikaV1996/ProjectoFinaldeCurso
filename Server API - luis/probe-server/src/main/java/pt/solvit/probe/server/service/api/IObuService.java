/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface IObuService {

    public long createObu(Obu obu);

    public Obu getObu(long obuId);

    public List<Obu> getObusWithHardware(long hardwareId);

    public List<Obu> getAllObus();

    public void updateObu(Obu obu);

    public void deleteObu(long obuId, User user);
}
