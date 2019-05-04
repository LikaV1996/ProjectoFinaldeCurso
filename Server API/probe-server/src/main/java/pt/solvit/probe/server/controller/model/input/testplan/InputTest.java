/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.testplan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.model.enums.TestType;
import pt.solvit.probe.server.controller.exception.BadRequestException;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Test", description = "Test data tranfer object")
@Validated
public class InputTest {

    private static final Logger LOGGER = Logger.getLogger(InputTest.class.getName());

    @JsonProperty("index")
    @NotNull(message = "An id must be provided.")
    private Long index;

    @JsonProperty("type")
    @NotNull(message = "A type must be provided.")
    private String type;

    @JsonProperty("delay")
    private Long delay;

    @JsonProperty("destination")
    @NotNull(message = "A destination must be provided.")
    private String[] destination;

    @JsonProperty("duration")
    private Long duration;

    @JsonProperty("message")
    private String message;

    @JsonProperty("priority")
    private String priority;

    @ApiModelProperty(example = "1", required = true, value = "Test index")
    public long getIndex() {
        return index;
    }

    @ApiModelProperty(example = "VBC", required = true, value = "Test type", allowableValues = "SMS, P2P, VBC, VGC, MTPY")
    public TestType getType() {
        try {
            return TestType.valueOf(type);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "{0} does not match TestType enum", type);
            return null;
        }
    }

    @ApiModelProperty(example = "60", value = "Test delay")
    public long getDelay() {
        return delay != null ? delay : 0;
    }

    @ApiModelProperty(example = "111111111", required = true, value = "Destination(s) phone number")
    public String[] getDestination() {
        return destination;
    }

    @ApiModelProperty(example = "60",value = "Call duration")
    public Long getDuration() {
        return duration;
    }

    @ApiModelProperty(example = "Message", value = "Message")
    public String getMessage() {
        return message;
    }

    @ApiModelProperty(example = "0", value = "Priority")
    public String getPriority() {
        return priority;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (index == null) {
            throw new BadRequestException("Invalid test.", "Index is null.", "string", "about:blank");
        }
        if (type == null) {
            throw new BadRequestException("Invalid test.", "Type is null.", "string", "about:blank");
        }
        if (destination == null) {
            throw new BadRequestException("Invalid test.", "Destination is null.", "string", "about:blank");
        }
        for (String curDestination : destination) {
            if (curDestination.isEmpty()) {
                throw new BadRequestException("Invalid test.", "Destination is empty.", "string", "about:blank");
            }
            if (!StringUtils.isStrictlyNumeric(curDestination)) {
                throw new BadRequestException("Invalid test.", "Destination is not strictly numeric.", "string", "about:blank");
            }
        }
        TestType testTypeEnum = getType();
        if (testTypeEnum == null) {
            throw new BadRequestException("Invalid test.", "Invalid type.", "string", "about:blank");
        }
        if (testTypeEnum == TestType.SMS) {
            if (duration != null) {
                throw new BadRequestException("Invalid test.", "SMS test can not have duration.", "string", "about:blank");
            }
            if (priority != null) {
                throw new BadRequestException("Invalid test.", "SMS test can not have priority.", "string", "about:blank");
            }
        } else {
            if (message != null) {
                throw new BadRequestException("Invalid test.", "Test " + type + " can not have message.", "string", "about:blank");
            }
            if (testTypeEnum != TestType.MPTY && destination.length != 1) {
                throw new BadRequestException("Invalid test.", "Test " + type + " can not have more than one destination.", "string", "about:blank");
            }
            if (priority != null) {
                if (testTypeEnum == TestType.P2P || testTypeEnum == TestType.MPTY) {
                    throw new BadRequestException("Invalid test.", "Test " + type + " can not have priority.", "string", "about:blank");
                }
                if (!ControllerUtil.validatePriority(priority)) {
                    throw new BadRequestException("Invalid test.", "Priority invalid.", "string", "about:blank");
                }
            }
        }
    }
}
