/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.model.enums.ModemType;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "SIM", description = "SIM data tranfer object")
@Validated
public class SimCard {

    private static final Logger LOGGER = Logger.getLogger(SimCard.class.getName());

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("modemType")
    @NotNull(message = "A modemType must be provided.")
    private String modemType;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("msisdn")
    private String msisdn;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("simPin")
    private String simPin;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("simPuk")
    private String simPuk;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("iccid")
    private String iccid;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("apn")
    private String apn;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("apnUser")
    private String apnUser;

    @JsonView(Profile.ExtendedView.class)
    @JsonProperty("apnPass")
    private String apnPass;

    @ApiModelProperty(example = "GSMR", required = true, value = "Modem type", allowableValues = "PLMN, GSMR")
    public ModemType getModemType() {
        try {
            return ModemType.valueOf(modemType);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match ModemType enum", modemType);
            return null;
        }
    }

    @ApiModelProperty(example = "msisdn", required = true, value = "Mobile Station International Subscriber Directory Number")
    public String getMsisdn() {
        return msisdn;
    }

    @ApiModelProperty(example = "pin", required = true, value = "SIM PIN")
    public String getSimPin() {
        return simPin;
    }

    @ApiModelProperty(example = "puk", required = true, value = "SIM PUK")
    public String getSimPuk() {
        return simPuk;
    }

    @ApiModelProperty(example = "iccid", value = "SIM ICCID")
    public String getIccid() {
        return iccid;
    }

    @ApiModelProperty(example = "acess point", required = true, value = "Access Point Name")
    public String getApn() {
        return apn;
    }

    @ApiModelProperty(example = "username", required = true, value = "APN username")
    public String getApnUser() {
        return apnUser;
    }

    @ApiModelProperty(example = "password", required = true, value = "APN password")
    public String getApnPass() {
        return apnPass;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (modemType == null) {
            throw new BadRequestException("Invalid component.", "ModemType is null.", "/probs/simcard-null-params", "about:blank");
        }
        if (getModemType() == null) {
            throw new BadRequestException("Invalid component.", "Invalid modemType.", "/probs/simcard-invalid-modemtype", "about:blank");
        }
    }
}
