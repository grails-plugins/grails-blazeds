import grails.plugins.springsecurity.Secured

import org.springframework.flex.remoting.RemotingDestination
import org.springframework.flex.remoting.RemotingInclude

@RemotingDestination
class EchoService {

    @RemotingInclude
    @Secured("ROLE_ADMIN")
    String echo(String message) {
        return message.reverse()
    }
}
