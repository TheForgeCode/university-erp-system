package edu.univ.erp.data.dao;

import edu.univ.erp.data.JdbcTemplate;
import edu.univ.erp.domain.Section;

import java.util.List;

public class SectionDaoJdbc {

    public List<Section> listForCourse(int courseId) {
        return JdbcTemplate.query(
            "SELECT * FROM sections WHERE course_id=? ORDER BY section_id",
            ps -> ps.setInt(1, courseId),
            rs -> {
                Section s = new Section();
                try {
                    s.sectionId = rs.getInt("section_id");
                    s.courseId = rs.getInt("course_id");
                    s.instructorId = (rs.getObject("instructor_id") == null) ? null : rs.getInt("instructor_id");
                    s.dayTime = rs.getString("day_time");
                    s.room = rs.getString("room");
                    s.capacity = rs.getInt("capacity");
                    s.semester = rs.getString("semester");
                    s.academicYear = rs.getInt("academic_year");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return s;
            }
        );
    }

    public Section findById(int id) {
        List<Section> list = JdbcTemplate.query(
            "SELECT * FROM sections WHERE section_id=?",
            ps -> ps.setInt(1, id),
            rs -> {
                Section s = new Section();
                try {
                    s.sectionId = rs.getInt("section_id");
                    s.courseId = rs.getInt("course_id");
                    s.instructorId = (rs.getObject("instructor_id") == null) ? null : rs.getInt("instructor_id");
                    s.dayTime = rs.getString("day_time");
                    s.room = rs.getString("room");
                    s.capacity = rs.getInt("capacity");
                    s.semester = rs.getString("semester");
                    s.academicYear = rs.getInt("academic_year");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return s;
            }
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public int countEnrolled(int sectionId) {
        List<Integer> list = JdbcTemplate.query(
            "SELECT COUNT(*) AS cnt FROM enrollments WHERE section_id=?",
            ps -> ps.setInt(1, sectionId),
            rs -> {
                try {
                    return rs.getInt("cnt");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        );
        return list.isEmpty() ? 0 : list.get(0);
    }

    public List<Section> findByStudentId(int studentId) {
        return JdbcTemplate.query(
            "SELECT s.* FROM sections s JOIN enrollments e ON s.section_id=e.section_id WHERE e.student_id=?",
            ps -> ps.setInt(1, studentId),
            rs -> {
                Section s = new Section();
                try {
                    s.sectionId = rs.getInt("section_id");
                    s.courseId = rs.getInt("course_id");
                    s.instructorId = (rs.getObject("instructor_id") == null) ? null : rs.getInt("instructor_id");
                    s.dayTime = rs.getString("day_time");
                    s.room = rs.getString("room");
                    s.capacity = rs.getInt("capacity");
                    s.semester = rs.getString("semester");
                    s.academicYear = rs.getInt("academic_year");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return s;
            }
        );
    }

    // required for instructor dashboard
    public List<Section> findByInstructor(int instructorId) {
        return JdbcTemplate.query(
            "SELECT * FROM sections WHERE instructor_id=?",
            ps -> ps.setInt(1, instructorId),
            rs -> {
                Section s = new Section();
                try {
                    s.sectionId = rs.getInt("section_id");
                    s.courseId = rs.getInt("course_id");
                    s.instructorId = rs.getInt("instructor_id");
                    s.dayTime = rs.getString("day_time");
                    s.room = rs.getString("room");
                    s.capacity = rs.getInt("capacity");
                    s.semester = rs.getString("semester");
                    s.academicYear = rs.getInt("academic_year");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return s;
            }
        );
    }

    public int insert(int courseId, Integer instructorId, String dayTime, String room,
                      int capacity, String sem, int academicYear) {

        return JdbcTemplate.insertAndGetId(
            "INSERT INTO sections(course_id, instructor_id, day_time, room, capacity, semester, academic_year) VALUES (?,?,?,?,?,?,?)",
            ps -> {
                ps.setInt(1, courseId);
                if (instructorId == null)
                    ps.setNull(2, java.sql.Types.INTEGER);
                else
                    ps.setInt(2, instructorId);
                ps.setString(3, dayTime);
                ps.setString(4, room);
                ps.setInt(5, capacity);
                ps.setString(6, sem);
                ps.setInt(7, academicYear);
            }
        );
    }

    // required for admin editing
    public void update(int sectionId, Integer instructorId, String dayTime, String room,
                       int capacity, String sem, int academicYear) {

        JdbcTemplate.update(
            "UPDATE sections SET instructor_id=?, day_time=?, room=?, capacity=?, semester=?, academic_year=? WHERE section_id=?",
            ps -> {
                if (instructorId == null)
                    ps.setNull(1, java.sql.Types.INTEGER);
                else
                    ps.setInt(1, instructorId);
                ps.setString(2, dayTime);
                ps.setString(3, room);
                ps.setInt(4, capacity);
                ps.setString(5, sem);
                ps.setInt(6, academicYear);
                ps.setInt(7, sectionId);
            }
        );
    }

    public List<Section> queryByInstructor(int instructorId) {
        return findByInstructor(instructorId);
    }
}