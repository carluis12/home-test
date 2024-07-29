package config.listeners;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;

@Slf4j
public class TestListener implements ITestListener {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Started test: %s browser: %s"
                .formatted(result.getName(), result.getTestContext().getAttribute("browserName")));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test Case: %s on %s  PASSED!"
                .formatted(result.getName(), result.getTestContext().getAttribute("browserName")));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test Case: %s on %s  FAILED!"
                .formatted(result.getName(), result.getTestContext().getAttribute("browserName")));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test Case: %s on %s  SKIPPED!"
                .formatted(result.getName(), result.getTestContext().getAttribute("browserName")));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
        String formattedDate = dateFormat.format(context.getStartDate());
        log.info("Tests Start at: %s".formatted(formattedDate));
    }

    @Override
    public void onFinish(ITestContext context) {
        String formattedDate = dateFormat.format(context.getEndDate());
        log.info("Tests Finish at: %s".formatted(formattedDate));
    }
}
