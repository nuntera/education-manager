A Mindera mindswap bootcamp exercise.  
  
# Education Manager API  
  
A robust REST API for managing educational institutions, handling student enrollments, and course management.  
  
## 🎯 Features. 
  
- Student Management (CRUD operations)  
- Course Management (CRUD operations)  
- Course Enrollment System  
- Email Validation and Uniqueness Check  
- Comprehensive Error Handling  
- Swagger/OpenAPI Documentation  
  
## 🛠️ Technologies  
  
- Java 17  
- Spring Boot 3.x  
- Spring Data JPA  
- Postgres Database  
- Maven  
- JUnit 5  
- RestAssured  
- Swagger/OpenAPI 3.0  

## 🚀 Getting Started  

1. Clone the repository:  
git clone https://github.com/nuntera/education-manager.git

2. Navigate to the project directory:  
cd education-manager  

3. Build the project:
mvn clean install

4. Run the application:  
mvn spring-boot:run  


The API will be available at `http://localhost:8080`  

## 📚 API Documentation  

Once the application is running, you can access the Swagger UI documentation at:  
`http://localhost:8080/swagger-ui.html`  

### Main Endpoints  

#### Students  
- `POST /api/v1/students` - Create a new student  
- `GET /api/v1/students` - Get all students  
- `GET /api/v1/students/{id}` - Get student by ID  
- `PUT /api/v1/students/{id}` - Update student  
- `DELETE /api/v1/students/{id}` - Delete student  
- `POST /api/v1/students/{studentId}/courses/{courseId}` - Enroll student in course  

#### Courses  
- `POST /api/v1/courses` - Create a new course  
- `GET /api/v1/courses` - Get all courses  
- `GET /api/v1/courses/{id}` - Get course by ID  
- `PUT /api/v1/courses/{id}` - Update course  
- `DELETE /api/v1/courses/{id}` - Delete course  

## 🧪 Testing  

The project includes comprehensive test coverage including:  
- Integration Tests  

Run tests with:  
mvn test  

## 🔒 Error Handling  

The API includes comprehensive error handling for:  
- Resource not found  
- Validation errors  
- Duplicate email addresses  
- Enrollment conflicts  
- General server errors  

## 📝 License  

This project is licensed under the MIT License  

## ✨ Acknowledgments  

- Mindera School  
