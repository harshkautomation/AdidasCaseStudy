package framework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

/**
 * FrameworkReporter class provides mechanism to generate test results and provides methods to capture screenshots and Pass, Fail statuses and other relevant information
 * to be displayed in reports.
 * 
 *
 */

public class FrameworkReporter {
	private static SoftAssert softAssert = null;
	protected static boolean isFailed = false;
	public static WebDriver driver;
	
	/**
	 * To initialize the WebDriver and SoftAssert instances. 
	 * @param driverset WebDriver instance passed as input parameter.
	 */
	public static void init(WebDriver driverset){
		 driver=driverset;
		 softAssert = new SoftAssert();
	}
	
	/**
	 * Method to capture screenshots during test execution.
	 * @param elementToCapture WebElement passed as input parameter of whose screenshot needs to be captured.
	 */
	public static void takeScreenshot(WebElement elementToCapture){
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File newSource = new File(source.getParent() + "/" + Math.abs(elementToCapture.hashCode()) + ".png");
		
		if(elementToCapture != null && elementToCapture.isDisplayed()){
			try{

				int x = elementToCapture.getLocation().getX();
				int y = elementToCapture.getLocation().getY();
				int width = elementToCapture.getSize().getWidth();
				int height = elementToCapture.getSize().getHeight();

				BufferedImage image = ImageIO.read(source);
				BufferedImage imagePart = image.getSubimage(x, y, width, height);
				ImageIO.write(imagePart, "png", newSource);
			}catch(IOException e1){
				reportLog("Failed to take screenshot of element. See exception : " + e1.getMessage());
				reportLog("Capturing entire page screen shot.....");
				takeScreenshot();
				return;
			}

			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			String relPath = "./screenshots/" + methodName + source.getName();
			try {
				File file = new File("");
				String path = file.getCanonicalPath() + "/reports/screenshots/" + methodName + source.getName();
				FileUtils.copyFile(newSource, new File(path));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			finally {
				if(newSource.exists())
					newSource.delete();
			}

			Reporter.log("SS +" + relPath + "");
		}
	}
	
	
	/**
	 * Takes the screenshot of the entire web page in its current state when this method is called.
	 * All the screenshots are stored in /reports/screenshots/ folder of the current project.
	 */
	public static void takeScreenshot() {
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String relPath = "./screenshots/" + methodName + source.getName();
		try {
			File file = new File("");
			String path = file.getCanonicalPath() + "/reports/screenshots/" + methodName + source.getName();
			FileUtils.copyFile(source, new File(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally {
			if(source.exists())
				source.delete();
		}

		Reporter.log("SS +" + relPath + "");

	}
	
	/**
	 * Method to display PASS status in the generated report with the text passed as input parameter.
	 * @param msg text passed as input parameter to be displayed as PASS status.
	 */
	public static void reportPass(String msg) {
		Reporter.log("PASS : " + msg, 0, true);
		if (softAssert == null)
			softAssert = new SoftAssert();
	}
	
	/**
	 * Method to display FAIL status in the generated report with the text passed as input parameter.
	 * This method reports FAILURE without capturing a screenshot of the web page.
	 * @param msg text passed as input parameter to be displayed as FAIL status.
	 */
	public static void reportFail_WithoutScreenshot(String msg){
		if (!isFailed) {
			isFailed = true;
		}
		Reporter.log("FAIL-->" + " MSG: " + msg, 0, true);
		if (softAssert == null)
			softAssert = new SoftAssert();
		softAssert.fail(msg);
		System.out.println(Reporter.getCurrentTestResult());
		Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
	}

	/**
	 * Method to display FAIL status in the generated report with the text passed as input parameter.
	 * This method also captures screenshot of the web page when a failure occurs.
	 * @param msg text passed as input parameter to be displayed as FAIL status.
	 */
	 
	public static void reportFail(String msg) {
		if (!isFailed) {
			isFailed = true;
		}
		Reporter.log("FAIL-->" + " MSG: " + msg, 0, true);
		if (softAssert == null)
			softAssert = new SoftAssert();
		softAssert.fail(msg);
		takeScreenshot();
		System.out.println(Reporter.getCurrentTestResult());
		Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
	}

	/**
	 * To display relevant information in the generated report. 
	 * @param msg Text to be displayed as INFO only in the generated report.
	 */
	public static void reportInfo(String msg) {

		Reporter.log("INFO : " + msg, 0, true);
	}

	public void reportCNR(String msg) {

		Reporter.log("SKIP : " + msg, 0, true);
	}

	/**
	 * To generate log information and capture it as logger information.
	 * @param msg Text to be displayed in logs.
	 */
	public static void reportLog(String msg) {

		Reporter.log("LOG : " + msg, 0, true);
	}

	/**
	 * This method calls assertAll() method of SoftAssert. Calling assertAll() will cause an exception to be thrown if at least one assertion failed.
	 */
	public static void assertTest() {
		softAssert.assertAll();
	}
	
	/**
	 * This method is used by takeFrameScreenshot() method in order to combine all the images appearing in a single frame section (represented as iframe)
	 * @param imgFiles File array of image files.
	 * @param fileFullPath path location of the file where the captured screenshot needs to be written.
	 */
	public static void combineALLImages(File[] imgFiles, File fileFullPath){
		try{
			int rows = Math.abs(imgFiles.length/2) + (imgFiles.length % 2);
			int cols = 2;

			BufferedImage sample = ImageIO.read(imgFiles[0]);
			BufferedImage finalImg = new BufferedImage(sample.getWidth() * cols, sample.getHeight() * rows, sample.getType());

			int index = 0;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					BufferedImage temp = ImageIO.read(imgFiles[index]);
					Graphics2D g = temp.createGraphics();
					g.setPaint(Color.RED);
					g.setStroke(new BasicStroke(5));
					g.drawRect(0, 0, sample.getWidth(), sample.getHeight());
					finalImg.createGraphics().drawImage(temp, (sample.getWidth() * j), (sample.getHeight() * i), null);
					index++;
				}
			}
			
			ImageIO.write(finalImg, "png", fileFullPath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
