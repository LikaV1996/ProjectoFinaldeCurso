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

    public List<ServerLogDao> findAll(Boolean ascending, String accessor_name, String access_type, Integer pageNumber, Integer pageLimit);

    public long findNumberOfEntries(String accessor_name, String access_type);

    public long add(ServerLogDao serverLogDao);

    public int deleteById(long id);

    public int deleteAll(List<ServerLogDao> serverLogDaoList);

    public int deleteAll();

    public void save(ServerLogDao serverLogDao);
}
