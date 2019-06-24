/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IObuUserController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.model.input.InputObuUser;
import pt.solvit.probe.server.model.ObuUser;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IObuUserService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
public class ObuUserController implements IObuUserController {


    @Autowired
    private IObuUserService obuUserService;




    @Override
    public ResponseEntity<List<ObuUser>> getAllObuUserRegistriesByUserID(HttpServletRequest request, @PathVariable("user-id") long userId) {
        User user = (User) request.getAttribute("user");

        List<ObuUser> obuUserList = obuUserService.getAllObuUserByUserID(userId, user);


        return ResponseEntity.ok().body(obuUserList);
    }

    @Override
    public ResponseEntity<List<ObuUser>> getAllObuUserRegistriesByUserID(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        List<ObuUser> obuUserList = obuUserService.getAllObuUser(user);


        return ResponseEntity.ok().body(obuUserList);
    }

    @Override
    public ResponseEntity<ObuUser> createObuUserRegistry(HttpServletRequest request, InputObuUser body) {
        User user = (User) request.getAttribute("user");

        body.validate();
        ObuUser obuUser = ControllerUtil.transformToObuUser(body);

        obuUserService.createObuUserRegistry(obuUser, user);


        return ResponseEntity.status(HttpStatus.CREATED).body(obuUser);
    }

    @Override
    public ResponseEntity<ObuUser> updateObuUserRegistry(HttpServletRequest request, long userId, long obuId, InputObuUser body) {
        User user = (User) request.getAttribute("user");

        body.validate();
        ObuUser obuUser = ControllerUtil.transformToObuUser(body);

        obuUserService.updateObuUser(obuUser, user);

        return ResponseEntity.ok().body(obuUser);
    }

    @Override
    public ResponseEntity<Void> deleteObuUserRegistry(HttpServletRequest request, long userId, long obuId) {
        User user = (User) request.getAttribute("user");

        obuUserService.deleteObuUser(obuId, userId, user);

        return ResponseEntity.ok().build();
    }
}
