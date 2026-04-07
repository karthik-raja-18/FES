package com.facultyeval.e2e.tests;

import com.facultyeval.e2e.base.BaseSeleniumTest;
import com.facultyeval.e2e.pages.LoginPage;
import com.facultyeval.e2e.pages.StudentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * E2E Test: Student Workflow
 * Steps:
 *  1. Login as Student
 *  2. Submit feedback for enrolled subject
 *  3. Logout
 */
public class StudentWorkflowTest extends BaseSeleniumTest {

    private static final String STUDENT_USER = "student1";
    private static final String STUDENT_PASS = "student1";
    private static final String FEEDBACK_TEXT = "The faculty explained concepts very clearly. Great teaching!";
    private static final int    STAR_RATING   = 4;

    @Test(description = "Student: Login → Submit Feedback → Logout")
    public void testStudentFeedbackWorkflow() {

        // --- Step 1: Login as Student ---
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(STUDENT_USER, STUDENT_PASS);

        // --- Step 2: Verify Student Dashboard ---
        StudentPage studentPage = new StudentPage(driver, wait);
        Assert.assertTrue(studentPage.isStudentDashboardVisible(),
                "❌ Student dashboard did not load after login.");

        // --- Step 3: Navigate to Feedback section ---
        studentPage.navigateToFeedback();

        // --- Step 4: Open feedback form for first enrolled subject ---
        studentPage.openFeedbackForFirstSubject();

        // --- Step 5: Fill in feedback details ---
        studentPage.enterFeedbackText(FEEDBACK_TEXT);
        studentPage.selectRating(STAR_RATING); // 4 stars

        // --- Step 6: Submit feedback ---
        studentPage.submitFeedback();
        Assert.assertTrue(studentPage.isFeedbackSubmittedSuccessfully(),
                "❌ Feedback submission failed — success message not found.");

        // --- Step 7: Logout ---
        studentPage.logout();
        Assert.assertTrue(
                driver.getCurrentUrl().contains("login") || driver.getTitle().toLowerCase().contains("login"),
                "❌ Logout did not redirect to login page.");
    }
}
