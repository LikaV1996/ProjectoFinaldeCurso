export class Routes{
    private basePath = "http://localhost:8080/api/v1/frontoffice";

    login = this.basePath + "/login";
    loginUser = this.basePath + "/login/user"

    getUsers = this.basePath + "/users";
    getUserByID = this.basePath + "/user/:id";
    //getUserByName = this.basePath + "/user/:username";
    getUserByParam = this.basePath + "/user/:param";
    createUser = this.basePath + "/user";
    suspendUser = this.basePath + "/user/:id/suspend";
    updateUser = this.basePath + "/user/:id";
    getUserObus = this.basePath + "/obu-user/user/:id"
    addObuToUser = this.basePath + "/obu-user";
    deleteObuFromUser = this.basePath + "/obu-user/obu/:idObu/user/:idUser"

    getServerLogs = this.basePath + "/server-log";
    getTestLogs = this.basePath + "/obu/:id/test-log";
    getSysLogs = this.basePath + "/obu/:id/system-log";

    getHardwares = this.basePath + "/hardware";
    getHardwareById = this.basePath + "/hardware/:id";
    updateHardware = this.basePath + "/hardware/:id";
    createHardware = this.basePath + "/hardware";
    deleteHardware = this.basePath + "/hardware/:id";

    getOBUs = this.basePath + "/obu";
    getOBUByID = this.basePath + "/obu/:id";
    createObu = this.basePath + "/obu";
    updateObu = this.basePath + "/obu/:id";
    getStatusFromOBU = this.basePath + "/obu/:id/status";
    deleteOBU = this.basePath + "/obu/:id";

    getConfigs = this.basePath + "/config";
    getConfigById = this.basePath + "/config/:id"
    createConfig = this.basePath +  "/config";
    updateConfig = this.basePath + "/config/:id";
    deleteConfig = this.basePath + "/config/:id";

    getTestPlans = this.basePath + "/test-plan";
    getTestPlanById = this.basePath + "/test-plan/:id";
    createTestPlan = this.basePath + "/test-plan";
    updateTestPlan = this.basePath + "/test-plan/:id";
    deleteTestPlan = this.basePath + "/test-plan/:id";
    getTestPlanSetups = this.basePath + "/test-plan/:id/setup"
    addSetupToTestPlan = this.basePath + "/test-plan/:idTestPlan/setup/:idSetup"
    deleteSetupFromTestPlan = this.basePath + "/test-plan/:idTestPlan/setup/:idSetup"

    getSetups = this.basePath + "/setup";
    getSetupById = this.basePath + "/setup/:id";
    createSetup = this.basePath + "/setup";
    updateSetup = this.basePath + "/setup/:id";
    deleteSetup = this.basePath + "/setup/:id";

    getObuConfigs = this.basePath + "/obu/:id/config"
    addConfigToObu = this.basePath + "/obu/:idObu/config/:idConfig"
    deleteConfigFromObu = this.basePath + "/obu/:idObu/config/:idConfig"

    getObuTestPlans = this.basePath + "/obu/:id/test-plan"
    addTestPlanToObu = this.basePath + "/obu/:idObu/test-plan/:idTestPlan"
    deleteTestPlanFromObu = this.basePath + "/obu/:idObu/test-plan/:idTestPlan"


}
