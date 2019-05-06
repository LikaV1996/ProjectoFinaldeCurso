export class Routes{
    private basePath = "http://localhost:8080/backoffice";

    login = this.basePath + "/login";
    loginUser = this.basePath + "/login/user"

    getUsers = this.basePath + "/users";
    getUserByID = this.basePath + "/user/:id";
    //getUserByName = this.basePath + "/user/:username";
    getUserByParam = this.basePath + "/user/:param";
    createUser = this.basePath + "/user";
    suspendUser = this.basePath + "/user/:id/suspend";
    updateUser = this.basePath + "user/:id/edit"

    getOBUs = this.basePath + "/obus";
    getOBUByID = this.basePath + "/obu/:id";
    createObu = this.basePath + "/obus";

    getConfigs = this.basePath + "/configs";
}
