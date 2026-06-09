package in.samco.client;

import java.util.Properties;

import in.samco.api.QuoteApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.MarketDepthResponse;
import in.samco.model.SessionTokenResponse;
import in.samco.util.SamcoConstants;

/**
 * (a) POST /session/token — Generate Session Token (Direct).
 *
 * Exchanges an OAuth app's AES-encrypted apiKey + apiSecret for a session JWT,
 * then reuses that JWT as the x-session-token on a follow-up Trade API call.
 *
 * See https://docs-tradeapi.samco.in/session/generate-token.html for the wire contract.
 */
public class SessionTokenSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        // 1. Exchange API key + secret for a session token.
        SessionTokenApi auth = new SessionTokenApi();
        SessionTokenResponse session = auth.generate(
                QuickStartSample.apiKey(cfg),
                QuickStartSample.apiSecret(cfg));
        System.out.println("status        : " + session.getStatus());
        System.out.println("statusMessage : " + session.getStatusMessage());
        System.out.println("accountID     : " + session.getAccountID());
        System.out.println("accountName   : " + session.getAccountName());

        // 2. Capture the JWT.
        String sessionToken = session.getSessionToken();
        if (sessionToken == null || sessionToken.isEmpty()) {
            System.err.println("Failed to obtain session token; aborting.");
            return;
        }

        // 3. Reuse the JWT as x-session-token on a follow-up call. Every Trade
        //    API method on the SDK that takes a `sessionToken`/`xSessionToken`
        //    string forwards it as the x-session-token header internally.
        QuoteApi quoteApi = new QuoteApi();
        MarketDepthResponse quote =
                quoteApi.getQuote(sessionToken, "SBIN", SamcoConstants.EXCHANGE_NSE);
        System.out.println("quote status  : " + quote.getStatus());
    }
}
