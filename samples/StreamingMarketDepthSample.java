package in.samco;

import java.util.List;

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
 * See ta-api-docs/streaming/streaming-market-data.md.
 */
public class StreamingMarketDepthSample {

    public static void main(String[] args) throws Exception {

        String sessionToken = new SessionTokenApi()
                .generate("<AES_ENCRYPTED_API_KEY>", "<AES_ENCRYPTED_API_SECRET>")
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
