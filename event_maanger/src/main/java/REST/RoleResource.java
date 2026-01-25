package REST;

import io.jsonwebtoken.Claims;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/auth/role")
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    @GET
    public Response getRole(@Context ContainerRequestContext requestContext) {

        Claims claims = (Claims) requestContext.getProperty("claims");

        if (claims == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorResponse("No valid token found"))
                    .build();
        }

        String username = claims.getSubject();
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Integer userId = claims.get("userId", Integer.class);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("username", username);
        responseMap.put("userId", userId);
        responseMap.put("isAdmin", isAdmin);
        responseMap.put("role", isAdmin ? "admin" : "user");

        return Response.ok()
                .entity(responseMap)
                .build();
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}

