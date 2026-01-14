package edu.univ.erp.service;

import edu.univ.erp.data.dao.AuthDaoJdbc;
import edu.univ.erp.data.dao.CourseDaoJdbc;
import edu.univ.erp.data.dao.SectionDaoJdbc;
import edu.univ.erp.data.dao.SettingsDaoJdbc;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Section;
import edu.univ.erp.util.PasswordHash;
import edu.univ.erp.util.Result;

import java.util.List;

public class AdminService {

    private final AuthDaoJdbc authDao = new AuthDaoJdbc();
    private final CourseDaoJdbc courseDao = new CourseDaoJdbc();
    private final SectionDaoJdbc sectionDao = new SectionDaoJdbc();
    private final SettingsDaoJdbc settingsDao = new SettingsDaoJdbc();

    // create a new user in the Auth DB
    public Result<Void> createUser(int userId, String username, String role, String plainPassword) {
        String hash = PasswordHash.hash(plainPassword);
        authDao.insertUser(userId, username, role, hash);
        return Result.ok(null);
    }

    // create course
    public Result<Integer> createCourse(String code, String title, int credits) {
        int id = courseDao.insert(code, title, credits);
        return Result.ok(id);
    }

    // update course (required by the assignment)
    public Result<Void> updateCourse(int courseId, String code, String title, int credits) {
        courseDao.update(courseId, code, title, credits);
        return Result.ok(null);
    }

    // list courses (needed by admin UI)
    public List<Course> listCourses() {
        return courseDao.listAll();
    }

    // create section
    public Result<Integer> createSection(int courseId, Integer instructorId, String dayTime,
                                         String room, int cap, String sem, int year) {
        int id = sectionDao.insert(courseId, instructorId, dayTime, room, cap, sem, year);
        return Result.ok(id);
    }

    // update section (required by assignment)
    public Result<Void> updateSection(int sectionId, Integer instructorId, String dayTime,
                                      String room, int cap, String sem, int year) {

        sectionDao.update(sectionId, instructorId, dayTime, room, cap, sem, year);
        return Result.ok(null);
    }

    // assign instructor to a section (explicit requirement)
    public Result<Void> assignInstructor(int sectionId, int instructorId) {
        Section s = sectionDao.findById(sectionId);
        if (s == null) return Result.fail("Section not found.");

        sectionDao.update(
            sectionId,
            instructorId,
            s.dayTime,
            s.room,
            s.capacity,
            s.semester,
            s.academicYear
        );
        return Result.ok(null);
    }

    // list users
    public List<UserAuth> listUsers() {
        return authDao.findAll();
    }

    // toggle maintenance mode
    public Result<Void> setMaintenance(boolean on) {
        settingsDao.set("maintenance_on", Boolean.toString(on));
        return Result.ok(null);
    }
}