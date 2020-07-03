# Java Bridge for Stocknote API

   Official Java Bridge for accessing Stocknote API
   
   This documentation covers details of the Java bridge / SDK provided by SAMCO, for accessing the <a href="https://developers.stocknote.com/api/?python#stocknote-api-documentation">SAMCO Stocknote APIs.</a>

## Overview

   * Java SDK is created for clients to easily access our Stocknote API platform from Java based applications.
   
   * Java SDK will be exposed as a downloadable JAR file
   
   * Include the JAR file into their build path and access the trade APIs using the inbuilt classes in Java SDK
   
   * Different Java classes and methods will be exposed in the SDK for handling the multiple Stocknote APIs
   
   * As an initial step, the client will use the Login method and in the Java response bean object, they will get user session identifier. Based on the user session identifier, they will be able to access other API’s
   
   * Client can process the API response appropriately
   
   * With StockNote API being a REST based interface and interfaces using JSON request and response messages, Java SDK provides request and response objects as native Java beans (after appropriate serialisation / de-serialisation)
   
### Note

   * All API response displayed in this documentation are JSON representation of response java object 
   
### Prerequisites 

* Java 8 JDK

### Steps

1. Get StockNote Java Bridge Jar from the below link

	*  https://github.com/Thasarathan/Java-Bridge/blob/master/dist/swagger-java-client.jar

2. Setup Jar File

    * For maven user

    * Install jar file into your local .m2 repository using the below command :
    
      mvn install:install-file -Dfile="where the jar is located" -DgroupId=in.samco -DartifactId=stocknote-bridge-java -Dversion=1.0.0 -Dpackaging=jar
	
    * Add the below dependency into pom.xml file .
	
	            <dependency>
		           <groupId>in.samco</groupId>
		           <artifactId>stocknote-bridge-java</artifactId>
		           <version>1.0.0</version>
	           </dependency>

     * For gradle user 

                    dependencies {
   			             compile group: 'in.samco', name: 'stocknote-bridge-java', version: '1.0.0'
			         }

     * Adding jar to build path in eclipse based IDE's 
     
        Goto  JavaBuild Path --> Libraries --> Add External JARs --> Include stocknote-bridge-java-1.0.0.jar 
            
			   
			   
###  List of supported API

 *  <a href="#login">Login</a>
 *  <a href="#equity_search">SearchEquityDerivative</a>
 *  <a href="#quote">Quote</a>
 *  <a href="#optionchain">OptionChain</a>
 *  <a href="#limit">UserLimits</a>
 *  <a href="#placeorder">PlaceOrder</a>
 *  <a href="#placeorderBO">PlaceOrderBO</a>
 *  <a href="#placeorderCO">PlaceOrderCO</a>
 *  <a href="#modify_order">ModifyOrder</a>
 *  <a href="#orderbook">OrderBook</a>
 *  <a href="#triggerorder">TriggerOrders</a>
 *  <a href="#order_status">OrderStatus</a>
 *  <a href="#cancel_order">CancelOrder</a>
 *  <a href="#cancelorderCO">CancelOrderCO</a>
 *  <a href="#cancelorderBO">CancelOrderBO</a>
 *  <a href="#tradebook">TradeBook</a>
 *  <a href="#positions">Positions</a>
 *  <a href="#positionConversion">PositionConversion</a>
 *  <a href="#positionSquareOff">PositionSquareOff</a>
 *  <a href="#holdings">Holdings</a>
 *  <a href="#intraDayCandleData">IntraDayCandleData</a>
 *  <a href="#indexIntraDayCandleData">IndexIntraDayCandleData</a>
 *  <a href="#historicalCandleData">HistoricalCandleData</a>
 *  <a href="#indexHistoricalCandleData">IndexHistoricalCandleData</a>
 *  <a href="#logout">Logout</a>


### <h3 id="login">Login Api:</h3>

   Java Bridge allows user authentication using UserLoginApi. A valid StockNote Trading Account and subscription to StockNote API Services is a pre-requisite for successful authentication.

#### Parameters:

    userId, password, yob
    
#### Login Sample Request:

    UserLoginApi userLoginApi = new UserLoginApi();
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUserId(userId);
    loginRequest.setPassword(password);
    loginRequest.setYob(yob);
    LoginResponse loginResponse = userLoginApi.login(loginRequest);
 
#### Sample Login Response:

    {
    "serverTime": "26/05/20 13:50:32",
    "msgId": "c662bbd1-0b24-4e86-a3d9-8c89d7529f2c",
    "sessionToken": "e0875f4aa3660b72ec636b0553acc7a9",
    "accountID": "client_id",
    "accountName": "client_name",
    "exchangeList": [
        "BSE",
        "MCX",
        "CDS",
        "NSE"
    ],
    "orderTypeList": [
        "L",
        "MKT",
        "SL"
    ],
    "productList": [
        "CNC",
        "CO",
        "MIS"
    ]
   }
    
#### Using the session token we can call other API’s through java SDK

### <h3 id="equity_search">Search Equity & Derivative:</h3>

   This API is used to search equity, derivatives and commodity scrips based on user provided search symbol and exchange name.

#### Parameters:

    xSessionToken, exchange, searchSymbolName
    
#### Sample Search Request:

    SearchEquityDerivativeApi api = new SearchEquityDerivativeApi();
    EquitySearchResponse searchEquityDerivative = api.searchEquityDerivative(xSessionToken, "GOLD", SamcoConstants.EXCHANGE_MFO);
   
