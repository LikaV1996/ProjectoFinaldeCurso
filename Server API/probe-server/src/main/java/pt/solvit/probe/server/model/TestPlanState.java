/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDateTime;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.TestPlanStateEnum;
import static pt.solvit.probe.server.util.DateUtil.ISO8601_DATE_FORMATTER;

/**
 *
 * @author AnaRita
 */
public class TestPlanState {

    @JsonView(Profile.ShortView.class)
    @JsonProperty("state")
    private TestPlanStateEnum state;

    @JsonView(Profile.ShortView.class)
    @JsonProperty("date")
    private LocalDateTime date;

    public TestPlanState(TestPlanStateEnum state, LocalDateTime date) {
        this.state = state;
        this.date = date;
    }

    public TestPlanStateEnum getState() {
        return state;
    }

    @JsonIgnore
    public LocalDateTime getLocalDateTime() {
        return date;
    }

    public String getDate() {
        return ISO8601_DATE_FORMATTER.format(date);
    }
}
