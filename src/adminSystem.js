class AdminSystem {
    constructor() {
        this.users = [];
        this.roles = [];
    }

    addUser(user) {
        this.users.push(user);
    }

    configureRole(role) {
        this.roles.push(role);
    }

    getLogs() {
        // 返回操作日志
    }
}