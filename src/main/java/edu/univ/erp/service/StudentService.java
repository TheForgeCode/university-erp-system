package edu.univ.erp.service;

import edu.univ.erp.data.dao.EnrollmentDaoJdbc;
import edu.univ.erp.data.dao.SectionDaoJdbc;
import edu.univ.erp.data.dao.SettingsDaoJdbc;
import edu.univ.erp.data.dao.GradeDaoJdbc;
import edu.univ.erp.data.dao.CourseDaoJdbc;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Grade;
import edu.univ.erp.domain.Course;
import edu.univ.erp.util.Result;

import java.util.List;

public class StudentService {

    private final EnrollmentDaoJdbc enrollmentDao = new EnrollmentDaoJdbc();
    private final SectionDaoJdbc sectionDao = new SectionDaoJdbc();
    private final SettingsDaoJdbc settingsDao = new SettingsDaoJdbc();
    private final GradeDaoJdbc gradeDao = new GradeDaoJdbc();
    private final CourseDaoJdbc courseDao = new CourseDaoJdbc();

    public Result<Void> register(int studentId, int sectionId) {
        boolean maintenance = Boolean.parseBoolean(
            settingsDao.get("maintenance_on") == null ? "false" : settingsDao.get("maintenance_on")
        );

        if (maintenance) {
            return Result.fail("Maintenance is ON: cannot register.");
        }

        Section s = sectionDao.findById(sectionId);
        if (s == null) {
            return Result.fail("Section not found.");
        }

        int enrolled = sectionDao.countEnrolled(sectionId);
        if (enrolled >= s.capacity) {
            return Result.fail("Section full.");
        }

        if (enrollmentDao.exists(studentId, sectionId)) {
            return Result.fail("Already registered.");
        }

        enrollmentDao.insert(studentId, sectionId);
        return Result.ok(null);
    }

    public Result<Void> drop(int studentId, int sectionId) {
        boolean maintenance = Boolean.parseBoolean(
            settingsDao.get("maintenance_on") == null ? "false" : settingsDao.get("maintenance_on")
        );

        if (maintenance) {
            return Result.fail("Maintenance is ON: cannot drop.");
        }

        if (!enrollmentDao.exists(studentId, sectionId)) {
            return Result.fail("Not enrolled.");
        }

        // Required by the assignment:
        // "Drop before deadline" — we assume deadline stored in settings table.
        String deadline = settingsDao.get("drop_deadline");
        if (deadline != null) {
            long now = System.currentTimeMillis();
            long deadlineMillis = Long.parseLong(deadline);
            if (now > deadlineMillis) {
                return Result.fail("Deadline passed: cannot drop.");
            }
        }

        enrollmentDao.delete(studentId, sectionId);
        return Result.ok(null);
    }

    public List<Section> viewTimetable(int studentId) {
        return sectionDao.findByStudentId(studentId);
    }

    // Required by assignment: student can view grades
    public List<Grade> viewGrades(int studentId, int sectionId) {
        Integer enrollmentId = enrollmentDao.getEnrollmentId(studentId, sectionId);
        if (enrollmentId == null) {
            return List.of();
        }
        return gradeDao.findByEnrollment(enrollmentId);
    }

    public List<Course> listCatalogSimple() {
        return courseDao.listAll();
    }
}