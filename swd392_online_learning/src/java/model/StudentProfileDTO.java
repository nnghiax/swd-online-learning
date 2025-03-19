package model;

 import java.util.Date;

public class StudentProfileDTO {
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    private String name;
    private String email;
    private String studentCode;
    private Date dateOfBirth;

    // Constructors
    public StudentProfileDTO() {
    }

    public StudentProfileDTO(String name, String email, String studentCode, Date dateOfBirth) {
        this.name = name;
        this.email = email;
        this.studentCode = studentCode;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}