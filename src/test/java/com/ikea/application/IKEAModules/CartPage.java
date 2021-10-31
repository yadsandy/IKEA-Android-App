package com.ikea.application.IKEAModules;

import java.util.List;
import org.openqa.selenium.By;
import org.testng.Assert;
import com.ikea.application.GenericFunctionsLibrary.GenericFunctions;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class CartPage extends GenericFunctions {
	public GenericFunctions generic=new GenericFunctions();

	public CartPage(AndroidDriver driver, GenericFunctions generic) {
		this.driver = driver;
		this.generic = generic;
		SetImplicitWaitInSeconds(10);
	}

	String APP_PACKAGE = generic.getPropertyValue("APP_PACKAGE");
	// Splash Locators
	public By region_Label = By.id(APP_PACKAGE + ":id/select_region_title");
	public By conitnue_Button = By.id(APP_PACKAGE + ":id/go_to_next_button");
	public By notNow_Button = By.id(APP_PACKAGE + ":id/negative_button");
	public By skip_Button = By.id(APP_PACKAGE + ":id/cta_button");

	// Home Page Locators
	public By search_Button = By.id(APP_PACKAGE + ":id/search_src_text");
	public By search_Label = By.id(APP_PACKAGE + ":id/search_title");
	public By cart_Button = By.xpath("//android.widget.ImageView[@content-desc='Cart']");

	// Product search Locators
	public By listOfProducts_Label = By.id(APP_PACKAGE + ":id/price_presentation_parent");
	public By productTitle_Label = By.id(APP_PACKAGE + ":id/title");
	public By productPrice_Label = By.id(APP_PACKAGE + ":id/price");
	public By productImage_Label = By.id(APP_PACKAGE + ":id/product_image");

	// Product page Locators
	public By addItem_Button = By.xpath("//android.widget.Button[@content-desc='Add to bag']");
	public By back_Button = By.xpath("//android.widget.ImageButton[@content-desc='Back']");

	// Cart Page Locators
	public By menuOptions_List = By.id(APP_PACKAGE + ":id/actionsMenu");
	public By removeItem_Label = By.id(APP_PACKAGE + ":id/design_menu_item_text"); 
	public By emptyCart_Label = By.id(APP_PACKAGE + ":id/empty_list_header");

	
	public void checkSpalshScreenAndMoveToHomepage() {
		if (generic.isVisible(region_Label, 5)) {
			generic.ClickOnElement(conitnue_Button);
			generic.ClickOnElement(notNow_Button);
			generic.ClickOnElement(skip_Button);
		} else {
			System.out.println("Splash screen is not visible");
		}

	}

	public void performSearchForAProduct(String productName) {
		generic.ClickAndFillTextField(search_Button, productName);
	}
	
	public String checkSearchResultAndClickOnProduct() {
		List<MobileElement> listOfAllProducts=driver.findElements(listOfProducts_Label);
		String productTitle=listOfAllProducts.get(0).findElement(productTitle_Label).getText();
		String productPrice=listOfAllProducts.get(0).findElement(productPrice_Label).getText();
		String productDescription=productTitle+""+productPrice;
		return productDescription;
	}
	
	public void clickProductAndCheckDetails(String expectedProductDescription) {
		generic.ClickOnElement(productImage_Label);
		generic.GoToSleep(2000);
		String productTitle=generic.GetText(productTitle_Label);
		String productPrice=generic.GetText(productPrice_Label);
		String actualProductDescription=productTitle+""+productPrice;
		Assert.assertTrue(expectedProductDescription.equals(actualProductDescription),"Values of Product are not same. Actual values are "+actualProductDescription+" and expected values are "+expectedProductDescription);
	}
	
	public void addProductToCartAndBackToHomePage() {
		generic.ClickOnElement(addItem_Button);
		generic.GoToSleep(3000);
		generic.ClickOnElement(addItem_Button);
		generic.GoToSleep(3000);
		generic.ClickOnElement(back_Button);
		generic.GoToSleep(1000);
		generic.ClickOnElement(back_Button);
		generic.GoToSleep(1000);
		generic.ClickOnElement(back_Button);
		Assert.assertTrue(generic.isVisible(search_Label,5), "Home Page is not visible");
		}
	
	public void moveToCartAndRemoveItems() {
		generic.ClickOnElement(cart_Button);
		generic.ClickOnElement(menuOptions_List);
		List<MobileElement> listOfOptions=driver.findElements(removeItem_Label);
		listOfOptions.get(1).click();
		Assert.assertTrue(generic.isVisible(emptyCart_Label,5), "Cart is not empty");
		}

}
