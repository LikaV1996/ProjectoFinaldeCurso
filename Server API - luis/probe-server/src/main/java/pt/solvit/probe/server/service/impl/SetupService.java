/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.solvit.probe.server.model.properties.DataTestProperties;
import pt.solvit.probe.server.model.properties.SetupProperties;
import pt.solvit.probe.server.model.properties.VoiceTestProperties;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.Test;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.repository.model.SetupDao;
import pt.solvit.probe.server.repository.model.TestDao;
import pt.solvit.probe.server.repository.model.TestPlanSetupDao;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.model.enums.TestType;
import pt.solvit.probe.server.repository.api.ISetupRepository;
import pt.solvit.probe.server.repository.api.ITestPlanSetupRepository;
import pt.solvit.probe.server.repository.api.ITestRepository;
import pt.solvit.probe.server.service.api.ISetupService;
import pt.solvit.probe.server.service.api.ITestPlanService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.exception.impl.AssociationAlreadyExistsException;
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
public class SetupService implements ISetupService {

    private static final Logger LOGGER = Logger.getLogger(SetupService.class.getName());

    @Autowired
    private ITestPlanService testPlanService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISetupRepository setupRepository;
    @Autowired
    private ITestRepository testRepository;
    @Autowired
    private ITestPlanSetupRepository testPlanSetupRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long createSetup(Setup setup, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Creating new setup");
        long setupId = setupRepository.add(transformToSetupDao(setup));

        //Add tests
        if (setup.getTests() != null) {
            for (Test curTest : setup.getTests()) {
                testRepository.add(transformToTestDao(curTest, setupId));
            }
        }

        return setupId;
    }

    @Override
    public Setup getSetup(long setupId) {
        LOGGER.log(Level.INFO, "Finding setup {0}", setupId);
        SetupDao setupDao;
        try {
            setupDao = setupRepository.findById(setupId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.SETUP);
        }

        return transformToSetup(setupDao);
    }

    @Override
    public List<Setup> getAllSetups() {
        LOGGER.log(Level.INFO, "Finding all setups");
        List<SetupDao> setupDaoList = setupRepository.findAll();
        List<Setup> setupList = ServiceUtil.map(setupDaoList, this::transformToSetup);
        return setupList;
    }

    @Override
    public void updateSetup(Setup setup, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {    //not admin
            if ( userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) ) { //but is super_user
                userOwnsSetup(transformToSetupDao(setup), loggedInUser);
            }
            else throw new PermissionException();
        }

