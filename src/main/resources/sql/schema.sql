-- Auth DB (simulated in same DB for dev)
CREATE TABLE IF NOT EXISTS auth_users (
  user_id INT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  role VARCHAR(20) NOT NULL,
  password_hash VARCHAR(200) NOT NULL,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
  user_id INT PRIMARY KEY,
  roll_no VARCHAR(50),
  program VARCHAR(100),
  academic_year INT
);

CREATE TABLE IF NOT EXISTS instructors (
  user_id INT PRIMARY KEY,
  department VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS courses (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) UNIQUE NOT NULL,
  title VARCHAR(200),
  credits INT
);

CREATE TABLE IF NOT EXISTS sections (
  section_id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  instructor_id INT,
  day_time VARCHAR(100),
  room VARCHAR(100),
  capacity INT NOT NULL,
  semester VARCHAR(20),
  academic_year INT,
  FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

CREATE TABLE IF NOT EXISTS enrollments (
  enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL,
  section_id INT NOT NULL,
  status VARCHAR(20) DEFAULT 'ENROLLED',
  UNIQUE (student_id, section_id)
);

CREATE TABLE IF NOT EXISTS grades (
  grade_id INT PRIMARY KEY AUTO_INCREMENT,
  enrollment_id INT NOT NULL,
  component VARCHAR(100),
  score DOUBLE,
  final_grade VARCHAR(10),
  FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id)
);

CREATE TABLE IF NOT EXISTS settings (
  key_name VARCHAR(100) PRIMARY KEY,
  value_text VARCHAR(200)
);
