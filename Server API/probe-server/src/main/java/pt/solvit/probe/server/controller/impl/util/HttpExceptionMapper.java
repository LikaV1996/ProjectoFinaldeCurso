/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import pt.solvit.probe.server.controller.model.output.error.ErrorModelOutput;
import pt.solvit.probe.server.repository.exception.RepositoryException;
import pt.solvit.probe.server.service.exception.DomainException;

/**
 *
 * @author AnaRita
 */
public class HttpExceptionMapper {

    public static ErrorModelOutput map(DomainException ex, HttpServletRequest req, HttpStatus code) {
        return new ErrorModelOutput(ex.getTitle(), ex, req, code);
    }

    public static ErrorModelOutput map(RepositoryException ex, HttpServletRequest req, HttpStatus code) {
        return new ErrorModelOutput(ex.getTitle(), ex, req, code);
    }
}
