/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.impl.util.Profile;
import pt.solvit.probe.server.controller.model.input.testplan.InputTestPlan;
import pt.solvit.probe.server.model.ObuTestPlan;
import pt.solvit.probe.server.model.Setup;
import pt.solvit.probe.server.model.TestPlan;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author AnaRita
 */
public interface ITestPlanController {

    @ApiOperation(value = "Returns all test plans", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of test plans."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_TESTPLAN, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<TestPlan>> getAllTestPlans(HttpServletRequest request);

    @ApiOperation(value = "Creates a test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 201, message = "Test plan was created successfully. To get the created configuration, make a HTTP GET request to the URI provided in header location of this response."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_TESTPLAN, 
            consumes = {MediaType.APPLICATION_JSON_VALUE}, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<TestPlan> createTestPlan(HttpServletRequest request, @RequestBody InputTestPlan body);

    @ApiOperation(value = "Returns a test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of test plan."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_TESTPLAN_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<TestPlan> getTestPlan(HttpServletRequest request,  @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Updates a test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The test plan was successfully updated."),
                    @ApiResponse(code = 401, message = "There was an error with authentication."),
                    @ApiResponse(code = 404, message = "The test plan with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PUT,
            value = AppConfiguration.URL_TESTPLAN_ID,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<TestPlan> updateTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId, @RequestBody InputTestPlan body);

    @ApiOperation(value = "Deletes a test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The test plan was successfully deleted."),
                @ApiResponse(code = 401, message = "There was an error with authentication."),
                @ApiResponse(code = 404, message = "The test plan with the requested id was not found.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_TESTPLAN_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> deleteTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Returns all setups from test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The test plan was successfully deleted."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_TESTPLAN_SETUP, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<Setup>> getAllSetupsFromTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Removes all setups from test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "All setups were successfully removed from test plan."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_TESTPLAN_SETUP, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> removeAllSetupsFromTestPlan(HttpServletRequest request, @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Adds setup to test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The setup was successfully added to test plan."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_TESTPLAN_SETUP_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> addSetupToTestPlan(HttpServletRequest request,  @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId);

    @ApiOperation(value = "Returns setup from test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of test plan setup."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_TESTPLAN_SETUP_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Setup> getSetupFromTestPlan(HttpServletRequest request,  @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId);

    @ApiOperation(value = "Removes setup from test plan", tags = {"Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The setup was successfully removed from test plan."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = AppConfiguration.URL_TESTPLAN_SETUP_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> removeSetupFromTestPlan(HttpServletRequest request,  @PathVariable("test-plan-id") long testPlanId, @PathVariable("setup-id") long setupId);

    @ApiOperation(value = "Returns all test plans from obu", tags = {"Obu Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "A list with partial representation of obu test plans."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ShortView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_TESTPLAN, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<ObuTestPlan>> getAllTestPlansFromObu(HttpServletRequest request,  @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Removes all test plans from obu", tags = {"Obu Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "All test plans were successfully removed from obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_OBU_TESTPLAN, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> removelAllTestPlansFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId);

    @ApiOperation(value = "Adds test plan to obu", tags = {"Obu Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The test plan was successfully added to obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.POST, 
            value = AppConfiguration.URL_OBU_TESTPLAN_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> addTestPlanToObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Returns test plan from obu", tags = {"Obu Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Representation of obu test plan."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @JsonView(Profile.ExtendedView.class)
    @RequestMapping(
            method = RequestMethod.GET, 
            value = AppConfiguration.URL_OBU_TESTPLAN_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ObuTestPlan> getTestPlanFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Cancel test plan from obu", tags = {"Obu Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The test plan was successfully canceled from obu."),
                @ApiResponse(code = 202, message = "A cancel request will be sent to obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = AppConfiguration.URL_OBU_TESTPLAN_ID_CANCEL, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> cancelTestPlanFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId);

    @ApiOperation(value = "Removes test plan from obu", tags = {"Obu Test Plan",})
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "The test plan was successfully removed from obu."),
                @ApiResponse(code = 400, message = "Invalid input. A description of the error will be in the returned JSON."),
                @ApiResponse(code = 401, message = "There was an error with authentication.")
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE, 
            value = AppConfiguration.URL_OBU_TESTPLAN_ID, 
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Void> removeTestPlanFromObu(HttpServletRequest request, @PathVariable("obu-id") long obuId, @PathVariable("test-plan-id") long testPlanId);
}
