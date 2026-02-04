package REST;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.AuthService;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;

    /**
     * Constructor with dependency injection
     * Used by Jersey for request-scoped instantiation
     */
    public AuthResource() {
        // Create single instance per request
        this.authService = new AuthService(new dao.hibernate.UserHibernate());
    }

    /**
     * Constructor for testing with injected AuthService
     * @param authService injected AuthService instance
     */
    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User registration endpoint
     * POST /api/auth/register
     */
    @POST
    @Path("/register")
    public Response register(RegistrationRequest request) {
        try {
            if (request.getLogin() == null || request.getPassword() == null || request.getEmail() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse("Login, password, and email are required"))
                        .build();
            }

            authService.registerUser(
                    request.getLogin(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getAddress(),
                    request.getPhoneNumber()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("login", request.getLogin());

            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(errorResponse(e.getMessage()))
                    .build();
        } catch (WebApplicationException e) {
            // Re-throw WebApplicationException from SQLErrorHandler
            throw e;
        } catch (Exception e) {
            // Try to handle as SQL error
            try {
                SQLErrorHandler.handleSQLException(e, "register user");
            } catch (WebApplicationException sqlEx) {
                throw sqlEx;
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse("Registration failed: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * User login endpoint
     * POST /api/auth/login
     */
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            System.out.println("[AuthResource] Login request received");
            System.out.println("[AuthResource] Request: " + request);
            System.out.println("[AuthResource] Login: " + (request != null ? request.getLogin() : "null"));
            System.out.println("[AuthResource] Password: " + (request != null ? "***" : "null"));

            if (request == null) {
                System.out.println("[AuthResource] Request object is null!");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse("Request body is empty"))
                        .build();
            }

            if (request.getLogin() == null || request.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorResponse("Login and password are required"))
                        .build();
            }

            String token = authService.login(request.getLogin(), request.getPassword());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return Response.ok()
                    .entity(response)
                    .build();

        } catch (IllegalArgumentException e) {
            System.out.println("[AuthResource] Login failed: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorResponse(e.getMessage()))
                    .build();
        } catch (Exception e) {
            System.out.println("[AuthResource] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse("Login failed: " + e.getMessage()))
                    .build();
        }
    }

    private Map<String, String> errorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    // Inner classes for request payloads
    public static class RegistrationRequest {
        private String login;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String address;
        private String phoneNumber;

        // Getters and Setters
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }

    public static class LoginRequest {
        private String login;
        private String password;

        // Getters and Setters
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}

