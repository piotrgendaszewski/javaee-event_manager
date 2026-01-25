package REST;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import JWT.JwtFilter;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class EventMangerApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Register JWT Filter
        classes.add(JwtFilter.class);

        // Register Auth Resources
        classes.add(AuthResource.class);
        classes.add(RoleResource.class);

        // Register Public Resources (no auth required)
        classes.add(PublicEventResource.class);

        // Register Authenticated User Resources
        classes.add(AuthenticatedUserResource.class);

        // Register Admin Resources
        classes.add(AdminResource.class);

        // Register Other Resources
        classes.add(UserResource.class);
        classes.add(EventResource.class);
        classes.add(LocationResource.class);
        classes.add(RoomResource.class);
        classes.add(TicketResource.class);
        classes.add(EventReviewResource.class);

        return classes;
    }
}