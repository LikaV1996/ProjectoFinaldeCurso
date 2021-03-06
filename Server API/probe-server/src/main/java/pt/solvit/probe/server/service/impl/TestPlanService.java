/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.ObuTestPlan;
import pt.solvit.probe.server.model.properties.ObuTestPlanProperties;
import pt.solvit.probe.server.model.properties.TestPlanProperties;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.TestPlan;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.repository.model.ObuTestPlanDao;
import pt.solvit.probe.server.repository.model.TestPlanDao;
import pt.solvit.probe.server.model.enums.CancelState;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.repository.api.IObuTestPlanRepository;
import pt.solvit.probe.server.repository.api.ITestPlanRepository;
import pt.solvit.probe.server.service.api.ISetupService;
import pt.solvit.probe.server.service.api.ITestPlanService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.repository.api.ITestPlanSetupRepository;
import pt.solvit.probe.server.repository.exception.impl.AssociationAlreadyExistsException;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.service.exception.impl.EntityOnUseException;
import pt.solvit.probe.server.service.exception.impl.EntityOwnershipException;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import static pt.solvit.probe.server.util.ServerUtil.GSON;

/**
 *
 * @author AnaRita
 */
@Service
public class TestPlanService implements ITestPlanService {

    private static final Logger LOGGER = Logger.getLogger(TestPlanService.class.getName());

    @Autowired
    private ISetupService setupService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITestPlanRepository testPlanRepository;
    @Autowired
    private IObuRepository obuRepository;
    @Autowired
    private IObuTestPlanRepository obuTestPlanRepository;
    @Autowired
    private ITestPlanSetupRepository testPlanSetupRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long createTestPlan(TestPlan testPlan, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();

        LOGGER.log(Level.INFO, "Creating new test plan");
        long testPlanId = testPlanRepository.add(transformToTestPlanDao(testPlan));

        /*  //removed
        //Add setups
        if (testPlan.getSetups() != null) {
            for (Setup curSetup : testPlan.getSetups()) {
                long setupId = setupService.createSetup(curSetup);
                LOGGER.log(Level.INFO, "Adding setup {0} to test plan {1}", new String[]{String.valueOf(setupId), String.valueOf(testPlanId)});
                testPlanSetupRepository.add(testPlanId, setupId);
            }
        }
        */

        return testPlanId;
    }

    @Override
    public TestPlan getTestPlan(long testPlanId) {
        LOGGER.log(Level.INFO, "Finding test plan {0}", testPlanId);
        TestPlanDao testPlanDao;
        try {
            testPlanDao = testPlanRepository.findById(testPlanId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.TESTPLAN);
        }
        return transformToTestPlan(testPlanDao);
    }

    @Override
    public List<TestPlan> getAllTestPlans() {
        LOGGER.log(Level.INFO, "Finding all test plans");
        List<TestPlanDao> testPlanDaoList = testPlanRepository.findAll();
        List<TestPlan> testPlanList = ServiceUtil.map(testPlanDaoList, this::transformToTestPlan);
        return testPlanList;
    }

    @Override
    public void deleteTestPlan(long testPlanId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if test plan {0} exists", testPlanId);
        TestPlanDao testPlanDao = testPlanRepository.findById(testPlanId);

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {    //not admin
            if ( userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) ) { //but is super_user
                userOwnsTestPlan(testPlanDao, loggedInUser);
            }
            else throw new PermissionException();
        }

        verifyTestPlanOnUseCondition(testPlanId, loggedInUser);

