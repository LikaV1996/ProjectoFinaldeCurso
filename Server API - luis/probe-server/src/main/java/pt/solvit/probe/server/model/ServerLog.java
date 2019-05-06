/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import pt.solvit.probe.server.model.enums.AccessType;
import static pt.solvit.probe.server.util.DateUtil.PRETTY_DATE_FORMATTER;

/**
 *
 * @author AnaRita
 */
public class ServerLog {

    private static final String ERROR_DATE = "Unknown";

    private Long id;
    private LocalDateTime date;
    private String accessPath;
    private AccessType accessType;
    private String accessUser;
    private LocalDateTime responseDate;
    private String status;
    private String detail;

    public ServerLog(Long id, LocalDateTime date, AccessType accessType, String accessPath, String accessUser,
            LocalDateTime responseDate, String status, String detail) {
        this.id = id;
        this.date = date;
        this.accessType = accessType;
        this.accessPath = accessPath;
        this.accessUser = accessUser;
        this.responseDate = responseDate;
        this.status = status;
        this.detail = detail;
    }

    public ServerLog(LocalDateTime date, AccessType accessType, String accessPath, String accessUser) {
        this.date = date;
        this.accessType = accessType;
        this.accessPath = accessPath;
        this.accessUser = accessUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String getAccessUser() {
        return accessUser;
    }

    public ServerLog setAccessUser(String accessUser) {
        this.accessUser = accessUser;
        return this;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

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
        sb.append("  |  ").append(accessUser);
        for (int userSize = accessUser.length(); userSize < maxUserSize; userSize++) {
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
