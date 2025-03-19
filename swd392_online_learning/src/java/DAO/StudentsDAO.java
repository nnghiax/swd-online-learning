package DAO;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.StudentProfileDTO;
import model.Students;

public class StudentsDAO extends DBContext {
    private static final Logger LOGGER = Logger.getLogger(StudentsDAO.class.getName());

    /**
     * Updates a student's basic information in the students table
     * @param student The student object containing updated information
     * @return true if the update was successful, false otherwise
     */
    public boolean updateStudentProfile(Students student) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection(); // Get a fresh connection
            String sql = "UPDATE students SET student_code = ?, date_of_birth = ? WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, student.getStudentCode());
            stmt.setDate(2, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmt.setInt(3, student.getUserId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating student profile", ex);
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Retrieves a student's profile information by user ID
     * @param userId The ID of the user to retrieve
     * @return StudentProfileDTO object with user details, or null if not found
     */
    public StudentProfileDTO getUserByID(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection(); // Get a fresh connection
            String sql = "SELECT u.id AS UserId, u.name, u.email, s.student_code, s.date_of_birth "
                    + "FROM users u "
                    + "JOIN students s ON u.id = s.user_id "
                    + "WHERE u.id = ?";
                    
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                StudentProfileDTO studentProfileDTO = new StudentProfileDTO();
                studentProfileDTO.setUserId(rs.getInt("UserId"));
                studentProfileDTO.setName(rs.getString("name"));
                studentProfileDTO.setEmail(rs.getString("email"));
                studentProfileDTO.setStudentCode(rs.getString("student_code"));
                studentProfileDTO.setDateOfBirth(rs.getDate("date_of_birth"));
                return studentProfileDTO;
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving student profile for user ID: " + userId, e);
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    /**
     * Updates both user and student information in a single transaction
     * @param userId The ID of the user to update
     * @param name Updated name
     * @param email Updated email
     * @param studentCode Updated student code
     * @param dateOfBirth Updated date of birth
     * @return true if the update was successful, false otherwise
     */
    public boolean updateUserProfile(int userId, String name, String email, String studentCode, Date dateOfBirth) {
        Connection conn = null;
        PreparedStatement psUsers = null;
        PreparedStatement psStudents = null;
        
        try {
            conn = getConnection(); // Get a fresh connection
            conn.setAutoCommit(false); // Start transaction
            
            // Update users table
            String updateUsersQuery = "UPDATE users SET name = ?, email = ? WHERE id = ?";
            psUsers = conn.prepareStatement(updateUsersQuery);
            psUsers.setString(1, name);
            psUsers.setString(2, email);
            psUsers.setInt(3, userId);
            psUsers.executeUpdate();

            // Update students table
            String updateStudentsQuery = "UPDATE students SET student_code = ?, date_of_birth = ? WHERE user_id = ?";
            psStudents = conn.prepareStatement(updateStudentsQuery);
            psStudents.setString(1, studentCode);
            psStudents.setDate(2, new java.sql.Date(dateOfBirth.getTime()));
            psStudents.setInt(3, userId);
            psStudents.executeUpdate();

            conn.commit(); // Complete transaction
            LOGGER.log(Level.INFO, "Transaction committed successfully");
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction on error
                }
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Error during rollback", rollbackEx);
            }
            LOGGER.log(Level.SEVERE, "Error during update", e);
            return false;
        } finally {
            // Close resources in reverse order
            try {
                if (psStudents != null) psStudents.close();
                if (psUsers != null) psUsers.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }
    
    /**
     * Get a fresh connection from DBContext
     */
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Recreate the connection by calling the superclass constructor
            try {
                String user = "sa";
                String pass = "123";
                String url = "jdbc:sqlserver://localhost:1433;databaseName=Swd392_Grp3;encrypt=true;trustServerCertificate=true;";
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = java.sql.DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException ex) {
                throw new SQLException("JDBC Driver not found", ex);
            }
        }
        return connection;
    }
    
    /**
     * Helper method to close database resources
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing resources", e);
        }
    }

    /**
     * Main method for testing the StudentsDAO functionality
     */
    public static void main(String[] args) {
        // Create DAO object
        StudentsDAO studentsDAO = new StudentsDAO();

        // User ID to update
        int userId = 1;

        // Information to update
        String name = "Quy Test";
        String email = "quytest@gmail.com";
        String studentCode = "HE173122";

        // Create Date object for date of birth
        java.util.Date dateOfBirth = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            dateOfBirth = sdf.parse("2003-06-09");
        } catch (java.text.ParseException e) {
            LOGGER.log(Level.SEVERE, "Date format error", e);
            return;
        }

        // Print information before update
        System.out.println("=== INFORMATION BEFORE UPDATE ===");
        StudentProfileDTO beforeUpdate = studentsDAO.getUserByID(userId);
        if (beforeUpdate != null) {
            printStudentInfo(beforeUpdate);
        } else {
            System.out.println("Student with ID " + userId + " not found");
            return;
        }

        // Perform update
        System.out.println("\n=== PERFORMING UPDATE ===");
        boolean updateSuccess = studentsDAO.updateUserProfile(userId, name, email, studentCode, dateOfBirth);
        System.out.println("Update status: " + (updateSuccess ? "Success" : "Failed"));

        // Print information after update
        System.out.println("\n=== INFORMATION AFTER UPDATE ===");
        StudentProfileDTO afterUpdate = studentsDAO.getUserByID(userId);
        if (afterUpdate != null) {
            printStudentInfo(afterUpdate);
            validateUpdate(afterUpdate, name, email, studentCode);
        } else {
            System.out.println("Could not retrieve information after update");
        }
    }
    
    /**
     * Helper method to print student information
     */
    private static void printStudentInfo(StudentProfileDTO student) {
        System.out.println("ID: " + student.getUserId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Student Code: " + student.getStudentCode());
        System.out.println("Date of Birth: " + student.getDateOfBirth());
    }
    
    /**
     * Helper method to validate if the update was successful
     */
    private static void validateUpdate(StudentProfileDTO student, String name, String email, String studentCode) {
        boolean nameUpdated = name.equals(student.getName());
        boolean emailUpdated = email.equals(student.getEmail());
        boolean codeUpdated = studentCode.equals(student.getStudentCode());

        System.out.println("\nRESULT: " + (nameUpdated && emailUpdated && codeUpdated ? 
                "Update successful!" : "Update incomplete!"));
        
        if (!nameUpdated) {
            System.out.println("- Name was not updated");
        }
        if (!emailUpdated) {
            System.out.println("- Email was not updated");
        }
        if (!codeUpdated) {
            System.out.println("- Student code was not updated");
        }
    }
}