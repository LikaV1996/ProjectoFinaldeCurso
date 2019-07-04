/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.model.logfiles.TestLog;
import pt.solvit.probe.server.model.ObuFile;
import pt.solvit.probe.server.model.enums.FileType;
import pt.solvit.probe.server.model.enums.LogDataType;
import pt.solvit.probe.server.repository.model.TestLogDao;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.util.FilesUtil;
import pt.solvit.probe.server.model.logfiles.FileHeader;
import pt.solvit.probe.server.model.logfiles.LogData;
import pt.solvit.probe.server.model.logfiles.SysLog;
import pt.solvit.probe.server.model.properties.FileProperties;
import pt.solvit.probe.server.repository.api.ISysLogRepository;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;
import static pt.solvit.probe.server.util.ServerUtil.GSON;
import pt.solvit.probe.server.service.api.IFileService;
import pt.solvit.probe.server.repository.api.ITestLogRepository;
import pt.solvit.probe.server.repository.model.SysLogDao;

/**
 *
 * @author AnaRita
 */
@Service
public class FileService implements IFileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    @Autowired
    private IUserService userService;

    @Autowired
    private ITestLogRepository testLogRepository;
    @Autowired
    private ISysLogRepository sysLogRepository;

    @Autowired
    private AppConfiguration appConfiguration;

    @Override
    public List<TestLog> getAllObuTestLogs(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Getting obu {0} test log list", obuId);
        List<TestLogDao> logFileList = testLogRepository.findAllByObuId(obuId, ascending, filename, pageNumber, pageLimit);
        return ServiceUtil.map(logFileList, this::parseTestLog);
    }

    @Override
    public List<SysLog> getAllObuSysLogs(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Getting obu {0} system log list", obuId);
        List<SysLogDao> logFileList = sysLogRepository.findAllByObuId(obuId, ascending, filename, pageNumber, pageLimit);
        return ServiceUtil.map(logFileList, this::parseSysLog);
    }

    @Override
    public long addTestLogToObu(ObuFile obuFile) {
        LOGGER.log(Level.INFO, "Adding test log {0} to obu {1}", new String[]{obuFile.getFileName(), String.valueOf(obuFile.getObuId())});
        return testLogRepository.add(transformToTestLogDao(obuFile));
    }

    @Override
    public long addSysLogToObu(ObuFile obuFile) {
        LOGGER.log(Level.INFO, "Adding system log {0} to obu {1}", new String[]{obuFile.getFileName(), String.valueOf(obuFile.getObuId())});
        return sysLogRepository.add(transformToSysLogDao(obuFile));
    }

    @Override
    public TestLog getObuTestLog(long obuId, long testLogId, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding test log {0} from obu {1}", new String[]{String.valueOf(testLogId), String.valueOf(obuId)});
        TestLogDao testLogDao = testLogRepository.findByObuIDAndID(obuId, testLogId);
        return parseTestLog(testLogDao);
    }

    @Override
    public SysLog getObuSysLog(long obuId, long sysLogId, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding system log {0} from obu {1}", new String[]{String.valueOf(sysLogId), String.valueOf(obuId)});
        SysLogDao sysLogDao = sysLogRepository.findByObuIDAndID(obuId, sysLogId);
        return parseSysLog(sysLogDao);
    }

    private SysLog parseSysLog(SysLogDao sysLogDao) {
        LOGGER.log(Level.INFO, "Parsing system log");

        if (sysLogDao.getFileData() == null)
            return transformToSysLog(sysLogDao, null);

        //Unzip data
        String fileName = appConfiguration.multipartLocation + "/unzippedFile";
        File sysLog = FilesUtil.unzipBytes(sysLogDao.getFileData(), fileName);

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(sysLog));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FileHeader fileHeader = FilesUtil.createSysLogHeader();
        List<LogData> logData = new ArrayList();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                //EventData eventData = FilesUtil.parseEventData(fileHeader, line.split(";"));
                logData.add(new LogData(LogDataType.EVENT, line));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Delete temp file
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transformToSysLog(sysLogDao, logData);
    }

    private TestLog parseTestLog(TestLogDao testLogDao) {
        LOGGER.log(Level.INFO, "Parsing test log");

        if (testLogDao.getFileData() == null)
            return transformToTestLog(testLogDao, null);


        //Unzip data
        String fileName = appConfiguration.multipartLocation + "/unzippedFile";
        File testLog = FilesUtil.unzipBytes(testLogDao.getFileData(), fileName);

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(testLog));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FileHeader fileHeader = null;
        List<LogData> logData = new ArrayList();
        String line;
        boolean firstLine = true;
        try {
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    fileHeader = FilesUtil.parseTestLogHeader(line.split(";"));
                    firstLine = false;
                } else {
                    String[] data = line.split(";");
                    if (data.length == fileHeader.getEventSize()) {
                        //EventData eventData = FilesUtil.parseEventData(fileHeader, data);
                        logData.add(new LogData(LogDataType.EVENT, line));
                    } else if (data.length == fileHeader.getScanningSize()) {
                        //ScanData scanData = FilesUtil.parseScanData(fileHeader, data);
                        logData.add(new LogData(LogDataType.SCANNING, line));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Delete temp file
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transformToTestLog(testLogDao, logData);
    }

    private TestLogDao transformToTestLogDao(ObuFile obuFile) {
        String properties = null;
        if (obuFile.getFileType() == FileType.TEST_LOG) {
            TestLog testLog = (TestLog) obuFile;

            FileProperties logProperties = new FileProperties(testLog);
            properties = GSON.toJson(logProperties);
        }

        return new TestLogDao(null, obuFile.getObuId(), obuFile.getFileName(), Timestamp.valueOf(obuFile.getCloseLocalDateTime()),
                Timestamp.valueOf(obuFile.getUploadLocalDateTime()), obuFile.getFileData(), properties);
    }

    private SysLogDao transformToSysLogDao(ObuFile obuFile) {
        String properties = null;
        if (obuFile.getFileType() == FileType.TEST_LOG) {
            TestLog testLog = (TestLog) obuFile;

            FileProperties logProperties = new FileProperties(testLog);
            properties = GSON.toJson(logProperties);
        }

        return new SysLogDao(null, obuFile.getObuId(), obuFile.getFileName(), Timestamp.valueOf(obuFile.getCloseLocalDateTime()),
                Timestamp.valueOf(obuFile.getUploadLocalDateTime()), obuFile.getFileData(), properties);
    }

    private SysLog transformToSysLog(SysLogDao sysLogDao, List<LogData> logData) {
        return new SysLog(sysLogDao.getId(), sysLogDao.getObuId(), sysLogDao.getFileName(), sysLogDao.getCloseDate().toLocalDateTime(),
                sysLogDao.getUploadDate().toLocalDateTime(), sysLogDao.getFileData(), logData);
    }

    private TestLog transformToTestLog(TestLogDao testLogDao, List<LogData> logData) {
        FileProperties properties = GSON.fromJson(testLogDao.getProperties(), FileProperties.class);

        return new TestLog(testLogDao.getId(), testLogDao.getObuId(), testLogDao.getFileName(), testLogDao.getCloseDate().toLocalDateTime(),
                testLogDao.getUploadDate().toLocalDateTime(), testLogDao.getFileData(),
                properties.getTestPlanId(), properties.getSetupId(),
                logData);
    }



    private void checkUserPermissions(User loggedInUser) {
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();
    }
}
