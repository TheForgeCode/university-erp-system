package edu.univ.erp.data.dao;

import edu.univ.erp.data.JdbcTemplate;
import edu.univ.erp.domain.Course;

import java.util.List;

public class CourseDaoJdbc {



    public List<Course> listAll() {
        return JdbcTemplate.query(
            "SELECT course_id, code, title, credits FROM courses ORDER BY code",
            null,
            rs -> {
                Course c = new Course();

                try {
                    c.courseId = rs.getInt("course_id");
                    c.code = rs.getString("code");
                    c.title = rs.getString("title");
                    c.credits = rs.getInt("credits");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return c;

            }
        );
    }
    public void update(int courseId, String code, String title, int credits) {
        JdbcTemplate.update(
            "UPDATE courses SET code = ?, title = ?, credits = ? WHERE course_id = ?",
            ps -> {
                ps.setString(1, code);
                ps.setString(2, title);
                ps.setInt(3, credits);
                ps.setInt(4, courseId);
            }
        );
    }



    public int insert(String code, String title, int credits) {
        return JdbcTemplate.insertAndGetId(
            "INSERT INTO courses(code, title, credits) VALUES(?,?,?)",
            ps -> {
                ps.setString(1, code);
                ps.setString(2, title);
                ps.setInt(3, credits);
            }
        );
    }


    public Course findById(int courseId) {
        List<Course> list = JdbcTemplate.query(
            
            "SELECT course_id, code, title, credits FROM courses WHERE course_id = ?",
            ps -> ps.setInt(1, courseId),
            rs -> {
                Course c = new Course();


                try {
                    c.courseId = rs.getInt("course_id");
                    c.code = rs.getString("code");
                    c.title = rs.getString("title");
                    c.credits = rs.getInt("credits");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return c;
            }
        );
        return list.isEmpty() ? null : list.get(0);
    }
}
