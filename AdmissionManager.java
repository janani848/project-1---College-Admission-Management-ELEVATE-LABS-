import java.sql.*;
import java.util.List;

public class AdmissionManager {

    // Create admissions table if it doesn't exist
    public void createAdmissionTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS admissions (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "student_id INTEGER," +
                     "program TEXT," +
                     "merit REAL," +
                     "FOREIGN KEY(student_id) REFERENCES students(id))";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error creating admissions table: " + e.getMessage());
        }
    }

    // Assign admission for a list of students to a given program
    public void calculateAndAssignAdmission(List<Student> students, String program) throws SQLException {
        if (students == null || students.isEmpty()) {
            System.out.println("No students provided for admission assignment.");
            return;
        }
        for (Student s : students) {
            double merit = s.getScore(); // Simple merit logic; customize as needed
            assignAdmission(s.getId(), program, merit);
        }
    }

    // Assign single admission, with duplicate check
    private void assignAdmission(int studentId, String program, double merit) throws SQLException {
        // Check if admission already exists
        String checkSql = "SELECT COUNT(*) FROM admissions WHERE student_id = ? AND program = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, studentId);
            checkPs.setString(2, program);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Admission already assigned for student ID " + studentId + " in program " + program);
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking existing admission: " + e.getMessage());
            throw e; // Re-throw after logging
        }

        // Insert admission record
        String insertSql = "INSERT INTO admissions (student_id, program, merit) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setInt(1, studentId);
            ps.setString(2, program);
            ps.setDouble(3, merit);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Assigned admission for student ID " + studentId + " in program " + program);
            } else {
                System.err.println("Failed to assign admission for student ID " + studentId);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting admission: " + e.getMessage());
            throw e;
        }
    }

    // Display the list of admissions sorted by merit
    public void showAdmissions() throws SQLException {
        String sql = "SELECT a.id, s.name, a.program, a.merit " +
                     "FROM admissions a JOIN students s ON a.student_id = s.id " +
                     "ORDER BY a.merit DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAdmission List:");
            System.out.println("ID | Student Name | Program | Merit");
            System.out.println("------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%d | %s | %s | %.2f%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("program"),
                    rs.getDouble("merit"));
            }
            if (!found) {
                System.out.println("No admission records found.");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving admissions: " + e.getMessage());
            throw e;
        }
    }
}

