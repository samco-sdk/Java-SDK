package in.samco;

import in.samco.api.update.SessionTokenApi;
import in.samco.api.update.WhoAmIApi;
import in.samco.model.SessionTokenResponse;
import in.samco.model.WhoAmIResponse;

/**
 * (b) GET /ip/whoami — IP diagnostics.
 *
 * Reports the source IP the SAMCO server sees, plus the currently registered
 * PRIMARY / SECONDARY IPs, and whether the caller matches one of them. Use it
 * to debug `403 — The IP is not the registered static IP` errors. Does NOT
 * consume the SEBI weekly IP-update slot.
 *
 * See ta-api-docs/static-ip/whoami.md.
 */
public class WhoAmISample {

    public static void main(String[] args) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate("<AES_ENCRYPTED_API_KEY>", "<AES_ENCRYPTED_API_SECRET>")
                .getSessionToken();

        WhoAmIResponse whoAmI = new WhoAmIApi().whoami(sessionToken);

        System.out.println("status        : " + whoAmI.getStatus());
        System.out.println("statusMessage : " + whoAmI.getStatusMessage());
        System.out.println("clientIp      : " + whoAmI.getClientIp());
        System.out.println("primaryIp     : " + whoAmI.getPrimaryIp());
        System.out.println("secondaryIp   : " + whoAmI.getSecondaryIp());
    }
}
