/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;

import pt.solvit.probe.server.model.Config;
import pt.solvit.probe.server.repository.model.ConfigDao;

/**
 *
 * @author AnaRita
 */
public interface IConfigRepository {

    public ConfigDao findById(long id);

    public List<ConfigDao> findAll();

    public long add(ConfigDao configDao);

    public int deleteById(long id);

    public int deleteAll(List<ConfigDao> configDaoList);

    public int deleteAll();

    public void update(ConfigDao configDao);
}
