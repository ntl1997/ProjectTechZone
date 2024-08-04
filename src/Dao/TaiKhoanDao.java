package Dao;

import Entity.TAIKHOAN;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Utils.XJdbc;

public class TaiKhoanDao extends TechZoneDao<TAIKHOAN, Integer> {

    final String Insert_SQL = "insert into TAIKHOAN (MACV, TENDN, TENNV, EMAIL, MATKHAU, DIACHI, DIENTHOAI, NGAYSINH, GIOITINH, TRANGTHAI) values(?,?,?,?,?,?,?,?,?,?)";
    final String Update_SQL = "update TAIKHOAN set MACV = ?, TENDN = ?, TENNV = ?, EMAIL = ?, MATKHAU = ?, DIACHI = ?, "
            + "DIENTHOAI = ?, NGAYSINH = ?, GIOITINH = ?, TRANGTHAI = ? where ID_TK = ?";
    final String Delete_SQL = "delete from TAIKHOAN where ID_TK=?";
    final String Select_all_SQL = "select * from TAIKHOAN";
    final String Select_ID_SQL = "select * from TAIKHOAN where ID_TK=?";
    final String SELECT_BY_TENDN = "SELECT * FROM TAIKHOAN WHERE TENDN = ?";

    @Override
    public void insert(TAIKHOAN entity) {
        XJdbc.update(Insert_SQL, entity.getMACV(), entity.getTENDN(), entity.getTENNV(), entity.getEMAIL(), entity.getMATKHAU(),
                entity.getDIACHI(), entity.getDIENTHOAI(), entity.getNGAYSINH(), entity.getGIOITINH(), entity.getTRANGTHAI());
    }

    @Override
    public void update(TAIKHOAN entity) {
        XJdbc.update(Update_SQL, entity.getMACV(), entity.getTENDN(), entity.getTENNV(), entity.getEMAIL(), entity.getMATKHAU(),
                entity.getDIACHI(), entity.getDIENTHOAI(),
                entity.getNGAYSINH(), entity.getGIOITINH(), entity.getTRANGTHAI(), entity.getID_TK());
    }

    @Override
    public void delete(Integer id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public TAIKHOAN selectById(Integer id) {
        List<TAIKHOAN> list = this.selectBySql(Select_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public TAIKHOAN selectByTenDN(String TENDN) {
        List<TAIKHOAN> list = this.selectBySql(SELECT_BY_TENDN, TENDN);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TAIKHOAN> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public List<TAIKHOAN> selectBySql(String sql, Object... args) {
        List<TAIKHOAN> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                TAIKHOAN e = new TAIKHOAN();
                e.setID_TK(rs.getInt("ID_TK"));
                e.setMACV(rs.getInt("MACV"));
                e.setTENDN(rs.getString("TENDN"));
                e.setTENNV(rs.getString("TENNV"));
                e.setEMAIL(rs.getString("EMAIL"));
                e.setMATKHAU(rs.getString("MATKHAU"));
                e.setDIACHI(rs.getString("DIACHI"));
                e.setDIENTHOAI(rs.getString("DIENTHOAI"));
                e.setNGAYSINH(rs.getDate("NGAYSINH"));
                e.setGIOITINH(rs.getInt("GIOITINH"));
                e.setTRANGTHAI(rs.getInt("TRANGTHAI"));
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

//    public List<TAIKHOAN> selectBySql2(String sql, Object... args) {
//        List<TAIKHOAN> list = new ArrayList<>();
//        try (PreparedStatement pstmt = XJdbc.getStatement(sql, args); ResultSet rs = pstmt.executeQuery()) {
//            while (rs.next()) {
//                TAIKHOAN tk = new TAIKHOAN();
//                tk.setTENDN(rs.getString("TENDN"));
//                tk.setMATKHAU(rs.getString("MATKHAU"));
//                list.add(tk);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
    
    private List<TAIKHOAN> getListOfArray(String sql, String[] cols, Object... args) {
    List<TAIKHOAN> list = new ArrayList<>();
    try (ResultSet rs = XJdbc.query(sql, args)) {
        while (rs.next()) {
            TAIKHOAN taiKhoan = new TAIKHOAN();
            for (String columnName : cols) {
                Object value = rs.getObject(columnName);
                if ("TENNV".equals(columnName)) {
                    taiKhoan.setTENNV((String) value);
                }
            }
            list.add(taiKhoan);
        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    return list;
}

public List<TAIKHOAN> getTop3() {
    String sql = "{CALL sp_LayNVXS}";
    String[] cols = {"TENNV"};
    return getListOfArray(sql, cols);
}

}
