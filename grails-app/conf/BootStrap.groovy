class BootStrap {

    def init = { servletContext ->
        User.withTransaction {
            User user = new User(
                    username: "admin",
                    passwd: "d033e22ae348aeb5660fc2140aec35850c4da997",
                    enabled: true
            ).save()

            Role role = new Role(
                    description: "Administrator",
                    authority: "ROLE_ADMIN"
            ).save()

            UserRole.create user, role
        }
    }
}
