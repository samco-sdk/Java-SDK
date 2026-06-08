package in.samco;

import in.samco.api.OrdersApi;
import in.samco.api.QuoteApi;
import in.samco.api.update.SessionTokenApi;
import in.samco.api.update.WhoAmIApi;
import in.samco.model.MarketDepthResponse;
import in.samco.model.OrderRequest;
import in.samco.model.OrderResponse;
import in.samco.model.SessionTokenResponse;
import in.samco.model.WhoAmIResponse;
import in.samco.util.SamcoConstants;

public class Sample {

    public static void main(String[] args) {

        try {

            // 1. Generate a session token directly from your app's API Key / API Secret.
            //    Both values must be AES-encrypted as described in the docs.
            SessionTokenApi sessionTokenApi = new SessionTokenApi();
            SessionTokenResponse sessionResponse =
                    sessionTokenApi.generate("<AES_ENCRYPTED_API_KEY>", "<AES_ENCRYPTED_API_SECRET>");
            System.out.println("Session token response : " + sessionResponse);

            String sessionToken = sessionResponse.getSessionToken();
            if (sessionToken == null || sessionToken.isEmpty()) {
                System.err.println("Failed to obtain session token, aborting.");
                return;
            }

            // 2. (Optional) Confirm the source IP the server sees matches a registered static IP.
            WhoAmIResponse whoAmI = new WhoAmIApi().whoami(sessionToken);
            System.out.println("WhoAmI : clientIp=" + whoAmI.getClientIp()
                    + " primaryIp=" + whoAmI.getPrimaryIp());

            // 3. Fetch a quote.
            QuoteApi quoteApi = new QuoteApi();
            MarketDepthResponse quote =
                    quoteApi.getQuote(sessionToken, "SBIN", SamcoConstants.EXCHANGE_NSE);

            if ("Success".equalsIgnoreCase(quote.getStatus())) {

                // 4. Place an order.
                OrdersApi ordersApi = new OrdersApi();
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setSymbolName("RELIANCE");
                orderRequest.setExchange(SamcoConstants.EXCHANGE_BSE);
                orderRequest.setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY);
                orderRequest.setOrderType(SamcoConstants.ORDER_TYPE_LIMIT);
                orderRequest.setQuantity("2");
                orderRequest.setDisclosedQuantity("");
                orderRequest.setPrice("1369");
                orderRequest.setOrderValidity(SamcoConstants.VALIDITY_DAY);
                orderRequest.setProductType(SamcoConstants.PRODUCT_MIS);
                orderRequest.setAfterMarketOrderFlag("NO");
                OrderResponse placeOrder = ordersApi.placeOrder(sessionToken, orderRequest);
                System.out.println("Place order response : " + placeOrder);
            }

        } catch (Exception ex) {
            System.out.println("Exception caught while calling trade api : " + ex.getMessage());
        }

    }
}
