package in.samco.client;

import java.util.List;
import java.util.Properties;

import in.samco.api.HoldingsApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.HoldingDetail;
import in.samco.model.HoldingResponse;
import in.samco.model.HoldingSummary;

/**
 * GET /holding/getHoldings — list demat holdings.
 *
 * See https://docs-tradeapi.samco.in/holding/get-holdings.html.
 */
public class HoldingsSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        HoldingResponse resp = new HoldingsApi().getHolding(sessionToken);

        System.out.println("status        : " + resp.getStatus());
        System.out.println("statusMessage : " + resp.getStatusMessage());

        HoldingSummary summary = resp.getHoldingSummary();
        if (summary != null) {
            System.out.println("summary       : " + summary);
        }

        List<HoldingDetail> holdings = resp.getHoldingDetails();
        if (holdings == null || holdings.isEmpty()) {
            System.out.println("(no holdings)");
            return;
        }
        System.out.println("holdings      : " + holdings.size());
        for (HoldingDetail h : holdings) {
            System.out.printf(
                    "  %-12s %-4s qty=%s sellable=%s avg=%s ltp=%s value=%s pnl=%s%n",
                    h.getTradingSymbol(), h.getExchange(),
                    h.getHoldingsQuantity(), h.getSellableQuantity(),
                    h.getAveragePrice(), h.getLastTradedPrice(),
                    h.getHoldingsValue(), h.getTotalGainAndLoss());
        }
    }
}