#### Sample Search Response:

    {
      "msgId" : "7356eb6b-ab17-4b2c-84e7-46c9280e15d4",
      "status" : "Success",
      "statusMessage" : "Request Successful",
      "commodityValues" : [ {
      "tradingSymbol" : "GOLD20FEBFUT",
      "instrumentName" : "FUTCOM",
      "quantityInLots" : "9304",
      "exchange" : "MFO"
     }, {
         "tradingSymbol" : "GOLDM20FEBFUT",
         "instrumentName" : "FUTCOM",
         "quantityInLots" : "15258",
         "exchange" : "MFO"
     }, {
         "tradingSymbol" : "GOLDM20MARFUT",
         "instrumentName" : "FUTCOM",
         "quantityInLots" : "1650",
         "exchange" : "MFO"
     }, {
         "tradingSymbol" : "GOLDPETAL20APRFUT",
         "instrumentName" : "FUTCOM",
         "quantityInLots" : "0",
         "exchange" : "MFO"
     } ]
    }

###  <h3 id="quote">Quote:</h3>

   Get market depth details for a specific equity scrip including but not limited to values like last trade price, previous close price, change value, change percentage, bids/asks, upper and lower circuit limits etc. This helps user with market picture of an equity scrip using which he will be able to place an order.

#### Parameters:

    xSessionToken, exchange, symbolName
    
#### Sample Quote request:

    QuoteApi quoteApi = new QuoteApi();
    MarketDepthResponse quote = quoteApi.getQuote(xSessionToken, "SBIN", SamcoConstants.EXCHANGE_NSE);
     
#### Sample Quote Response:

    {
      "serverTime": "26/05/20 14:09:56",
      "msgId": "70414ca5-230e-467b-81cb-62af0a5f2b95",
      "status": "Success",
      "statusMessage": "Quote details retrieved successfully",
      "symbolName": "SBIN",
      "tradingSymbol": "SBIN-EQ",
      "exchange": "NSE",
      "companyName": "STATE BANK OF INDIA",
      "lastTradedTime": "26/05/2020 14:09:55",
      "lastTradedPrice": "151.00",
      "previousClose": "150.85",
      "changeValue": "0.15",
      "changePercentage": "0.10",
      "lastTradedQuantity": "16",
      "lowerCircuitLimit": "135.80",
      "upperCircuitLimit": "165.90",
      "averagePrice": "151.96",
      "openValue": "152.40",
      "highValue": "153.20",
      "lowValue": "150.55",
      "closeValue": "150.85",
      "totalBuyQuantity": "3140492",
      "totalSellQuantity": "9899407",
      "totalTradedValue": "592.47019 (Crs)",
      "totalTradedVolume": "38988562",
      "yearlyHighPrice": "373.80",
      "yearlyLowPrice": "149.45",
      "bestBids": [
          {
              "number": "1",
              "quantity": "9105",
              "price": "150.95"
          },
          {
              "number": "2",
              "quantity": "16880",
              "price": "150.90"
          },
          {
              "number": "3",
              "quantity": "20776",
              "price": "150.85"
          },
          {
              "number": "4",
              "quantity": "38364",
              "price": "150.80"
          },
          {
              "number": "5",
              "quantity": "23566",
              "price": "150.75"
          }
      ],
      "listingId": "3045_NSE"
      }

### <h3 id="optionchain">OptionChain:</h3>

    To search OptionChain for equity, derivatives and commodity scrips based on user provided search symbol and exchange name. 
      
#### Parameters:

    xSessionToken, searchSymbolName, exchange, expiryDate, strikePrice, optionType
    
#### Sample OptionChain Request:

    OptionApi optionApi = new OptionApi();
    OptionChainResponse optionChainResponse = optionApi.getOptionContracts(xSessionToken, "INFY", SamcoConstants.EXCHANGE_NSE, "2020-06-25", "950", "CE");

#### Sample OptionChain Response:

    {
      "serverTime" : "01/06/20 18:49:55",
      "msgId" : "5e1e2e47-6565-457e-9d10-4e2b7d09d15b",
      "status" : "Success",
      "statusMessage" : "OptionChain details retrived successfully. ",
      "optionChainDetails" : [ {
        "tradingSymbol" : "INFY20JUN950CE",
        "exchange" : "NFO",
        "symbol" : "74352_NFO",
        "strikePrice" : "950.00",
        "expiryDate" : "2020-06-25",
        "instrument" : "OPTSTK",
        "optionType" : "CE",
        "underLyingSymbol" : "INFY",
        "spotPrice" : "699.55",
        "lastTradedPrice" : "0.00",
        "openInterest" : "0",
        "openInterestChange" : "0",
        "volume" : "0"
      } ]
    }

###  <h3 id="limit">UserLimits:</h3>

   Gets the user cash balances, available margin for trading in equity and commodity segments.
      
#### Parameters:

    xSessionToken
    
#### Sample UserLimit Request:

    UserLimitsApi userLimitsApi = new UserLimitsApi();
    LimitResponse limits = userLimitsApi.getLimits(xSessionToken);

#### Sample UserLimit Response:

    {
      "serverTime" : "29/05/20 15:34:05",
      "msgId" : "7792c01b-618d-46b5-86d2-1a1c647c72d0",
      "equityLimit" : {
        "grossAvailableMargin" : "50000000000",
        "payInToday" : "0",
        "notionalCash" : "0",
        "marginUsed" : "92",
        "netAvailableMargin" : "49999999908"
      },
      "commodityLimit" : {
        "grossAvailableMargin" : "0",
        "payInToday" : "0",
        "notionalCash" : "0",
        "marginUsed" : "0",
        "netAvailableMargin" : "0"
      }
    }

### <h3 id="placeorder">PlaceOrder:</h3>

   To place an equity/derivative order to the exchange i.e the place order request typically registers the order with OMS and when it happens successfully, a success response is returned. Successful placement of an order via the API does not imply its successful execution. To be precise, under normal scenarios, the whole flow of order execution starting with order placement, routing to OMS and transfer to the exchange, order execution, and confirmation from exchange happen real time. But due to various reasons like market hours, exchange related checks etc. This may not happen instantly. So when an order is successfully placed the PlaceOrder API returns an OrderNumber in response, and in scenarios as above the actual order status can be checked separately using the OrderStatus API call .This is for Placing CNC, MIS and NRML Orders.
    
