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

    public TestLogDao findTestLogFromObu(long obuId, long id);

    public List<TestLogDao> findAllTestLogsByObuId(long obuId);

    public long add(TestLogDao testLogDao);
}
