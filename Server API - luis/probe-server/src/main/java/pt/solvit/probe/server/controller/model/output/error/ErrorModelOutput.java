/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.output.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class ErrorModelOutput {

    @JsonProperty("type")
    protected String type = "about:blank";

    @JsonProperty("title")
    protected String title;

    @JsonProperty("detail")
    protected String detail;

    @JsonProperty("status")
    protected Integer status;

    @JsonProperty("instance")
    protected String instance = "about:blank";

    /*
    @JsonProperty("invalid-params")
    protected HashMap<String,Object> invalidParams;
    */

    public ErrorModelOutput(String title, String detail, HttpStatus status) {
        this.title = title;
        this.detail = detail;
        this.status = status.value();
    }

    public ErrorModelOutput(String message, Throwable ex, HttpServletRequest request, HttpStatus status) {
        this(message, ex.getMessage(), status);
        this.instance = request.getRequestURI();
    }

    @ApiModelProperty(example = "about:blank", value = "")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ApiModelProperty(required = true, value = "")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ApiModelProperty(required = true, value = "")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @ApiModelProperty(required = true, value = "")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ApiModelProperty(example = "", value = "")
    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    /*
    public HashMap<String, Object> getInvalidParams() {
        return invalidParams;
    }

    public void setInvalidParams(HashMap<String, Object> invalidParams) {
        this.invalidParams = invalidParams;
    }
    */
}
