package com.intuit.data.runtime.client

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.junit.Test
import org.osgi.framework.Bundle
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference

import com.intuit.data.runtime.client.servlet.ServicesServlet

public class ServicesServletTest {

    @Test
    void 'call GET and return list of services'() {

        def servlet = new ServicesServlet(bundleContext)

        StringWriter stringWriter = new StringWriter()

        def response = [
            getWriter:{ new PrintWriter(stringWriter) },
            setContentType:{def a ->
                assertThat a, is(equalTo("application/json"))
            }
        ] as HttpServletResponse

        def request = [
            getPathInfo:{ null }
        ] as HttpServletRequest

        servlet.doGet(request, response)

        assertThat stringWriter.buffer.toString(), containsString('''["/framework/service/42"]''')
    }

    @Test
    void 'call GET with representations and return list of services'() {

        def servlet = new ServicesServlet(bundleContext)

        StringWriter stringWriter = new StringWriter()

        def response = [
            getWriter:{ new PrintWriter(stringWriter) },
            setContentType:{def a ->
                assertThat a, is(equalTo("application/json"))
            }
        ] as HttpServletResponse

        def request = [
            getPathInfo:{ "/representations" }
        ] as HttpServletRequest

        servlet.doGet(request, response)

        assertThat stringWriter.buffer.toString(), containsString('''[{"properties":{"service.id":42},"bundle":"com.test.bundle","usingBundles":[]}]''')
    }

    def getBundleContext(){
        BundleContext bundleContext = [getAllServiceReferences:{ def a, def b ->
                [
                    [getBundle : {
                            [getSymbolicName:{"com.test.bundle"}] as Bundle
                        }
                        , getUsingBundles:{null},
                        getPropertyKeys:{
                            ["service.id"] as String[]
                        }, getProperty:{def key -> 42L}
                    ] as ServiceReference
                ] as ServiceReference[]
            }

        ] as BundleContext
        bundleContext
    }
}
