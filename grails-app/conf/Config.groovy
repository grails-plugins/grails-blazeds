log4j = {
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

grails.doc.authors = 'Sebastien Arbogast, Burt Beckwith'
grails.doc.license = 'Apache License 2.0'
grails.doc.title = 'BlazeDS Plugin'

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'UserRole'
grails.plugins.springsecurity.authority.className = 'Role'
grails.plugins.springsecurity.requestMap.className = 'Requestmap'
grails.plugins.springsecurity.securityConfigType = 'Requestmap'
