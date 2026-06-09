package in.samco.client;

import java.util.Properties;

import in.samco.api.update.SessionTokenApi;
import in.samco.api.update.WhoAmIApi;
import in.samco.model.WhoAmIResponse;

/**
 * (b) GET /ip/whoami — IP diagnostics.
 *
 * Reports the source IP the SAMCO server sees, plus the currently registered
 * PRIMARY / SECONDARY IPs, and whether the caller matches one of them. Use it
 * to debug `403 — The IP is not the registered static IP` errors. Does NOT
 * consume the SEBI weekly IP-update slot.
 *
 * See https://docs-tradeapi.samco.in/static-ip/whoami.html.
 */
public class WhoAmISample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        WhoAmIResponse whoAmI = new WhoAmIApi().whoami(sessionToken);

        System.out.println("status        : " + whoAmI.getStatus());
        System.out.println("statusMessage : " + whoAmI.getStatusMessage());
        System.out.println("clientIp      : " + whoAmI.getClientIp());
        System.out.println("primaryIp     : " + whoAmI.getPrimaryIp());
        System.out.println("secondaryIp   : " + whoAmI.getSecondaryIp());
    }
}