#### Parameters:

    xSessionToken, symbolName, exchange, transactionType, orderType, price, quantity, disclosedQuantity, orderValidity, productType, marketProtection, afterMarketOrderFlag
    
#### Sample PlaceOrder Request:


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
    OrderResponse placeOrder = ordersApi.placeOrder(xSessionToken, orderRequest);
		     
#### Sample PlaceOrder Response:  

    {
      "serverTime" : "29/05/20 12:43:06",
      "msgId" : "f1330206-cb2f-42eb-9925-b7d825c07bdd",
      "orderNumber" : "200529000000059",
      "status" : "Success",
      "statusMessage" : "MIS Order request placed successfully",
      "exchangeOrderStatus" : "PENDING",
      "orderDetails" : {
        "pendingQuantity" : "2",
        "avgExecutionPrice" : "0.00",
        "orderPlacedBy" : "--",
        "tradingSymbol" : "RELIANCE",
        "triggerPrice" : "0.00",
        "exchange" : "BSE",
        "totalQuantity" : "2",
        "transactionType" : "BUY",
        "productType" : "MIS",
        "orderType" : "L",
        "quantity" : "2",
        "filledQuantity" : "0",
        "orderPrice" : "1600.0",
        "filledPrice" : "0.00",
        "exchangeOrderNo" : "1590728958294000024",
        "orderValidity" : "DAY",
        "orderTime" : "29/05/2020 12:43:04"
      }
    }

### <h3 id="placeorderBO">PlaceOrderBO:</h3>

   To place an equity/derivative order to the exchange i.e the place order BO request typically registers the order with OMS and when it happens successfully, a success response is returned. Successful placement of an order via the API does not imply its successful execution. To be precise, under normal scenarios, the whole flow of order execution starting with order placement, routing to OMS and transfer to the exchange, order execution, and confirmation from exchange happen real time. But due to various reasons like market hours, exchange related checks etc. This may not happen instantly. So when an order is successfully placed the placeOrderBO returns an orderNumber in response, and in scenarios as above the actual order status can be checked separately using the orderStatus API call. This is for Placing BO Orders.
        
#### Parameters:

    xSessionToken, exchange, symbolName, transactionType, orderType, quantity, disclosedQuantity, price, priceType, valueType, orderValidity, productType, squareOffValue, stopLossValue, trailingStopLoss
    
#### Sample PlaceOrderBO Request:

    OrdersApi ordersApi = new OrdersApi();
    OrderRequestBO orderRequest = new OrderRequestBO();
    orderRequest.setSymbolName("TCS");
    orderRequest.setExchange(SamcoConstants.EXCHANGE_BSE);
    orderRequest.setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY);
    orderRequest.setOrderType(SamcoConstants.ORDER_TYPE_MARKET);
    orderRequest.setQuantity("10");
    orderRequest.setDisclosedQuantity("1");
    orderRequest.setPrice("2000");
    orderRequest.setPriceType("LTP");
    orderRequest.setValueType("Absolute");
    orderRequest.setOrderValidity(SamcoConstants.VALIDITY_DAY);
    orderRequest.setProductType(SamcoConstants.PRODUCT_BO);
    orderRequest.setSquareOffValue("100");
    orderRequest.setStopLossValue("50");
    orderRequest.setTrailingStopLoss("30");
    OrderResponseBO placeOrderBO = ordersApi.placeOrderBO(xSessionToken, orderRequest);
    
#### Sample PlaceOrderBO Response:

    {
      "serverTime" : "01/06/20 14:58:38",
      "msgId" : "de2d8caf-b76d-4a24-bb6c-fa654ed355bb",
      "orderNumber" : "200601000000133",
      "status" : "Success",
      "statusMessage" : "Bracket Order request placed successfully",
      "exchangeOrderStatus" : "EXECUTED",
      "orderDetails" : {
        "pendingQuantity" : "0",
        "avgExecutionPrice" : "669.00",
        "orderPlacedBy" : "--",
        "tradingSymbol" : "INFY-EQ",
        "triggerPrice" : "0.00",
        "exchange" : "NSE",
        "totalQuantity" : "10",
        "transactionType" : "BUY",
        "productType" : "BO",
        "orderType" : "L",
        "quantity" : "10",
        "filledQuantity" : "10",
        "orderPrice" : "669.0",
        "filledPrice" : "669.00",
        "exchangeOrderNo" : "1100000000030886",
        "orderValidity" : "DAY",
        "orderTime" : "01/06/2020 14:58:36"
      }
    }

### <h3 id="placeorderCO">PlaceOrderCO:</h3>

   To place an equity/derivative order to the exchange i.e the place order CO request typically registers the order with OMS and when it happens successfully, a success response is returned. Successful placement of an order via the API does not imply its successful execution. To be precise, under normal scenarios, the whole flow of order execution starting with order placement, routing to OMS and transfer to the exchange, order execution, and confirmation from exchange happen real time. But due to various reasons like market hours, exchange related checks etc. This may not happen instantly. So when an order is successfully placed the placeOrderCO returns an orderNumber in response, and in scenarios as above the actual order status can be checked separately using the orderStatus API call. This is for Placing CO Orders.
        
#### Parameters:

    xSessionToken, symbolName, exchange, transactionType, orderType, price, quantity, disclosedQuantity, orderValidity, productType, marketProtection, triggerPrice
    
