includeTargets << grailsScript('_GrailsBootstrap')

target(blazedsInit: 'Copies an initial services-config.xml to WEB-INF/flex') {
	depends(checkVersion, configureProxy)

	String flexDir = "$basedir/web-app/WEB-INF/flex"
	ant.mkdir dir: flexDir

	ant.copy file:  "$blazedsPluginDir/src/resources/services-config.xml",
	         todir: flexDir, verbose: true
}

setDefaultTarget 'blazedsInit'
