package com.intuit.data.runtime.client.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.intuit.data.runtime.client.servlet.util.ServletUtil;
import com.intuit.data.runtime.resource.representation.BundleRepresentation;

/**
 * This {@link HttpServlet} implements the bundle list representation of the Bundles Resource (5.1.2) of the OSGi
 * RFC-182 draft version 8. @see <a href =https://github.com/osgi/design/tree/master/rfcs/rfc0182>
 * https://github.com/osgi/design/tree/master/rfcs/rfc0182</a>
 * 
 */
public class BundlesRepresentationsServlet extends HttpServlet {

    public static final String ALIAS = "/framework/bundles/representations";
    private static final long serialVersionUID = 8101122973161058308L;
    private final BundleContext bundleContext;

    public BundlesRepresentationsServlet(final BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        ServletUtil.jsonResponse(response, getBundleRepresentationsList());

    }

    public List<BundleRepresentation> getBundleRepresentationsList() {

        final Bundle[] bundles = bundleContext.getBundles();
        final List<BundleRepresentation> bundleList = new ArrayList<>();

        for (final Bundle bundle : bundles) {
            bundleList.add(new BundleRepresentation(bundle.getBundleId(), bundle.getLastModified(),
                    bundle.getLocation(), bundle.getState(), bundle.getSymbolicName(), bundle.getVersion().toString()));

        }
        return bundleList;
    }
}
