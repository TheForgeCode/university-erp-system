package edu.univ.erp.data;

import edu.univ.erp.util.DBPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JdbcTemplate {

    @FunctionalInterface
    public interface Setter {
        void set(PreparedStatement ps) throws Exception;
    }



    public static <T> List<T> query(String sql, Setter setter, Function<ResultSet, T> mapper) {
        try (Connection c = DBPool.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (setter != null) setter.set(ps);

            try (ResultSet rs = ps.executeQuery()) {
                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapper.apply(rs));
                }
                return list;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static int update(String sql, Setter setter) {
        try (Connection c = DBPool.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {   // fixed: no generated keys here

            if (setter != null) setter.set(ps);
            return ps.executeUpdate();
        } catch (Exception e) {


            throw new RuntimeException(e);
        }
    }

    public static int insertAndGetId(String sql, Setter setter) {
        try (Connection c = DBPool.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (setter != null) setter.set(ps);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        
    }
}
