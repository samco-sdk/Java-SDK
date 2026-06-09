package in.samco.client;

import java.util.List;
import java.util.Properties;

import in.samco.api.PositionsApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.PositionDetail;
import in.samco.model.PositionResponse;
import in.samco.model.PositionSummary;
import in.samco.util.SamcoConstants;

/**
 * GET /position/getPositions?positionType=NET — list net (carry-forward) positions.
 *
 * Calls PositionsApi.getPositions with positionType = NET. Use
 * SamcoConstants.POSITION_TYPE_DAY for intraday positions instead.
 *
 * See https://docs-tradeapi.samco.in/position/get-positions.html.
 */
public class PositionsSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        PositionResponse resp = new PositionsApi()
                .getPositions(sessionToken, SamcoConstants.POSITION_TYPE_NET);

        System.out.println("status        : " + resp.getStatus());
        System.out.println("statusMessage : " + resp.getStatusMessage());

        PositionSummary summary = resp.getPositionSummary();
        if (summary != null) {
            System.out.println("gainingToday  : " + summary.getGainingTodayCount());
            System.out.println("losingToday   : " + summary.getLosingTodayCount());
            System.out.println("totalP&L      : " + summary.getTotalGainAndLossAmount());
            System.out.println("dayP&L        : " + summary.getDayGainAndLossAmount());
        }

        List<PositionDetail> details = resp.getPositionDetails();
        if (details == null || details.isEmpty()) {
            System.out.println("(no NET positions)");
            return;
        }
        System.out.println("positions     : " + details.size());
        for (PositionDetail p : details) {
            System.out.printf(
                    "  %-12s %-4s qty=%s avgBuy=%s avgSell=%s ltp=%s realized=%s unrealized=%s%n",
                    p.getTradingSymbol(), p.getExchange(),
                    p.getNetQuantity(), p.getAverageBuyPrice(), p.getAverageSellPrice(),
                    p.getLastTradedPrice(),
                    p.getRealizedGainAndLoss(), p.getUnrealizedGainAndLoss());
        }
    }
}
