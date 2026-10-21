package org.diegobercian.repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import org.diegobercian.db.Conexion;

/** Identifiers come exclusively from the compiled module definitions. Values are always bound. */
public final class CrudRepository {
    public List<Map<String,Object>> page(String table, String key, List<String> columns, int offset, int limit) throws SQLException {
        return query("SELECT " + String.join(",", columns) + " FROM " + table + " ORDER BY " + key + " LIMIT ? OFFSET ?", limit, offset);
    }
    public long position(String table,String key,Object id) throws SQLException {
        return ((Number)query("SELECT COUNT(*) AS total FROM "+table+" WHERE "+key+" < ?",id).get(0).get("total")).longValue();
    }
    public long count(String table) throws SQLException {
        return ((Number)query("SELECT COUNT(*) AS total FROM " + table).get(0).get("total")).longValue();
    }
    public List<Map<String,Object>> query(String sql, Object... values) throws SQLException {
        try (Connection c = Conexion.getInstance().getConexion(); PreparedStatement s = c.prepareStatement(sql)) {
            bind(s, Arrays.asList(values));
            try (ResultSet rs = s.executeQuery()) {
                List<Map<String,Object>> rows = new ArrayList<>();
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    Map<String,Object> row = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    for (int i=1;i<=meta.getColumnCount();i++) {
                        Object value = rs.getObject(i);
                        if (value instanceof java.sql.Date) value = ((java.sql.Date)value).toLocalDate();
                        row.put(meta.getColumnLabel(i),value);
                    }
                    rows.add(row);
                }
                return rows;
            }
        }
    }
    public Object call(String procedure, List<Object> values) throws SQLException {
        String marks = String.join(",", Collections.nCopies(values.size(), "?"));
        try (Connection c = Conexion.getInstance().getConexion()) {
            c.setAutoCommit(false);
            try (CallableStatement s = c.prepareCall("{call " + procedure + "(" + marks + ")}")) {
                bind(s, values);
                Object generated = null;
                boolean result = s.execute();
                while (result || s.getUpdateCount() != -1) {
                    if (result) {
                        try (ResultSet rows = s.getResultSet()) {
                            if (rows.getMetaData().getColumnCount()==1 && "codigoGenerado".equalsIgnoreCase(rows.getMetaData().getColumnLabel(1)) && rows.next()) generated=rows.getObject(1);
                        }
                    }
                    result=s.getMoreResults();
                }
                c.commit(); return generated;
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }
    private static void bind(PreparedStatement s, List<?> values) throws SQLException {
        for (int i=0;i<values.size();i++) {
            Object value=values.get(i);
            if (value instanceof LocalDate) s.setDate(i+1, java.sql.Date.valueOf((LocalDate)value));
            else if (value instanceof java.math.BigDecimal) s.setBigDecimal(i+1,(java.math.BigDecimal)value);
            else s.setObject(i+1,value);
        }
    }
}
