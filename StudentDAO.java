
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "name TEXT NOT NULL," +
                     "dob TEXT," +
                     "score REAL," +
                     "contact TEXT," +
                     "address TEXT)";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Students table created/verified successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating students table: " + e.getMessage());
        }
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, dob, score, contact, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getDob());
            ps.setDouble(3, student.getScore());
            ps.setString(4, student.getContact());
            ps.setString(5, student.getAddress());
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("dob"),
                    rs.getDouble("score"),
                    rs.getString("contact"),
                    rs.getString("address")
                );
                list.add(s);
            }
        }
        return list;
    }
}
