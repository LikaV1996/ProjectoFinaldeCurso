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

    public SysLogDao findByObuIDAndID(long obuId, long id);

    public List<SysLogDao> findAllByObuId(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit);

    public long findAllEntriesByObuId(long obuId, String filename);

    public long add(SysLogDao sysLogDao);
}
