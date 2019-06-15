/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.TestPlanDao;

/**
 *
 * @author AnaRita
 */
public interface ITestPlanRepository {

    public TestPlanDao findById(long id);

    public List<TestPlanDao> findAll();

    public long add(TestPlanDao testPlanDao);

    public int deleteById(long id);

    public int deleteAll(List<TestPlanDao> testPlanDaoList);

    public int deleteAll();

    public void update(TestPlanDao testPlanDao);
}
