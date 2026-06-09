package in.samco.client;

import java.util.Properties;

import in.samco.api.QuoteApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.MarketDepthResponse;
import in.samco.model.QuoteDetails;
import in.samco.model.SessionTokenResponse;
import in.samco.util.SamcoConstants;

/**
 * (d) GET /quote/getQuote — Get Quote.
 *
 * Calls QuoteApi.getQuote with the JWT obtained from the Session Token API.
 * The SDK forwards the JWT as the x-session-token header.
 *
 * See https://docs-tradeapi.samco.in/quote/get-quote.html.
 */
public class QuoteSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        // 1. Obtain a session JWT (re-use across many calls within its TTL).
        SessionTokenResponse session = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg));
        String sessionToken = session.getSessionToken();

        // 2. Fetch a quote.
        QuoteApi quoteApi = new QuoteApi();
        MarketDepthResponse quote =
                quoteApi.getQuote(sessionToken, "SBIN", SamcoConstants.EXCHANGE_NSE);

        System.out.println("status              : " + quote.getStatus());
        System.out.println("statusMessage       : " + quote.getStatusMessage());

        QuoteDetails q = quote.getQuoteDetails();
        if (q != null) {
            System.out.println("tradingSymbol       : " + q.getTradingSymbol());
            System.out.println("lastTradedPrice     : " + q.getLastTradedPrice());
            System.out.println("openValue           : " + q.getOpenValue());
            System.out.println("highValue           : " + q.getHighValue());
            System.out.println("lowValue            : " + q.getLowValue());
            System.out.println("closeValue          : " + q.getCloseValue());
            System.out.println("averagePrice        : " + q.getAveragePrice());
            System.out.println("totalBuyQuantity    : " + q.getTotalBuyQuantity());
            System.out.println("totalSellQuantity   : " + q.getTotalSellQuantity());
            System.out.println("totalTradedVolume   : " + q.getTotalTradedVolume());
            System.out.println("yearlyHighPrice     : " + q.getYearlyHighPrice());
            System.out.println("yearlyLowPrice      : " + q.getYearlyLowPrice());
            System.out.println("listingId           : " + q.getListingId());
        }
    }
}
