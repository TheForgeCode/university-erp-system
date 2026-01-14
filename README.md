# University ERP System
Java Swing • H2 Database • JDBC

## Overview
This project is a lightweight **University ERP (Enterprise Resource Planning) system**
implemented as a Java desktop application. It models core academic workflows and
demonstrates clean software architecture using only core Java technologies.

The system supports **three user roles**: Admin, Instructor, and Student, with
role-based access control and clearly separated application layers.

## Features

### Role-Based Access
- Secure login for Admin, Instructor, and Student
- Role-specific dashboards and permissions

### Admin Functions
- Create users with assigned roles
- Create courses and sections
- Assign instructors to sections
- Toggle system maintenance mode
- View all registered users

### Student Functions
- View course catalog
- Enroll in and drop sections
- View personal timetable
- View grades for enrolled sections
- Enrollment actions restricted during maintenance mode

### Instructor Functions
- Enter grade components for students
- Load and view grades using enrollment IDs

## Tech Stack
- **Language:** Java 24
- **UI:** Java Swing
- **Database:** H2 (file-based)
- **Data Access:** Plain JDBC
- **Build Tool:** Maven
- **Security:** SHA-256 + Base64 password hashing
- **Environment:** Linux (VS Code + Terminal)

## Architecture
The application follows a layered architecture:

- **Domain Layer** 
  Plain Java classes representing entities such as User, Student, Course, Section,
  Enrollment, Grade, and Settings.

- **DAO Layer**
  JDBC-based Data Access Objects for each database table.

- **Service Layer**
  Business logic, validation, and role-based authorization.

- **UI Layer**
  Java Swing interfaces for login and role-specific dashboards.

`AppMain` serves as the entry point and initializes the database on startup.


## Database Design
The database schema is defined in `schema.sql` and includes:

- auth_users
- students
- instructors
- courses
- sections
- enrollments
- grades
- settings

### Automatic Initialization
On first run:
- Tables are created if they do not exist
- Default users and sample data are seeded
- Maintenance mode is set to OFF
- Database file is created at `data/univerp.mv.db`

## Authentication and Security
- Passwords are never stored in plain text
- Passwords are hashed using SHA-256 and Base64 encoding
- Login verifies hashed credentials
- No external cryptography libraries are used

## How to Run the Project

### Prerequisites
- JDK 24 installed
- Maven installed

### Clean Setup
bash
rm -rf target/
rm -rf data/


## Build and Run Using Maven
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.univ.erp.AppMain"

## Alternative Manual Compilation
javac -d out $(find src/main/java -name "*.java")
java -cp out:$(find ~/.m2/repository/com/h2database/h2 -name "*.jar") edu.univ.erp.AppMain

## Sample Login Credentials
| Role       | Username | Password  |
| ---------- | -------- | --------- |
| Admin      | admin1   | adminpass |
| Instructor | inst1    | instpass  |
| Student    | stu1     | stupass   |
| Student    | stu2     | stupass2  |

## Expected Startup Behavior
Database file is created if missing
Schema and seed data are initialized automatically
Login screen appears immediately

## Conclusion
This University ERP System demonstrates a clean, layered Java application that
models real academic workflows using core technologies only. It emphasizes
structured JDBC usage, role-based access control, secure authentication, and
maintainable system design, and provides a solid foundation for future extensions.
