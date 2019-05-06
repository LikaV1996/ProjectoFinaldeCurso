/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.ObuStatusDao;

/**
 *
 * @author AnaRita
 */
public interface IObuStatusRepository {

    public List<ObuStatusDao> findByObuId(long obuId);

    public ObuStatusDao findLastByObuId(long obuId);

    public long add(ObuStatusDao obuStatusDao);
}
