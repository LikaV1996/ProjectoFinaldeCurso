/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface IHardwareService {

    public long createHardware(Hardware hardware, User loggedInUser);

    public Hardware getHardware(long hardwareId, User loggedInUser);

    public Hardware getHardware(String serialNumber, User loggedInUser);

    public List<Hardware> getAllHardware(User loggedInUser);

    public void deleteHardware(long hardwareId, User loggedInUser);

    public long updateHardware(Hardware hardware, User loggedInUser);
}
