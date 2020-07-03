package in.samco;

import io.samco.client.ApiException;
import io.samco.client.SamcoConstants;
import io.samco.client.api.OrdersApi;
import io.samco.client.api.QuoteApi;
import io.samco.client.api.UserLoginApi;
import io.samco.client.model.LoginRequest;
import io.samco.client.model.LoginResponse;
import io.samco.client.model.MarketDepthResponse;
import io.samco.client.model.OrderRequest;
import io.samco.client.model.OrderResponse;

public class Sample {

	public static void main(String[] args) {

		try {

			UserLoginApi userLoginApi = new UserLoginApi();
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setUserId("user_id");
			loginRequest.setPassword("password");
			loginRequest.setYob("yob");
			LoginResponse loginResponse = userLoginApi.login(loginRequest);
			String session = loginResponse.getSessionToken();
			System.out.println("session : " + loginResponse.getSessionToken());

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
					orderRequest.setOrderType(SamcoConstants.ORDER_TYPE_MARKET);
					orderRequest.setQuantity("2");
					orderRequest.setDisclosedQuantity("");
					orderRequest.setOrderValidity(SamcoConstants.VALIDITY_DAY);
					orderRequest.setProductType(SamcoConstants.PRODUCT_MIS);
					orderRequest.setAfterMarketOrderFlag("NO");
					OrderResponse placeOrder = ordersApi.placeOrder(loginResponse.getSessionToken(), orderRequest);
					System.out.println("place order success response : " + placeOrder);

				}
			}

		} catch (ApiException ex) {
			System.out.println("Exception caught while fetching trade api : " + ex.getResponseBody());
		}

	}
}

