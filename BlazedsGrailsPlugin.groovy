import org.springframework.web.servlet.DispatcherServlet
import org.epseelon.grails.blazeds.security.AuthorizationAspect
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator

class BlazedsGrailsPlugin {
    // the plugin version
    def version = "1.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [acegi: "0.5.3 > *"]
    def loadAfter = ["acegi"]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/login/auth.gsp",
            "grails-app/views/login/denied.gsp",
            "grails-app/views/login/openIdAuth.gsp",
            "grails-app/domain/Requestmap.groovy",
            "grails-app/domain/Role.groovy",
            "grails-app/domain/User.groovy",
            "grails-app/controllers/LoginController.groovy",
            "grails-app/controllers/LogoutController.groovy",
            "grails-app/conf/SecurityConfig.groovy",
            "web-app/css/*",
            "web-app/images/*",
            "web-app/images/skin/*",
            "web-app/js/*",
            "web-app/js/prototype/*"
    ]

    def author = "Sebastien Arbogast"
    def authorEmail = "sebastien.arbogast@gmail.com"
    def title = "Grails BlazeDS 4 Integration"
    def description = '''\\
Basic plugin to integrate BlazeDS 4 into Grails so that you can connect to a Grails backend with a Flex 4 frontend
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/blazeds"

    def doWithWebDescriptor = { xml ->

        //listeners
        def listeners = xml.listener
        listeners[listeners.size() - 1] + {
            listener {
                'listener-class'('flex.messaging.HttpFlexSession')
            }
        }

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
        autoProxyCreator(AnnotationAwareAspectJAutoProxyCreator){
            proxyTargetClass = true
        }
        securityAspect(AuthorizationAspect)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->

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
