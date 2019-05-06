/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model.enums;

/**
 *
 * @author AnaRita
 */
public enum UserProfile {
    ADMIN("Admin", 2),
    SUPER_USER("Super User", 1),
    NORMAL_USER("Normal User", 0);

    private final String name;
    private final int level;

    private UserProfile(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
