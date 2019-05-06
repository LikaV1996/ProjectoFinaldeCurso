/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.exception;

/**
 *
 * @author AnaRita
 */
public class RepositoryException extends RuntimeException {

    private String title;
    private String detail;

    public RepositoryException(String title, String detail) {
        super(detail);
        this.title = title;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
