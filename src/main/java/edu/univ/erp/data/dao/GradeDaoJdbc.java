package edu.univ.erp.data.dao;

import edu.univ.erp.data.JdbcTemplate;
import edu.univ.erp.domain.Grade;

import java.util.List;

public class GradeDaoJdbc {

    public void insert(int enrollmentId, String component, Double score) {
        JdbcTemplate.update(
            "INSERT INTO grades(enrollment_id, component, score) VALUES (?,?,?)",
            ps -> {
                ps.setInt(1, enrollmentId);
                ps.setString(2, component);
                ps.setDouble(3, score);
            }
        );
    }

    public List<Grade> findByEnrollment(int enrollmentId) {
        return JdbcTemplate.query(
            "SELECT grade_id, enrollment_id, component, score, final_grade FROM grades WHERE enrollment_id=?",
            ps -> ps.setInt(1, enrollmentId),
            rs -> {
                Grade g = new Grade();
                try {
                    g.gradeId = rs.getInt("grade_id");
                    g.enrollmentId = rs.getInt("enrollment_id");
                    g.component = rs.getString("component");
                    g.score = rs.getDouble("score");
                    g.finalGrade = rs.getString("final_grade");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return g;
            }
        );
    }

    public void updateFinalGrade(int gradeId, String finalGrade) {
        JdbcTemplate.update(
            "UPDATE grades SET final_grade=? WHERE grade_id=?",
            ps -> {
                ps.setString(1, finalGrade);
                ps.setInt(2, gradeId);
            }
        );
    }

    // required for instructor gradebook view
    public List<Grade> findBySection(int sectionId) {
        return JdbcTemplate.query(
            "SELECT g.grade_id, g.enrollment_id, g.component, g.score, g.final_grade " +
            "FROM grades g JOIN enrollments e ON g.enrollment_id = e.enrollment_id " +
            "WHERE e.section_id = ?",
            ps -> ps.setInt(1, sectionId),
            rs -> {
                Grade g = new Grade();
                try {
                    g.gradeId = rs.getInt("grade_id");
                    g.enrollmentId = rs.getInt("enrollment_id");
                    g.component = rs.getString("component");
                    g.score = rs.getDouble("score");
                    g.finalGrade = rs.getString("final_grade");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return g;
            }
        );
    }
}