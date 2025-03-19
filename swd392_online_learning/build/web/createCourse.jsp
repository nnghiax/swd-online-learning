<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Course</title>
    <!-- Bootstrap CSS -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        .form-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background-color: #f9f9f9;
        }
        .form-container h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            font-weight: bold;
        }
        .form-group .required {
            color: red;
        }
        .error-message {
            color: red;
            font-size: 14px;
        }
        .upload-options {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
        }
        .upload-options .btn {
            flex: 1;
            margin: 0 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2>Create New Course</h2>
            <form action="createCourse" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
                <!-- Course Title -->
                <div class="form-group">
                    <label for="courseTitle">Course Title <span class="required">*</span></label>
                    <input type="text" class="form-control" id="courseTitle" name="courseTitle" placeholder="Enter course title" required>
                </div>

                <!-- Course Description -->
                <div class="form-group">
                    <label for="courseDescription">Course Description <span class="required">*</span></label>
                    <textarea class="form-control" id="courseDescription" name="courseDescription" rows="4" placeholder="Enter course description" required></textarea>
                </div>

                <!-- Course Category -->
                <div class="form-group">
                    <label for="courseCategory">Course Category <span class="required">*</span></label>
                    <select class="form-control" id="courseCategory" name="courseCategory" required>
                        <option value="">Select category</option>
                        <option value="Beginner">Beginner</option>
                        <option value="Intermediate">Intermediate</option>
                        <option value="Advanced">Advanced</option>
                    </select>
                </div>

                <!-- Course Price -->
                <div class="form-group">
                    <label for="coursePrice">Course Price <span class="required">*</span></label>
                    <input type="number" class="form-control" id="coursePrice" name="coursePrice" placeholder="Enter course price" min="0" required>
                </div>

                <!-- Upload Materials -->
                <div class="form-group">
                    <label>Upload Materials <span class="required">*</span></label>
                    <div class="upload-options">
                        <button type="button" class="btn btn-outline-primary" data-toggle="modal" data-target="#videoUploadModal">
                            <i class="fas fa-video"></i> Upload Video
                        </button>
                        <button type="button" class="btn btn-outline-primary" onclick="showUploadSection('document')">
                            <i class="fas fa-file"></i> Upload Document
                        </button>
                        <button type="button" class="btn btn-outline-primary" onclick="showUploadSection('exam')">
                            <i class="fas fa-file-alt"></i> Upload Exam
                        </button>
                    </div>
                </div>

                <!-- Error Message -->
                <div id="errorMessage" class="error-message" style="display: none;">Please fill in all required fields.</div>

                <!-- Submit Button -->
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-upload"></i> Request Course Upload
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Video Upload Modal -->
    <div class="modal fade" id="videoUploadModal" tabindex="-1" aria-labelledby="videoUploadModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="videoUploadModalLabel">Upload Video</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form id="videoUploadForm">
                        <div class="form-group">
                            <label for="videoFile">Video File</label>
                            <input type="file" class="form-control-file" id="videoFile" name="videoFile" accept="video/*" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="uploadVideo()">Upload</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <!-- Custom JavaScript for form validation and upload section toggling -->
    <script>
        function validateForm() {
            let isValid = true;
            const requiredFields = document.querySelectorAll('[required]');

            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('is-invalid');
                } else {
                    field.classList.remove('is-invalid');
                }
            });

            if (!isValid) {
                document.getElementById('errorMessage').style.display = 'block';
            } else {
                document.getElementById('errorMessage').style.display = 'none';
            }

            return isValid;
        }

        function uploadVideo() {
            const videoFile = document.getElementById('videoFile').files[0];
            if (videoFile) {
                // Perform upload logic here (e.g., using AJAX)
                alert('Video uploaded successfully: ' + videoFile.name);
                $('#videoUploadModal').modal('hide'); // Close the modal
            } else {
                alert('Please select a video file to upload.');
            }
        }
    </script>
</body>
</html>