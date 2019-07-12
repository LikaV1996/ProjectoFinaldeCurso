/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.api;

import java.util.List;
import pt.solvit.probe.server.repository.model.TestPlanSetupDao;

/**
 *
 * @author AnaRita
 */
public interface ITestPlanSetupRepository {

    public TestPlanSetupDao findById(long testPlanId, long setupId);

    public List<TestPlanSetupDao> findFromTestPlan(long testPlanId);

    public List<TestPlanSetupDao> findFromSetup(long setupId);

    public int add(long testPlanId, long setupId);

    public int deleteById(long testPlanId, long setupId);

    public int deleteAllByTestPlanId(long testPlanId);
}
