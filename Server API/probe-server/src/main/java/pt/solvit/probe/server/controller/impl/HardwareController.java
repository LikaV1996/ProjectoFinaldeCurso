/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.InputHardware;
import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.controller.api.IHardwareController;
import pt.solvit.probe.server.service.api.IHardwareService;
import pt.solvit.probe.server.service.exception.impl.PermissionException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HardwareController implements IHardwareController {

    @Autowired
    private IHardwareService hardwareService;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<List<Hardware>> getAllHardwares(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();

        List<Hardware> hardwareList = hardwareService.getAllHardware(user);


        return ResponseEntity.ok().body(hardwareList);
    }

    @Override
    public ResponseEntity<Hardware> createHardware(HttpServletRequest request, @RequestBody InputHardware body) {

        User user = (User) request.getAttribute("user");

        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();

        body.validate();
        Hardware hardware = ControllerUtil.transformToHardware(body, user.getUserName());
        long hardwareId = hardwareService.createHardware(hardware, user);
        hardware = hardwareService.getHardware(hardwareId, user);


        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_HARDWARE_ID, hardwareId);

        return ResponseEntity.created(createdURI).body(hardware);
    }

    @Override
    public ResponseEntity<Hardware> getHardware(HttpServletRequest request, @PathVariable("hardware-id") long hardwareId) {

        User user = (User) request.getAttribute("user");

        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();

        Hardware hardware = hardwareService.getHardware(hardwareId, user);


        return ResponseEntity.ok(hardware);
    }

    @Override
    public ResponseEntity<Void> deleteHardware(HttpServletRequest request, @PathVariable("hardware-id") long hardwareId) {

        User user = (User) request.getAttribute("user");

        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();

        hardwareService.deleteHardware(hardwareId, user);


        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Hardware> updateHardware(HttpServletRequest request, @RequestBody InputHardware body, @PathVariable("hardware-id")  long hardwareId) {

        User user = (User) request.getAttribute("user");

        if ( ! userService.checkUserPermissions(user, UserProfile.ADMIN))
            throw new PermissionException();


        body.validate();
        Hardware hardware = hardwareService.getHardware(hardwareId, user);

        updateHardware(body, hardware, user.getUserName());

        hardwareService.updateHardware(hardware, user);

        hardware = hardwareService.getHardware(hardwareId, user);


        //URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_HARDWARE_ID, hardwareId);

        //return ResponseEntity.created(createdURI).body(hardware);

        return ResponseEntity.ok().body(hardware);
    }


    private void updateHardware(InputHardware inputHardware, Hardware hardware, String modifier) {

        hardware.setSerialNumber( inputHardware.getSerialNumber() );
        hardware.setComponents( ControllerUtil.transformToComponentList(inputHardware.getComponents()) );
        hardware.setModifier( modifier );

    }


}
