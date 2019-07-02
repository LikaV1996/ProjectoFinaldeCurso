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
import pt.solvit.probe.server.controller.api.IObuController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.impl.util.UriBuilder;
import pt.solvit.probe.server.controller.model.input.InputObu;
import pt.solvit.probe.server.controller.model.input.InputObuFlags;
import pt.solvit.probe.server.model.Hardware;
import pt.solvit.probe.server.model.Obu;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IObuService;
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

    @Override
    public ResponseEntity<List<Obu>> getAllObus(HttpServletRequest request) {

        User user = (User) request.getAttribute("user");

        List<Obu> obuList = obuService.getAllObus(user);

        return ResponseEntity.ok().body(obuList);
    }

    @Override
    public ResponseEntity<Obu> createObu(HttpServletRequest request, @RequestBody InputObu body) {

        User user = (User) request.getAttribute("user");

        body.validateForCreate();
        Hardware hardware = hardwareService.getHardware( body.getHardwareId() );
        Obu obu = Obu.makeObuFromInput( body, user.getUserName() );
        long obuId = obuService.createObu(obu, user);


        obu = obuService.getObuByID(obuId, user);

        URI createdURI = UriBuilder.buildUri(AppConfiguration.URL_OBU_ID, obuId);

        return ResponseEntity.created(createdURI).body(obu);
    }

    @Override
    public ResponseEntity<Obu> getObuByID(HttpServletRequest request, @PathVariable("obu-id") long obuId) {

        User user = (User) request.getAttribute("user");

        Obu obu = obuService.getObuByID(obuId, user);

        return ResponseEntity.ok().body(obu);
    }

    @Override
    public ResponseEntity<Obu> updateObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @RequestBody InputObu body) {

        User user = (User) request.getAttribute("user");

        body.validateForUpdate();
        Obu updateObu = obuService.getObuByID(obuId, user);

        updateObu(updateObu, body, user.getUserName());

        obuService.updateObu(updateObu, user);

        updateObu = obuService.getObuByID(obuId, user);


        return ResponseEntity.ok().body(updateObu);
    }

    @Override
    public ResponseEntity<Void> deleteObu(HttpServletRequest request, @PathVariable("obu-id") long obuId) {

        User user = (User) request.getAttribute("user");

        obuService.deleteObu(obuId, user);


        return ResponseEntity.ok().build();
    }

    /*
    @Override
    public ResponseEntity<Void> updateObuFlags(HttpServletRequest request, @PathVariable("obu-id") long obuId, @RequestBody InputObuFlags body) {

        User user = (User) request.getAttribute("user");

        Obu obu = obuService.getObuByID(obuId, user);

        body.validateForCreate();
        updateObuFlags(obu, body, user.getUserName());


        obuService.updateObu(obu, user);


        return ResponseEntity.ok().build();
    }
    */

    private void updateObu(Obu obu, InputObu inputObu, String modifier){
        obu.setHardwareId(inputObu.getHardwareId());
        obu.setSims(inputObu.getSims());
        obu.setObuName(inputObu.getObuName());

        //  removed
        //obu.setFactoryConfig(ControllerUtil.transformToConfig( inputObu.getFactoryConfig(), obu.getCreator() ));

        obu.setAuthenticate(inputObu.isAuthenticate());
        obu.setUploadRequest(inputObu.isUploadRequest());
        obu.setClearAlarmsRequest(inputObu.isClearAlarmsRequest());
        obu.setResetRequest(inputObu.isResetRequest());
        obu.setShutdownRequest(inputObu.isShutdownRequest());

        obu.setModifier(modifier);
    }

    /*
    private void updateObuFlags(Obu obu, InputObuFlags inputObuFlags, String modifier){
        obu.setAuthenticate(inputObuFlags.isAuthenticate());
        obu.setUploadRequest(inputObuFlags.isUploadRequest());
        obu.setClearAlarmsRequest(inputObuFlags.isClearAlarmsRequest());
        obu.setResetRequest(inputObuFlags.isResetRequest());
        obu.setShutdownRequest(inputObuFlags.isShutdownRequest());

        obu.setModifier(modifier);
    }
    */

    /*
    private static String generateObuPassword() {
        String generatedPassword = ControllerUtil.getAlphaNumeric(6);
        return new String(Base64.getEncoder().encode(generatedPassword.getBytes()), StandardCharsets.UTF_8);
    }
    */
}
