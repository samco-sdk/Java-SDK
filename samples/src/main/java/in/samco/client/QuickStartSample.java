package in.samco.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * End-to-end showcase: loads config.properties once and runs every other
 * sample's {@code run(Properties)} in sequence.
 *
 * <p>This class also hosts the shared {@link #loadConfig()} helper that each
 * sample's standalone {@code main} delegates to.
 *
 * <h2>config.properties resolution order</h2>
 * <ol>
 *   <li>{@code ./config.properties} (current working directory)</li>
 *   <li>{@code config.properties} next to the running jar</li>
 *   <li>{@code /config.properties} bundled inside the jar (classpath)</li>
 * </ol>
 */
public class QuickStartSample {

    private static final String CONFIG_FILE = "config.properties";

    public static void main(String[] args) throws Exception {

        Properties cfg = loadConfig();
        requireRealCredentials(cfg);

        runStep("1. SessionTokenSample", () -> SessionTokenSample.run(cfg));
        runStep("2. WhoAmISample",       () -> WhoAmISample.run(cfg));
        runStep("3. QuoteSample",        () -> QuoteSample.run(cfg));
        runStep("4. PositionsSample",    () -> PositionsSample.run(cfg));
        runStep("5. HoldingsSample",     () -> HoldingsSample.run(cfg));
        runStep("6. OrderBookSample",    () -> OrderBookSample.run(cfg));
        runStep("7. OrderStatusSample",  () -> OrderStatusSample.run(cfg));

        if (flag(cfg, "samco.runPlaceOrder")) {
            runStep("8. PlaceOrderSample", () -> PlaceOrderSample.run(cfg));
        } else {
            skipped("PlaceOrderSample", "samco.runPlaceOrder=false (destructive — set to true to enable)");
        }

        if (flag(cfg, "samco.runStreaming")) {
            runStep("9. StreamingQuoteSample",        () -> StreamingQuoteSample.run(cfg));
            runStep("10. StreamingMarketDepthSample", () -> StreamingMarketDepthSample.run(cfg));
        } else {
            skipped("Streaming samples", "samco.runStreaming=false");
        }
    }

    @FunctionalInterface
    private interface SampleStep {
        void run() throws Exception;
    }

    private static void runStep(String title, SampleStep step) {
        section(title);
        try {
            step.run();
        } catch (Exception ex) {
            System.out.println("[" + title + "] failed: " + ex.getClass().getSimpleName()
                    + ": " + ex.getMessage());
        }
    }

    /**
     * Resolve {@code config.properties} from (1) CWD, (2) next-to-jar, or
     * (3) the classpath copy bundled inside the jar.
     */
    public static Properties loadConfig() {
        Properties props = new Properties();

        Path cwd = Paths.get(CONFIG_FILE).toAbsolutePath();
        if (Files.isRegularFile(cwd)) {
            return readFrom(cwd);
        }

        Path nextToJar = resolveNextToJar();
        if (nextToJar != null && Files.isRegularFile(nextToJar)) {
            return readFrom(nextToJar);
        }

        try (InputStream in = QuickStartSample.class.getResourceAsStream("/" + CONFIG_FILE)) {
            if (in != null) {
                props.load(in);
                return props;
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read bundled config.properties", ex);
        }

        throw new IllegalStateException(
                "config.properties not found. Expected at " + cwd
                        + " or alongside the jar, or bundled on the classpath.");
    }

    /**
     * Read {@code samco.apiKey} / {@code samco.apiSecret}, failing with a
     * helpful message if either is missing or still a placeholder.
     */
    public static void requireRealCredentials(Properties cfg) {
        String apiKey = cfg.getProperty("samco.apiKey", "").trim();
        String apiSecret = cfg.getProperty("samco.apiSecret", "").trim();
        if (isBlank(apiKey) || isPlaceholder(apiKey)
                || isBlank(apiSecret) || isPlaceholder(apiSecret)) {
            throw new IllegalStateException(
                    "samco.apiKey / samco.apiSecret are missing or still placeholders. "
                            + "Edit config.properties (next to the jar or in the working directory) "
                            + "with your AES-encrypted credentials before running.");
        }
    }

    public static String apiKey(Properties cfg) {
        return cfg.getProperty("samco.apiKey", "").trim();
    }

    public static String apiSecret(Properties cfg) {
        return cfg.getProperty("samco.apiSecret", "").trim();
    }

    private static boolean flag(Properties cfg, String key) {
        return "true".equalsIgnoreCase(cfg.getProperty(key, "false").trim());
    }

    private static boolean isBlank(String s) {
        return s == null || s.isEmpty();
    }

    private static boolean isPlaceholder(String s) {
        return s.startsWith("<") && s.endsWith(">");
    }

    private static Properties readFrom(Path path) {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            props.load(in);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read " + path, ex);
        }
        return props;
    }

    private static Path resolveNextToJar() {
        try {
            Path codeSource = Paths.get(
                    QuickStartSample.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI());
            Path parent = Files.isDirectory(codeSource) ? codeSource : codeSource.getParent();
            return parent == null ? null : parent.resolve(CONFIG_FILE);
        } catch (URISyntaxException | SecurityException ex) {
            return null;
        }
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("==== " + title + " ====");
    }

    private static void skipped(String title, String reason) {
        System.out.println();
        System.out.println("---- " + title + " (skipped: " + reason + ") ----");
    }
}
