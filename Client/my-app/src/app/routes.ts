export class Routes{
    private basePath = "http://localhost:8080/api/v1";

    login = this.basePath + "/login";

    getUsers = this.basePath + "/users";
    getUserByID = this.basePath + "/user/:id";
    createUser = this.basePath + "/user";
    suspendUser = this.basePath + "/user/:id/suspend";
}
