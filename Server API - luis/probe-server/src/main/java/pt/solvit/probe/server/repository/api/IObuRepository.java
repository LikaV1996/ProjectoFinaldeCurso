/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.ObuDao;

/**
 *
 * @author AnaRita
 */
public interface IObuRepository {

    public ObuDao findById(long id);

    public ObuDao findReadyObu(long hardwareId);

    public List<ObuDao> findByHardwareId(long hardwareId);

    public List<ObuDao> findAll();

    public long add(ObuDao obuDao);

    public int deleteById(long id);

    public int deleteAll();

    public void update(ObuDao obuDao);
}
