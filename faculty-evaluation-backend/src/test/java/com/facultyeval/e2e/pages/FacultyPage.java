package com.facultyeval.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for Faculty Dashboard.
 * Updated with exact IDs from the frontend implementation.
 */
public class FacultyPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Dashboard ---
    private final By overallRating      = By.id("faculty-overall-rating");
    private final By totalEvaluations   = By.id("faculty-total-evals");
    private final By feedbackItems      = By.className("feedback-item");

    // --- Navigation / Logout ---
    private final By logoutButton       = By.xpath("//button[contains(text(),'Log Out')]");

    public FacultyPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Wait for faculty dashboard to load (by checking URL) */
    public boolean isFacultyDashboardVisible() {
        try {
            return wait.until(ExpectedConditions.urlContains("faculty"));
        } catch (Exception e) {
            return false;
        }
    }

    /** Navigate to feedback section (already on dashboard, but can click tab if needed) */
    public void navigateToFeedback() {
        // Feedbacks are displayed on the main dashboard page
    }

    /** Check if feedback section is visible (by checking if overall rating exists) */
    public boolean isFeedbackSectionVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(overallRating)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns count of feedback records shown */
    public int getFeedbackCount() {
        try {
            List<WebElement> items = driver.findElements(feedbackItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /** Check if specific feedback text is present in the dashboard */
    public boolean hasFeedbackText(String feedbackText) {
        try {
            // Feedback text is usually in a paragraph inside feedback-item
            By feedbackTextLocator = By.xpath("//*[contains(@class, 'feedback-item')]//p[contains(text(),\"" + feedbackText + "\")]");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(feedbackTextLocator)).isDisplayed();
        } catch (Exception e) {
            // Fallback to searching page source if class-based xpath fails
            return driver.getPageSource().contains(feedbackText);
        }
    }

    /** Logout from faculty session */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}
