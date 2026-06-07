package in.samco;

import java.util.List;

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
 * See ta-api-docs/streaming/streaming-quote-data.md.
 */
public class StreamingQuoteSample {

    public static void main(String[] args) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate("<AES_ENCRYPTED_API_KEY>", "<AES_ENCRYPTED_API_SECRET>")
                .getSessionToken();

        StreamingListener listener = new StreamingListener() {
            @Override
            public void onOpen() {
                System.out.println("[quote-stream] connected");
            }

            @Override
            public void onQuote(QuoteTick tick) {
                // Parsed quote frame — fields like ltp, ltq, volume, oi, sym, exc, etc.
                System.out.println("[quote-stream] tick: " + tick);
            }

            @Override
            public void onMessage(String text) {
                // Raw frames that did not match a known shape (ack frames, errors, …).
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

        // Subscribe to two symbols: SBIN on NSE and an F&O contract on NFO.
        List<SymbolRef> symbols = List.of(
                new SymbolRef("3045_NSE"),
                new SymbolRef("89017_NFO"));
        client.subscribeQuote(symbols);

        // Keep the process alive long enough to receive ticks.
        Thread.sleep(30_000);

        // Cleanly tear down — important: unsubscribe so the server stops sending.
        client.unsubscribeQuote(symbols);
        client.close();
    }
}
