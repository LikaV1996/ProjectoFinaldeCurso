/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.ObuConfigDao;

/**
 *
 * @author AnaRita
 */
public interface IObuConfigRepository {

    public ObuConfigDao findById(long obuId, long configId, Long userID);

    public List<ObuConfigDao> findAll(Long userID);

    public List<ObuConfigDao> findByObuId(long obuId, Long userID);

    public List<ObuConfigDao> findByConfigId(long configId, Long userID);

    public int addConfigToObu(ObuConfigDao obuConfigDao);

    public int removeConfigFromObu(long obuId, long configId);

    public int removeAllConfigsFromObu(long obuId);

    public void update(ObuConfigDao obuConfigDao);
}
