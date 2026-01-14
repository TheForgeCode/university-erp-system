package edu.univ.erp.data.dao;

import edu.univ.erp.data.JdbcTemplate;
import edu.univ.erp.domain.UserAuth;

import java.util.List;

public class AuthDaoJdbc {

    public UserAuth findByUsername(String username) {
        List<UserAuth> list = JdbcTemplate.query(
            "SELECT user_id, username, role, password_hash, status FROM auth_users WHERE username = ?",
            ps -> ps.setString(1, username),

            rs -> {
                UserAuth u = new UserAuth();
                try {
                    u.userId = rs.getInt("user_id");

                    u.username = rs.getString("username");
                    u.role = rs.getString("role");
                    u.passwordHash = rs.getString("password_hash");
                    u.status = rs.getString("status");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return u;
            }
        );
        return list.isEmpty() ? null : list.get(0);


    }

    public void updateLastLogin(int userId) {
        JdbcTemplate.update(

            "UPDATE auth_users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?",
            ps -> ps.setInt(1, userId)
        );
    }

    public void insertUser(int userId, String username, String role, String passwordHash) {


        JdbcTemplate.update(
            "INSERT INTO auth_users(user_id, username, role, password_hash, status) VALUES (?,?,?,?,?)",
            ps -> {
                ps.setInt(1, userId);
                ps.setString(2, username);

                ps.setString(3, role);
                ps.setString(4, passwordHash);
                ps.setString(5, "ACTIVE");
            }
        );
    }

    public void updatePassword(int userId, String newHash) {
        JdbcTemplate.update(
            "UPDATE auth_users SET password_hash = ? WHERE user_id = ?",


            ps -> {
                ps.setString(1, newHash);
                ps.setInt(2, userId);
            }

        );
    }


    public List<UserAuth> findAll() {
        return JdbcTemplate.query(
            "SELECT user_id, username, role, password_hash, status FROM auth_users ORDER BY user_id",
            null,
            rs -> {
                UserAuth u = new UserAuth();

                try {
                    u.userId = rs.getInt("user_id");
                    u.username = rs.getString("username");
                    u.role = rs.getString("role");

                    u.passwordHash = rs.getString("password_hash");
                    u.status = rs.getString("status");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return u;

            }
        );
    }

    
}