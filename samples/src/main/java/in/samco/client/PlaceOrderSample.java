package in.samco.client;

import java.util.Properties;

import in.samco.api.OrdersApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.model.OrderRequest;
import in.samco.model.OrderResponse;
import in.samco.util.SamcoConstants;

/**
 * (e) POST /order/placeOrder — Place Order, LIMIT variant.
 *
 * LIMIT order: orderType = "L" with an explicit `price`. Order goes to the
 * exchange and rests until either the limit price is hit or the order is
 * cancelled / expired.
 *
 * See https://docs-tradeapi.samco.in/order/place-order.html.
 */
public class PlaceOrderSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        // 1. Session JWT.
        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        // 2. Build a LIMIT BUY order: 1 share of IDEA on NSE @ Rs 13.40, CNC, DAY validity.
        OrderRequest order = new OrderRequest();
        order.setSymbolName("IDEA");
        order.setExchange(SamcoConstants.EXCHANGE_NSE);
        order.setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY);
        order.setOrderType(SamcoConstants.ORDER_TYPE_LIMIT);   // "L"
        order.setQuantity("1");
        order.setDisclosedQuantity("1");
        order.setPrice("13.40");                               // required for LIMIT
        order.setOrderValidity(SamcoConstants.VALIDITY_DAY);
        order.setProductType(SamcoConstants.PRODUCT_CNC);
        order.setAfterMarketOrderFlag("NO");

        // 3. Place it.
        OrderResponse resp = new OrdersApi().placeOrder(sessionToken, order);

        System.out.println("status        : " + resp.getStatus());
        System.out.println("statusMessage : " + resp.getStatusMessage());
        System.out.println("orderNumber   : " + resp.getOrderNumber());
    }
}
