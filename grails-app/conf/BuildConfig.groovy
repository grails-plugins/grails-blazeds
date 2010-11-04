grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.docs.output.dir = 'docs' // docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits 'global'

	log 'warn'

	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenRepo 'http://maven.springframework.org/external' // blazeds
		mavenRepo 'http://maven.springframework.org/milestone' // flex-core:1.5.0.M1
		ebr() // SpringSource  http://www.springsource.com/repository
		mavenCentral()

		mavenRepo 'http://maven.sinusgear.com/maven_repo' // flex-messaging-opt, flex-rds-server
	}

	dependencies {
		// TODO open-ended versions
		runtime 'org.codehaus.jackson:com.springsource.org.codehaus.jackson:1.4.3'
		runtime 'org.apache.xalan:com.springsource.org.apache.xml.serializer:2.7.1'
		runtime('org.apache.commons:com.springsource.org.apache.commons.httpclient:3.1.0') {
			excludes 'com.springsource.org.apache.commons.logging'
		}
		runtime 'edu.emory.mathcs.backport:com.springsource.edu.emory.mathcs.backport:3.1.0'

		runtime('org.springframework.flex:spring-flex-core:1.5.0.M1') {
			transitive = false
		}

//		http://maven.sinusgear.com/maven_repo/com/adobe/flex/flex-messaging-opt/4.0.0.14931.1/

		runtime 'com.adobe.blazeds:blazeds-common:4.0.0.14931',
		        'com.adobe.blazeds:blazeds-core:4.0.0.14931',
				  'com.adobe.blazeds:blazeds-proxy:4.0.0.14931',
				  'com.adobe.blazeds:blazeds-remoting:4.0.0.14931'

//		runtime 'com.adobe.flex:flex-messaging-opt:4.0.0.14931.1',
//		        'com.adobe.flex:flex-rds-server:4.0.0.14931.1'
		runtime 'com.adobe.flex:flex-messaging-opt:4.0.0.14931.1'
	}
}
