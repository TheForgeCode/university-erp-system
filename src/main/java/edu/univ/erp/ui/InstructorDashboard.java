package edu.univ.erp.ui;

import edu.univ.erp.domain.Grade;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.data.dao.SectionDaoJdbc;
import edu.univ.erp.data.dao.EnrollmentDaoJdbc;
import edu.univ.erp.domain.Enrollment;
import edu.univ.erp.util.Result;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InstructorDashboard extends JFrame {

    private final UserAuth me;
    private final InstructorService instructorService = new InstructorService();
    private final SectionDaoJdbc sectionDao = new SectionDaoJdbc();
    private final EnrollmentDaoJdbc enrollmentDao = new EnrollmentDaoJdbc();

    public InstructorDashboard(UserAuth me) {
        this.me = me;
        setTitle("Instructor Dashboard - " + me.username);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {

        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Instructor: " + me.username));

        // Logout button added
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        top.add(logoutBtn);

        add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("My Sections", buildSectionsPanel());
        tabs.add("Enrollments", buildEnrollmentPanel());
        tabs.add("Add Grade", buildAddGradePanel());
        tabs.add("View Grades", buildViewGradesPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildSectionsPanel() {
        JPanel p = new JPanel(new BorderLayout());

        // Get sections taught by this instructor
        List<Section> secs = sectionDao.queryByInstructor(me.userId);

        StringBuilder sb = new StringBuilder();
        for (Section s : secs) {
            sb.append("Section ").append(s.sectionId)
              .append(" Course ").append(s.courseId)
              .append(" Room ").append(s.room)
              .append(" Time ").append(s.dayTime)
              .append("\n");
        }

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);

        p.add(new JScrollPane(ta), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildEnrollmentPanel() {
        JPanel p = new JPanel(new BorderLayout());

        JTextField secFld = new JTextField();
        JButton loadBtn = new JButton("Load Enrollments");

        JTextArea area = new JTextArea();
        area.setEditable(false);

        loadBtn.addActionListener(e -> {
            try {
                int sid = Integer.parseInt(secFld.getText().trim());
                List<Enrollment> list = enrollmentDao.findBySection(sid);

                StringBuilder sb = new StringBuilder();
                for (Enrollment en : list) {
                    sb.append("Enrollment ").append(en.enrollmentId)
                      .append(" Student ").append(en.studentId)
                      .append(" Status ").append(en.status)
                      .append("\n");
                }

                area.setText(sb.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid section id");
            }
        });

        JPanel top = new JPanel(new GridLayout(0, 2, 6, 6));
        top.add(new JLabel("Section ID")); top.add(secFld);
        top.add(new JLabel("")); top.add(loadBtn);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }

    private JPanel buildAddGradePanel() {
        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField enrollmentFld = new JTextField();
        JTextField compFld = new JTextField();
        JTextField scoreFld = new JTextField();

        JButton addScoreBtn = new JButton("Add Score");

        addScoreBtn.addActionListener(e -> {
            try {
                int ent = Integer.parseInt(enrollmentFld.getText().trim());
                String comp = compFld.getText().trim();
                double sc = Double.parseDouble(scoreFld.getText().trim());

                Result<Void> r = instructorService.addScore(ent, comp, sc);

                if (!r.ok)
                    JOptionPane.showMessageDialog(this, r.message, "Error", JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this, "Saved");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        p.add(new JLabel("Enrollment ID")); p.add(enrollmentFld);
        p.add(new JLabel("Component")); p.add(compFld);
        p.add(new JLabel("Score")); p.add(scoreFld);
        p.add(new JLabel("")); p.add(addScoreBtn);

        return p;
    }

    private JPanel buildViewGradesPanel() {
        JPanel p = new JPanel(new BorderLayout());

        JTextField entFld = new JTextField();
        JButton loadBtn = new JButton("Load Grades");
        JTextArea area = new JTextArea();
        area.setEditable(false);

        loadBtn.addActionListener(e -> {
            try {
                int eid = Integer.parseInt(entFld.getText().trim());
                List<Grade> gs = instructorService.getGradesForEnrollment(eid);

                StringBuilder sb = new StringBuilder();
                for (Grade g : gs) {
                    sb.append(g.component)
                      .append(" = ")
                      .append(g.score)
                      .append(" Final: ")
                      .append(g.finalGrade)
                      .append("\n");
                }

                area.setText(sb.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid enrollment id");
            }
        });

        JPanel top = new JPanel(new GridLayout(0, 2, 6, 6));
        top.add(new JLabel("Enrollment ID")); top.add(entFld);
        top.add(new JLabel("")); top.add(loadBtn);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        return p;
    }
}