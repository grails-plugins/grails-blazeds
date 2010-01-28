import org.springframework.web.servlet.DispatcherServlet

class BlazedsGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Sebastien Arbogast"
    def authorEmail = "sebastien.arbogast@gmail.com"
    def title = "Grails BlazeDS 4 Integration"
    def description = '''\\
Basic plugin to integrate BlazeDS 4 into Grails so that you can connect to a Grails backend with a Flex 4 frontend
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/blazeds"

    def doWithWebDescriptor = { xml ->
        //servlets
        def servlets = xml.servlet
        servlets[servlets.size() - 1] + {
            servlet {
                'servlet-name'("flex")
                'display-name'("Spring-Flex Dispatcher Servlet")
                'servlet-class'(DispatcherServlet.name)
                'load-on-startup'("1")
            }
            servlet {
                'servlet-name'("RDSDispatchServlet")
                'display-name'("Flash Builder wizard helper")
                'servlet-class'("flex.rds.server.servlet.FrontEndServlet")
                'init-param' {
                    'param-name'("messageBrokerId")
                    'param-value'("_messageBroker")
                }
                'init-param' {
                    'param-name'("useAppserverSecurity")
                    'param-value'("false")
                }
                'load-on-startup'("10")
            }
        }

        //servlet-mappings
        // servlet mappings
        def servletMappings = xml.'servlet-mapping'
        servletMappings[servletMappings.size() - 1] + {
            'servlet-mapping'(id: "RDS_DISPATCH_MAPPING") {
                'servlet-name'("RDSDispatchServlet")
                'url-pattern'("/CFIDE/main/ide.cfm")
            }
            'servlet-mapping' {
                'servlet-name'("flex")
                'url-pattern'("/messagebroker/*")
            }
        }
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
