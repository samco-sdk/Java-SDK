package in.samco.client;

import java.util.List;
import java.util.Properties;

import in.samco.api.OrdersApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.OrderBookEntry;
import in.samco.model.OrderBookResponse;

/**
 * GET /order/orderBook — list all orders for the trading day.
 *
 * See https://docs-tradeapi.samco.in/order/order-book.html.
 */
public class OrderBookSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        OrderBookResponse resp = new OrdersApi().getOrderBook(sessionToken);

        System.out.println("status        : " + resp.getStatus());
        System.out.println("statusMessage : " + resp.getStatusMessage());

        List<OrderBookEntry> orders = resp.getOrderBookDetails();
        if (orders == null || orders.isEmpty()) {
            System.out.println("(no orders for today)");
            return;
        }
        System.out.println("orders        : " + orders.size());
        for (OrderBookEntry o : orders) {
            System.out.printf(
                    "  %s %-12s %-4s %-4s %-6s qty=%s/%s price=%s status=%s%n",
                    o.getOrderNumber(), o.getTradingSymbol(), o.getExchange(),
                    o.getTransactionType(), o.getOrderType(),
                    o.getFilledQuantity(), o.getQuantity(),
                    o.getOrderPrice(), o.getOrderStatus());
        }
    }
}
