/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.User;

/**
 *
 * @author AnaRita
 */
public interface ISetupService {

    public long createSetup(Setup setup, User loggedInUser);

    public Setup getSetup(long setupId);

    public List<Setup> getAllSetups();

    public void updateSetup(Setup setup, User loggedInUser);

    public void deleteSetup(long setupId, User loggedInUser);

    //Test Plan Setup    
    public Setup getTestPlanSetup(long testPlanId, long setupId);

    public List<Setup> getAllTestPlanSetups(long testPlanId);

    public void addSetupToTestPlan(long testPlanId, long setupId, User loggedInUser);

    public void removeSetupFromTestPlan(long testPlanId, long setupId, User loggedInUser);

    public void removeAllSetupsFromTestPlan(long testPlanId, User loggedInUser);
}
