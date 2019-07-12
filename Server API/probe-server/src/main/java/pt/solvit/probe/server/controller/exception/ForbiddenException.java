/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.exception;

import org.springframework.http.HttpStatus;
import pt.solvit.probe.server.controller.model.output.error.ErrorModelOutput;

public class ForbiddenException extends ApiResponseException {

    public ForbiddenException(String title, String detail, HttpStatus status, String instance, String type, Exception ex) {
        super(title, detail, status, instance, type, ex);
    }

    public ForbiddenException(ErrorModelOutput error) {
        this(error.getTitle(), error.getDetail(), HttpStatus.valueOf(error.getStatus()), error.getInstance(), error.getType(), null);
    }

    public ForbiddenException(String title, String detail, String instance, String type) {
        this(title, detail, HttpStatus.FORBIDDEN, instance, type, null);
    }
}
