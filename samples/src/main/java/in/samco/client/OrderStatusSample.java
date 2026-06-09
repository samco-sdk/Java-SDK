package in.samco.client;

import java.util.Properties;

import in.samco.api.OrdersApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.OrderDetails;
import in.samco.model.OrderStatusResponse;

/**
 * GET /order/getOrderStatus?orderNumber=... — fetch the current status of an order.
 *
 * Reads the order number from {@code samco.orderNumber} in config.properties.
 * If unset/blank, the sample falls back to the first entry returned by the
 * order book; if that's empty too, it logs a skip message and returns.
 *
 * See https://docs-tradeapi.samco.in/order/order-status.html.
 */
public class OrderStatusSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        OrdersApi ordersApi = new OrdersApi();

        String orderNumber = cfg.getProperty("samco.orderNumber", "").trim();
        if (orderNumber.isEmpty()) {
            var book = ordersApi.getOrderBook(sessionToken).getOrderBookDetails();
            if (book == null || book.isEmpty()) {
                System.out.println("(skipped: no samco.orderNumber configured and order book is empty)");
                return;
            }
            orderNumber = book.get(0).getOrderNumber();
            System.out.println("orderNumber   : " + orderNumber + "  (from first order-book entry)");
        } else {
            System.out.println("orderNumber   : " + orderNumber);
        }

        OrderStatusResponse resp = ordersApi.getOrderStatus(sessionToken, orderNumber);

        System.out.println("orderStatus   : " + resp.getOrderStatus());
        System.out.println("statusMessage : " + resp.getStatusMessage());

        OrderDetails d = resp.getOrderDetails();
        if (d != null) {
            System.out.println("tradingSymbol : " + d.getTradingSymbol());
            System.out.println("exchange      : " + d.getExchange());
            System.out.println("txnType       : " + d.getTransactionType());
            System.out.println("orderType     : " + d.getOrderType());
            System.out.println("quantity      : " + d.getQuantity());
            System.out.println("filledQty     : " + d.getFilledQuantity());
            System.out.println("pendingQty    : " + d.getPendingQuantity());
            System.out.println("orderPrice    : " + d.getOrderPrice());
            System.out.println("filledPrice   : " + d.getFilledPrice());
            System.out.println("avgExecPrice  : " + d.getAvgExecutionPrice());
            System.out.println("orderTime     : " + d.getOrderTime());
        }
    }
}
