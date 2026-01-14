DELETE FROM grades;
DELETE FROM enrollments;
DELETE FROM sections;
DELETE FROM courses;
DELETE FROM students;
DELETE FROM instructors;
DELETE FROM auth_users;
DELETE FROM settings;

INSERT INTO settings(key_name, value_text)
VALUES ('maintenance_on','false');

INSERT INTO auth_users(user_id, username, role, password_hash, status) VALUES
(1,'admin1','Admin','jYeI9Ko5n9P7qvN0iU1tClJjz0n8U1pfaHxlkG3Z8Wo=','ACTIVE'),
(2,'inst1','Instructor','Reu2LzEfTJo3eVz1W06Rrutx8WcNsznDnKySHEw0AQ0=','ACTIVE'),
(3,'stu1','Student','lsOU2UQzR1u7oBMOaS1W1hjz11K0H3eeHkM3PiMofLM=','ACTIVE'),
(4,'stu2','Student','VwTSS0gK51AF/xAH4n3juwn2Drs1wyksLx3TE3FhDAE=','ACTIVE');

INSERT INTO instructors(user_id, department) VALUES
(2,'Computer Science');

INSERT INTO students(user_id, roll_no, program, academic_year) VALUES
(3,'CS2025001','BTech CS',2),
(4,'CS2025002','BTech CS',2);

INSERT INTO courses(course_id, code, title, credits) VALUES
(1,'CS101','Intro to Programming',4),
(2,'CS201','Data Structures',4);

INSERT INTO sections(section_id, course_id, instructor_id, day_time, room, capacity, semester, academic_year) VALUES
(1,1,2,'Mon-Wed 09:00-10:30','R101',30,'Fall',2025),
(2,2,2,'Tue-Thu 11:00-12:30','R102',25,'Fall',2025);

INSERT INTO enrollments(student_id, section_id, status)
VALUES (3,1,'ENROLLED');

INSERT INTO grades(enrollment_id, component, score, final_grade)
VALUES (1,'midterm',45.0,NULL);