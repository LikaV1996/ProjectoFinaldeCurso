/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import pt.solvit.probe.server.model.enums.AccessType;

import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;
import static pt.solvit.probe.server.util.DateUtil.PRETTY_DATE_FORMATTER;

/**
 *
 * @author AnaRita
 */
public class ServerLog {

    private static final String ERROR_DATE = "Unknown";

    @JsonProperty("id")
    private Long id;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("accessPath")
    private String accessPath;

    @JsonProperty("accessType")
    private AccessType accessType;

    @JsonProperty("accessorName")
    private String accessorName;

    @JsonProperty("responseDate")
    private LocalDateTime responseDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("detail")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detail;

    public ServerLog(Long id, LocalDateTime date, AccessType accessType, String accessPath, String accessorName,
            LocalDateTime responseDate, String status, String detail) {
        this.id = id;
        this.date = date;
        this.accessType = accessType;
        this.accessPath = accessPath;
        this.accessorName = accessorName;
        this.responseDate = responseDate;
        this.status = status;
        this.detail = detail;
    }

    public ServerLog(LocalDateTime date, AccessType accessType, String accessPath, String accessorName) {
        this.date = date;
        this.accessType = accessType;
        this.accessPath = accessPath;
        this.accessorName = accessorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public LocalDateTime getDateLocalDateTime() {
        return date;
    }

    public String getDate() { return ISO8601_DATE_FORMATTER.format(date); }

    public AccessType getAccessType() {
        return accessType;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String getAccessorName() {
        return accessorName;
    }

    public ServerLog setAccessorName(String accessorName) {
        this.accessorName = accessorName;
        return this;
    }

    @JsonIgnore
    public LocalDateTime getResponseDateLocalDateTime() {
        return responseDate;
    }

    public String getResponseDate() { return ISO8601_DATE_FORMATTER.format(responseDate); }

    public String getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public ServerLog setResponse(HttpStatus status, String detail) {
        this.responseDate = LocalDateTime.now();
        this.status = String.valueOf(status.value()) + " " + status.getReasonPhrase();
        this.detail = detail;
        return this;
    }

    public ServerLog setResponse(HttpStatus status) {
        this.responseDate = LocalDateTime.now();
        this.status = String.valueOf(status.value()) + " " + status.getReasonPhrase();
        return this;
    }

    public String toLogString(int maxTimeSize, int maxUserSize, int maxUriSize, int maxStatusSize) {

        StringBuilder sb = new StringBuilder();

        sb.append(" ").append(PRETTY_DATE_FORMATTER.format(date));
        sb.append("  |  ").append(accessorName);
        for (int userSize = accessorName.length(); userSize < maxUserSize; userSize++) {
            sb.append(" ");
        }
        sb.append("  |  ").append(accessPath);
        for (int uriSize = accessPath.length(); uriSize < maxUriSize; uriSize++) {
            sb.append(" ");
        }
        sb.append("  |  ");
        if (responseDate == null) {
            sb.append(ERROR_DATE);
            for (int timeSize = ERROR_DATE.length(); timeSize < maxTimeSize; timeSize++) {
                sb.append(" ");
            }
            sb.append("  |  ").append("500 Internal Server Error");
        } else {
            sb.append(PRETTY_DATE_FORMATTER.format(responseDate)).append("  |  ").append(status);
            if (detail != null) {
                sb.append(": ").append(detail);
            }
        }
        return sb.toString();
    }
}
