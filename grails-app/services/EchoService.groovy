import org.springframework.flex.remoting.RemotingDestination
import org.springframework.security.annotation.Secured
import org.springframework.flex.remoting.RemotingInclude

@RemotingDestination
class EchoService {

    boolean transactional = true

    @RemotingInclude
    @Secured("ROLE_ADMIN")
    String echo(String message) {
        return message.reverse()
    }
}
