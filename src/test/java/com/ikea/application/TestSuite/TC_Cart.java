package com.ikea.application.TestSuite;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ikea.application.GenericFunctionsLibrary.GenericFunctions;
import com.ikea.application.IKEAModules.CartPage;

public class TC_Cart extends GenericFunctions {
	CartPage cartPage;
	GenericFunctions generic;
	String productName = "Table";

	@BeforeMethod
	public void StartDriver() throws IOException, InterruptedException {
		driver = StartDriverAndroidApp();
		generic = new GenericFunctions();
		cartPage = new CartPage(driver, generic);
		SetImplicitWaitInSeconds(10);
	}

	@AfterMethod
	public void stopDriver() throws IOException {
		StopDriver();
	}

	@Test
	public void addAndRemoveItemsFromCart() {
		// Check Splash Screen of IKEA app and perform action to reach at Home Page
		cartPage.checkSpalshScreenAndMoveToHomepage();
		//Search for a product
		cartPage.performSearchForAProduct(productName);
		//Get details of product name and price and click on the product from search results page
		String expectedProductDescription=cartPage.checkSearchResultAndClickOnProduct();
		// Check details of product at product details page and compare details with the seatch result page data 
		cartPage.clickProductAndCheckDetails(expectedProductDescription);
		// Add product twice to the cart and navigate back to home page
		cartPage.addProductToCartAndBackToHomePage();
		// Go to cart and rmeove the added products
		cartPage.moveToCartAndRemoveItems();
	
	}

}
