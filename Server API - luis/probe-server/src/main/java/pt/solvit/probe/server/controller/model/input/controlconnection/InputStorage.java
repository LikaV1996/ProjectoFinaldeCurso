/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.model.input.controlconnection;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pt.solvit.probe.server.controller.exception.BadRequestException;

/**
 *
 * @author AnaRita
 */
@ApiModel(value = "Storage", description = "Storage data tranfer object")
@Validated
public class InputStorage {

    @JsonProperty("total")
    @NotNull(message = "A total must be provided.")
    private Long total;

    @JsonProperty("free")
    @NotNull(message = "A free must be provided.")
    private Long free;

    @JsonProperty("usable")
    @NotNull(message = "A usable must be provided.")
    private Long usable;

    @ApiModelProperty(required = true, value = "Total space")
    public long getTotal() {
        return total;
    }

    @ApiModelProperty(required = true, value = "Free space")
    public long getFree() {
        return free;
    }

    @ApiModelProperty(required = true, value = "Usable space")
    public long getUsable() {
        return usable;
    }

    @ApiModelProperty(hidden = true)
    public void validate() {
        if (free == null) {
            throw new BadRequestException("Invalid storage.", "Free is null.", "/probs/storage-null-params", "about:blank");
        }
        if (usable == null) {
            throw new BadRequestException("Invalid storage.", "Usable is null.", "/probs/storage-null-params", "about:blank");
        }
    }
}
