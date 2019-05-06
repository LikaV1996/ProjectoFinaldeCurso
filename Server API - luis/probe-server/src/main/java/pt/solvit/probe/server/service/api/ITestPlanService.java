/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.ObuTestPlan;
import pt.solvit.probe.server.model.TestPlan;
import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface ITestPlanService {

    public long createTestPlan(TestPlan testPlan);

    public TestPlan getTestPlan(long testPlanId);

    public List<TestPlan> getAllTestPlans();

    public void deleteTestPlan(long testPlanId, User user);

    public void verifyTestPlanOnUseCondition(long testPlanId);

    //Obu Test Plan
    public ObuTestPlan getObuTestPlan(long obuId, long testPlanId);

    public List<ObuTestPlan> getAllObuTestPlans(long obuId);

    public void addTestPlanToObu(long obuId, long testPlanId);

    public boolean cancelTestPlanFromObu(long obuId, long testPlanId);

    public void removeTestPlanFromObu(long obuId, long testPlanId);

    public void removeAllTestPlansFromObu(long obuId);
}
