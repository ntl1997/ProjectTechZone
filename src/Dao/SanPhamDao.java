/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import java.sql.*;
import java.util.*;
import Entity.SANPHAM;
import Utils.XJdbc;
/**
 *
 * @author Tun
 */
public class SanPhamDao extends TechZoneDao<SANPHAM, String> {
    
    final String Insert_SQL ="insert into SANPHAM (TENSP, MALOAIPK, MAHANGSX, SOLUONG, GIABAN, HINH, MOTA) values(?,?,?,?,?,?,?)";
    final String Update_SQL="update SANPHAM set TENSP=?, MALOAIPK=?, MAHANGSX=?, SOLUONG=?, GIABAN=?, HINH=?, MOTA=? where ID_SP = ?";
    final String Delete_SQL="delete from SANPHAM where ID_SP=?";
    final String Select_all_SQL="select * from SANPHAM";
    final String Select_ID_SQL="select * from SANPHAM where ID_SP=?";

    @Override
    public void insert(SANPHAM entity) {
        XJdbc.update(Insert_SQL, entity.getTENSP(), entity.getMALOAIPK(), entity.getMAHANGSX(), entity.getSOLUONG(), entity.getGIABAN(), 
                entity.getHINH(), entity.getMOTA());
    }

    @Override
    public void update(SANPHAM entity) {
        XJdbc.update(Update_SQL, entity.getTENSP(), entity.getMALOAIPK(), entity.getMAHANGSX(), entity.getSOLUONG(), entity.getGIABAN(), 
                entity.getHINH(), entity.getMOTA(), entity.getID_SP());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public List<SANPHAM> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public SANPHAM selectById(String id) {
        List <SANPHAM> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<SANPHAM> selectBySql(String sql, Object... args) {
        List <SANPHAM> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
                SANPHAM e = new SANPHAM();
                e.setID_SP(rs.getInt("ID_SP"));
                e.setTENSP(rs.getString("TENSP"));
                e.setMALOAIPK(rs.getInt("MALOAIPK"));
                e.setMAHANGSX(rs.getInt("MAHANGSX"));
                e.setSOLUONG(rs.getInt("SOLUONG"));
                e.setGIABAN(rs.getFloat("GIABAN"));
                e.setHINH(rs.getString("HINH"));
                e.setMOTA(rs.getString("MOTA"));                
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    
}
