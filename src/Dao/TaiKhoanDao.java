package Dao;

import Entity.TAIKHOAN;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Utils.XJdbc;

public class TaiKhoanDao extends TechZoneDao<TAIKHOAN, String> {
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM TAIKHOAN WHERE TENDN = ?";

    @Override
    public void insert(TAIKHOAN entity) {
        // Implement insert logic here
    }

    @Override
    public void update(TAIKHOAN entity) {
        // Implement update logic here
    }

    @Override
    public void delete(String id) {
        // Implement delete logic here
    }

    @Override
    public TAIKHOAN selectById(String id) {
        List<TAIKHOAN> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TAIKHOAN> selectAll() {
        // Implement selectAll logic here
        return null;
    }

    @Override
    protected List<TAIKHOAN> selectBySql(String sql, Object... args) {
        List<TAIKHOAN> list = new ArrayList<>();
        try (PreparedStatement pstmt = XJdbc.getStatement(sql, args);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                TAIKHOAN tk = new TAIKHOAN();
                tk.setTENDN(rs.getString("TENDN"));
                tk.setMATKHAU(rs.getString("MATKHAU"));
                // Populate other fields as needed
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
