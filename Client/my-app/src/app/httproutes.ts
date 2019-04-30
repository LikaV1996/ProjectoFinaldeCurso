export class Routes{
    private basePath = "http://localhost:8080/api/v1";

    login = this.basePath + "/login";

    getUsers = this.basePath + "/users";
    //getUserByID = this.basePath + "/user/:id";
    //getUserByName = this.basePath + "/user/:username";
    getUserByParam = this.basePath + "/user/:param";
    createUser = this.basePath + "/users";
    suspendUser = this.basePath + "/user/:id/suspend";

    getOBUs = this.basePath + "/obus";
    getOBUByID = this.basePath + "/obu/:id";
}
