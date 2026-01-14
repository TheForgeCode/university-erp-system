package edu.univ.erp.service;

import edu.univ.erp.data.dao.GradeDaoJdbc;
import edu.univ.erp.data.dao.EnrollmentDaoJdbc;
import edu.univ.erp.data.dao.SectionDaoJdbc;
import edu.univ.erp.domain.Grade;
import edu.univ.erp.domain.Section;
import edu.univ.erp.util.Result;

import java.util.List;

public class InstructorService {

    private final GradeDaoJdbc gradeDao = new GradeDaoJdbc();
    private final EnrollmentDaoJdbc enrollmentDao = new EnrollmentDaoJdbc();
    private final SectionDaoJdbc sectionDao = new SectionDaoJdbc();

    // enter a score for a component (quiz/midterm/end sem)
    public Result<Void> addScore(int enrollmentId, String component, Double score) {
        gradeDao.insert(enrollmentId, component, score);
        return Result.ok(null);
    }

    // retrieve all grade components for a given enrollment
    public List<Grade> getGradesForEnrollment(int enrollmentId) {
        return gradeDao.findByEnrollment(enrollmentId);
    }

    // instructor must see only their own sections in the UI (service supports lookup)
    public List<Section> getSectionsForInstructor(int instructorId) {
        return sectionDao.findByInstructor(instructorId);
    }

    // retrieve all enrollmentIds for a section to build gradebook
    public List<Integer> getEnrollmentsForSection(int sectionId) {
        return enrollmentDao.findEnrollmentIdsBySection(sectionId);
    }

    // compute final grade based on instructor’s weighted rule
    public Result<Void> computeFinalGrade(int gradeId, String finalGrade) {
        gradeDao.updateFinalGrade(gradeId, finalGrade);
        return Result.ok(null);
    }

    // simple stats: average score for a specific component in a section
    public Double computeAverageForComponent(int sectionId, String component) {
        List<Grade> grades = gradeDao.findBySection(sectionId);
        double sum = 0.0;
        int count = 0;

        for (Grade g : grades) {
            if (g.component.equalsIgnoreCase(component)) {
                sum += g.score;
                count++;
            }
        }

        return count == 0 ? null : sum / count;
    }
}