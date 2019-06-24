/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

//import jdk.nashorn.internal.objects.NativeJSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.controller.api.IDocsController;
import pt.solvit.probe.server.controller.impl.util.LinksAndTemplates;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DocsController implements IDocsController {

    /*
    @Autowired
    private IAuthenticationService authenticationService;
    */

    @Override
    public ResponseEntity<?> getLinks(HttpServletRequest request) {

        LinksAndTemplates lt = new LinksAndTemplates(request.getRequestURI());
        lt.addLink("I'm a link", "I'm the href", "I'm the title");
        lt.addLink("I'm a resource", "I'm the href", "I'm the title");

        return ResponseEntity.ok().body(lt.getAllLinksAndTemplates());
    }


}
