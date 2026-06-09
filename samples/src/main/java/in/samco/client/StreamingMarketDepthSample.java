package in.samco.client;

import java.util.List;
import java.util.Properties;

import in.samco.api.update.SessionTokenApi;
import in.samco.streaming.DepthTick;
import in.samco.streaming.StreamingClient;
import in.samco.streaming.StreamingListener;
import in.samco.streaming.SymbolRef;

/**
 * (f2) Streaming Market-Depth Data — WebSocket client sample.
 *
 * Same wss://stream.samco.in connection as the quote stream, but subscribes to
 * the market-depth channel — each tick carries the 5-level order book (best
 * bids / best asks) plus derived analytics fields (delta/gamma/theta/vega/iv
 * for F&amp;O contracts).
 *
 * See https://docs-tradeapi.samco.in/streaming/streaming-market-data.html.
 */
public class StreamingMarketDepthSample {

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
                System.out.println("[depth-stream] connected");
            }

            @Override
            public void onDepth(DepthTick tick) {
                System.out.println("[depth-stream] tick: " + tick);
            }

            @Override
            public void onMessage(String text) {
                System.out.println("[depth-stream] raw: " + text);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("[depth-stream] error: " + error);
            }

            @Override
            public void onClosed(int statusCode, String reason) {
                System.out.println("[depth-stream] closed: " + statusCode + " " + reason);
            }
        };

        StreamingClient client = new StreamingClient(listener);
        client.connect(sessionToken).join();

        List<SymbolRef> symbols = List.of(new SymbolRef("3045_NSE"));
        client.subscribeMarketDepth(symbols);

        Thread.sleep(30_000);

        client.unsubscribeMarketDepth(symbols);
        client.close();
    }
}
