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
    updateUser = this.basePath + "user/:id/edit"

    getHardwares = this.basePath + "/hardware";
    getHardwareById = this.basePath + "/hardware/:id";
    updateHardware = this.basePath + "/hardware/:id";
    createHardware = this.basePath + "/hardware";

    getOBUs = this.basePath + "/obu";
    getOBUByID = this.basePath + "/obu/:id";
    createObu = this.basePath + "/obus";
    updateObu = this.basePath + "/obu/:id";

    getConfigs = this.basePath + "/config";
    getConfigById = this.basePath + "/config/:id"
    createConfig = this.basePath +  "/config";

    getTestPlans = this.basePath + "/test-plan";
    getTestPlanById = this.basePath + "/test-plan/:id";
}
