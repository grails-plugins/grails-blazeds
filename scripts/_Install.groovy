//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//
ant.mkdir(dir:"${basedir}/web-app/WEB-INF/flex")
ant.copy(
        todir: "${basedir}/web-app/WEB-INF/flex",
        file: "${pluginBasedir}/src/resources/services-config.xml",
        overwrite: false
)
ant.copy(
        todir: "${basedir}/web-app/WEB-INF",
        file: "${pluginBasedir}/src/resources/flex-servlet.xml",
        overwrite: false
)
