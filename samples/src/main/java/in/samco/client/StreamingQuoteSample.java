package in.samco.client;

import java.util.List;
import java.util.Properties;

import in.samco.api.update.SessionTokenApi;
import in.samco.streaming.QuoteTick;
import in.samco.streaming.StreamingClient;
import in.samco.streaming.StreamingListener;
import in.samco.streaming.SymbolRef;

/**
 * (f1) Streaming Quote Data — WebSocket client sample.
 *
 * Opens a connection to wss://stream.samco.in via the SDK's StreamingClient,
 * authenticates with the session JWT, and subscribes to a quote channel for
 * one or more symbols. The SDK handles the JSON envelope
 * ({@code {"request":{"streaming_type":"quote","data":{"symbols":[...]},
 *           "request_type":"subscribe","response_format":"json"}}})
 * internally — callers only deal with SymbolRef.
 *
 * A SymbolRef is encoded as "&lt;securityToken&gt;_&lt;exchange&gt;",
 * e.g. "3045_NSE" for SBIN on NSE Cash, or "89017_NFO" for an F&amp;O leg.
 *
 * See https://docs-tradeapi.samco.in/streaming/streaming-quote-data.html.
 */
public class StreamingQuoteSample {

    public static void main(String[] args) throws Exception {
        Properties cfg = QuickStartSample.loadConfig();
        QuickStartSample.requireRealCredentials(cfg);
        run(cfg);
    }

    public static void run(Properties cfg) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate(QuickStartSample.apiKey(cfg), QuickStartSample.apiSecret(cfg))
                .getSessionToken();

        StreamingListener listener = new StreamingListener() {
            @Override
            public void onOpen() {
                System.out.println("[quote-stream] connected");
            }

            @Override
            public void onQuote(QuoteTick tick) {
                System.out.println("[quote-stream] tick: " + tick);
            }

            @Override
            public void onMessage(String text) {
                System.out.println("[quote-stream] raw: " + text);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("[quote-stream] error: " + error);
            }

            @Override
            public void onClosed(int statusCode, String reason) {
                System.out.println("[quote-stream] closed: " + statusCode + " " + reason);
            }
        };

        StreamingClient client = new StreamingClient(listener);
        client.connect(sessionToken).join();

        List<SymbolRef> symbols = List.of(
                new SymbolRef("3045_NSE"),
                new SymbolRef("89017_NFO"));
        client.subscribeQuote(symbols);

        Thread.sleep(30_000);

        client.unsubscribeQuote(symbols);
        client.close();
    }
}