        LOGGER.log(Level.INFO, "Updating setup {0}", setup.getId());
        setupRepository.update(transformToSetupDao(setup));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSetup(long setupId, User loggedInUser) {
        LOGGER.log(Level.INFO, "Checking if setup {0} exists", setupId);
        SetupDao setupDao = setupRepository.findById(setupId);

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN) ) {    //not admin
            if ( userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER) ) { //but is super_user
                userOwnsSetup(setupDao, loggedInUser);
            }
            else throw new PermissionException();
        }

        verifySetupOnUseCondition(setupDao.getId());

        LOGGER.log(Level.INFO, "Deleting setup {0}", setupId);
        setupRepository.deleteById(setupId);
    }

    @Override
    public Setup getTestPlanSetup(long testPlanId, long setupId) {
        LOGGER.log(Level.INFO, "Finding setup {0} from test plan {1}", new String[]{String.valueOf(setupId), String.valueOf(testPlanId)});
        TestPlanSetupDao testPlanSetupDao = testPlanSetupRepository.findById(testPlanId, setupId);
        Setup setup = getSetup(testPlanSetupDao.getSetupId());
        return setup;
    }

    @Override
    public List<Setup> getAllTestPlanSetups(long testPlanId) {
        LOGGER.log(Level.INFO, "Finding all setups from test plan {0}", testPlanId);
        List<TestPlanSetupDao> testPlanSetupDaoList = testPlanSetupRepository.findFromTestPlan(testPlanId);
        List<Setup> setupList = new ArrayList();
        if (!testPlanSetupDaoList.isEmpty()) {
            for (TestPlanSetupDao curTestPlanSetupDao : testPlanSetupDaoList) {
                setupList.add(getSetup(curTestPlanSetupDao.getSetupId()));
            }
        }
        return setupList;
    }

    @Override
    public void addSetupToTestPlan(long testPlanId, long setupId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        testPlanService.getTestPlan(testPlanId);

        getSetup(setupId);

        testPlanService.verifyTestPlanOnUseCondition(testPlanId, loggedInUser);

        LOGGER.log(Level.INFO, "Checking if setup {0} is already associated to  test plan {1}", new String[]{String.valueOf(setupId), String.valueOf(testPlanId)});
        try {
            testPlanSetupRepository.findById(testPlanId, setupId);
            throw new AssociationAlreadyExistsException(EntityType.SETUP, EntityType.TESTPLAN);
        } catch (EntityWithIdNotFoundException e) {
            //Ignore
        }

        LOGGER.log(Level.INFO, "Adding setup {0} to test plan {1}", new String[]{String.valueOf(setupId), String.valueOf(testPlanId)});
        testPlanSetupRepository.add(testPlanId, setupId);
    }

    @Override
    public void removeSetupFromTestPlan(long testPlanId, long setupId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        //getTestPlanSetup(testPlanId, setupId);

        testPlanService.verifyTestPlanOnUseCondition(testPlanId, loggedInUser);

        LOGGER.log(Level.INFO, "Removing setup {0} from test plan {1}", new String[]{String.valueOf(setupId), String.valueOf(testPlanId)});
        testPlanSetupRepository.deleteById(testPlanId, setupId);
    }

    @Override
    public void removeAllSetupsFromTestPlan(long testPlanId, User loggedInUser) {

        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();

        testPlanService.getTestPlan(testPlanId);

        testPlanService.verifyTestPlanOnUseCondition(testPlanId, loggedInUser);

        LOGGER.log(Level.INFO, "Removing all setups from test plan {0}", testPlanId);
        testPlanSetupRepository.deleteAllByTestPlanId(testPlanId);
    }

    private List<TestDao> getSetupTests(long setupId) {
        LOGGER.log(Level.INFO, "Finding all tests from setup {0}", setupId);
        return testRepository.findBySetupId(setupId);
    }

    private void verifySetupOnUseCondition(long setupId) {
        LOGGER.log(Level.INFO, "Checking if setup {0} associated to any test plan", setupId);
        List<TestPlanSetupDao> testPlanSetupList = testPlanSetupRepository.findFromSetup(setupId);
        if (!testPlanSetupList.isEmpty()) {
            throw new EntityOnUseException(EntityType.SETUP);
        }
    }

    private void userOwnsSetup(SetupDao setupDao, User user) {
        LOGGER.log(Level.INFO, "Checking if user {0} ownes setup {1}", new String[]{user.getUserName(), String.valueOf(setupDao.getId())});
        if (!setupDao.getCreator().equals(user.getUserName())) {
            throw new EntityOwnershipException(EntityType.SETUP);
        }
    }

    private SetupDao transformToSetupDao(Setup setup) {
        return new SetupDao(setup.getId(), setup.getSetupName(), setup.getPropertiesString(),
                setup.getCreator(), Timestamp.valueOf(setup.getCreationLocalDateTime()),
                setup.getModifier(), setup.getModifiedLocalDateTime() != null ? Timestamp.valueOf(setup.getModifiedLocalDateTime()) : null);
    }

    private Setup transformToSetup(SetupDao setupDao) {
        List<TestDao> testDaoList = getSetupTests(setupDao.getId());
        if (testDaoList.isEmpty()) {
            testDaoList = null;
        }

        List<Test> testList = null;
        if (testDaoList != null) {
            testList = new ArrayList();
            for (TestDao curTestDao : testDaoList) {
                testList.add(transformToTest(curTestDao));
            }
        }
        SetupProperties properties = GSON.fromJson(setupDao.getProperties(), SetupProperties.class);

        return new Setup(setupDao.getId(), setupDao.getSetupName(), properties.getModemType(), properties.getScanning(), testList,
                setupDao.getCreator(), setupDao.getCreationDate().toLocalDateTime(),
                setupDao.getModifier(), setupDao.getModifiedDate() != null ? setupDao.getModifiedDate().toLocalDateTime() : null);
    }

    private TestDao transformToTestDao(Test test, long setupId) {
        return new TestDao(test.getId(), test.getIndex(), test.getType().name(), test.getDelay(), setupId, test.getPropertiesString());
    }

    private Test transformToTest(TestDao testDao) {
        TestType type = TestType.valueOf(testDao.getTestType());

        if (type == TestType.SMS) {
            DataTestProperties dataProperties = GSON.fromJson(testDao.getProperties(), DataTestProperties.class);

            return new Test(testDao.getId(), testDao.getTestIndex(), type, testDao.getDelay(), dataProperties.getDestination(),
                    null, dataProperties.getMessage(), null);
        }
        VoiceTestProperties voiceProperties = GSON.fromJson(testDao.getProperties(), VoiceTestProperties.class);

        return new Test(testDao.getId(), testDao.getTestIndex(), type, testDao.getDelay(), voiceProperties.getDestination(),
                voiceProperties.getDuration(), null, voiceProperties.getPriority());
    }

    private void checkUserPermissions(User loggedInUser) {
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.SUPER_USER))
            throw new PermissionException();
    }
}
