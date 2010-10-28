includeTargets << grailsScript('_GrailsBootstrap')

target(blazeDsInit: 'Copies services-config.xml and extra jar files') {
	depends(checkVersion, configureProxy)

	String flexDir = "$basedir/web-app/WEB-INF/flex"
	ant.mkdir dir: flexDir

	ant.copy file:  "$blazedsPluginDir/src/resources/services-config.xml",
	         todir: "$basedir/web-app/WEB-INF/flex",
				verbose: true
}

setDefaultTarget 'blazeDsInit'
