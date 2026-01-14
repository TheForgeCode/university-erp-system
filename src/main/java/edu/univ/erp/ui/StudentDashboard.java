package edu.univ.erp.ui;

import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Grade;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.util.Result;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {

    private final UserAuth me;
    private final StudentService studentService = new StudentService();

    public StudentDashboard(UserAuth me) {
        this.me = me;
        setTitle("Student Dashboard - " + me.username);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {

        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Student: " + me.username));

        // Logout button added
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        top.add(logoutBtn);

        add(top, BorderLayout.NORTH);

        // Catalog Area
        JPanel left = new JPanel(new BorderLayout());
        JTextArea catalogArea = new JTextArea();
        catalogArea.setEditable(false);
        JButton loadCatalogBtn = new JButton("Load Catalog");
        loadCatalogBtn.addActionListener(e -> loadCatalog(catalogArea));

        left.add(new JLabel("Course Catalog", SwingConstants.CENTER), BorderLayout.NORTH);
        left.add(new JScrollPane(catalogArea), BorderLayout.CENTER);
        left.add(loadCatalogBtn, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);

        // Timetable Area
        JPanel center = new JPanel(new BorderLayout());
        JTextArea timetableArea = new JTextArea();
        timetableArea.setEditable(false);
        JButton loadTimetableBtn = new JButton("Load Timetable");
        loadTimetableBtn.addActionListener(e -> loadTimetable(timetableArea));

        center.add(new JLabel("My Timetable", SwingConstants.CENTER), BorderLayout.NORTH);
        center.add(new JScrollPane(timetableArea), BorderLayout.CENTER);
        center.add(loadTimetableBtn, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        // Right panel: Register, Drop, View Grades
        JPanel right = new JPanel(new GridLayout(0, 1, 6, 6));
        JTextField sectionField = new JTextField();

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> doRegister(sectionField));

        JButton dropBtn = new JButton("Drop");
        dropBtn.addActionListener(e -> doDrop(sectionField));

        JButton viewGradesBtn = new JButton("View Grades");
        viewGradesBtn.addActionListener(e -> viewGrades(sectionField));

        right.add(new JLabel("Section ID:"));
        right.add(sectionField);
        right.add(registerBtn);
        right.add(dropBtn);
        right.add(viewGradesBtn);

        add(right, BorderLayout.EAST);
    }

    private void loadCatalog(JTextArea area) {
        var courses = studentService.listCatalogSimple();

        StringBuilder sb = new StringBuilder();
        for (var c : courses) {
            sb.append(c.code).append("  ").append(c.title)
              .append(" | Credits: ").append(c.credits)
              .append("\n");
        }
        if (courses.isEmpty()) sb.append("(No courses available)\n");

        area.setText(sb.toString());
    }

    private void loadTimetable(JTextArea area) {
        List<Section> secs = studentService.viewTimetable(me.userId);

        StringBuilder sb = new StringBuilder();
        for (Section s : secs) {
            sb.append("Section ").append(s.sectionId)
              .append(" | Course ").append(s.courseId)
              .append(" | ").append(s.dayTime)
              .append(" | Room ").append(s.room)
              .append("\n");
        }
        if (secs.isEmpty()) sb.append("(No registrations)\n");

        area.setText(sb.toString());
    }

    private void doRegister(JTextField fld) {
        String s = fld.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter section id");
            return;
        }
        try {
            int sid = Integer.parseInt(s);
            Result<Void> r = studentService.register(me.userId, sid);

            if (!r.ok)
                JOptionPane.showMessageDialog(this, r.message, "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Registered");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number");
        }
    }

    private void doDrop(JTextField fld) {
        String s = fld.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter section id");
            return;
        }
        try {
            int sid = Integer.parseInt(s);
            Result<Void> r = studentService.drop(me.userId, sid);

            if (!r.ok)
                JOptionPane.showMessageDialog(this, r.message, "Error", JOptionPane.ERROR_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Dropped");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number");
        }
    }

    private void viewGrades(JTextField fld) {
        String s = fld.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter section id");
            return;
        }
        try {
            int sid = Integer.parseInt(s);
            List<Grade> list = studentService.viewGrades(me.userId, sid);

            StringBuilder sb = new StringBuilder();
            for (Grade g : list) {
                sb.append(g.component).append(": ").append(g.score)
                  .append(" Final: ").append(g.finalGrade == null ? "-" : g.finalGrade)
                  .append("\n");
            }
            if (list.isEmpty()) sb.append("(No grades)\n");

            JOptionPane.showMessageDialog(this, sb.toString(), "Grades", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number");
        }
    }
}