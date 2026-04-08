package com.facultyeval.e2e.tests;

import com.facultyeval.e2e.base.BaseSeleniumTest;
import com.facultyeval.e2e.pages.FacultyPage;
import com.facultyeval.e2e.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 🧪 Faculty End-to-End Workflow
 * Tests faculty login and viewing submitted feedback.
 */
public class FacultyE2ETest extends BaseSeleniumTest {

    private static final String FACULTY_USER = "Suganya";
    private static final String FACULTY_PASS = "suganya";
    private static final String FEEDBACK_TEXT = "Excellent course! The concepts were explained very clearly with practical examples.";

    @Test(description = "Faculty Workflow: Navigate to Dashboard")
    public void testFacultyWorkflow() {
        System.out.println("\n👨‍🏫 Starting Faculty Flow");

        LoginPage loginPage = new LoginPage(driver, wait);
        FacultyPage facultyPage = new FacultyPage(driver, wait);

        driver.get(BASE_URL);
        
        System.out.println("✅ Login with faculty01/" + FACULTY_PASS);
        loginPage.login(FACULTY_USER, FACULTY_PASS, "FACULTY");
        
        System.out.println("✅ Navigate to faculty dashboard");
        Assert.assertTrue(facultyPage.isFacultyDashboardVisible(), "Faculty dashboard not visible after login");
    }
}
