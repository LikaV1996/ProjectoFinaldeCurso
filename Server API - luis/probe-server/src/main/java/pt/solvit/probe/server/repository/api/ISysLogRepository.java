/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.SysLogDao;

/**
 *
 * @author AnaRita
 */
public interface ISysLogRepository {

    public SysLogDao findSysLogFromObu(long obuId, long id);

    public List<SysLogDao> findAllSysLogsByObuId(long obuId);

    public long add(SysLogDao sysLogDao);
}
