package com.facultyeval.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object for Admin Dashboard.
 * Covers: subject creation, student enrollment, logout.
 */
public class AdminPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Dashboard ---
    private final By adminDashboard     = By.xpath("//*[contains(text(),'Admin') and (contains(@class,'dashboard') or contains(@class,'header') or contains(@class,'title'))]");

    // --- Subject Creation ---
    private final By subjectsMenu       = By.xpath("//*[contains(text(),'Subject') or contains(@href,'subject')]");
    private final By addSubjectButton   = By.xpath("//button[contains(text(),'Add') or contains(text(),'Create') or contains(text(),'New')]");
    private final By subjectNameField   = By.xpath("//input[@name='subjectName' or @name='name' or @placeholder='Subject Name']");
    private final By subjectCodeField   = By.xpath("//input[@name='subjectCode' or @name='code' or @placeholder='Subject Code']");
    private final By saveSubjectButton  = By.xpath("//button[@type='submit' or contains(text(),'Save') or contains(text(),'Submit')]");
    private final By successMessage     = By.xpath("//*[contains(@class,'success') or contains(@class,'alert-success') or contains(text(),'successfully') or contains(text(),'created')]");

    // --- Student Enrollment ---
    private final By enrollmentMenu     = By.xpath("//*[contains(text(),'Enroll') or contains(@href,'enroll')]");
    private final By studentDropdown    = By.xpath("//select[@name='student' or @name='studentId'] | //input[@placeholder='Select Student']");
    private final By subjectDropdown    = By.xpath("//select[@name='subject' or @name='subjectId'] | //input[@placeholder='Select Subject']");
    private final By enrollButton       = By.xpath("//button[contains(text(),'Enroll') or contains(text(),'Assign')]");

    // --- Logout ---
    private final By logoutButton       = By.xpath("//button[contains(text(),'Logout') or contains(text(),'Sign Out')] | //a[contains(text(),'Logout')]");

    public AdminPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Wait for admin dashboard to load */
    public boolean isAdminDashboardVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(adminDashboard));
            return true;
        } catch (Exception e) {
            // Fallback: check URL contains admin
            return driver.getCurrentUrl().toLowerCase().contains("admin");
        }
    }

    /** Navigate to Subjects section and create a new subject */
    public void createSubject(String subjectName, String subjectCode) {
        // Click Subjects menu
        wait.until(ExpectedConditions.elementToBeClickable(subjectsMenu)).click();

        // Click Add/Create button
        wait.until(ExpectedConditions.elementToBeClickable(addSubjectButton)).click();

        // Fill subject name
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(subjectNameField));
        nameInput.clear();
        nameInput.sendKeys(subjectName);

        // Fill subject code if field is present
        try {
            WebElement codeInput = driver.findElement(subjectCodeField);
            codeInput.clear();
            codeInput.sendKeys(subjectCode);
        } catch (Exception ignored) { /* field may not exist */ }

        // Save
        driver.findElement(saveSubjectButton).click();
    }

    /** Verify subject creation success message */
    public boolean isSubjectCreatedSuccessfully() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Enroll a student into a subject */
    public void enrollStudentIntoSubject(String studentName, String subjectName) {
        // Navigate to enrollment section
        wait.until(ExpectedConditions.elementToBeClickable(enrollmentMenu)).click();

        // Select student
        WebElement studentSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(studentDropdown));
        selectDropdownOption(studentSelect, studentName);

        // Select subject
        WebElement subjectSelect = driver.findElement(subjectDropdown);
        selectDropdownOption(subjectSelect, subjectName);

        // Click Enroll
        wait.until(ExpectedConditions.elementToBeClickable(enrollButton)).click();
    }

    /** Verify enrollment success */
    public boolean isEnrollmentSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Logout from admin session */
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }

    // --- Helper ---
    private void selectDropdownOption(WebElement element, String visibleText) {
        // Works for both <select> and searchable dropdowns
        String tagName = element.getTagName();
        if ("select".equalsIgnoreCase(tagName)) {
            new org.openqa.selenium.support.ui.Select(element).selectByVisibleText(visibleText);
        } else {
            element.clear();
            element.sendKeys(visibleText);
            // Click matching option in dropdown list
            By option = By.xpath("//*[contains(@class,'option') and contains(text(),'" + visibleText + "')]");
            try {
                wait.until(ExpectedConditions.elementToBeClickable(option)).click();
            } catch (Exception ignored) { /* text may auto-match */ }
        }
    }
}
