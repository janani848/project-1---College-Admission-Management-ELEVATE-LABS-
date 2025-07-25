
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CollegeAdmissionApp {
    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
        AdmissionManager admissionManager = new AdmissionManager();

        studentDAO.createTableIfNotExists();
        admissionManager.createAdmissionTableIfNotExists();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- College Admission Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. Assign Admissions");
            System.out.println("4. Show Admission Results");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        addStudent(scanner, studentDAO);
                        break;
                    case 2:
                        listStudents(studentDAO);
                        break;
                    case 3:
                        assignAdmissions(scanner, studentDAO, admissionManager);
                        break;
                    case 4:
                        admissionManager.showAdmissions();
                        break;
                    case 0:
                        System.out.println("Exiting. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + e.getMessage());
            }
        }
    }

    private static void addStudent(Scanner scanner, StudentDAO studentDAO) throws SQLException {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Enter score (double): ");
        double score = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter contact info: ");
        String contact = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        Student student = new Student(name, dob, score, contact, address);
        studentDAO.addStudent(student);
        System.out.println("Student added successfully!");
    }

    private static void listStudents(StudentDAO studentDAO) throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        System.out.println("\nAll Students:");
        for (Student s : students) {
            System.out.println(s);
        }
        if (students.isEmpty()) {
            System.out.println("No students found.");
        }
    }

    private static void assignAdmissions(Scanner scanner, StudentDAO studentDAO, AdmissionManager admissionManager) throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students to assign admissions for.");
            return;
        }
        System.out.print("Enter program name for admission: ");
        String program = scanner.nextLine();
        admissionManager.calculateAndAssignAdmission(students, program);
        System.out.println("Admission assignment complete.");
    }
}
