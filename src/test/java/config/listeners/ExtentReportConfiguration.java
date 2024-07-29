package config.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class ExtentReportConfiguration implements IReporter {

    private static final String FILE_FORMAT = ".html";
    private static final String FILE_NAME = "report_";
    private static final String OUTPUT_FILES_FOLDER = "/target/outputfiles";
    private static final String OUTPUT_REPORTS_FOLDER = "/reports/";
    private static final String OUTPUT_FOLDER = OUTPUT_FILES_FOLDER + OUTPUT_REPORTS_FOLDER;
    private String reportName;
    private String baseDir;
    private ExtentReports extent;

    public ExtentReportConfiguration() {
        setBaseDir(System.getProperty("user.dir"));
    }

    public void setReportName(String name) {
        this.reportName = FILE_NAME + name + FILE_FORMAT;
    }

    private void createOutputReportFolder() {
        File dir = new File(
                String.format("%s%s%s", getBaseDir(), OUTPUT_FILES_FOLDER, OUTPUT_REPORTS_FOLDER));
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        init();

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();

                buildTestNodes(context.getPassedTests(), Status.PASS);
                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);

            }
        }

        for (String s : Reporter.getOutput()) {
            extent.createTest(s);
        }
        extent.flush();
    }


    private void init() {
        createOutputReportFolder();
        setReportName("tests");

        ExtentSparkReporter extentHtmlReporter = new ExtentSparkReporter(
                baseDir + OUTPUT_FOLDER + getReportName());

        extentHtmlReporter.config().setDocumentTitle("Test Report");
        extentHtmlReporter.config().setReportName("Test Report");
        extentHtmlReporter.config().setTheme(Theme.DARK);
        extentHtmlReporter.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(extentHtmlReporter);
        extent.setReportUsesManualConfiguration(true);
    }


    private void buildTestNodes(IResultMap tests, Status status) {

        ExtentTest test;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.createTest(result.getMethod().getMethodName());

                for (String group : result.getMethod().getGroups()) {
                    test.assignCategory(group);
                }

                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                } else {
                    test.log(status,
                            String.format("%s%s%s", "Test ", StringUtils.lowerCase(status.toString()), "ed"));
                }

                String description = String
                        .format("%s%s", "Description: ", testDescription(result));

                Markup m = MarkupHelper.createJsonCodeBlock(result.getParameters());
                test.info(m);

                test.getModel()
                        .setDescription(description);
                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }


    private String testDescription(ITestResult result) {
        String descriptionEmptyMessage = "The test has no description";
        if (result.getMethod().getDescription() == null || result.getMethod().getDescription().isEmpty()) {
            return descriptionEmptyMessage;
        }
        return result.getMethod().getDescription();
    }
}
