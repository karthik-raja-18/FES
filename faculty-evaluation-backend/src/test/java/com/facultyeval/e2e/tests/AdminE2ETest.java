package com.facultyeval.e2e.tests;

import com.facultyeval.e2e.base.BaseSeleniumTest;
import com.facultyeval.e2e.pages.AdminPage;
import com.facultyeval.e2e.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 🧪 Admin End-to-End Workflow
 * Tests subject creation, student enrollment, and faculty assignment.
 */
public class AdminE2ETest extends BaseSeleniumTest {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    private static final String SUBJECT_NAME = "Java Programming";
    private static final String SUBJECT_CODE = "CS202";

    @Test(description = "Admin Workflow: Create Subject, Enroll Student, Assign Faculty")
    public void testAdminWorkflow() {
        System.out.println("\n🔧 Starting Admin Flow");
        
        LoginPage loginPage = new LoginPage(driver, wait);
        AdminPage adminPage = new AdminPage(driver, wait);

        driver.get(BASE_URL);
        
        System.out.println("✅ Login with admin/" + ADMIN_PASS);
        loginPage.login(ADMIN_USER, ADMIN_PASS, "ADMIN");
        
        System.out.println("✅ Navigate to admin dashboard");
        Assert.assertTrue(adminPage.isAdminDashboardVisible(), "Admin dashboard not visible after login");
        
        System.out.println("✅ Create subject: \"" + SUBJECT_NAME + "\" (" + SUBJECT_CODE + ")");
        adminPage.createSubject(SUBJECT_NAME, SUBJECT_CODE);
        
        System.out.println("✅ Verify subject creation");
        Assert.assertTrue(adminPage.isSubjectCreatedSuccessfully(), "Subject creation failed");
        
        System.out.println("👤 Enroll student into subject");
        System.out.println("👤 Enrolling: Karthik into " + SUBJECT_NAME + " (" + SUBJECT_CODE + ")");
        adminPage.enrollStudentIntoSubject("Karthik", SUBJECT_NAME); 
        Assert.assertTrue(adminPage.isEnrollmentSuccessful(), "Student enrollment failed");
        
        System.out.println("👨‍🏫 Assign faculty to subject");
        adminPage.assignFacultyToSubject("faculty01", SUBJECT_NAME);
        Assert.assertTrue(adminPage.isFacultyAssignmentSuccessful(), "Faculty assignment failed");

        System.out.println("✅ Logout");
        adminPage.logout();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Logout from admin failed");
    }
}
