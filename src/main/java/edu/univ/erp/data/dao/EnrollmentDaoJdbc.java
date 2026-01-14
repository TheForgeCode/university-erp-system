package edu.univ.erp.data.dao;

import edu.univ.erp.data.JdbcTemplate;
import edu.univ.erp.domain.Enrollment;

import java.util.List;

public class EnrollmentDaoJdbc {
    public boolean exists(int studentId, int sectionId) {
        List<Integer> list = JdbcTemplate.query("SELECT COUNT(*) AS cnt FROM enrollments WHERE student_id=? AND section_id=?",
            ps -> { ps.setInt(1, studentId); ps.setInt(2, sectionId); },
            rs -> {
                try {
                    return rs.getInt("cnt");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        return (list.size() > 0 && list.get(0) > 0);
    }

    public int countBySection(int sectionId) {
        List<Integer> list = JdbcTemplate.query("SELECT COUNT(*) AS cnt FROM enrollments WHERE section_id=?",
            ps -> ps.setInt(1, sectionId),
            rs -> {
                try {
                    return rs.getInt("cnt");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        return list.isEmpty() ? 0 : list.get(0);
    }

    public void insert(int studentId, int sectionId) {
        JdbcTemplate.update("INSERT INTO enrollments(student_id, section_id, status) VALUES (?,?,?)",
            ps -> { ps.setInt(1, studentId); ps.setInt(2, sectionId); ps.setString(3, "ENROLLED"); });
    }

    public void delete(int studentId, int sectionId) {
        JdbcTemplate.update("DELETE FROM enrollments WHERE student_id=? AND section_id=?", ps -> { ps.setInt(1, studentId); ps.setInt(2, sectionId); });
    }

    public Integer getEnrollmentId(int studentId, int sectionId) {
        List<Integer> list = JdbcTemplate.query(
            "SELECT enrollment_id FROM enrollments WHERE student_id=? AND section_id=?",
            ps -> { ps.setInt(1, studentId); ps.setInt(2, sectionId); },
            rs -> {
                try {
                    return rs.getInt("enrollment_id");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        );


        return list.isEmpty() ? null : list.get(0);
    }

    public List<Enrollment> findBySection(int sectionId) {
        return JdbcTemplate.query(
            "SELECT enrollment_id, student_id, section_id, status FROM enrollments WHERE section_id=?",
            ps -> ps.setInt(1, sectionId),
            rs -> {
                Enrollment e = new Enrollment();
                try {
                    e.enrollmentId = rs.getInt("enrollment_id");
                    e.studentId = rs.getInt("student_id");
                    e.sectionId = rs.getInt("section_id");
                    e.status = rs.getString("status");

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                return e;
            }
        );
    }

    public List<Integer> findEnrollmentIdsBySection(int sectionId) {
        return JdbcTemplate.query(
            "SELECT enrollment_id FROM enrollments WHERE section_id=?",
            ps -> ps.setInt(1, sectionId),

            rs -> {
                try {
                    return rs.getInt("enrollment_id");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                    
                }
            }
        );
    }
}
