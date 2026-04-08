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
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("login-btn");
    private final By errorMessage  = By.id("login-error-msg");
    
    // Role selection buttons - using more flexible selectors
    private final By adminRoleButton = By.xpath("//button[contains(@id,'role-ADMIN')]");
    private final By facultyRoleButton = By.xpath("//button[contains(@id,'role-FACULTY')]");
    private final By studentRoleButton = By.xpath("//button[contains(@id,'role-STUDENT')]");
    
    // Alternative role selectors if IDs don't work
    private final By adminRoleAlt = By.xpath("//button[contains(text(),'Admin')]");
    private final By facultyRoleAlt = By.xpath("//button[contains(text(),'Faculty')]");
    private final By studentRoleAlt = By.xpath("//button[contains(text(),'Student')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Navigate to login page */
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl);
    }

    /** Select role before login */
    public void selectRole(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(adminRoleButton)).click();
                } catch (Exception e) {
                    wait.until(ExpectedConditions.elementToBeClickable(adminRoleAlt)).click();
                }
                break;
            case "FACULTY":
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(facultyRoleButton)).click();
                } catch (Exception e) {
                    wait.until(ExpectedConditions.elementToBeClickable(facultyRoleAlt)).click();
                }
                break;
            case "STUDENT":
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(studentRoleButton)).click();
                } catch (Exception e) {
                    wait.until(ExpectedConditions.elementToBeClickable(studentRoleAlt)).click();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    /** Perform login with given credentials (without role selection) */
    public void login(String username, String password) {
        WebElement user = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        user.clear();
        user.sendKeys(username);

        pass.clear();
        pass.sendKeys(password);

        loginBtn.click();
    }

    /** Perform login without role selection */
    public void loginWithoutRole(String username, String password) {
        login(username, password);
    }

    /** Perform login with role selection */
    public void login(String username, String password, String role) {
        selectRole(role);
        login(username, password);
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
