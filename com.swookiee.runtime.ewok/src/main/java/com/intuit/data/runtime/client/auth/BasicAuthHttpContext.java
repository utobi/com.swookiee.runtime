package com.intuit.data.runtime.client.auth;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.osgi.service.http.HttpContext;

/**
 * This HttpContext provides a simple Basic-Auth mechanism. Unfortunately it is not yet connected to a real user
 * credentials administration.
 * 
 */
public class BasicAuthHttpContext implements HttpContext {

    @Override
    public boolean handleSecurity(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (request.getHeader("Authorization") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        if (authenticated(request)) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public URL getResource(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMimeType(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    protected boolean authenticated(final HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        final String usernameAndPassword = new String(DatatypeConverter.parseBase64Binary(authHeader.substring(6)));

        final int userNameIndex = usernameAndPassword.indexOf(":");
        final String username = usernameAndPassword.substring(0, userNameIndex);
        final String password = usernameAndPassword.substring(userNameIndex + 1);

        // TODO lpf: get it from some central administration
        return (username.equals("admin") && password.equals("admin"));
    }
}
