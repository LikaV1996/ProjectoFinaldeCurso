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

    public long createHardware(Hardware hardware);

    public Hardware getHardware(long hardwareId);

    public Hardware getHardware(String serialNumber);

    public List<Hardware> getAllHardware();

    public void deleteHardware(long hardwareId, User user);

    public long updateHardware(Hardware hardware);
}
