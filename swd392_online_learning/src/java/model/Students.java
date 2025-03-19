package model;

import java.util.Date;

public class Students {
    private int userId;
    private String studentCode;
    private Date dateOfBirth;

    // Constructors
    public Students() {
    }

    public Students(int userId, String studentCode, Date dateOfBirth) {
        this.userId = userId;
        this.studentCode = studentCode;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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