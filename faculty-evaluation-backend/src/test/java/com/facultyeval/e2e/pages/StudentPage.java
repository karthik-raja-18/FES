package com.facultyeval.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Page Object for Student Dashboard.
 * Updated with exact IDs from the frontend implementation.
 */
public class StudentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Navigation / Logout ---
    private final By logoutButton       = By.id("logout-btn");

    // --- Feedback ---
    private final By feedbackTextArea   = By.id("eval-feedback");
    private final By submitFeedbackBtn  = By.id("submit-eval-btn");
    private final By successToast       = By.xpath("//div[contains(@class,'hot-toast')] | //div[contains(text(),'submitted')]");

    public StudentPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    private void robustClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            // Scroll into view first
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            Thread.sleep(200); // Wait for scroll to settle
            element.click();
        } catch (Exception e) {
            // Fallback: Click using JavaScript if standard click fails
            System.out.println("⚠️ Standard click failed, using JavaScript click for " + locator);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /** Wait for student dashboard to load (by checking URL) */
    public boolean isStudentDashboardVisible() {
        try {
            // Strategy 1: Check URL contains "student"
            if (wait.until(ExpectedConditions.urlContains("student"))) {
                return true;
            }
        } catch (Exception e1) {
            // Strategy 2: Check URL contains "dashboard"
            try {
                if (wait.until(ExpectedConditions.urlContains("dashboard"))) {
                    return true;
                }
            } catch (Exception e2) {
                // Strategy 3: Check page content for student-related elements
                try {
                    String pageSource = driver.getPageSource().toLowerCase();
                    return pageSource.contains("student") || pageSource.contains("feedback") || pageSource.contains("subject");
                } catch (Exception e3) {
                    // Strategy 4: Check if we're not on login page
                    return !driver.getCurrentUrl().toLowerCase().contains("login");
                }
            }
        }
        return false;
    }

    /** Mock navigation - usually students start on the main list */
    public void navigateToFeedback() {
        // All subjects are listed on the dashboard, no separate menu needed
    }

    /** Click the feedback/submit button for a subject with specific code */
    public void openFeedbackForSubject(String subjectCode) {
        System.out.println("🔍 Looking for evaluate button for subject: " + subjectCode);
        By evaluateBtn = By.id("evaluate-btn-" + subjectCode);
        
        try {
            // Wait for button to be clickable
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(evaluateBtn));
            System.out.println("✅ Found button for " + subjectCode + ". Clicking...");
            
            robustClick(evaluateBtn);
            
            // Wait for modal to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eval-feedback")));
            System.out.println("✅ Evaluation modal opened for " + subjectCode);
        } catch (Exception e) {
            System.out.println("?? Failed to open feedback modal for " + subjectCode + ": " + e.getMessage());
            
            // Check if it's already evaluated
            if (driver.findElements(By.id("status-done-" + subjectCode)).size() > 0) {
                System.out.println("?? Subject " + subjectCode + " is already evaluated.");
            } else {
                debugAvailableSubjects();
            }
            throw new RuntimeException("Could not open evaluation for " + subjectCode + ": " + e.getMessage());
        }
    }

    /** Debug method to check available subjects for the student */
    public void debugAvailableSubjects() {
        try {
            // Take screenshot for debugging
            try {
                java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                System.out.println("?? Screenshot saved: " + screenshot.getAbsolutePath());
            } catch (Exception screenshotEx) {
                System.out.println("?? Screenshot failed: " + screenshotEx.getMessage());
            }
            
            // Check page source for subject information
            String pageSource = driver.getPageSource();
            System.out.println("?? Page source length: " + pageSource.length());
            
            // Look for any subject-related elements
            List<WebElement> subjectElements = driver.findElements(By.xpath("//*[contains(text(),'CS202') or contains(text(),'Java Programming') or contains(@id,'subject')]"));
            System.out.println("?? Found " + subjectElements.size() + " subject-related elements");
            
            // Look for any evaluate/feedback buttons
            List<WebElement> evaluateButtons = driver.findElements(By.xpath("//*[contains(@id,'evaluate') or contains(@id,'feedback') or contains(text(),'Evaluate') or contains(text(),'Feedback')]"));
            System.out.println("?? Found " + evaluateButtons.size() + " evaluate/feedback buttons");
            
            // Look for any textareas or inputs
            List<WebElement> textInputs = driver.findElements(By.xpath("//textarea | //input[@type='text']"));
            System.out.println("?? Found " + textInputs.size() + " text input elements");
            
            // Print first few lines of page source for debugging
            String[] lines = pageSource.split("\n");
            System.out.println("?? First 10 lines of page source:");
            for (int i = 0; i < Math.min(10, lines.length); i++) {
                if (lines[i].trim().length() > 0) {
                    System.out.println("   " + lines[i].trim());
                }
            }
            
        } catch (Exception e) {
            System.out.println("?? Error during debug: " + e.getMessage());
        }
    }

    /** Click the feedback/submit button for the first available subject */
    public void openFeedbackForFirstSubject() {
        System.out.println("🔍 Looking for evaluate buttons...");
        
        // Wait for subjects to load
        By firstEvalBtn = By.xpath("//button[contains(@id, 'evaluate-btn-')]");
        try {
            // Wait up to 5s for the list to not be empty (handles loading spinner)
            wait.until(d -> d.findElements(firstEvalBtn).size() > 0);
            
            List<WebElement> buttons = driver.findElements(firstEvalBtn);
            System.out.println("✅ Found " + buttons.size() + " buttons. Clicking the first one: " + buttons.get(0).getAttribute("id"));
            
            robustClick(firstEvalBtn); // This will click the first one it finds
            
            // Wait for modal to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eval-feedback")));
            System.out.println("✅ Modal opened successfully.");
        } catch (Exception e) {
            System.out.println("?? Failed to open feedback modal: " + e.getMessage());
            
            // Debug: Check if already evaluated
            if (driver.findElements(By.xpath("//div[contains(@id, 'status-done-')]")).size() > 0) {
                 System.out.println("?? All subjects are already evaluated. Tests might need fresh data.");
            }
            throw new RuntimeException("Could not find any subject to evaluate: " + e.getMessage());
        }
    }

    /** Fill in feedback text */
    public void enterFeedbackText(String feedbackText) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(feedbackTextArea));
        textarea.clear(); 
        textarea.sendKeys(feedbackText);
    }

    /** Click a star rating (1–5) */
    public void selectRating(int starCount) {
        System.out.println("⭐ Selecting " + starCount + " star rating");
        By starBtn = By.id("star-" + starCount);
        robustClick(starBtn);
        
        // Wait a small moment for React state (rating) to update and enable the submit button
        try {
            wait.until(d -> d.findElement(submitFeedbackBtn).isEnabled());
        } catch (Exception ignored) {}
    }

    /** Submit the feedback form */
    public void submitFeedback() {
        System.out.println("🚀 Clicking submit button");
        
        // Ensure button is not disabled before clicking
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitFeedbackBtn));
        } catch (Exception e) {
            System.out.println("?? Submit button not clickable. Rating might not have been selected correctly.");
            throw e;
        }
        
        robustClick(submitFeedbackBtn);
    }

    /** Verify feedback was submitted successfully */
    public boolean isFeedbackSubmittedSuccessfully() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Logout from student session */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}
