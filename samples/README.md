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

## 1. Install the SDK JAR locally (one-time)

From this directory:

```bash
mvn install:install-file \
  -Dfile=../dist/samco-bridge-java-3.2.0.jar \
  -DgroupId=in.samco \
  -DartifactId=samco-bridge-java \
  -Dversion=3.2.0 \
  -Dpackaging=jar
```

This drops the JAR into your local `~/.m2/repository/` so the `pom.xml` here can resolve it as a normal Maven dependency.

## 2. Build

```bash
mvn compile
```

## 3. Run a sample

Pick a main class and run it:

```bash
mvn -q compile exec:java -Dexec.mainClass=in.samco.SessionTokenSample
```

| Sample class                          | Demonstrates                                                 | Source-of-truth doc                                  |
|---------------------------------------|--------------------------------------------------------------|------------------------------------------------------|
| `in.samco.SessionTokenSample`         | `POST /session/token` — direct JWT auth                      | `ta-api-docs/session/generate-token.md`              |
| `in.samco.WhoAmISample`               | `GET /ip/whoami` — IP diagnostics                            | `ta-api-docs/static-ip/whoami.md`                    |
| `in.samco.OAuthTokenExchangeSample`   | OAuth 2.1 `POST /oauth/token` code exchange + refresh        | `ta-api-docs/oauth/authorize-flow.md`                |
| `in.samco.QuoteSample`                | `GET /quote/getQuote`                                        | `ta-api-docs/quote/get-quote.md`                     |
| `in.samco.PlaceOrderSample`           | `POST /order/placeOrder` (LIMIT variant)                     | `ta-api-docs/order/place-order.md`                   |
| `in.samco.StreamingQuoteSample`       | WebSocket quote channel (`StreamingClient.subscribeQuote`)   | `ta-api-docs/streaming/streaming-quote-data.md`      |
| `in.samco.StreamingMarketDepthSample` | WebSocket market-depth channel                               | `ta-api-docs/streaming/streaming-market-data.md`     |
| `in.samco.Sample`                     | End-to-end smoke: Session + WhoAmI + Quote + PlaceOrder      | —                                                    |

## Credentials

Every sample has placeholders that **must** be replaced before running:

- `<AES_ENCRYPTED_API_KEY>` and `<AES_ENCRYPTED_API_SECRET>` — your OAuth app credentials, AES-encrypted as described in [the session-token doc](https://docs-tradeapi.samco.in/session/generate-token).
- `<AUTH_CODE_FROM_CALLBACK>` — only in `OAuthTokenExchangeSample`, the short-lived (10-minute) code returned to your OAuth redirect URI.

For production code, source these from environment variables or a secrets manager rather than hard-coding them.

## Troubleshooting

- **`403 — The IP is not the registered static IP`** — run `WhoAmISample` from the *same host* to see the IP our server received, then update your registered IP in the Dashboard if it differs.
- **`401 — Invalid API key` / `EOAUTH001`** — the app may not be Active in the Dashboard, or the `apiKey` is not AES-encrypted.
- **Session expired** — session tokens are valid for ~24 hours. Re-run `SessionTokenSample` (or use the OAuth refresh-token flow) to obtain a new one.
- **`StreamingQuoteSample` / `StreamingMarketDepthSample` exit after ~30 s** — by design (`Thread.sleep(30_000)` in `main`). Increase the value or replace it with your own loop for longer runs; always call `unsubscribeQuote` / `unsubscribeMarketDepth` before `close()`.