#### Sample PlaceOrderCO Request:

    OrdersApi ordersApi = new OrdersApi();
    OrderRequestCO orderRequest = new OrderRequestCO();
    orderRequest.setSymbolName("RELIANCE");
    orderRequest.setExchange(SamcoConstants.EXCHANGE_NSE);
    orderRequest.setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY);
    orderRequest.setOrderType(SamcoConstants.ORDER_TYPE_MARKET);
    orderRequest.setQuantity("15");
    orderRequest.setDisclosedQuantity("");
    orderRequest.setOrderValidity(SamcoConstants.VALIDITY_DAY);
    orderRequest.setProductType(SamcoConstants.PRODUCT_CO);
    orderRequest.setTriggerPrice("1300");
    OrderResponseCO placeOrderCO = ordersApi.placeOrderCO(xSessionToken, orderRequest);
    
#### Sample PlaceOrderCO Response:

    {
      "serverTime" : "01/06/20 14:36:34",
      "msgId" : "b0d3192d-824f-4493-90cf-4657e827742e",
      "orderNumber" : "200601000000129",
      "status" : "Success",
      "statusMessage" : "CO Order request placed successfully",
      "exchangeOrderStatus" : "EXECUTED",
      "orderDetails" : {
        "pendingQuantity" : "0",
        "avgExecutionPrice" : "669.00",
        "orderPlacedBy" : "--",
        "tradingSymbol" : "INFY-EQ",
        "triggerPrice" : "650.00",
        "exchange" : "NSE",
        "totalQuantity" : "15",
        "transactionType" : "BUY",
        "productType" : "CO",
        "orderType" : "L",
        "quantity" : "15",
        "filledQuantity" : "15",
        "orderPrice" : "689.05",
        "filledPrice" : "669.00",
        "exchangeOrderNo" : "1100000000030191",
        "orderValidity" : "DAY",
        "orderTime" : "01/06/2020 14:36:32"
      }
    }

###  <h3 id="modify_order">Modify Order:</h3>

   User would be able to modify some attributes of an order as long as it is with open/pending status in system. For modification order identifier is mandatory. With order identifier you need to send the optional parameter(s) which needs to be modified. In case the optional parameters aren't sent, the default will be considered from the original order. Modifiable attributes include quantity, Order Type (L,MKT, SL,SL-M). This API cannot be used for modifying attributes of an executed/rejected/cancelled order. Only the attribute that needs to be modified should be sent in the request alongwith the Order Identifier.

        
#### Parameters:

    xSessionToken, orderNumber, orderType, quantity, disclosedQuantity, orderValidity, price, triggerPrice, parentOrderId, marketProtection
    
#### Sample ModifyOrder Request:

    OrdersApi ordersApi = new OrdersApi();
    ModifyOrderRequest modifyOrderRequest = new ModifyOrderRequest();
    modifyOrderRequest.setQuantity("20");
    OrderResponse modifyOrder = ordersApi.modifyOrder(xSessionToken, "200529000000059", modifyOrderRequest);

#### Sample ModifyOrder Response:

    {
      "serverTime" : "29/05/20 14:12:42",
      "msgId" : "773d0380-6f07-4269-93e5-a6d4b2b8c5d3",
      "orderNumber" : "200529000000059",
      "status" : "Success",
      "statusMessage" : "Order 200529000000059 modified successfully",
      "exchangeOrderStatus" : "PENDING",
      "orderDetails" : {
        "pendingQuantity" : "20",
        "avgExecutionPrice" : "0.00",
        "orderPlacedBy" : "DA35672",
        "tradingSymbol" : "RELIANCE",
        "triggerPrice" : "0.00",
        "exchange" : "BSE",
        "totalQuantity" : "20",
        "transactionType" : "BUY",
        "productType" : "MIS",
        "orderType" : "L",
        "quantity" : "20",
        "filledQuantity" : "0",
        "orderPrice" : "1600.0",
        "filledPrice" : "0.00",
        "exchangeOrderNo" : "1590728958294000024",
        "orderValidity" : "DAY",
        "orderTime" : "29/05/2020 14:12:39"
       }
    }

### <h3 id="orderbook">OrderBook:</h3>

   Orderbook retrieves and displays details of all orders placed by the user on a specific day. This API returns all states of the orders, namely, open, pending, rejected and executed ones.
       
#### Parameters:

    xSessionToken
    
#### Sample OrderBook Request:

    OrdersApi ordersApi = new OrdersApi();
    OrderBookResponse orderBook = ordersApi.getOrderBook(xSessionToken);

#### Sample OrderBook Response:

    {
      "serverTime" : "29/05/20 15:43:49",
      "msgId" : "d2b6770c-348b-4bd0-91fa-feb5b3d10d8d",
      "status" : "Success",
      "orderBookDetails" : [ {
        "orderNumber" : "200529000000200",
        "exchange" : "NSE",
        "tradingSymbol" : "RELIANCE",
        "transactionType" : "SELL",
        "productCode" : "CO",
        "orderValue" : "0.0",
        "orderTime" : "29-May-2020 15:02:02",
        "userId" : "DA35672",
        "orderType" : "SL-M",
        "orderPrice" : "1290.00",
        "triggerPrice" : "1290.00",
        "orderValidity" : "DAY",
        "orderStatus" : "Trigger Pending",
        "filledQuantity" : "0",
        "fillPrice" : "0.00",
        "averagePrice" : "0.00",
        "rejectionReason" : "--",
        "exchangeConfirmationTime" : "29-May-2020 15:02:02",
        "coverOrderPercentage" : "0.05",
        "orderRemarks" : "--",
        "exchangeOrderNumber" : "1100000000085407",
        "symbol" : "2885_NSE",
        "displayStrikePrice" : "00.00",
        "displayNetQuantity" : "1",
        "status" : "Trigger Pending",
        "exchangeStatus" : "trigger pending",
        "expiry" : "NA",
        "pendingQuantity" : "1",
        "totalQuanity" : "1",
        "optionType" : "XX",
        "orderPlaceBy" : "--"
       } ]
    }

