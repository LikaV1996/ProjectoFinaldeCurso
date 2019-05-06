/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.InputHardware;
import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.controller.api.IHardwareController;
import pt.solvit.probe.server.controller.model.input.InputComponent;
import pt.solvit.probe.server.model.Component;
import pt.solvit.probe.server.service.api.IHardwareService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HardwareController implements IHardwareController {

    @Autowired
    private IHardwareService hardwareService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<List<Hardware>> getAllHardware(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<Hardware> hardwareList = hardwareService.getAllHardware();

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_HARDWARE);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(hardwareList);
    }

    @Override
    public ResponseEntity<Void> createHardware(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @RequestBody InputHardware body) {

        User user = (User) request.getAttribute("user");

        body.validate();
        Hardware hardware = transformToHardware(body, user.getUserName());
        long hardwareId = hardwareService.createHardware(hardware);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.POST, HttpStatus.CREATED, AppConfiguration.URL_HARDWARE);
        //serverLogService.createServerLog(serverLog);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_HARDWARE_ID, hardwareId);

        return ResponseEntity.created(createdURI).build();
    }

    @Override
    public ResponseEntity<Hardware> getHardware(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("hardware-id") long hardwareId) {

        //User user = userService.checkUserCredentials(authorization);

        Hardware hardware = hardwareService.getHardware(hardwareId);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_HARDWARE_ID, hardwareId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(hardware);
    }

    @Override
    public ResponseEntity<Void> deleteHardware(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization,
            @PathVariable("hardware-id") long hardwareId) {

        User user = (User) request.getAttribute("user");

        hardwareService.deleteHardware(hardwareId, user);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_HARDWARE_ID, hardwareId);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    private Hardware transformToHardware(InputHardware inputHardware, String creator) {
        List<Component> componentList = null;
        if (inputHardware.getComponents() != null) {
            componentList = new ArrayList();
            for (InputComponent curInputComponent : inputHardware.getComponents()) {
                componentList.add(transformToComponent(curInputComponent));
            }
        }

        return new Hardware(null, inputHardware.getSerialNumber(), componentList, creator, LocalDateTime.now(), null, null);
    }

    private Component transformToComponent(InputComponent inputComponent) {
        return new Component(inputComponent.getSerialNumber(), inputComponent.getComponentType(),
                inputComponent.getManufacturer(), inputComponent.getModel(), inputComponent.getModemType(), inputComponent.getImei());
    }
}