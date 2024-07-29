package util.constants;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface that provides constants for the project.
 */
public interface Config {

    String PROPERTIES = "project.properties";

    String HUB_URL = "http://localhost:4444/wd/hub";

    //Browser
    List<String> CHROME_ARGUMENTS = Collections.unmodifiableList(Arrays.asList(
            "disable-popup-blocking",
            "start-maximized",
            "test-type",
            "--disable-impl-side-painting",
            "disable-infobars",
            "--silent",
            "--disable-web-security",
            "--allow-running-insecure-content",
            "--disable-dev-shm-usage",
            "--ignore-certificate-errors",
            "--window-size=1920,1080",
            "--no-sandbox",
            "--lang=en-US"));
    String PREFS = "prefs";
    Map<String, Object> PASSWORD_PREFS = Stream.of(
                    new AbstractMap.SimpleImmutableEntry<>("credentials_enable_service", false),
                    new AbstractMap.SimpleImmutableEntry<>("password_manager_enabled", false))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

}