### <h3 id="triggerorder">TriggerOrders:</h3>

   To get the trigger order numbers in case of BO and CO orders so that their attribute values can be modified for BO orders, it will give the order identifiers. For Stop loss leg and target leg. Similarly for CO orders, it will return order identifier of stop loss leg only. Using the order identifier, the user would be able to modify the order attributes using the modifyOrder API. Refer modifyOrder API documentation for the parameters details. 
        
#### Parameters:

    xSessionToken, orderNumber
    
#### Sample TriggerOrders Request:

    OrdersApi ordersApi = new OrdersApi();
    TriggerOrdersResponse triggerOrderNumbers = ordersApi.getTriggerOrderNumbers(xSessionToken, "200514000000041");

#### Sample TriggerOrders Response:

    {
      "serverTime" : "01/06/20 14:06:46",
      "msgId" : "2172fc5c-a72b-4c79-bb43-398bebf85af4",
      "status" : "Success",
      "statusMessage" : "SubOrder details retrieved successfully.",
      "triggerOrders" : [ {
        "targetOrderNo" : "200601000000027",
        "orderStatus" : "Open",
        "orderPrice" : "760.10",
        "triggerPrice" : "0.00",
        "mainOrderNo" : "200601000000026"
      }, {
        "stopLossOrderNo" : "200601000000028",
        "orderStatus" : "Cancelled",
        "orderPrice" : "650.00",
        "triggerPrice" : "650.00",
        "mainOrderNo" : "200601000000026"
      } ]
    }

###  <h3 id="order_status">Order Status:</h3>

   Get status of an order placed previously. This API returns all states of the orders,but not limited to open, pending, and partially filled ones.
     
#### Parameters:

    xSessionToken, orderNumber
    
#### Sample OrderStatus Request:

    OrdersApi ordersApi = new OrdersApi();
    OrderStatusResponse orderStatusResponse = ordersApi.getOrderStatus(xSessionToken, "200618000000010");

#### Sample OrderStatus Response:

    {
      "serverTime" : "29/05/20 13:31:37",
      "msgId" : "a92ce76f-5970-44c5-a1b8-1038537b28c6",
      "orderNumber" : "200529000000059",
      "orderStatus" : "PENDING",
      "orderDetails" : {
        "pendingQuantity" : "2",
        "avgExecutionPrice" : "0.00",
        "orderPlacedBy" : "--",
        "tradingSymbol" : "RELIANCE",
        "triggerPrice" : "0.00",
        "exchange" : "BSE",
        "totalQuantity" : "2",
        "transactionType" : "BUY",
        "productType" : "MIS",
        "orderType" : "L",
        "quantity" : "2",
        "filledQuantity" : "0",
        "orderPrice" : "1600.0",
        "filledPrice" : "0.00",
        "exchangeOrderNo" : "1590728958294000024",
        "orderValidity" : "DAY",
        "orderTime" : "29/05/2020 12:43:04"
      }
    }

###  <h3 id="cancel_order">Cancel Order:</h3>

   An order which is open or pending in system can be cancelled. In other words, cancellation cannot be initiated for already Executed, Rejected orders.This is for CNC, MIS and NRML Orders.
      
#### Parameters:

    xSessionToken, orderNumber
    
#### Sample CancelOrder Request:

    OrdersApi ordersApi = new OrdersApi();
    CancelOrderResponse cancelOrderResponse = ordersApi.cancelOrder(xSessionToken, "200529000000059");
    
#### Sample CancelOrder Response:

    {
      "serverTime" : "29/05/20 14:50:36",
      "msgId" : "25d6d99b-3224-4a77-b129-a5d0bd38349b",
      "status" : "Success",
      "orderNumber" : "200529000000059",
      "statusMessage" : "Order cancelled successfully"
    }

### <h3 id="cancelorderCO">CancelOrderCO:</h3>

   For Cancellation/exit of CO orders pass main leg Order number. If main leg is in Open/Pending state that order will be cancelled. If the main leg is executed and the sublegs are created and in open/Trigger pending state, the order will be exited. If the main leg is executed and if Stop loss is hit, API will return error message "SubOrder is in Executed status. Cannot exit/cancel such orders.
  
#### Parameters:

    xSessionToken, orderNumber
    
#### Sample CancelOrderCO Request:

    OrdersApi ordersApi = new OrdersApi();
    CancelOrderResponse cancelOrderResponse = ordersApi.cancelOrderCO(xSessionToken, "200618000000075");

#### sample CancelOrderCO Response:

    {
      "serverTime" : "01/06/20 16:06:25",
      "msgId" : "3b7ed673-9a5b-4014-afdf-158c8490beba",
      "status" : "Success",
      "orderNumber" : "200601000000129",
      "statusMessage" : "Cover Order 200601000000129exited successfully"
    }

### <h3 id="cancelorderBO">CancelOrderBO:</h3>

   For Cancellation/exit of BO orders pass main leg Order number. If main leg is in Open/Pending state that order will be cancelled. If the main leg is executed and the sublegs are created and in open/Trigger pending state, the order will be exited. If the main leg is executed and if either of Stop loss or target is hit, API will return error message "SubOrder is in Executed status. Cannot exit/cancel such orders.
        
#### Parameters:

    xSessionToken, orderNumber
    
#### Sample CancelOrderBO Request:

    OrdersApi ordersApi = new OrdersApi();
    CancelOrderResponse cancelOrderResponse = ordersApi.cancelOrderBO(xSessionToken, "200619000000003");

#### sample CancelOrderBO Response:

    {
      "serverTime" : "01/06/20 16:11:24",
      "msgId" : "c02e4a34-cb58-4822-8988-3736b22831e5",
      "status" : "Success",
      "orderNumber" : "200601000000134",
      "statusMessage" : "Bracket Order exited successfully"
    }

