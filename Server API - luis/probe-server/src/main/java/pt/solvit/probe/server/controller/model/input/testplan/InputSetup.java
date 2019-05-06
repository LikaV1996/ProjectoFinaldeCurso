/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.testplan;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.model.enums.ModemType;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Setup", description = "Setup data tranfer object")
@Validated
public class InputSetup {

    private static final Logger LOGGER = Logger.getLogger(InputSetup.class.getName());

    @JsonProperty("modemType")
    @NotNull(message = "A modemType must be provided.")
    private String modemType;

    @JsonProperty("scanning")
    @Valid
    private Scanning scanning;

    @JsonProperty("tests")
    @Valid
    private List<InputTest> tests;

    @ApiModelProperty(example = "GSMR", required = true, value = "Modem type", allowableValues = "PLMN, GSMR")
    public ModemType getModemType() {
        try {
            return ModemType.valueOf(modemType);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match ModemType enum", modemType);
            return null;
        }
    }

    @ApiModelProperty(value = "Scanning properties")
    public Scanning getScanning() {
        return scanning;
    }

    @ApiModelProperty(value = "Test list")
    public List<InputTest> getTests() {
        return tests;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (modemType == null) {
            throw new BadRequestException("Invalid setup.", "ModemType is null.", "string", "about:blank");
        }
        if (getModemType() == null) {
            throw new BadRequestException("Invalid setup.", "Invalid modemType.", "string", "about:blank");
        }
        if (scanning != null) {
            scanning.validate();
        }
        if (tests != null) {
            for (InputTest curTest : tests) {
                curTest.validate();
            }
            int i = 0;
            for (InputTest curTest1 : tests) {
                int j = 0;
                for (InputTest curTest2 : tests) {
                    if (i != j) {
                        if (curTest1.getIndex() == curTest2.getIndex()) {
                            throw new BadRequestException("Invalid setup.", "Different tests can not have the same index.", "string", "about:blank");
                        }
                    }
                    j++;
                }
                i++;
            }
        }
    }
}