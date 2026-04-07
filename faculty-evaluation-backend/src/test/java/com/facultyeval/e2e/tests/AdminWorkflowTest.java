package com.facultyeval.e2e.tests;

import com.facultyeval.e2e.base.BaseSeleniumTest;
import com.facultyeval.e2e.pages.AdminPage;
import com.facultyeval.e2e.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * E2E Test: Admin Workflow
 * Steps:
 *  1. Login as Admin
 *  2. Create a Subject
 *  3. Enroll a Student into the subject
 *  4. Logout
 */
public class AdminWorkflowTest extends BaseSeleniumTest {

    private static final String ADMIN_USER    = "admin";
    private static final String ADMIN_PASS    = "admin123";
    private static final String SUBJECT_NAME  = "Software Engineering";
    private static final String SUBJECT_CODE  = "SE101";
    private static final String STUDENT_NAME  = "student1";

    @Test(description = "Admin: Login → Create Subject → Enroll Student → Logout")
    public void testAdminFullWorkflow() {

        // --- Step 1: Navigate and Login ---
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.navigateTo(BASE_URL);
        loginPage.login(ADMIN_USER, ADMIN_PASS);

        // --- Step 2: Verify Admin Dashboard loaded ---
        AdminPage adminPage = new AdminPage(driver, wait);
        Assert.assertTrue(adminPage.isAdminDashboardVisible(),
                "❌ Admin dashboard did not load after login.");

        // --- Step 3: Create a Subject ---
        adminPage.createSubject(SUBJECT_NAME, SUBJECT_CODE);
        Assert.assertTrue(adminPage.isSubjectCreatedSuccessfully(),
                "❌ Subject creation failed — success message not found.");

        // --- Step 4: Enroll Student into Subject ---
        adminPage.enrollStudentIntoSubject(STUDENT_NAME, SUBJECT_NAME);
        Assert.assertTrue(adminPage.isEnrollmentSuccessful(),
                "❌ Student enrollment failed — success message not found.");

        // --- Step 5: Logout ---
        adminPage.logout();
        // Verify redirect back to login page
        Assert.assertTrue(
                driver.getCurrentUrl().contains("login") || driver.getTitle().toLowerCase().contains("login"),
                "❌ Logout did not redirect to login page.");
    }
}
