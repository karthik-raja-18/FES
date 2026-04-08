package com.facultyeval.e2e.tests;

import com.facultyeval.e2e.base.BaseSeleniumTest;
import com.facultyeval.e2e.pages.LoginPage;
import com.facultyeval.e2e.pages.StudentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 🧪 Student End-to-End Workflow
 * Tests student login and feedback submission.
 */
public class StudentE2ETest extends BaseSeleniumTest {

    private static final String STUDENT_USER = "Karthik";
    private static final String STUDENT_PASS = "karthik";
    private static final String FEEDBACK_TEXT = "Excellent course! The concepts were explained very clearly with practical examples.";

    @Test(description = "Student Workflow: Submit Feedback")
    public void testStudentWorkflow() {
        System.out.println("\n👨‍🎓 Starting Student Flow");

        LoginPage loginPage = new LoginPage(driver, wait);
        StudentPage studentPage = new StudentPage(driver, wait);

        driver.get(BASE_URL);
        
        System.out.println("✅ Login with " + STUDENT_USER);
        loginPage.login(STUDENT_USER, STUDENT_PASS, "STUDENT");
        
        System.out.println("✅ Navigate to student dashboard");
        Assert.assertTrue(studentPage.isStudentDashboardVisible(), "Student dashboard not visible after login");
        
        System.out.println("📝 Navigate to feedback section");
        studentPage.navigateToFeedback();
        
        System.out.println("📝 Open feedback for subject: CS202 (Java Programming)");
        studentPage.openFeedbackForSubject("CS202");
        
        System.out.println("✅ Enter feedback text: \"" + FEEDBACK_TEXT + "\"");
        studentPage.enterFeedbackText(FEEDBACK_TEXT);
        
        System.out.println("⭐ Select 5-star rating");
        studentPage.selectRating(5);
        
        System.out.println("✅ Submit feedback");
        studentPage.submitFeedback();
        
        System.out.println("✅ Verify feedback submission");
        Assert.assertTrue(studentPage.isFeedbackSubmittedSuccessfully(), "Feedback submission failed");
        
        System.out.println("✅ Logout");
        studentPage.logout();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Logout from student failed");
    }
}
