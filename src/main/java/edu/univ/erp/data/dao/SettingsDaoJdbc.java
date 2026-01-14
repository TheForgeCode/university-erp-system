package edu.univ.erp.data.dao;

import edu.univ.erp.data.JdbcTemplate;

import java.util.List;

public class SettingsDaoJdbc {

    public String get(String key) {
        List<String> list = JdbcTemplate.query(
            "SELECT value_text FROM settings WHERE key_name=?",
            ps -> ps.setString(1, key),
            rs -> {
                try {
                    return rs.getString("value_text");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        );
        return list.isEmpty() ? null : list.get(0);
    }

    public void set(String key, String value) {
        // check if row exists
        String existing = get(key);

        if (existing == null) {
            JdbcTemplate.update(
                "INSERT INTO settings(key_name, value_text) VALUES (?,?)",
                ps -> {
                    ps.setString(1, key);
                    ps.setString(2, value);
                }
            );
        } else {
            JdbcTemplate.update(
                "UPDATE settings SET value_text=? WHERE key_name=?",
                ps -> {
                    ps.setString(1, value);
                    ps.setString(2, key);
                }
            );
        }
    }
}