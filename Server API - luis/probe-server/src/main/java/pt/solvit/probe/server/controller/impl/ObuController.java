/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IObuController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.InputObu;
import pt.solvit.probe.server.controller.model.input.InputObuFlags;
import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.ObuState;
import pt.solvit.probe.server.service.api.IObuService;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.api.IHardwareService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ObuController implements IObuController {

    @Autowired
    private IObuService obuService;
    @Autowired
    private IHardwareService hardwareService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<Obu>> getAllObus(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<Obu> obuList = obuService.getAllObus();

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obuList);
    }

    @Override
    public ResponseEntity<Obu> createObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestBody InputObu body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Hardware hardware = hardwareService.getHardware( body.getHardwareId() );
        Obu obu = Obu.makeObu( body, user.getUserName() );
        long obuId = obuService.createObu(obu);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.CREATED, AppConfiguration.URL_OBU);
        //serverLogService.createServerLog(serverLog);

        obu = obuService.getObu(obuId);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_OBU_ID, obuId);

        return ResponseEntity.created(createdURI).body(obu);
    }

    @Override
    public ResponseEntity<Obu> getObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        //User user = userService.checkUserCredentials(authorization);

        Obu obu = obuService.getObu(obuId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_OBU_ID, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(obu);
    }

    @Override
    public ResponseEntity<Obu> updateObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @RequestBody InputObu body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Obu obu = obuService.getObu(obuId);
        //obu.setHardwareId(body.getHardwareId());
        obu.setSims(body.getSims());
        obu.setObuName(body.getObuName());
        obu.setModifier(user.getUserName());
        obuService.updateObu(obu);
        obu = obuService.getObu(obuId);
        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.PUT, HttpStatus.OK, AppConfiguration.URL_OBU_ID, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().body(obu);
    }

    @Override
    public ResponseEntity<Void> deleteObu(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId) {

        User user = (User) request.getAttribute("user");

        obuService.deleteObu(obuId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_OBU_ID, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateObuFlags(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("obu-id") long obuId, @RequestBody InputObuFlags body) {

        //User user = userService.checkUserCredentials(authorization);

        Obu obu = obuService.getObu(obuId);

        if (body.isAuthenticate() != null) {
            obu.setAuthenticate(body.isAuthenticate());
        }
        if (body.isUploadRequest() != null) {
            obu.setUploadRequest(body.isUploadRequest());
        }
        if (body.isClearAlarmsRequest() != null) {
            obu.setClearAlarmsRequest(body.isClearAlarmsRequest());
        }
        if (body.isResetRequest() != null) {
            obu.setResetRequest(body.isResetRequest());
        }
        if (body.isShutdownRequest() != null) {
            obu.setShutdownRequest(body.isShutdownRequest());
        }
        obuService.updateObu(obu);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.OK, AppConfiguration.URL_OBU_FLAGS, obuId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    /*
    private Obu makeObu(InputObu inputObu, String creator, Hardware hardware) {
        long hardwareId = inputObu.getHardwareId();
        String obuName = hardware.getSerialNumber();
        String obuPassword = generateObuPassword();
        return new Obu(null, hardwareId, ObuState.READY, obuName, obuPassword, inputObu.getSims(), creator, LocalDateTime.now(), null, null);
    }

    private static String generateObuPassword() {
        String generatedPassword = ControllerUtil.getAlphaNumeric(6);
        return new String(Base64.getEncoder().encode(generatedPassword.getBytes()), StandardCharsets.UTF_8);
    }
    */
}