        LOGGER.log(Level.INFO, "Deleting test plan {0}", testPlanId);
        testPlanRepository.deleteById(testPlanId);
    }

    @Override
    public void updateTestPlan(TestPlan testPlan, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {    //not admin
            if ( userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) ) { //but is super_user
                userOwnsTestPlan(transformToTestPlanDao(testPlan), loggedInUser);
            }
            else throw new PermissionException();
        }

        LOGGER.log(Level.INFO, "Updating test plan {0}", testPlan.getId());
        testPlanRepository.update(transformToTestPlanDao(testPlan));
    }

    @Override
    public void verifyTestPlanOnUseCondition(long testPlanId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if test plan is associated to any obu");

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        List<ObuTestPlanDao> obuTestPlanList = obuTestPlanRepository.findByTestPlanId(testPlanId, userID);
        if (!obuTestPlanList.isEmpty()) {
            throw new EntityOnUseException(EntityType.TESTPLAN);
        }
    }

    @Override
    public ObuTestPlan getObuTestPlan(long obuId, long testPlanId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding test plan {0} from obu {1}", new String[]{String.valueOf(testPlanId), String.valueOf(obuId)});

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        ObuTestPlanDao obuTestPlan = obuTestPlanRepository.findById(obuId, testPlanId, userID);
        return transformToObuTestPlan(obuTestPlan);
    }

    @Override
    public List<ObuTestPlan> getAllObuTestPlans(long obuId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Finding all configurations from obu {0}", obuId);

        Long userID = getUserIdIfNotAdmin(loggedInUser);

        List<ObuTestPlanDao> obuTestPlanList = obuTestPlanRepository.findByObuId(obuId, userID);
        return ServiceUtil.map(obuTestPlanList, this::transformToObuTestPlan);
    }

    @Override
    public void addTestPlanToObu(long obuId, long testPlanId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if obu with ID {0} already exists", obuId);

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {
            throw new PermissionException();
        }

        obuRepository.findById(obuId, null);

        LOGGER.log(Level.INFO, "Checking if test plan {0} exists", testPlanId);
        testPlanRepository.findById(testPlanId);

        LOGGER.log(Level.INFO, "Adding test plan {0} to obu {1}", new String[]{String.valueOf(testPlanId), String.valueOf(obuId)});
        ObuTestPlanDao obuTestPlan = transformToObuTestPlanDao(new ObuTestPlan(obuId, testPlanId));
        try {
            obuTestPlanRepository.add(obuTestPlan);
        } catch (DuplicateKeyException ex) {
            throw new AssociationAlreadyExistsException(EntityType.TESTPLAN, EntityType.OBU);
        }
    }

    @Override
    public boolean cancelTestPlanFromObu(long obuId, long testPlanId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {
            throw new PermissionException();
        }

        ObuTestPlan obuTestPlan = getObuTestPlan(obuId, testPlanId, loggedInUser);

        LOGGER.log(Level.INFO, "Canceling test plan {0} from obu {1}", new String[]{String.valueOf(testPlanId), String.valueOf(obuId)});
        if (obuTestPlan.getStateList() == null) { //If test plan was not downloaded >> Canceled
            obuTestPlan.setCancelState(CancelState.CANCELED);

            updateObuTestPlan(obuTestPlan);
            LOGGER.log(Level.INFO, "Test plan successfully canceled from obu");

            return true;
        } else { //If test plan was already downloaded >> Cancel request
            obuTestPlan.setCancelState(CancelState.CANCEL_REQUEST);
            updateObuTestPlan(obuTestPlan);
            LOGGER.log(Level.INFO, "Cancel request will be sent to obu");

            return false;
        }
    }

    @Override
    public void removeTestPlanFromObu(long obuId, long testPlanId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {
            throw new PermissionException();
        }

        ObuTestPlan obuTestPlan = getObuTestPlan(obuId, testPlanId, loggedInUser);

        LOGGER.log(Level.INFO, "Removing test plan {0} from obu {1}", new String[]{String.valueOf(testPlanId), String.valueOf(obuId)});
        if (obuTestPlan.getStateList() != null) {//If test plan was already downloaded
            throw new BadRequestException("Invalid operation.", "Test plan has already been downloaded by obu.", "/probs/testplan-already-downloaded", "about:blank");
        }
        obuTestPlanRepository.deleteById(obuId, testPlanId);
    }

    @Override
    public void removeAllTestPlansFromObu(long obuId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {
            throw new PermissionException();
        }

        LOGGER.log(Level.INFO, "Removing all test plans from obu {0}", obuId);
        obuTestPlanRepository.deleteAllByObuId(obuId);
    }

    private void updateObuTestPlan(ObuTestPlan obuTestPlan) {
        LOGGER.log(Level.FINE, "Updating obuTestPlan");
        obuTestPlanRepository.update(transformToObuTestPlanDao(obuTestPlan));
    }

    private void userOwnsTestPlan(TestPlanDao testPlanDao, User user) {
        LOGGER.log(Level.INFO, "Checking if user {0} owns test plan {1}", new String[]{user.getUserName(), String.valueOf(testPlanDao.getId())});
        if (!testPlanDao.getCreator().equals(user.getUserName())) {
            throw new EntityOwnershipException(EntityType.TESTPLAN);
        }
    }

    private TestPlanDao transformToTestPlanDao(TestPlan testPlan) {
        return new TestPlanDao(testPlan.getId(), testPlan.getTestplanName(), Timestamp.valueOf(testPlan.getStartDateLocalDateTime()),
                Timestamp.valueOf(testPlan.getStopDateLocalDateTime()), testPlan.getPropertiesString(),
                testPlan.getCreator(), Timestamp.valueOf(testPlan.getCreationLocalDateTime()),
                testPlan.getModifier(), testPlan.getModifiedLocalDateTime() != null ? Timestamp.valueOf(testPlan.getModifiedLocalDateTime()): null);
    }

    private TestPlan transformToTestPlan(TestPlanDao testPlanDao) {
        List<Setup> setupList = setupService.getAllTestPlanSetups(testPlanDao.getId());
        if (setupList.isEmpty()) {
            setupList = null;
        }

        TestPlanProperties properties = GSON.fromJson(testPlanDao.getProperties(), TestPlanProperties.class);

        return new TestPlan(testPlanDao.getId(), testPlanDao.getTestplanName(), testPlanDao.getStartDate().toLocalDateTime(), testPlanDao.getStopDate().toLocalDateTime(),
                properties.getTriggerCoordinates(), properties.getPeriodDuration(),
                //setupList,
                properties.getMaxRetries(),
                properties.getRetryDelay(), properties.getRedialTriggers(),
                testPlanDao.getCreator(), testPlanDao.getCreationDate().toLocalDateTime(),
                testPlanDao.getModifier(), (testPlanDao.getModifiedDate() != null ? testPlanDao.getModifiedDate().toLocalDateTime() : null));
    }

    private ObuTestPlanDao transformToObuTestPlanDao(ObuTestPlan obuTestPlan) {
        return new ObuTestPlanDao(obuTestPlan.getObuId(), obuTestPlan.getTestPlanId(), obuTestPlan.getPropertiesString());
    }

    private ObuTestPlan transformToObuTestPlan(ObuTestPlanDao obuTestPlanDao) {
        TestPlan testPlan = getTestPlan(obuTestPlanDao.getTestPlanId());

        ObuTestPlanProperties properties = GSON.fromJson(obuTestPlanDao.getProperties(), ObuTestPlanProperties.class);

        return new ObuTestPlan(obuTestPlanDao.getObuId(), obuTestPlanDao.getTestPlanId(), properties.getCancelState(), properties.getStateList(), testPlan);
    }

    private void checkUserPermissions(User loggedInUser) {
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();
    }

    private Long getUserIdIfNotAdmin(User loggedInUser){
        return userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ? null : loggedInUser.getId();
    }
}
