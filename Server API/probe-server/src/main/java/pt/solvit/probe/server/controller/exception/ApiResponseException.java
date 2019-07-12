/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.exception;

import org.springframework.http.HttpStatus;
import pt.solvit.probe.server.controller.model.output.error.ErrorModelOutput;

/**
 *
 * @author AnaRita
 */
public class ApiResponseException extends RuntimeException {

    private ErrorModelOutput error;
    private Exception exception;

    public ApiResponseException(String title, String detail, HttpStatus status, String instance, String type, Exception ex) {
        super(title);
        this.error = new ErrorModelOutput(title, detail, status);
        this.exception = ex;
        if (type != null && !type.equals("")) {
            this.error.setType(type);
        }
        if (instance != null && !instance.equals("")) {
            this.error.setInstance(instance);
        }
    }

    public ErrorModelOutput getError() {
        return this.error;
    }

    public String getExceptionDetail() {
        return this.exception != null ? exception.getMessage() : this.error.getDetail();
    }
}
