/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @author AnaRita
 */
@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
})
public class AppConfiguration {

    @Value("${spring.datasource.url}")
    public String datasourceUrl;

    @Value("${spring.datasource.username}")
    public String datasourceUsername;

    @Value("${spring.datasource.password}")
    public String datasourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    public String datasourceDriverClassName;

    @Value("${server.port}")
    public String serverPort;

    @Value("${server.username}")
    public String serverUserName;

    @Value("${server.password}")
    public String serverPassword;

    @Value("${spring.http.multipart.location}")
    public String multipartLocation;

    /**
     * **************
     * CONTROL CONNECTION COMMANDS **************
     */
    public static final String CMD_RESET = "reset";
    public static final String CMD_SHUTDOWN = "shutdown";
    public static final String CMD_CLEAR_ALARMS = "clearAlarms";
    public static final String CMD_UPLOAD_LOGS = "uploadLogs";
    public static final String CMD_DOWNLOAD_CONFIG = "downloadConfig/";
    public static final String CMD_CANCEL_CONFIG = "cancelConfig/";
    public static final String CMD_DOWNLOAD_TESTPLAN = "downloadTestPlan/";
    public static final String CMD_CANCEL_TESTPLAN = "cancelTestPlan/";

    /**
     * **************
     * API ROUTES **************
     */
    public static final String URL_API_CONTROL_CONNECTION = "/api/obu/{obu-id}/control-connection",
            URL_API_REGISTER = "/api/obu/register",
            URL_API_STATUS = "/api/obu/{obu-id}/status",
            URL_API_CONFIG_ID = "/api/obu/{obu-id}/config/{config-id}",
            URL_API_TESTPLAN_ID = "/api/obu/{obu-id}/test-plan/{test-plan-id}",
            URL_API_UPLOAD = "/api/obu/{obu-id}/upload";

    /**
     * **************
     * HARDWARE ROUTES **************
     */
    public static final String URL_HARDWARE = "/backoffice/hardware",
            URL_HARDWARE_ID = "/backoffice/hardware/{hardware-id}";

    /**
     * **************
     * OBU ROUTES **************
     */
    public static final String URL_OBU = "/backoffice/obu",
            URL_OBU_ID = "/backoffice/obu/{obu-id}",
            URL_OBU_FLAGS = "/backoffice/obu/{obu-id}/flags",
            URL_OBU_POSITION = "/backoffice/obu/{obu-id}/position",
            URL_OBU_NETWORK_INTERFACES = "/backoffice/obu/{obu-id}/network-interfaces",
            URL_OBU_COMPONENT = "/backoffice/obu/{obu-id}/component",
            URL_OBU_COMPONENT_ID = "/backoffice/obu/{obu-id}/component/{component-id}",
            URL_OBU_TESTLOG = "/backoffice/obu/{obu-id}/test-log",
            URL_OBU_TESTLOG_ID = "/backoffice/obu/{obu-id}/test-log/{test-log-id}",
            URL_OBU_SYSLOG = "/backoffice/obu/{obu-id}/system-log",
            URL_OBU_SYSLOG_ID = "/backoffice/obu/{obu-id}/system-log/{sys-log-id}";

    /**
     * **************
     * CONFIGURATION ROUTES **************
     */
    public static final String URL_CONFIG = "/backoffice/config",
            URL_CONFIG_ID = "/backoffice/config/{config-id}",
            URL_OBU_CONFIG = "/backoffice/obu/{obu-id}/config",
            URL_OBU_CONFIG_ID = "/backoffice/obu/{obu-id}/config/{config-id}",
            URL_OBU_CONFIG_ID_CANCEL = "/backoffice/obu/{obu-id}/config/{config-id}/cancel";

    /**
     * **************
     * TEST PLAN ROUTES **************
     */
    public static final String URL_TESTPLAN = "/backoffice/test-plan",
            URL_TESTPLAN_ID = "/backoffice/test-plan/{test-plan-id}",
            URL_TESTPLAN_SETUP = "/backoffice/test-plan/{test-plan-id}/setup",
            URL_TESTPLAN_SETUP_ID = "/backoffice/test-plan/{test-plan-id}/setup/{setup-id}",
            URL_OBU_TESTPLAN = "/backoffice/obu/{obu-id}/test-plan",
            URL_OBU_TESTPLAN_ID = "/backoffice/obu/{obu-id}/test-plan/{test-plan-id}",
            URL_OBU_TESTPLAN_ID_CANCEL = "/backoffice/obu/{obu-id}/test-plan/{test-plan-id}/cancel";

    /**
     * **************
     * SETUP ROUTES **************
     */
    public static final String URL_SETUP = "/backoffice/setup",
            URL_SETUP_ID = "/backoffice/setup/{setup-id}";

    /**
     * **************
     * SERVER LOG ROUTES **************
     */
    public static final String URL_SERVER_LOG = "/backoffice/server-log",
            URL_SERVER_LOG_OBU = "/backoffice/server-log/obu",
            URL_SERVER_LOG_USER = "/backoffice/server-log/user";

    /**
     * **************
     * AUTHENTICATION ROUTES **************
     */
    public static final String URL_LOGIN = "/backoffice/login",
            URL_GET_LOGGEDIN_USER = "/backoffice/login/user";

    /**
     * **************
     * USER ROUTES **************
     */
    public static final String URL_GET_USERS = "/backoffice/users",
            URL_GET_USER_BY_ID = "/backoffice/user/{user-id}",
            URL_CREATE_USER = "/backoffice/user",
            URL_DELETE_USER = "/backoffice/user",
            URL_SUSPEND_USER_WITH_ID = "/backoffice/user/{user-id}/suspend",
            URL_UPDATE_USER_WITH_ID = "/backoffice/user/{user-id}";

    /**
     * **************
     * DATABASE ROUTES **************
     */
    public static final String URL_RESET_DB = "/backoffice/reset-db",
            URL_FACTORY_RESET_DB = "/backoffice/factory-reset-db";

}
