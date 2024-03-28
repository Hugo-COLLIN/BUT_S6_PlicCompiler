import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

class MyTestClass {
    @Test
    public void testBuildString()
    {
        /* Do */
        // create a string with "Approval" and append "Tests" to it
        String s = "Approval";
        s += " Tests";
        /* Verify */
        // Verify the resulting string
        Approvals.verify(s);
    }
}