### <h3 id="tradebook">TradeBook:</h3>

   Details of all successfully executed orders placed by the user.
       
#### Parameters:

    xSessionToken
    
#### Sample TradeBook Request:

    TradeBookApi tradeBookApi = new TradeBookApi();
    TradeBookResponse tradeBook = tradeBookApi.getTradeBook(xSessionToken);
    
#### Sample TradeBook Response:
 
    {
      "serverTime" : "29/05/20 15:43:49",
      "msgId" : "d2b6770c-348b-4bd0-91fa-feb5b3d10d8d",
      "status" : "Success",
      "orderBookDetails" : [ {
        "orderNumber" : "200529000000200",
        "exchange" : "NSE",
        "tradingSymbol" : "RELIANCE",
        "transactionType" : "SELL",
        "productCode" : "CO",
        "orderValue" : "0.0",
        "orderTime" : "29-May-2020 15:02:02",
        "userId" : "DA35672",
        "orderType" : "SL-M",
        "orderPrice" : "1290.00",
        "triggerPrice" : "1290.00",
        "orderValidity" : "DAY",
        "orderStatus" : "Complete",
        "filledQuantity" : "0",
        "fillPrice" : "0.00",
        "averagePrice" : "0.00",
        "rejectionReason" : "--",
        "exchangeConfirmationTime" : "29-May-2020 15:02:02",
        "coverOrderPercentage" : "0.05",
        "orderRemarks" : "--",
        "exchangeOrderNumber" : "1100000000085407",
        "symbol" : "2885_NSE",
        "displayStrikePrice" : "00.00",
        "displayNetQuantity" : "1",
        "status" : "Trigger Pending",
        "exchangeStatus" : "trigger pending",
        "expiry" : "NA",
        "pendingQuantity" : "1",
        "totalQuanity" : "1",
        "optionType" : "XX",
        "orderPlaceBy" : "--"
       } ]
    }

### <h3 id="positions">Positions:</h3>

   Get position details of the user (The details of equity, derivative, commodity, currency borrowed or owned by the user).
        
#### Parameters:

    xSessionToken, positionType
    
#### Sample Positions Request:

    PositionsApi positionsApi = new PositionsApi();
    PositionResponse positions = positionsApi.getPositions(xSessionToken, SamcoConstants.POSITION_TYPE_DAY);
    
#### Sample Positions Response:

    {
      "serverTime" : "01/06/20 15:13:06",
      "msgId" : "9e143fb4-f9cb-4ea7-bd6d-bad6008816db",
      "positionDetails" : [ {
        "averagePrice" : "669.00",
        "exchange" : "NSE",
        "markToMarketPrice" : "0.00",
        "lastTradedPrice" : "669.00",
        "previousClose" : "705.45",
        "productCode" : "BO",
        "tradingSymbol" : "INFY-EQ",
        "calculatedNetQuantity" : "10.0",
        "averageBuyPrice" : "669.00",
        "averageSellPrice" : "0.00",
        "boardLotQuantity" : "1",
        "boughtPrice" : "6690.00",
        "buyQuantity" : "10",
        "carryForwardQuantity" : "0",
        "carryForwardValue" : "0.00",
        "multiplier" : "1",
        "netPositionValue" : "-6690.00",
        "netQuantity" : "10",
        "netValue" : "-6690.00",
        "positionType" : "DAY",
        "positionConversions" : [ "CNC", "NRML" ],
        "soldValue" : "0.00",
        "transactionType" : "BUY",
        "realizedGainAndLoss" : "0.00",
        "unrealizedGainAndLoss" : "0.00",
        "companyName" : "INFOSYS LIMITED"
      }]
    }

### <h3 id="positionConversion">PositionConversion:</h3>

   Convert an existing position of a margin product to a different margin product type. All or a subset of an existing position quantity can be converted to a different product type.The available margin product types are MARGIN_INTRADAY_SQUAREOFF(MIS), CASHNCARRY(CNC), NORMAL(NRML).
       
#### Parameters:

    xSessionToken, exchange, symbolName, transactionType, positionType, quantityToConvert, fromProductType, toProductType, netQuantity
    
#### Sample PositionConverstion Request:

    PositionsApi positionsApi = new PositionsApi();
    PositionConversionRequest conversionRequest = new PositionConversionRequest();
    conversionRequest.setSymbolName("RELIANCE");
    conversionRequest.setExchange(SamcoConstants.EXCHANGE_BSE);
    conversionRequest.setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY);
    conversionRequest.setPositionType(SamcoConstants.POSITION_TYPE_DAY);
    conversionRequest.setQuantityToConvert("2");
    conversionRequest.setFromProductType(SamcoConstants.PRODUCT_MIS);
    conversionRequest.setToProductType(SamcoConstants.PRODUCT_CNC);
    conversionRequest.setNetQuantity("2");
    PositionConversionResponse positionConversionResponse = positionsApi.convertPosition(xSessionToken, conversionRequest);

#### Sample PostionConverstion Response:

    {
      "serverTime" : "01/06/20 15:06:42",
      "msgId" : "ba32c75f-ee4b-4af6-a580-f17ad36fefd4",
      "status" : "Success",
      "statusMsg" : "Position Conversion from MIS to CNC successful"
    }

### <h3 id="positionSquareOff">PositionSquareOff:</h3>

   The PositionsApi class helps the user to Square Off existing position. Mostly used in day trading, in which user buy or sell a particular quantity of a stock and later in the day reverse the transaction to earn a profit.
       
#### Parameters:

    xSessionToken, symbolName,exchange,transactionType,productType,netQuantity
    
