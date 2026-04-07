package com.facultyeval.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for Faculty Dashboard.
 * Covers: viewing feedback submitted by students, logout.
 */
public class FacultyPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Dashboard ---
    private final By facultyDashboard   = By.xpath("//*[contains(text(),'Faculty') and (contains(@class,'dashboard') or contains(@class,'header') or contains(@class,'title'))]");

    // --- Feedback View ---
    private final By feedbackMenu       = By.xpath("//*[contains(text(),'Feedback') or contains(@href,'feedback')]");
    private final By feedbackTable      = By.xpath("//table | //*[contains(@class,'feedback-list') or contains(@class,'feedback-table')]");
    private final By feedbackRows       = By.xpath("//table//tr[position()>1] | //*[contains(@class,'feedback-item') or contains(@class,'feedback-row')]");
    private final By noFeedbackMessage  = By.xpath("//*[contains(text(),'No feedback') or contains(text(),'no records') or contains(text(),'empty')]");

    // --- Logout ---
    private final By logoutButton       = By.xpath("//button[contains(text(),'Logout') or contains(text(),'Sign Out')] | //a[contains(text(),'Logout')]");

    public FacultyPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Wait for faculty dashboard to load */
    public boolean isFacultyDashboardVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(facultyDashboard));
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl().toLowerCase().contains("faculty");
        }
    }

    /** Navigate to feedback section */
    public void navigateToFeedback() {
        wait.until(ExpectedConditions.elementToBeClickable(feedbackMenu)).click();
    }

    /** Check if feedback table/list is visible */
    public boolean isFeedbackSectionVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(feedbackTable));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns count of feedback records shown */
    public int getFeedbackCount() {
        try {
            List<WebElement> rows = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(feedbackRows));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /** Check if feedback is available (at least 1 record OR section visible) */
    public boolean canViewFeedback() {
        return isFeedbackSectionVisible() || getFeedbackCount() > 0;
    }

    /** Check if empty/no-feedback message is shown */
    public boolean isNoFeedbackMessageShown() {
        try {
            return driver.findElement(noFeedbackMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Logout from faculty session */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}
