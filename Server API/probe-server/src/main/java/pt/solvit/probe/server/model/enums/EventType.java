/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.enums;

/**
 *
 * @author AnaRita
 */
public enum EventType {
    /*Hardware*/
    //System
    SYSTEM_START,
    SYSTEM_RESET,
    SYSTEM_SHUTTING_DOWN,
    SYSTEM_WARNING, //cause
    SYSTEM_CRITICAL, //cause
    ALARMS_CLEARED,
    RESET_REQUEST, //requestOrigin, [originNumber]
    FREE_MODEM_REQUEST, //requestOrigin, [originNumber]
    //GPS
    GPS_STARTING,
    GPS_ON,
    GPS_OFF,
    GPS_ERROR, //cause
    GPS_FAILURE, //cause
    GPS_NO_COVERAGE,
    //Modem
    MODEM_STARTING,
    MODEM_IDLE,
    MODEM_OFF,
    MODEM_ERROR, //cause
    MODEM_FAILURE, //cause
    MODEM_LIMITED,
    /*Functional*/
    //Register
    REGISTRATION_ATTEMPT, //serverInterface, retryCount (começa em 0)
    REGISTRATION_OK, //serverInterface
    REGISTRATION_ERROR, //serverInterface, cause
    //Configuration
    CONFIG_SCHEDULED, //id
    CONFIG_ACTIVATION, //id
    CONFIG_REJECTED, //id, cause
    CONFIG_CANCELED, //id
    CONFIG_ERROR, //id, cause
    //Control Connection
    CONTROL_CONNECTION_REQUEST, //requestOrigin
    CONTROL_CONNECTION_ATTEMPT, //serverInterface, retryCount
    CONTROL_CONNECTION_OK, //serverInterface
    CONTROL_CONNECTION_ERROR, //serverInterface, cause        
    //Download
    DOWNLOAD_REQUEST, //requestOrigin, downloadType, id
    DOWNLOAD_ATTEMPT, //serverInterface, downloadType, id, retryCount
    DOWNLOAD_OK, //serverInterface, downloadType, id
    DOWNLOAD_ERROR, //serverInterface, downloadType, id, cause
    //Upload
    UPLOAD_REQUEST, //requestOrigin
    UPLOAD_ATTEMPT, //serverInterface, fileName, retryCount
    UPLOAD_OK, //serverInterface, fileName
    UPLOAD_ERROR, //serverInterface, fileName, cause
    //Test Plan
    TESTPLAN_SCHEDULED, //id
    TESTPLAN_STARTING, //id
    TESTPLAN_REJECTED, //id, cause
    TESTPLAN_INTERRUPTED, //id, cause (ex.: New test plan)
    TESTPLAN_SUSPENDED, //id, cause (ex.:Shutdown)
    TESTPLAN_RESUME, //id
    TESTPLAN_END, //id
    TESTPLAN_CANCELED, //id
    TESTPLAN_ERROR, //id, cause
    //Setup
    SETUP_START, //modemType
    SETUP_PAUSE, //modemType, cause
    SETUP_RESUME, //modemType
    SETUP_END, //modemType
    SCANNING_START,//modemType
    SCANNING_STOP,//modemType
    TESTING_START,//modemType
    TESTING_STOP,//modemType
    //Call
    CALL_INCOMING, //modemType, number
    CALL_WAITING, //modemType, number
    CALL_ATTEMPT, //modemType, number, callType, retryCount
    CALL_DIALING, //modemType, number
    CALL_ALERTING, //modemType, number
    CALL_ACTIVE, //modemType, number
    CALL_HELD, //modemType, number
    CALL_END, //modemType, number, cause(ex: no_carrier, duration_exceeded, canceled, call_incoming)
    CALL_INFO, //modemType, number, releaseCause
    CALL_ERROR, //modemType, [number], cause: (ex: no_carrier, busy, no_dialtone, command_error, group_not_subscribed, call_in_progress, invalid_destination)
    //Sms
    SMS_RECEIVED, //modemType, number, message
    SMS_ATTEMPT, //modemType, number, message, retryCount
    SMS_SENT, //modemType, number
    SMS_ERROR; //modemType, [number], cause
}
