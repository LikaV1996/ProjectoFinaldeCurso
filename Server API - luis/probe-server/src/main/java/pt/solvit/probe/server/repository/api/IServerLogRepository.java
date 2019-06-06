/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.ServerLogDao;

/**
 *
 * @author AnaRita
 */
public interface IServerLogRepository {

    public ServerLogDao findById(long id);

    public List<ServerLogDao> findAll(boolean ascending);

    public long add(ServerLogDao serverLogDao);

    public int deleteById(long id);

    public int deleteAll(List<ServerLogDao> serverLogDaoList);

    public int deleteAll();

    public void save(ServerLogDao serverLogDao);
}
