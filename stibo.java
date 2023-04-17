import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class SherwinWilliamsTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
        actions = new Actions(driver);
    }

    @Test
    public void testSherwinWilliams() {
        // Go to website
        driver.get("https://www.sherwin-williams.com/");

        // Click "Shop Products & Color"
        HomePage homePage = new HomePage(driver);
        homePage.clickShopProductsAndColor();

        // Hover on "Homeowner Paints, Stains & Supplies"
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.hoverOnHomeownerPaints();

        // Click on "Paint" on "Home interiors"
        PaintsPage paintsPage = productsPage.clickHomeInteriors();

        // Search for "tuberose paint"
        paintsPage.searchFor("tuberose paint");

        // Click on searched product "SW 6578 | tuberose | Interior / Exterior"
        paintsPage.clickSearchedProduct("SW 6578 | tuberose | Interior / Exterior");

        // Verify you are on the right page by checking the url
        String expectedUrl = "https://www.sherwin-williams.com/en-us/color/color-family/red-paint-colors/SW6578-tuberose";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);

        // Open a new tab with the url "https://www.sherwin-williams.com/en-us/color/color-family/red-paint-colors/SW6578-tuberose"
        ((JavascriptExecutor) driver).executeScript("window.open('https://www.sherwin-williams.com/en-us/color/color-family/red-paint-colors/SW6578-tuberose', '_blank');");
        driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());

        // Verify product name is displayed "SW 6578 Tuberose" (not in breadcrumbs)
        ProductPage productPage = new ProductPage(driver);
        Assert.assertEquals(productPage.getProductName(), "SW 6578 Tuberose");

        // Click "FULL DETAILS"
        productPage.clickFullDetails();

        // Verify "Hex Value: #D47C8C" (just verify string value)
        String expectedHexValue = "Hex Value: #D47C8C";
        Assert.assertEquals(productPage.getHexValue(), expectedHexValue);

        // Click on "Color sample" link
        productPage.clickColorSample();

        // Close the popup
        productPage.closePopup();

        // Search for "tuberose" in "Search for a color" field
        driver.switchTo().window(driver.getWindowHandles().toArray()[0].toString());
        homePage.searchFor("tuberose");

        // Get innerText from tuberose rectangle (property) and verify the value "SW 6578\n\nTuberose\n\nOrder samples"
        String expectedInnerText = "SW 6578\n\nTuberose\n\nOrder samples";
        Assert.assertEquals(homePage.getInnerText(), expectedInnerText );

driver.close();
}

    public static void main(String[] args) throws InterruptedException {
        SherwinWilliamsTest test = new SherwinWilliamsTest ();
        test.testSherwinWilliams();
    }

  public class HomePage {
        @FindBy(xpath = "//button[text()='Shop Products & Color']")
        private WebElement shopProductsAndColorBtn;

        @FindBy(xpath = "//a[contains(@href,'/homeowners/paint') and text()='Homeowner Paints, Stains & Supplies']")
        private WebElement homeownerPaintsStainsAndSuppliesLink;

        @FindBy(xpath = "//a[contains(@href,'/homeowners/paint/interior-paint-coatings') and text()='Paint']")
        private WebElement paintOnHomeInteriorsLink;

        @FindBy(id = "SearchInput")
        private WebElement searchInput;

        public void clickShopProductsAndColor() {
            shopProductsAndColorBtn.click();
        }

        public void hoverOnHomeownerPaintsStainsAndSupplies() {
            Actions actions = new Actions(driver);
            actions.moveToElement(homeownerPaintsStainsAndSuppliesLink).perform();
        }

        public void clickPaintOnHomeInteriors() {
            paintOnHomeInteriorsLink.click();
        }

        public void searchFor(String text) {
            searchInput.sendKeys(text);
            searchInput.submit();
        }

        public ProductPage clickSearchedProduct() {
            WebElement searchedProduct = driver.findElement(By.xpath("//a[contains(@href,'/en-us/color/color-families/red-paint-colors/SW6578-tuberose') and contains(@class,'swatch') and contains(@class,'search-result')]"));
            searchedProduct.click();
            return PageFactory.initElements(driver, ProductPage.class);
        }

        public void verifyInnerText(String expectedText) {
            WebElement tuberoseRect = driver.findElement(By.xpath("//span[text()='Tuberose']"));
            String actualText = tuberoseRect.getAttribute("innerText");
            assert actualText.equals(expectedText);
        }
    }


