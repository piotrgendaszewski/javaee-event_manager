package REST;

import dao.hibernate.UserHibernate;
import jakarta.ws.rs.*;
import service.UserService;

@Path("/users")
@Produces("application/json")
@Consumes("application/json")
public class UserResource {

    private final UserService userService;

    public UserResource() {
        this.userService = new UserService(new UserHibernate());
    }

    @GET
    public String hello() {
        return "Hello, User!";
    }
}
