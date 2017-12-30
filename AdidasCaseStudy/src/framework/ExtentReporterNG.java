package framework;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.BaseTestCase;


public class ExtentReporterNG implements IReporter, ITestListener {
	ExtentReports extent;
	protected static int count = 0;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		extent = new ExtentReports(outputDirectory + File.separator + "Report.html", true, DisplayOrder.OLDEST_FIRST);
		extent.loadConfig(new File("data/config/extent-config.xml"));
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();

				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
			}
		}
		// for all Reporter output
		for (String s : Reporter.getOutput()) {

			extent.setTestRunnerOutput(s);

		}

		extent.flush();
		extent.close();
		
		if(suites.size() == 1)
			genrateEmailableReport(suites.get(0).getResults().values(), suites.get(0), outputDirectory);
	}

	private void genrateEmailableReport(Collection<ISuiteResult> results, ISuite suite, String filePath){
		try{
			String reportContent = createHtmlTable(results, suite);
			PrintWriter writer = new PrintWriter(filePath + File.separator + "EmailableReport.html", "UTF-8");
			writer.write(reportContent);
			writer.close();
		} catch (IOException e) {
			Reporter.log("Failed to create emailable report.");
		}
	}

	private String createHtmlTable(Collection<ISuiteResult> results, ISuite suite){
		int totalPassed = 0, totalFailed = 0, totalSkipped = 0;
		String htmlTableFrame = "<br>" +
		                         "<head>" + 
                                  "<title>{$} Report</title>" +
				                    "<style>table, th, td {border: 1px solid black; border-collapse: collapse;}th, td {padding: 5px;}th {text-align: central;}</style>" +
                                 "</head>" +
	                                "<table bgcolor=\"#00FFFF\">" + 
				                      "<tr>" +
		                                  "<th colspan=\"5\" style=\"text-align:center; color:#4D4DFF\">" + suite.getName() + "</th>" +
				                      "</tr>" +
				                      "<tr>" +
		                                  "<th> Module Name </th>" +
		                                  "<th> No of Test </th>" +
		                                  "<th> Passed </th>" +
		                                  "<th> Failed </th>" +
		                                  "<th> Skipped </th>" +
		                               "</tr>";    
			
		for (ISuiteResult sr : results) {
			ITestContext c = sr.getTestContext();

			int passed = c.getPassedTests().size(), failed = c.getFailedTests().size(), skipped = c.getSkippedTests().size();
			
			htmlTableFrame = addHtmlRow(htmlTableFrame, c.getName(), String.valueOf(passed + failed + skipped), 
					String.valueOf(passed), String.valueOf(failed), String.valueOf(skipped));
			
			totalPassed += passed;
			totalFailed += failed;
			totalSkipped += skipped;
		}
		
		htmlTableFrame = addHtmlRow(htmlTableFrame, "Total", String.valueOf(totalPassed + totalFailed + totalSkipped), 
				String.valueOf(totalPassed), String.valueOf(totalFailed), String.valueOf(totalSkipped));
		
		String status = totalFailed > 0 || totalPassed == 0 ? "Failed" : "Passed";
		htmlTableFrame = htmlTableFrame.replace("{$}", status);
		
		return htmlTableFrame.concat("</table><br>");
	}
	
	private String addHtmlRow(String currentHtml, String...rowData){
		String htmlRow = "<tr>" +
	                        "<td>" + rowData[0] + "</td>" +
	                        "<td>" + rowData[1] + "</td>" +
	                        "<td>" + rowData[2] + "</td>" +
	                        "<td>" + rowData[3] + "</td>" +
	                        "<td>" + rowData[4] + "</td>" +
	                      "</tr>";
		htmlRow = rowData[0].equalsIgnoreCase("total") ? htmlRow.replace("<tr>", "<tr bgcolor=\"#E6FFE6\">") : htmlRow;
		
		return currentHtml.concat(htmlRow);
	}
	
	private void buildTestNodes(IResultMap tests, LogStatus status) {
		ExtentTest test;

		if (tests.size() > 0) {

			List<ITestResult> resultList = new LinkedList<ITestResult>(tests.getAllResults());

			class ResultComparator implements Comparator<ITestResult> {
				public int compare(ITestResult r1, ITestResult r2) {
					return getTime(r1.getStartMillis()).compareTo(getTime(r2.getStartMillis()));
				}
			}

			Collections.sort(resultList, new ResultComparator());

			for (ITestResult result : resultList) {
				//AdminUI related changes start here
				StringBuilder sb = new StringBuilder();
				for (Object param : result.getParameters()) {
					sb.append(param.toString());
				}
               test = extent.startTest(result.getMethod().getMethodName() + sb.toString()).assignCategory(result.getTestContext().getName());
             //AdminUI related changes end here 
                         /*if(result.getTestName()!=null){
                            test = extent.startTest(result.getMethod().getMethodName()+ result.getTestName()).assignCategory(result.getTestContext().getName());
                
                  	 	}else {
            		
            		  test = extent.startTest(result.getMethod().getMethodName() + result.getTestName()).assignCategory(result.getTestContext().getName());
            			}*/
                              

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));
                        // add grp
			//	for (String group : result.getMethod().getGroups())
			//		test.assignCategory(group);

				if (result.getThrowable() != null) {

					updatelogs(result, test);
					test.setDescription(result.getName());
					test.log(status, "Test " + status.toString().toLowerCase() + "ed");
					test.log(status, result.getThrowable());
				} else {

					updatelogs(result, test);

					test.log(status, "Test " + status.toString().toLowerCase() + "ed");

				}

				// screenshot fetched from Listerner
				String screenshot = (String) result.getAttribute("screenshot");
				String count = (String) result.getAttribute("count");

				if (screenshot != null) {
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(screenshot));
				}
				extent.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	void updatelogs(ITestResult result, ExtentTest test) {

		Iterator<String> itr = Reporter.getOutput(result).iterator();
		while (itr.hasNext()) {

			String text = itr.next();

			if (text.contains("PASS")) {
				test.log(LogStatus.PASS, text);

			} else if (text.contains("FAIL : ") || text.contains("FAIL-->")) {

				test.log(LogStatus.FAIL, text);
			}

			else if (text.contains("SS")) {
				// screenshot fetched from Reporter
				String nwText =  text.replace("<br>", "");//TODO:Remove after execution and debug why br is appended also check with new version and replace neText with text.
				System.out.println("Text---->" + nwText);
				String path = nwText.substring(nwText.lastIndexOf("+") + 1);
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(path));

			}

			else if (text.contains("SKIP")) {
				test.log(LogStatus.SKIP, text);

			} else if (text.contains("INFO")) {
				test.log(LogStatus.INFO, text);
			}

		}
	}

	@Override
	public void onFinish(ITestContext arg0) {

	}

	@Override
	public void onStart(ITestContext tc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult tr) {
		count++;

		Object currentClass = tr.getInstance();
		WebDriver driver = ((BaseTestCase) currentClass).getDriver();

		// ITestContext context = tr.getTestContext();
		// WebDriver driver = (WebDriver) context.getAttribute("driver");

		try {

			File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String relPath = "./screenshots/" + tr.getMethod().getMethodName() + source.getName();
			File file = new File("");
			String path = file.getCanonicalPath() + "/reports/screenshots/" + tr.getMethod().getMethodName()
					+ source.getName();
			FileUtils.copyFile(source, new File(path));

			tr.setAttribute("screenshot", relPath);
			tr.setAttribute("count", String.valueOf(count));

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error generating screenshot: " + e);
		}

		Reporter.log(
				"*****************-********************************-*********************************************************-**********************");
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestStart(ITestResult tr) {

		Reporter.log("<pre>" + "Test Name : " + tr.getMethod().getMethodName() + "</pre>");

	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		Reporter.log(
				"*****************-********************************-*********************************************************-**********************");
	}

}

