package hu.frankdavid.hotel.security;

import hu.frankdavid.hotel.service.LoggedInUser;
import hu.frankdavid.hotel.entity.Role;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethodInvoker;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@ServerInterceptor
public class AuthorizationInterceptor implements ContainerRequestFilter {
    @Inject
    LoggedInUser loggedInUser;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker)
                context.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        Role[] rolesAllowed = getRolesAllowed(method);
        if(rolesAllowed != null) {
            boolean allowed = false;
            if(rolesAllowed.length == 0)
                allowed = loggedInUser.notEmpty();
            for (Role role : rolesAllowed) {
                if(loggedInUser.hasRole(role))
                    allowed = true;
            }
            if(!allowed) {
                context.abortWith(error401Response());
            }
        }
    }

    private Role[] getRolesAllowed(Method method) {
        RolesAllowed annotation = method.getAnnotation(RolesAllowed.class);
        if(annotation == null) {
            annotation = method.getDeclaringClass().getAnnotation(RolesAllowed.class);
        }
        return annotation != null ? annotation.value() : null;
    }

    private Response error401Response() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
