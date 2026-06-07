package in.samco;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import in.samco.api.QuoteApi;
import in.samco.model.MarketDepthResponse;
import in.samco.util.SamcoConstants;

/**
 * (c) OAuth 2.1 authorization-code flow — backend token-exchange step.
 *
 * The browser redirect that produces the auth code is out of band:
 *
 *   1. Your app redirects the user-agent to
 *      https://tradeapi.samco.in/oauth/authorize?response_type=code
 *         &client_id=<API_KEY>&redirect_uri=<YOUR_CALLBACK>&state=<STATE>&scope=all
 *   2. The user authorises on SAMCO's consent page.
 *   3. The browser is redirected back to <YOUR_CALLBACK>?code=<AUTH_CODE>&state=<STATE>.
 *
 * This sample picks up at step 4 — exchanging the short-lived authorization
 * code at POST /oauth/token for an access_token + refresh_token, then using
 * the access_token as x-session-token on a Trade API call.
 *
 * The SDK does not wrap /oauth/token, so we use java.net.http.HttpClient.
 *
 * See ta-api-docs/oauth/authorize-flow.md.
 */
public class OAuthTokenExchangeSample {

    private static final String TOKEN_URL = "https://tradeapi.samco.in/oauth/token";

    public static void main(String[] args) throws Exception {

        String authCode = "<AUTH_CODE_FROM_CALLBACK>"; // single-use, 10-min TTL

        // --- Step 4a: exchange the auth code for an access_token + refresh_token ---
        String exchangeBody = """
                {
                  "grant_type" : "authorization_code",
                  "code"       : "%s"
                }
                """.formatted(authCode);

        HttpClient http = HttpClient.newHttpClient();
        HttpRequest exchangeReq = HttpRequest.newBuilder(URI.create(TOKEN_URL))
                .header("Content-Type", "application/json")
                .header("Accept",       "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(exchangeBody))
                .build();

        HttpResponse<String> exchangeResp =
                http.send(exchangeReq, HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP " + exchangeResp.statusCode());
        System.out.println(exchangeResp.body());

        // In production, parse with Gson/Jackson:
        //   {"status":"Success","data":{"access_token":"...","refresh_token":"...","expires_in":86400, ...}}
        String accessToken  = extractField(exchangeResp.body(), "access_token");
        String refreshToken = extractField(exchangeResp.body(), "refresh_token");

        // --- Step 5: use access_token as x-session-token on a Trade API call ---
        QuoteApi quoteApi = new QuoteApi();
        MarketDepthResponse quote =
                quoteApi.getQuote(accessToken, "SBIN", SamcoConstants.EXCHANGE_NSE);
        System.out.println("quote status  : " + quote.getStatus());

        // --- (optional) Step 6: refresh the access_token before it expires ---
        String refreshBody = """
                {
                  "grant_type"    : "refresh_token",
                  "refresh_token" : "%s"
                }
                """.formatted(refreshToken);

        HttpResponse<String> refreshResp = http.send(
                HttpRequest.newBuilder(URI.create(TOKEN_URL))
                        .header("Content-Type", "application/json")
                        .header("Accept",       "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(refreshBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        System.out.println("refresh HTTP " + refreshResp.statusCode());
        // Refresh-token rotation: persist the NEW access_token + refresh_token
        // atomically; the previous refresh_token is immediately marked inactive.
    }

    /** Toy extractor — replace with a real JSON library in production. */
    private static String extractField(String json, String field) {
        int i = json.indexOf("\"" + field + "\"");
        if (i < 0) {
            return null;
        }
        int q1 = json.indexOf('"', json.indexOf(':', i) + 1);
        int q2 = json.indexOf('"', q1 + 1);
        return json.substring(q1 + 1, q2);
    }
}
