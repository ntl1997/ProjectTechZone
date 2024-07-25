/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.*;
import java.util.*;

/**
 *
 * @author luand
 */
public class XJdbc {

    // Danh sách các tham số Object...args
    public static PreparedStatement getStatement(String sql, Object... args) throws SQLException {
        Connection con = ConnectDB.getConnect();
        PreparedStatement statement;
        if (sql.trim().startsWith("{")) {
            statement = con.prepareCall(sql); // Proc
        } else {
            statement = con.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }
        return statement;
    }
    // Sử dụng select 
    public static ResultSet query(String sql,Object...args) throws SQLException{
        PreparedStatement statement = XJdbc.getStatement(sql, args);
        return statement.executeQuery();
    }
    // Sử dụng cho sql bất kì
    public static Object value(String sql, Object...args){
        try {
            ResultSet result = XJdbc.query(sql, args);
            if (result.next()) {
                return result.getObject(0);
            }
            result.getStatement().getConnection().close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Sử dụng insert, update, delete và proc
    public static int update(String sql, Object...args){
        try {
            PreparedStatement state = XJdbc.getStatement(sql, args);
            try {
                return state.executeUpdate();
            } finally {
                state.getConnection().close();
            }               
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
