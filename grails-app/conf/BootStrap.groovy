class BootStrap {

    def init = {servletContext ->
        User.withTransaction {
            User user = new User(
                    username: "admin",
                    userRealName: "Admin",
                    passwd: "d033e22ae348aeb5660fc2140aec35850c4da997",
                    email: "admin@epseelon"
            )
            user.save()

            Role role = new Role(
                    description: "Administrator",
                    authority: "ROLE_ADMIN"
            )
            role.save()

            user.addToAuthorities(role)
        }
    }

    def destroy = {
    }


} 