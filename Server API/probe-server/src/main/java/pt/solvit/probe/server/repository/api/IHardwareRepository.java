/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.HardwareDao;

/**
 *
 * @author AnaRita
 */
public interface IHardwareRepository {

    public HardwareDao findById(long id);

    public HardwareDao findBySerialNumber(String serialNumber);

    public List<HardwareDao> findAll();

    public long add(HardwareDao hardwareDao);

    public long updateByID(HardwareDao hardwareDao);

    public int deleteById(long id);

    public int deleteAll(List<HardwareDao> hardwareDaoList);

    public int deleteAll();
}
