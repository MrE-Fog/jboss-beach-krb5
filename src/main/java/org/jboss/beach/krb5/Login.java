package org.jboss.beach.krb5;

import java.io.Console;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.sun.security.auth.login.ConfigFile;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class Login {
    public static void main(final String[] args) throws LoginException, URISyntaxException {
//        System.setProperty("java.security.auth.login.config", resource("/jaas.config"));
        final Console console = System.console();
        final Subject subject = null;
        final CallbackHandler handler = new CallbackHandler() {
            @Override
            public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (final Callback callback : callbacks) {
                    if (callback instanceof NameCallback) {
                        ((NameCallback) callback).setName(console.readLine("User: "));
                    } else if (callback instanceof PasswordCallback) {
                        ((PasswordCallback) callback).setPassword(console.readPassword("Password: "));
                    }
                    else
                        throw new IllegalStateException("Unknown callback " + callback);
                }
            }
        };
        final Configuration configuration = new ConfigFile(Login.class.getResource("/jaas.config").toURI());
        final LoginContext lc = new LoginContext("Kerberos", subject, handler, configuration);
        lc.login();
        lc.logout();
    }

    private static String resource(final String name) {
        return Login.class.getResource(name).getPath();
    }
}
