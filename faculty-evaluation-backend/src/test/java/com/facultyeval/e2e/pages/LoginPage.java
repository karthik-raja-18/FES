package com.facultyeval.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for the Login Page.
 * Handles login actions for Admin, Student, and Faculty users.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Locators ---
    private final By usernameField = By.xpath("//input[@name='username' or @id='username' or @placeholder='Username']");
    private final By passwordField = By.xpath("//input[@name='password' or @id='password' or @placeholder='Password']");
    private final By loginButton   = By.xpath("//button[@type='submit' or contains(text(),'Login') or contains(text(),'Sign In')]");
    private final By errorMessage  = By.xpath("//*[contains(@class,'error') or contains(@class,'alert')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Navigate to login page */
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl);
    }

    /** Perform login with given credentials */
    public void login(String username, String password) {
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        user.clear();
        user.sendKeys(username);

        WebElement pass = driver.findElement(passwordField);
        pass.clear();
        pass.sendKeys(password);

        driver.findElement(loginButton).click();
    }

    /** Check if login error is displayed */
    public boolean isErrorDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
