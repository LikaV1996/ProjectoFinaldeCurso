/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.SetupDao;

/**
 *
 * @author AnaRita
 */
public interface ISetupRepository {

    public SetupDao findById(long id);

    public List<SetupDao> findAll();

    public long add(SetupDao setupDao);

    public int deleteById(long id);

    public int deleteAll(List<SetupDao> setupDaoList);

    public int deleteAll();

    public void update(SetupDao setupDao);
}
