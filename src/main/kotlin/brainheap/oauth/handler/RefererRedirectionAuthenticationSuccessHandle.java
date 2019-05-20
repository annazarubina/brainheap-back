package brainheap.oauth.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RefererRedirectionAuthenticationSuccessHandle extends SimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    public RefererRedirectionAuthenticationSuccessHandle() {
        super();
        setUseReferer(true);
    }
}