#### Sample PositionSquareoff Request:

    PositionsApi positionsApi = new PositionsApi();
    PositionSquareOffListRequest squareOffListRequest = new PositionSquareOffListRequest();
    PositionSquareOffRequest squareOffRequest = new PositionSquareOffRequest();
    squareOffRequest.setExchange(SamcoConstants.EXCHANGE_BSE);
    squareOffRequest.setSymbolName("TCS");
    squareOffRequest.setProductType(SamcoConstants.PRODUCT_MIS);
    squareOffRequest.setNetQuantity("1");
    squareOffRequest.setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY);
    List<PositionSquareOffRequest> list = new ArrayList<>();
    list.add(squareOffRequest);
    squareOffListRequest.setPositionSquareOffRequestList(list);
    PositionSquareOffListResponse squareOffPosition = positionsApi.squareOffPosition(xSessionToken, squareOffListRequest);

#### Sample PostionConverstion Response:

    {
      "serverTime": "25/06/20 20:04:30",
      "msgId": "fcb519b8-dd74-422a-8a65-1dc0a0caedb7",
      "positionSquareOffResponseList": [
        {
          "status": "Success",
          "statusMessage": "Position square off successful -TCS-EQ NetQty:1"
        }
      ]
    }

###  <h3 id="holdings">Holdings:</h3>

   Get the details of the Stocks which client is holding. Here, you will be able to get the Client holdings which are bought under ‘CNC’ product type and are not sold yet.
       
#### Parameters:

    xSessionToken
    
#### Sample Holdings Request:

    HoldingsApi holdingsApi = new HoldingsApi();
    HoldingResponse holding = holdingsApi.getHolding(xSessionToken);

#### Sample Holdings Response:

    {
      "serverTime": "25/06/20 13:46:16",
      "msgId": "d58af813-8ed2-400d-b769-9d89f873376d",
      "status": "Success",
      "statusMessage": "User Holding details retrieved successfully",
      "holdingSummary": {
        "gainingTodayCount": "1",
        "losingTodayCount": "2",
        "totalGainAndLossAmount": "-17.15",
        "portfolioValue": "13.40"
      },
      "holdingDetails": [
        {
            "averagePrice": "22.50",
            "exchange": "BSE",
            "lastTradedPrice": "0.00",
            "previousClose": "23.00",
            "productCode": "CNC",
            "symbolDescription": "ASHOK ALCO-CHEM LTD.",
            "tradingSymbol": "ASHOKALC",
            "totalGainAndLoss": "-22.50",
            "holdingsQuantity": "1",
            "collateralQuantity": "-1",
            "holdingsValue": "0.00",
            "sellableQuantity": "0"
        },
        {
            "averagePrice": "1.57",
            "exchange": "NSE",
            "lastTradedPrice": "2.30",
            "previousClose": "2.10",
            "productCode": "CNC",
            "symbolDescription": "JAIPRAKASH ASSOCIATES LTD",
            "tradingSymbol": "JPASSOCIAT-EQ",
            "totalGainAndLoss": "1.45",
            "holdingsQuantity": "2",
            "collateralQuantity": "-2",
            "holdingsValue": "4.60",
            "sellableQuantity": "0"
        }
      ]
    }

###  <h3 id="intraDayCandleData">IntraDayCandleData:</h3>

   Gets the Intraday candle data such as Open, high, low, close and volume within specific time period per min for a specific symbol.
      
#### Parameters:

    xSessionToken, exchange, symbolName, fromDate, toDate
    
#### Sample IntraDayCandleData Request:

    IntraDayCandleDataApi intraDayCandleDataApi = new IntraDayCandleDataApi();
    IntraDayCandleResponses intraDayCandleResponses = intraDayCandleDataApi.getIntradayCandleData(xSessionToken, "INFY", "2020-04-20", SamcoConstants.EXCHANGE_BSE, "2020-05-10");

#### Sample IntraDayCandleData Response:

    {
      "serverTime" : "31/05/20 20:46:52",
      "msgId" : "ff875ecd-5f42-47f6-a81e-37e6920acefc",
      "intradayCandleData" : [ {
        "dateTime" : "2020-04-27 11:50:00.0",
        "open" : "632.3",
        "high" : "632.3",
        "low" : "632.3",
        "close" : "632.3",
        "volume" : "1"
      }, {
        "dateTime" : "2020-04-27 12:28:00.0",
        "open" : "632.3",
        "high" : "632.3",
        "low" : "632.3",
        "close" : "632.3",
        "volume" : "0"
      }, {
        "dateTime" : "2020-04-27 12:29:00.0",
        "open" : "632.3",
        "high" : "632.3",
        "low" : "632.3",
        "close" : "632.3",
        "volume" : "0"
      }]
    }

### <h3 id="indexIntraDayCandleData">IndexIntraDayCandleData:</h3>

   Gets the Index intraday candle data such as Open, high, low, close and volume within specific time period per min for a specific index.
      
#### Parameters:

    xSessionToken, indexName, fromDate, toDate
    
#### Sample IndexIntraDayCandleData Request:

    IntraDayCandleDataApi intraDayCandleDataApi = new IntraDayCandleDataApi();
    IndexIntraDayCandleDataResponse indexIntradayCandleData = intraDayCandleDataApi.getIndexIntradayCandleData(xSessionToken, "NIFTY 200", "2020-01-15", "2020-02-10");

#### Sample IndexIntraDayCandleData Response:

    {
      "serverTime" : "02/06/20 18:19:42",
      "msgId" : "6e6ad246-9808-4bfc-8cf3-724f4f745113",
      "status" : "Success",
      "statusMessage" : "Index IntraDay Candle data retrieved successfully ",
      "indexIntraDayCandleData" : [ {
        "dateTime" : "2019-08-26 09:07:00.0",
        "open" : "5664.2",
        "high" : "5664.2",
        "low" : "5664.2",
        "close" : "5664.2",
        "volume" : "0"
      }, {
        "dateTime" : "2019-08-26 09:15:00.0",
        "open" : "5662.15",
        "high" : "5662.15",
        "low" : "5632.1",
        "close" : "5632.1",
        "volume" : "0"
      }, {
        "dateTime" : "2019-08-26 09:16:00.0",
        "open" : "5631.3",
        "high" : "5638.55",
        "low" : "5628.5",
        "close" : "5638.55",
        "volume" : "0"
      } ]
    }

