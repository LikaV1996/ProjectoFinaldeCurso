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
     *
     * URL API BEGINNING
     */
    private static final String URL_BEGINNING = "/api/v1/backoffice";

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
     * **************   //TODO        these we don't use
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
    public static final String URL_HARDWARE = URL_BEGINNING + "/hardware",
            URL_HARDWARE_ID = URL_BEGINNING + "/hardware/{hardware-id}";

    /**
     * **************
     * OBU ROUTES **************
     */
    public static final String URL_OBU = URL_BEGINNING + "/obu",
            URL_OBU_ID = URL_BEGINNING + "/obu/{obu-id}",
            URL_OBU_FLAGS = URL_BEGINNING + "/obu/{obu-id}/flags",
            URL_OBU_POSITION = URL_BEGINNING + "/obu/{obu-id}/position",
            URL_OBU_NETWORK_INTERFACES = URL_BEGINNING + "/obu/{obu-id}/network-interfaces",
            URL_OBU_COMPONENT = URL_BEGINNING + "/obu/{obu-id}/component",
            URL_OBU_COMPONENT_ID = URL_BEGINNING + "/obu/{obu-id}/component/{component-id}",
            URL_OBU_TESTLOG = URL_BEGINNING + "/obu/{obu-id}/test-log",
            URL_OBU_TESTLOG_ID = URL_BEGINNING + "/obu/{obu-id}/test-log/{test-log-id}",
            URL_OBU_SYSLOG = URL_BEGINNING + "/obu/{obu-id}/system-log",
            URL_OBU_SYSLOG_ID = URL_BEGINNING + "/obu/{obu-id}/system-log/{sys-log-id}";

    /**
     * **************
     * CONFIGURATION ROUTES **************
     */
    public static final String URL_CONFIG = URL_BEGINNING + "/config",
            URL_CONFIG_ID = URL_BEGINNING + "/config/{config-id}",
            URL_OBU_CONFIG = URL_BEGINNING + "/obu/{obu-id}/config",
            URL_OBU_CONFIG_ID = URL_BEGINNING + "/obu/{obu-id}/config/{config-id}",
            URL_OBU_CONFIG_ID_CANCEL = URL_BEGINNING + "/obu/{obu-id}/config/{config-id}/cancel";

    /**
     * **************
     * TEST PLAN ROUTES **************
     */
    public static final String URL_TESTPLAN = URL_BEGINNING + "/test-plan",
            URL_TESTPLAN_ID = URL_BEGINNING + "/test-plan/{test-plan-id}",
            URL_TESTPLAN_SETUP = URL_BEGINNING + "/test-plan/{test-plan-id}/setup",
            URL_TESTPLAN_SETUP_ID = URL_BEGINNING + "/test-plan/{test-plan-id}/setup/{setup-id}",
            URL_OBU_TESTPLAN = URL_BEGINNING + "/obu/{obu-id}/test-plan",
            URL_OBU_TESTPLAN_ID = URL_BEGINNING + "/obu/{obu-id}/test-plan/{test-plan-id}",
            URL_OBU_TESTPLAN_ID_CANCEL = URL_BEGINNING + "/obu/{obu-id}/test-plan/{test-plan-id}/cancel";

    /**
     * **************
     * SETUP ROUTES **************
     */
    public static final String URL_SETUP = URL_BEGINNING + "/setup",
            URL_SETUP_ID = URL_BEGINNING + "/setup/{setup-id}";

    /**
     * **************
     * SERVER LOG ROUTES **************
     */
    public static final String URL_SERVER_LOG = URL_BEGINNING + "/server-log",
            URL_SERVER_LOG_OBU = URL_BEGINNING + "/server-log/obu",
            URL_SERVER_LOG_USER = URL_BEGINNING + "/server-log/user";

    /**
     * **************
     * AUTHENTICATION ROUTES **************
     */
    public static final String URL_LOGIN = URL_BEGINNING + "/login",
            URL_GET_LOGGEDIN_USER = URL_BEGINNING + "/login/user";

    /**
     * **************
     * USER ROUTES **************
     */
    public static final String URL_GET_USERS = URL_BEGINNING + "/users",
            URL_GET_USER_BY_ID = URL_BEGINNING + "/user/{user-id}",
            URL_CREATE_USER = URL_BEGINNING + "/user",
            URL_DELETE_USER = URL_BEGINNING + "/user",
            URL_SUSPEND_USER_WITH_ID = URL_BEGINNING + "/user/{user-id}/suspend",
            URL_UPDATE_USER_WITH_ID = URL_BEGINNING + "/user/{user-id}";

    /**
     * **************
     * DATABASE ROUTES **************
     */
    public static final String URL_RESET_DB = URL_BEGINNING + "/reset-db",
            URL_FACTORY_RESET_DB = URL_BEGINNING + "/factory-reset-db";

}
