package controller;

import DAO.StudentsDAO;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.StudentProfileDTO;
import model.Students;
import model.Users;

public class StudentsController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StudentsController.class.getName());
    private StudentsDAO studentsDAO = new StudentsDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get user ID from session or request parameter
        int userId = getUserIdFromRequest(request);
        
        StudentProfileDTO studentProfile = studentsDAO.getUserByID(userId);
        
        if (studentProfile != null) {
            request.setAttribute("studentProfile", studentProfile);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            request.setAttribute("messFail", "Could not find student profile");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Check if this is already a forwarded request to prevent infinite loops
        if (request.getAttribute("forwarded") != null) {
            // Already forwarded, just display the profile page
            doGet(request, response);
            return;
        }
        
        // Get updated information from form
        String userIdStr = request.getParameter("userId");
        int userId = (userIdStr != null && !userIdStr.isEmpty()) ? Integer.parseInt(userIdStr) : 
                     getUserIdFromRequest(request);
        
        String name = request.getParameter("name");
        String studentCode = request.getParameter("studentCode");
        String email = request.getParameter("email");
        
        // Process date of birth
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        java.util.Date dateOfBirth = null;
        
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                dateOfBirth = sdf.parse(dateOfBirthStr);
            } catch (java.text.ParseException e) {
                LOGGER.log(Level.WARNING, "Invalid date format: " + dateOfBirthStr, e);
                request.setAttribute("messFail", "Invalid date format");
                request.setAttribute("forwarded", true); // Mark as forwarded
                doGet(request, response);
                return;
            }
        }
        
        // Validate input data
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            studentCode == null || studentCode.trim().isEmpty() || 
            dateOfBirth == null) {
            
            request.setAttribute("messFail", "All fields are required");
            request.setAttribute("forwarded", true); // Mark as forwarded
            doGet(request, response);
            return;
        }
        
        // Update profile with the new method signature
        boolean updateSuccess = studentsDAO.updateUserProfile(userId, name, email, studentCode, dateOfBirth);
        
        if (updateSuccess) {
            // Get updated information to display
            StudentProfileDTO updatedUser = studentsDAO.getUserByID(userId);
            
            // Update session if current user is updating their own information
            Users currentUser = (Users) session.getAttribute("currentUser");
            if (currentUser != null && currentUser.getId() == userId) {
                session.setAttribute("currentUser", updatedUser);
            }
            
            request.setAttribute("messSuccess", "Profile updated successfully");
        } else {
            request.setAttribute("messFail", "Failed to update profile");
        }
        
        // Instead of forwarding to /students, which causes an infinite loop
        // directly call doGet or use redirect
        request.setAttribute("forwarded", true); // Mark as forwarded
        doGet(request, response);
    }
    
    /**
     * Helper method to get user ID from request or session
     * @param request The HTTP request
     * @return User ID or default (1)
     */
    private int getUserIdFromRequest(HttpServletRequest request) {
        // Try to get from request parameter
        String userIdParam = request.getParameter("userId");
        if (userIdParam != null && !userIdParam.isEmpty()) {
            try {
                return Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid userId parameter: " + userIdParam);
            }
        }
        
        // Try to get from session
        HttpSession session = request.getSession(false);
        if (session != null) {
            Users currentUser = (Users) session.getAttribute("currentUser");
            if (currentUser != null) {
                return currentUser.getId();
            }
        }
        
        // Default user ID if not found
        return 1;
    }
}