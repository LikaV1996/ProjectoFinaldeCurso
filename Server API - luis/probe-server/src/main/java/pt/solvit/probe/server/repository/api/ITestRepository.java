/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.TestDao;

/**
 *
 * @author AnaRita
 */
public interface ITestRepository {

    public TestDao findById(long id);

    public List<TestDao> findAll();

    public List<TestDao> findBySetupId(long setupId);

    public long add(TestDao testDao);

    public int deleteById(long id);
}
