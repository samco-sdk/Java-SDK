package in.samco;

import in.samco.api.AccessTokenApi;
import in.samco.api.OrdersApi;
import in.samco.api.QuoteApi;
import in.samco.api.UserLoginApi;
import in.samco.model.AccessTokenRequest;
import in.samco.model.AccessTokenResponse;
import in.samco.model.LoginRequest;
import in.samco.model.LoginResponse;
import in.samco.model.MarketDepthResponse;
import in.samco.model.OrderRequest;
import in.samco.model.OrderResponse;
import in.samco.util.SamcoConstants;

public class Sample {

    public static void main(String[] args) {

        try {

            AccessTokenApi accessTokenApi = new AccessTokenApi();
            AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
            accessTokenRequest.setUid("user_id");
            accessTokenRequest.setSecretApiKey("api_key_secret");

            AccessTokenResponse accessTokenResponse = accessTokenApi.accessToken(accessTokenRequest);
            System.out.println("Access token response : " + accessTokenResponse);

            String accessToken = accessTokenResponse.getAccessToken();

            UserLoginApi userLoginApi = new UserLoginApi();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserId("user_id");
            loginRequest.setPassword("password");
            loginRequest.setAccessToken(accessToken);
            loginRequest.setYob("yob");
            LoginResponse loginResponse = userLoginApi.login(loginRequest);
            System.out.println("Login Response : " + loginResponse);

            String session = loginResponse.getSessionToken();
            System.out.println("session : " + session);

            if (session != null && !"".equalsIgnoreCase(session)) {

                QuoteApi quoteApi = new QuoteApi();
                MarketDepthResponse quote = quoteApi.getQuote(loginResponse.getSessionToken(), "SBIN",
                        SamcoConstants.EXCHANGE_NSE);

                if ("Success".equalsIgnoreCase(quote.getStatus())) {

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
                    OrderResponse placeOrder = ordersApi.placeOrder(loginResponse.getSessionToken(), orderRequest);
                    System.out.println("place order success response : " + placeOrder);
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception caught while fetching trade api : " + ex.getMessage());
        }

    }
}
