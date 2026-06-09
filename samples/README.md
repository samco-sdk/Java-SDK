# SAMCO Stocknote Java Bridge — Samples

Runnable Java samples that exercise the v3.2.0 Trade APIs. For an overview of the SDK itself, see the [top-level README](../README.md).

Each sample is a self-contained `main`-class file; they share no state. Pick the one that matches the API you want to try, replace the placeholder credentials, and run it with `mvn exec:java`.

## Prerequisites

- **Java 17+** (the SDK is compiled with `maven.compiler.release=17`).
- **Maven 3.6+**.
- The SDK JAR at `../dist/samco-bridge-java-3.2.0.jar`.
- A SAMCO trading account with:
  - An OAuth app created in the [Web Dashboard](https://docs-tradeapi.samco.in/dashboard/user-manual) (gives you `apiKey` + `apiSecret`).
  - At least one static IP registered for that app — order-related calls reject traffic from non-whitelisted IPs.

## 1. Build

From this directory:

```bash
mvn -U clean install
```

(or `mvn -U clean package` if you do not want to install the samples artifact into your local `~/.m2/repository/`).

The samples `pom.xml` references the SDK JAR directly via a `system`-scoped dependency on `../dist/samco-bridge-java-3.2.0.jar`, so no separate `mvn install:install-file` bootstrap is required — a fresh clone builds out of the box.

The build produces a self-contained fat-jar at
`target/samco-bridge-java-samples-3.2.0.jar` and seeds an editable
`target/config.properties` next to it.

## 2. Configure credentials

Edit `target/config.properties` and replace the placeholders with your
credentials:

```properties
samco.apiKey=<YOUR_API_KEY>
samco.apiSecret=<YOUR_API_SECRET>
# Opt-ins
samco.runPlaceOrder=false   # set true to place a real LIMIT BUY order
samco.runStreaming=true     # streaming samples run for ~30s each
```

A copy of `config.properties` is also bundled inside the jar as a
classpath fallback — but it ships with placeholders, so you must edit the
external file (or one in your working directory) before running.

## 3. Run as a self-contained jar

```bash
java -jar target/samco-bridge-java-samples-3.2.0.jar
```

The fat-jar's default `Main-Class` is `in.samco.client.QuickStartSample`. It loads
`config.properties` (CWD → next-to-jar → classpath fallback) and runs every
other sample in sequence with section headers.

## 4. Run a single sample

Each sample also has its own `main(...)` that reads the same
`config.properties`:

```bash
mvn -q compile exec:java -Dexec.mainClass=in.samco.client.SessionTokenSample
```

| Sample class                                 | Demonstrates                                               | Docs                                                                            |
|----------------------------------------------|------------------------------------------------------------|---------------------------------------------------------------------------------|
| `in.samco.client.SessionTokenSample`         | `POST /session/token` — direct JWT auth                    | [Generate session token](https://docs-tradeapi.samco.in/session/generate-token.html)        |
| `in.samco.client.WhoAmISample`               | `GET /ip/whoami` — IP diagnostics                          | [Who Am I](https://docs-tradeapi.samco.in/static-ip/whoami.html)                            |
| `in.samco.client.QuoteSample`                | `GET /quote/getQuote`                                      | [Get quote](https://docs-tradeapi.samco.in/quote/get-quote.html)                            |
| `in.samco.client.PositionsSample`            | `GET /position/getPositions?positionType=NET`              | [Get positions](https://docs-tradeapi.samco.in/position/get-positions.html)                 |
| `in.samco.client.HoldingsSample`             | `GET /holding/getHoldings`                                 | [Get holdings](https://docs-tradeapi.samco.in/holding/get-holdings.html)                    |
| `in.samco.client.OrderBookSample`            | `GET /order/orderBook`                                     | [Order book](https://docs-tradeapi.samco.in/order/order-book.html)                          |
| `in.samco.client.OrderStatusSample`          | `GET /order/getOrderStatus?orderNumber=...`                | [Order status](https://docs-tradeapi.samco.in/order/order-status.html)                      |
| `in.samco.client.PlaceOrderSample`           | `POST /order/placeOrder` (LIMIT variant)                   | [Place order](https://docs-tradeapi.samco.in/order/place-order.html)                        |
| `in.samco.client.StreamingQuoteSample`       | WebSocket quote channel (`StreamingClient.subscribeQuote`) | [Streaming quote data](https://docs-tradeapi.samco.in/streaming/streaming-quote-data.html)  |
| `in.samco.client.StreamingMarketDepthSample` | WebSocket market-depth channel                             | [Streaming market data](https://docs-tradeapi.samco.in/streaming/streaming-market-data.html)|
| `in.samco.client.QuickStartSample`           | End-to-end smoke: Session + WhoAmI + Quote + PlaceOrder    | —                                                                               |

## Credentials

All samples read credentials from `config.properties` via the shared
loader in `QuickStartSample`. The resolver checks, in order:

1. `./config.properties` (current working directory)
2. `config.properties` next to the running jar
3. `/config.properties` bundled inside the jar (classpath fallback)

Required keys:

- `samco.apiKey` and `samco.apiSecret` — your OAuth app credentials, as described in
  [the session-token doc](https://docs-tradeapi.samco.in/session/generate-token).

Optional:

- `samco.runPlaceOrder` (default `false`) — opt-in for `PlaceOrderSample`.
- `samco.runStreaming` (default `true`) — opt-in for the two streaming samples.

For production, source these from a secrets manager rather than disk.

## Troubleshooting

- **`403 — The IP is not the registered static IP`** — run `WhoAmISample` from the *same host* to see the IP our server received, then update your registered IP in the Dashboard if it differs.
- **`401 — Invalid API key` / `EOAUTH001`** — the app may not be Active in the Dashboard, or the `apiKey` is not valid.
- **Session expired** — session tokens are valid for ~24 hours. Re-run `SessionTokenSample` to obtain a new one.
- **`StreamingQuoteSample` / `StreamingMarketDepthSample` exit after ~30 s** — by design (`Thread.sleep(30_000)` in `main`). Increase the value or replace it with your own loop for longer runs; always call `unsubscribeQuote` / `unsubscribeMarketDepth` before `close()`.
