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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IObuUserController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.controller.model.input.InputObuUser;
import pt.solvit.probe.server.model.ObuUser;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IObuUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ObuUserController implements IObuUserController {


    @Autowired
    private IObuUserService obuUserService;




    @Override
    public ResponseEntity<List<ObuUser>> getAllObuUserRegistries(HttpServletRequest request, @PathVariable("user-id") long userId) {
        User user = (User) request.getAttribute("user");

        List<ObuUser> obuUserList = obuUserService.getAllObuUserByUserID(userId, user);


        return ResponseEntity.ok().body(obuUserList);
    }

    @Override
    public ResponseEntity<List<ObuUser>> getAllObuUserRegistries(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        List<ObuUser> obuUserList = obuUserService.getAllObuUser(user);


        return ResponseEntity.ok().body(obuUserList);
    }

    @Override
    public ResponseEntity<ObuUser> createObuUserRegistry(HttpServletRequest request, @RequestBody InputObuUser body) {
        User user = (User) request.getAttribute("user");

        body.validate();
        ObuUser obuUser = ControllerUtil.transformToObuUser(body);

        obuUserService.createObuUserRegistry(obuUser, user);


        return ResponseEntity.status(HttpStatus.CREATED).body(obuUser);
    }

    @Override
    public ResponseEntity<ObuUser> updateObuUserRegistry(HttpServletRequest request, @PathVariable("user-id") long userId, @PathVariable("obu-id") long obuId, @RequestBody  InputObuUser body) {
        User user = (User) request.getAttribute("user");

        body.validate();
        ObuUser obuUser = ControllerUtil.transformToObuUser(body);

        obuUserService.updateObuUserRole(obuUser, user);

        return ResponseEntity.ok().body(obuUser);
    }

    @Override
    public ResponseEntity<Void> deleteObuUserRegistry(HttpServletRequest request, @PathVariable("user-id") long userId, @PathVariable("obu-id") long obuId) {
        User user = (User) request.getAttribute("user");

        obuUserService.deleteObuUser(obuId, userId, user);

        return ResponseEntity.ok().build();
    }
}
