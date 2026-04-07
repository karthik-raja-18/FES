package com.facultyeval.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for Student Dashboard.
 * Covers: viewing enrolled subjects, submitting feedback, logout.
 */
public class StudentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Dashboard ---
    private final By studentDashboard   = By.xpath("//*[contains(text(),'Student') and (contains(@class,'dashboard') or contains(@class,'header') or contains(@class,'title'))]");

    // --- Feedback ---
    private final By feedbackMenu       = By.xpath("//*[contains(text(),'Feedback') or contains(@href,'feedback')]");
    private final By subjectList        = By.xpath("//*[contains(@class,'subject') or contains(@class,'course')]//button[contains(text(),'Feedback') or contains(text(),'Submit')]");
    private final By feedbackTextArea   = By.xpath("//textarea[@name='feedback' or @name='comment' or @placeholder='Enter feedback']");
    private final By ratingStars        = By.xpath("//*[contains(@class,'star') or contains(@class,'rating')]//input | //*[@class='star']");
    private final By submitFeedbackBtn  = By.xpath("//button[@type='submit' or contains(text(),'Submit') or contains(text(),'Send')]");
    private final By successMessage     = By.xpath("//*[contains(@class,'success') or contains(@class,'alert-success') or contains(text(),'submitted') or contains(text(),'successfully')]");

    // --- Logout ---
    private final By logoutButton       = By.xpath("//button[contains(text(),'Logout') or contains(text(),'Sign Out')] | //a[contains(text(),'Logout')]");

    public StudentPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Wait for student dashboard to load */
    public boolean isStudentDashboardVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(studentDashboard));
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl().toLowerCase().contains("student");
        }
    }

    /** Navigate to feedback section */
    public void navigateToFeedback() {
        wait.until(ExpectedConditions.elementToBeClickable(feedbackMenu)).click();
    }

    /** Click the feedback/submit button for the first available subject */
    public void openFeedbackForFirstSubject() {
        List<WebElement> feedbackButtons = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(subjectList));
        if (!feedbackButtons.isEmpty()) {
            feedbackButtons.get(0).click();
        }
    }

    /** Fill in feedback text */
    public void enterFeedbackText(String feedbackText) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(feedbackTextArea));
        textarea.clear();
        textarea.sendKeys(feedbackText);
    }

    /** Click a star rating (1–5) if star rating exists */
    public void selectRating(int starCount) {
        try {
            List<WebElement> stars = driver.findElements(ratingStars);
            if (stars.size() >= starCount) {
                stars.get(starCount - 1).click();
            }
        } catch (Exception ignored) { /* rating widget may differ */ }
    }

    /** Submit the feedback form */
    public void submitFeedback() {
        wait.until(ExpectedConditions.elementToBeClickable(submitFeedbackBtn)).click();
    }

    /** Verify feedback was submitted successfully */
    public boolean isFeedbackSubmittedSuccessfully() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Logout from student session */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}
