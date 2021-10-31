package com.ikea.application.GenericFunctionsLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class GenericFunctions {

	@SuppressWarnings("rawtypes")
	public static AndroidDriver driver;
	private static AppiumDriverLocalService server = null;
	public int port;

	public GenericFunctions() {
	}

	@SuppressWarnings("rawtypes")
	public GenericFunctions(AndroidDriver driver) {
		this.driver = driver;
	}

	/***********************************************************************************************
	 * Function Description : Method to get value from config.properties file
	 *********************************************************************************************/

	public String getPropertyValue(String propertyName) {
		String directory = System.getProperty("user.dir");
		String propFileName = directory + "/Config.properties";
		File file = new File(propFileName);
		FileInputStream fileInput = null;
		Properties prop = new Properties();
		String propertyValue = "";

		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		propertyValue = prop.getProperty(propertyName);
		return propertyValue;
	}

	//Initiates the app with all the settings and main package and Activity are declared here
	public AndroidDriver StartDriverAndroidApp() throws IOException, InterruptedException {
		AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
		serviceBuilder.usingAnyFreePort();
		server = AppiumDriverLocalService.buildService(serviceBuilder);
		server.start();
		System.out.println("Appium Service Address : - " + server.getUrl());
		port=server.getUrl().getPort();
		DesiredCapabilities capabilities = new DesiredCapabilities();
		System.out.println("==set device==");
		capabilities.setCapability("platformName", org.openqa.selenium.Platform.ANDROID);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getPropertyValue("DeviceName"));
		capabilities.setCapability(MobileCapabilityType.APP,
				System.getProperty("user.dir") + getPropertyValue("APP_LOCATION"));
		capabilities.setCapability("appPackage", getPropertyValue("APP_PACKAGE"));
		capabilities.setCapability("appActivity", getPropertyValue("APP_ACTIVITY"));
		capabilities.setCapability("automationName", "UiAutomator2");
		capabilities.setCapability("fullReset", true);
		System.out.println("==set app==");
		driver = new AndroidDriver(server.getUrl(), capabilities);
		System.out.println("==========complete launchApp========");
		return driver;
	}

	//Sets implicit Wait by accepting timeout in seconds
	public String SetImplicitWaitInSeconds(int timeOut) {
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		return "Timeout set to " + timeOut + " seconds.";
	}

	//Set sleep Wait by accepting timeout in milliseconds
	public void GoToSleep(int TimeInMillis) {
		try {
			Thread.sleep(TimeInMillis);
		} catch (Exception e) {
		}
	}

	//Kill the appium server via commandline
	public void stopAppiumServer(int port) throws IOException {
		Runtime.getRuntime().exec("cmd.exe");
		String AppiumServerPortNumber = Integer.toString(port);
		String command = "cmd /c echo off & FOR /F \"usebackq tokens=5\" %a in" + " (`netstat -nao ^| findstr /R /C:\""
				+ AppiumServerPortNumber + "\"`) do (FOR /F \"usebackq\" %b in"
				+ " (`TASKLIST /FI \"PID eq %a\" ^| findstr /I node.exe`) do taskkill /F /PID %a)";

		String s = null;
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			System.out.println(" ---------->>> Exception happened: ");
			e.printStackTrace();
		}

		System.out.println("------------>>> Appium server stopped");
	}

	//Stops the driver
	public void StopDriver() throws IOException {
		System.out.println("Stopping driver...");
		driver.removeApp(getPropertyValue("APP_PACKAGE"));
		stopAppiumServer(port);
	}

	//Checks Presence and Visibility of an element on page of given path using id of element with custom timeout
	public Boolean isVisible(By element, int timeOutInSec) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//Click on element present on a page
	public void ClickOnElement(By locator) {
		try {
			driver.findElement(locator).click();
		} catch (Exception e) {
			GoToSleep(2000);
			driver.findElement(locator).click();
		}
	}

	//Click on element and fill the field and press enter
	public void ClickAndFillTextField(By locator, String value) {
		driver.findElement(locator).click();
		driver.findElement(locator).sendKeys(value);
		((AndroidDriver)driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		GoToSleep(2000);
	}

	//Get text from the page
	public String GetText(By locator) {
		return driver.findElement(locator).getText();
	}
}
