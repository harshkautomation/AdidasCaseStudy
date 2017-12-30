package base;

import org.openqa.selenium.WebDriver;

import AdidasUI.AdidasUIService;

/**
 * Provide all possible services given by Adidas UI.
 */

public class AdidasService {

    /**
     * Provides a new instance of AdidasUIService class.
     * @param driver WebDriver driver variable passed as input parameter.
     * @return
     */
	public static AdidasUIService adidasUIService(WebDriver driver){
		return new AdidasUIService(driver);
	}
}
