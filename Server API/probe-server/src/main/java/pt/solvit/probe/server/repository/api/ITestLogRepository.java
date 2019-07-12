/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.TestLogDao;

/**
 *
 * @author AnaRita
 */
public interface ITestLogRepository {

    public TestLogDao findByObuIDAndID(long obuId, long id);

    public List<TestLogDao> findAllByObuId(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit);

    public long findAllEntriesByObuId(long obuId, String filename);

    public long add(TestLogDao testLogDao);
}