### <h3 id="historicalCandleData">HistoricalCandleData:</h3>

   Gets the historical candle data such as Open, high, low, close, last traded price and volume within specific dates for a specific symbol. From date is mandatory. End date is optional and defaults to Today.
       
#### Parameters:

    xSessionToken, exchange, symbolName, fromDate, toDate
    
#### Sample HistoricalCandleData Request:

    HistoricalCandleDataApi historicalCandleDataApi = new HistoricalCandleDataApi();
    HistoricalCandleResponse historicalCandleData = historicalCandleDataApi.getHistoricalCandleData(xSessionToken, "INFY", "2019-01-01", SamcoConstants.EXCHANGE_BSE, "2020-02-01");

#### Sample HistoricalCandleData respone:

    {
      "serverTime" : "31/05/20 20:18:07",
      "msgId" : "07701e56-fb82-4315-bad0-449da8482549",
      "historicalCandleData" : [ {
        "date" : "2019-01-01",
        "open" : "661.0",
        "high" : "667.0",
        "low" : "654.3",
        "close" : "664.65",
        "ltp" : "664.65",
        "volume" : "221951"
      }, {
        "date" : "2019-01-02",
        "open" : "668.0",
        "high" : "673.7",
        "low" : "662.45",
        "close" : "669.3",
        "ltp" : "669.3",
        "volume" : "419594"
      }, {
        "date" : "2019-01-03",
        "open" : "670.9",
        "high" : "677.0",
        "low" : "663.8",
        "close" : "667.55",
        "ltp" : "667.55",
        "volume" : "355183"
      }]
    }

### <h3 id="indexHistoricalCandleData">IndexHistoricalCandleData:</h3>

   Gets the Index historical candle data such as Open, high, low, close, last traded price and volume within specific dates for a specific index. From date is mandatory. End date is optional and defaults to Today.
        
#### Parameters:

    xSessionToken, indexName, fromDate, toDate
    
#### Sample IndexHistoricalCandleData Request:

    HistoricalCandleDataApi historicalCandleDataApi = new HistoricalCandleDataApi();
    IndexCandleDataResponse indexCandleDataResponse = historicalCandleDataApi.getIndexCandleData(xSessionToken, "NIFTY 200", "2015-03-04", "2017-03-05");

#### Sample IndexHistoricalCandleData Response:

    {
      "serverTime" : "31/05/20 20:31:02",
      "msgId" : "cf232a72-e7d3-42a3-a834-9e8cbd65aa20",
      "status" : "Success",
      "statusMessage" : "Index HistoricalCandle data retrieved successfully ",
      "indexCandleData" : [ {
        "date" : "2015-03-04",
        "open" : "4702.2",
        "high" : "4706.3",
        "low" : "4594.3",
        "close" : "4607.4",
        "ltp" : "4607.4",
        "volume" : "0"
      }, {
        "date" : "2015-03-05",
        "open" : "4612.65",
        "high" : "4630.65",
        "low" : "4576.85",
        "close" : "4621.7",
        "ltp" : "4621.7",
        "volume" : "0"
      }, {
        "date" : "2015-03-09",
        "open" : "4598.15",
        "high" : "4598.15",
        "low" : "4527.4",
        "close" : "4534.15",
        "ltp" : "4534.15",
        "volume" : "0"
      }]
    }

### <h3 id="logout">Logout:</h3>

   Logging out user from the application
      
#### Parameters:

    xSessionToken
    
#### Sample Logout Request:

    UserLogoutApi logoutApi = new UserLogoutApi();
    SimpleResponse logout = logoutApi.logout(xSessionToken);

#### Sample Logout Response:

    {
      "serverTime" : "31/05/20 21:27:52",
      "msgId" : "41627994-5c96-411c-b15c-dbda00029269",
      "status" : "Success",
      "statusMessage" : "User has successfully logged out"
    }
    
 
## Constant List:

   This section contains the list of possible constant values that can be passed for input attributes like exchanges, product types etc.
   
### Product types:
   
    PRODUCT_MIS 
    PRODUCT_CNC
    PRODUCT_NRML
    PRODUCT_CO
    PRODUCT_BO
 
    Example:- setProductType(SamcoConstants.PRODUCT_MIS)
 
### Exchanges:

    EXCHANGE_NSE
    EXCHANGE_BSE
    EXCHANGE_NFO
    EXCHANGE_CDS
    EXCHANGE_MCX
    
    Example:- setExchange(SamcoConstants.EXCHANGE_BSE)
 
### Transaction types:

    TRANSACTION_TYPE_BUY
    TRANSACTION_TYPE_SELL
    
    Example:- setTransactionType(SamcoConstants.TRANSACTION_TYPE_BUY)
 
### Order types:

    ORDER_TYPE_MARKET
    ORDER_TYPE_LIMIT 
    ORDER_TYPE_SLM 
    ORDER_TYPE_SL 
    
     Example:- setOrderType(SamcoConstants.ORDER_TYPE_MARKET);

### Validity types:

    VALIDITY_DAY 
    VALIDITY_IOC 
    
    Example:- setOrderValidity(SamcoConstants.VALIDITY_DAY)
    
### Position types:

    POSITION_TYPE_DAY
    POSITION_TYPE_NET
    
    Example:- setPositionType(SamcoConstants.POSITION_TYPE_DAY)
