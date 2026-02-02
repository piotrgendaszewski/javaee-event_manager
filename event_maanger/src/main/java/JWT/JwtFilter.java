package JWT;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();
        
        System.out.println("[JWT Filter] Path: " + path + ", Method: " + method);

        // Allow OPTIONS requests (for CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("[JWT Filter] OPTIONS request - allowing without token");
            return;
        }

        // Allow public endpoints without authentication
        // /public/* - public event browsing
        if (path.startsWith("public/")) {
            System.out.println("[JWT Filter] Public endpoint - allowing access without token");
            return;
        }

        // Allow only auth/login and auth/register without token
        // Other auth/* endpoints require JWT
        // /private/* endpoints also require JWT (for authenticated users)
        // /admin/* endpoints require JWT (will be validated here, admin check done in AdminResource)
        if (path.equals("auth/login") || path.equals("auth/register")) {
            System.out.println("[JWT Filter] Auth login/register endpoint - allowing access without token");
            return;
        }

        // All other endpoints require JWT authentication
        String authHeader = requestContext.getHeaderString("Authorization");
        
        System.out.println("[JWT Filter] Authorization header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[JWT Filter] Missing or invalid Authorization header - blocking request");
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Missing or invalid Authorization header\"}")
                    .build()
            );
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            Claims claims = JwtUtil.validateToken(token);

            requestContext.setProperty("claims", claims);
            requestContext.setProperty("userId", claims.get("userId", Integer.class));
            requestContext.setProperty("username", claims.getSubject());
            requestContext.setProperty("isAdmin", claims.get("isAdmin", Boolean.class));
            
            System.out.println("[JWT Filter] Token validated successfully for user: " + claims.getSubject());

        } catch (RuntimeException e) {
            System.out.println("[JWT Filter] Token validation failed: " + e.getMessage());
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build()
            );
        }
    }
}





