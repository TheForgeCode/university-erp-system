package edu.univ.erp.ui;

import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.util.Result;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final UserAuth me;
    private final AdminService adminService = new AdminService();

    public AdminDashboard(UserAuth me) {
        this.me = me;
        setTitle("Admin Dashboard - " + me.username);
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {

        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Admin: " + me.username));

        // Logout button added
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        top.add(logoutBtn);

        add(top, BorderLayout.NORTH);

        // main center area with three tabs
        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Users", buildUsersPanel());
        tabs.add("Courses", buildCoursesPanel());
        tabs.add("Sections", buildSectionsPanel());

        add(tabs, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(0, 1, 8, 8));
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton maintOn = new JButton("Maintenance ON");
        JButton maintOff = new JButton("Maintenance OFF");

        maintOn.addActionListener(e -> {
            Result<Void> r = adminService.setMaintenance(true);
            JOptionPane.showMessageDialog(this, r.ok ? "Maintenance ON" : r.message);
        });

        maintOff.addActionListener(e -> {
            Result<Void> r = adminService.setMaintenance(false);
            JOptionPane.showMessageDialog(this, r.ok ? "Maintenance OFF" : r.message);
        });

        right.add(maintOn);
        right.add(maintOff);

        add(right, BorderLayout.EAST);
    }

    private JPanel buildUsersPanel() {
        JPanel p = new JPanel(new BorderLayout());

        List<UserAuth> users = adminService.listUsers();

        String[] cols = {"User ID", "Username", "Role", "Status"};
        Object[][] data = new Object[users.size()][4];

        for (int i = 0; i < users.size(); i++) {
            UserAuth u = users.get(i);
            data[i][0] = u.userId;
            data[i][1] = u.username;
            data[i][2] = u.role;
            data[i][3] = u.status;
        }

        JTable table = new JTable(data, cols);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField uidFld = new JTextField();
        JTextField unameFld = new JTextField();
        JTextField roleFld = new JTextField();
        JTextField passFld = new JTextField();

        JButton createUserBtn = new JButton("Create User");
        createUserBtn.addActionListener(e -> {
            try {
                int uid = Integer.parseInt(uidFld.getText().trim());
                String uname = unameFld.getText().trim();
                String role = roleFld.getText().trim();
                String pass = passFld.getText().trim();

                Result<Void> r = adminService.createUser(uid, uname, role, pass);

                if (!r.ok) {
                    JOptionPane.showMessageDialog(this, r.message, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "User created");
                    dispose();
                    new AdminDashboard(me).setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        form.add(new JLabel("User ID")); form.add(uidFld);
        form.add(new JLabel("Username")); form.add(unameFld);
        form.add(new JLabel("Role")); form.add(roleFld);
        form.add(new JLabel("Password")); form.add(passFld);
        form.add(new JLabel("")); form.add(createUserBtn);

        p.add(form, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildCoursesPanel() {
        JPanel p = new JPanel(new BorderLayout());

        List<Course> courses = adminService.listCourses();
        String[] cols = {"ID", "Code", "Title", "Credits"};
        Object[][] data = new Object[courses.size()][4];

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            data[i][0] = c.courseId;
            data[i][1] = c.code;
            data[i][2] = c.title;
            data[i][3] = c.credits;
        }

        JTable table = new JTable(data, cols);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField codeFld = new JTextField();
        JTextField titleFld = new JTextField();
        JTextField creditsFld = new JTextField();

        JButton createCourseBtn = new JButton("Create Course");
        createCourseBtn.addActionListener(e -> {
            try {
                String code = codeFld.getText().trim();
                String title = titleFld.getText().trim();
                int cr = Integer.parseInt(creditsFld.getText().trim());

                Result<Integer> r = adminService.createCourse(code, title, cr);

                if (!r.ok) {
                    JOptionPane.showMessageDialog(this, r.message);
                } else {
                    JOptionPane.showMessageDialog(this, "Course created");
                    dispose();
                    new AdminDashboard(me).setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        form.add(new JLabel("New Code")); form.add(codeFld);
        form.add(new JLabel("New Title")); form.add(titleFld);
        form.add(new JLabel("Credits")); form.add(creditsFld);
        form.add(new JLabel("")); form.add(createCourseBtn);

        p.add(form, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildSectionsPanel() {
        JPanel p = new JPanel(new BorderLayout());

        List<Course> courses = adminService.listCourses();
        StringBuilder sb = new StringBuilder();

        for (Course c : courses) {
            sb.append("Course ").append(c.code).append("  ID ").append(c.courseId).append("\n");
        }

        JTextArea info = new JTextArea(sb.toString());
        info.setEditable(false);

        p.add(new JScrollPane(info), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField cidFld = new JTextField();
        JTextField instFld = new JTextField();
        JTextField timeFld = new JTextField();
        JTextField roomFld = new JTextField();
        JTextField capFld = new JTextField();
        JTextField semFld = new JTextField();
        JTextField yearFld = new JTextField();

        JButton createSectionBtn = new JButton("Create Section");
        createSectionBtn.addActionListener(e -> {
            try {
                int cid = Integer.parseInt(cidFld.getText().trim());
                String time = timeFld.getText().trim();
                String room = roomFld.getText().trim();
                int cap = Integer.parseInt(capFld.getText().trim());
                String sem = semFld.getText().trim();
                int year = Integer.parseInt(yearFld.getText().trim());

                Integer instId = instFld.getText().trim().isEmpty()
                        ? null
                        : Integer.parseInt(instFld.getText().trim());

                Result<Integer> r = adminService.createSection(cid, instId, time, room, cap, sem, year);

                if (!r.ok) {
                    JOptionPane.showMessageDialog(this, r.message);
                } else {
                    JOptionPane.showMessageDialog(this, "Section created");
                    dispose();
                    new AdminDashboard(me).setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        form.add(new JLabel("Course ID")); form.add(cidFld);
        form.add(new JLabel("Instructor ID")); form.add(instFld);
        form.add(new JLabel("Day or Time")); form.add(timeFld);
        form.add(new JLabel("Room")); form.add(roomFld);
        form.add(new JLabel("Capacity")); form.add(capFld);
        form.add(new JLabel("Semester")); form.add(semFld);
        form.add(new JLabel("Year")); form.add(yearFld);
        form.add(new JLabel("")); form.add(createSectionBtn);

        p.add(form, BorderLayout.SOUTH);
        return p;
    }
}
