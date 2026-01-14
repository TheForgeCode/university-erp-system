package edu.univ.erp.init;

import edu.univ.erp.util.DBPool;
import edu.univ.erp.util.PasswordHash;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {

    public static void init() {
        try {
            DBPool.init();
            runSchema();
            seedDefaults();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void runSchema() throws Exception {
        InputStream is = DBInitializer.class.getResourceAsStream("/sql/schema.sql");
        if (is == null) {
            System.err.println("schema.sql not found");
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        String[] parts = sb.toString().split(";");
        try (Connection c = DBPool.getConnection();
             Statement st = c.createStatement()) {

            for (String p : parts) {
                String trimmed = p.trim();
                if (!trimmed.isEmpty()) st.execute(trimmed);
            }
        }
    }

    private static void seedDefaults() throws Exception {

        try (Connection c = DBPool.getConnection();
             Statement st = c.createStatement()) {

            var rs = st.executeQuery("SELECT COUNT(*) AS cnt FROM auth_users");
            int cnt = 0;
            if (rs.next()) cnt = rs.getInt("cnt");
            rs.close();

            if (cnt > 0) return;

            String adminHash = PasswordHash.hash("adminpass");
            String instHash = PasswordHash.hash("instpass");
            String stuHash1 = PasswordHash.hash("stupass");
            String stuHash2 = PasswordHash.hash("stupass2");

            st.execute("INSERT INTO auth_users(user_id, username, role, password_hash, status) " +
                    "VALUES (1,'admin1','Admin','" + adminHash + "','ACTIVE')");


            st.execute("INSERT INTO auth_users(user_id, username, role, password_hash, status) " +
                    "VALUES (2,'inst1','Instructor','" + instHash + "','ACTIVE')");


            st.execute("INSERT INTO auth_users(user_id, username, role, password_hash, status) " +
                    "VALUES (3,'stu1','Student','" + stuHash1 + "','ACTIVE')");


            st.execute("INSERT INTO auth_users(user_id, username, role, password_hash, status) " +
                    "VALUES (4,'stu2','Student','" + stuHash2 + "','ACTIVE')");

            st.execute("INSERT INTO instructors(user_id, department) VALUES (2,'Computer Science')");

            st.execute("INSERT INTO students(user_id, roll_no, program, academic_year) " +
                    "VALUES (3,'CS2025001','BTech CS',2)");
            st.execute("INSERT INTO students(user_id, roll_no, program, academic_year) " +
                    "VALUES (4,'CS2025002','BTech CS',2)");

            st.execute("INSERT INTO courses(course_id, code, title, credits) " +
                    "VALUES (1,'CS101','Intro to Programming',4)");


            st.execute("INSERT INTO courses(course_id, code, title, credits) " +
                    "VALUES (2,'CS201','Data Structures',4)");


            st.execute("INSERT INTO sections(section_id, course_id, instructor_id, day_time, room, capacity, semester, academic_year) " +
                    "VALUES (1,1,2,'Mon-Wed 09:00-10:30','R101',30,'Fall',2025)");

            st.execute("INSERT INTO sections(section_id, course_id, instructor_id, day_time, room, capacity, semester, academic_year) " +
                    "VALUES (2,2,2,'Tue-Thu 11:00-12:30','R102',25,'Fall',2025)");


            st.execute("INSERT INTO enrollments(student_id, section_id, status) VALUES (3,1,'ENROLLED')");

            st.execute("INSERT INTO grades(enrollment_id, component, score, final_grade) " +
                    "VALUES (1,'midterm',45.0,NULL)");

            var setCheck = st.executeQuery("SELECT COUNT(*) AS cnt FROM settings WHERE key_name='maintenance_on'");
            boolean exists = false;

            if (setCheck.next()) exists = setCheck.getInt("cnt") > 0;
            setCheck.close();

            if (!exists)
                st.execute("INSERT INTO settings(key_name, value_text) VALUES ('maintenance_on','false')");
            else
                st.execute("UPDATE settings SET value_text='false' WHERE key_name='maintenance_on'");
        }
    }

    
}