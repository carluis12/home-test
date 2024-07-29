package util.enums;

/**
 * Enum that represents the browser types.
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    SAFARI,
    EDGE,
    EXPLORER;

    public static BrowserType getBrowser(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> CHROME;
            case "safari" -> SAFARI;
            case "explorer", "edge", "microsoftedge" -> EDGE;
            default -> FIREFOX;
        };
    }
}
