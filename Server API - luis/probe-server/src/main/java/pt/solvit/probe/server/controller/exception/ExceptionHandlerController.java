/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.exception;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.solvit.probe.server.controller.model.output.error.ErrorModelOutput;
import pt.solvit.probe.server.controller.impl.util.HttpExceptionMapper;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.repository.exception.RepositoryException;
import pt.solvit.probe.server.service.exception.DomainException;
import pt.solvit.probe.server.service.exception.InternalException;
import pt.solvit.probe.server.service.impl.ServerLogService;

/**
 *
 * @author AnaRita
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger LOGGER = Logger.getLogger(ExceptionHandlerController.class.getName());

    /*
    @Autowired
    ServerLogService serverLogService;
    */


    /**
     * Bad Request. Http status = 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadRequestException.class})
    protected ErrorModelOutput handleBadRequestException(HttpServletRequest request, BadRequestException ex) {
        LOGGER.log(Level.SEVERE, ex.getExceptionDetail());
        forServerLog(request, ex.getExceptionDetail());
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.BAD_REQUEST.toString(), ex.getExceptionDetail());
        return ex.getError();
    }

    /**
     * Unauthorized. Http status = 401
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {UnauthorizedException.class})
    protected ErrorModelOutput handleUnauthorized(HttpServletRequest request, UnauthorizedException ex) {
        LOGGER.log(Level.SEVERE, ex.getExceptionDetail());
        forServerLog(request, ex.getExceptionDetail());
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.UNAUTHORIZED.toString(), ex.getExceptionDetail());
        return ex.getError();
    }

    /**
     * Not Acceptable. Http status = 406
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = {NotAcceptableException.class})
    protected ErrorModelOutput handleNotAcceptableException(HttpServletRequest request, NotAcceptableException ex) {
        LOGGER.log(Level.SEVERE, ex.getExceptionDetail());
        forServerLog(request, ex.getExceptionDetail());
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.NOT_ACCEPTABLE.toString(), ex.getExceptionDetail());
        return ex.getError();
    }

    /**
     * Default error handler. All unheld exceptions will be treated with Http
     * status = 500 DataAccessException, JsonSyntaxException,
     * NullPointerException, FileNotFoundException, etc
     * UnsupportedEncodingException
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorModelOutput handleThrowable(HttpServletRequest request, final Throwable ex) {
        LOGGER.log(Level.SEVERE, "", ex);
        forServerLog(request, "Internal server error");
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal server error");
        return new ErrorModelOutput("Internal server error.", "An unexpected internal server error occured.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * INTERNAL ERROR RELATED EXCEPTIONS HANDLED HERE!
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {InternalException.class})
    protected ErrorModelOutput handleInternalException(HttpServletRequest request, DomainException ex) {
        LOGGER.log(Level.SEVERE, "", ex);
        forServerLog(request, ex.getDetail());
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.BAD_REQUEST.toString(), ex.getDetail());
        return HttpExceptionMapper.map(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * DOMAIN RELATED EXCEPTIONS HANDLED HERE!
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DomainException.class})
    protected ErrorModelOutput handleDomainException(HttpServletRequest request, DomainException ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage());
        forServerLog(request, ex.getDetail());
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.BAD_REQUEST.toString(), ex.getDetail());
        return HttpExceptionMapper.map(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * REPOSITORY RELATED EXCEPTIONS HANDLED HERE!
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {RepositoryException.class})
    protected ErrorModelOutput handleRepositoryException(HttpServletRequest request, RepositoryException ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage());
        forServerLog(request, ex.getDetail());
        //makeServerLog(request.getRequestURI(), (User)request.getAttribute("user"), HttpStatus.BAD_REQUEST.toString(), ex.getDetail());
        return HttpExceptionMapper.map(ex, request, HttpStatus.BAD_REQUEST);
    }



    private void forServerLog(HttpServletRequest request, String detail){
        request.setAttribute("response_detail", detail);
    }
}
