package com.facultyeval.e2e.tests;

import com.facultyeval.e2e.base.BaseSeleniumTest;
import com.facultyeval.e2e.pages.FacultyPage;
import com.facultyeval.e2e.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * E2E Test: Faculty Workflow
 * Steps:
 *  1. Login as Faculty
 *  2. View submitted feedback
 *  3. Logout
 */
public class FacultyWorkflowTest extends BaseSeleniumTest {

    private static final String FACULTY_USER = "faculty1";
    private static final String FACULTY_PASS = "faculty1";

    @Test(description = "Faculty: Login → View Feedback")
    public void testFacultyViewFeedbackWorkflow() {

        // --- Step 1: Login as Faculty ---
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(FACULTY_USER, FACULTY_PASS);

        // --- Step 2: Verify Faculty Dashboard ---
        FacultyPage facultyPage = new FacultyPage(driver, wait);
        Assert.assertTrue(facultyPage.isFacultyDashboardVisible(),
                "❌ Faculty dashboard did not load after login.");

        // --- Step 3: Navigate to Feedback Section ---
        facultyPage.navigateToFeedback();

        // --- Step 4: Verify Feedback section is accessible ---
        Assert.assertTrue(facultyPage.isFeedbackSectionVisible(),
                "❌ Feedback section is not visible for faculty.");

        // --- Step 5: Verify feedback records exist ---
        // Passes if feedback table is shown OR records are listed
        boolean hasFeedback   = facultyPage.getFeedbackCount() > 0;
        boolean noFeedbackMsg = facultyPage.isNoFeedbackMessageShown();

        Assert.assertTrue(hasFeedback || noFeedbackMsg,
                "❌ Neither feedback records nor empty-state message found on the page.");

        if (hasFeedback) {
            System.out.println("✅ Faculty can view " + facultyPage.getFeedbackCount() + " feedback record(s).");
        } else {
            System.out.println("ℹ️ No feedback records yet, but empty-state is shown correctly.");
        }

        // --- Step 6: Logout ---
        facultyPage.logout();
        Assert.assertTrue(
                driver.getCurrentUrl().contains("login") || driver.getTitle().toLowerCase().contains("login"),
                "❌ Logout did not redirect to login page.");
    }
}
