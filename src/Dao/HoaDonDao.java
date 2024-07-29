/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import java.sql.*;
import java.util.*;
import Entity.HOADON;
import Utils.XJdbc;
/**
 *
 * @author Tun
 */
public class HoaDonDao extends TechZoneDao<HOADON, String>{
    
    final String Insert_SQL ="insert into HOADON (MATAIKHOAN, MAKH, NGAYLAP, TONGTIEN, PHANTRAMGG, TICHDIEM, THANHTIEN, TRANGTHAI) values(?,?,?,?,?,?,?,?)";
    final String Update_SQL="update HOADON set MATAIKHOAN = ?, MAKH = ?, NGAYLAP = ?, TONGTIEN = ?, PHANTRAMGG = ?, TICHDIEM = ?, THANHTIEN = ?, TRANGTHAI = ? where ID_HD = ?";
    final String Delete_SQL="delete from HOADON where ID_HD=?";
    final String Select_all_SQL="select * from HOADON";
    final String Select_ID_SQL="select * from HOADON where ID_HD=?";

    @Override
    public void insert(HOADON entity) {
        XJdbc.update(Insert_SQL, entity.getMATAIKHOAN(), entity.getMAKH(), entity.getNGAYLAP(), entity.getTONGTIEN(), entity.getPHANTRAMGG(), 
                entity.getTICHDIEM(), entity.getTHANHTIEN(), entity.getTRANGTHAI());
    }

    @Override
    public void update(HOADON entity) {
        XJdbc.update(Update_SQL, entity.getMATAIKHOAN(), entity.getMAKH(), entity.getNGAYLAP(), entity.getTONGTIEN(), entity.getPHANTRAMGG(), 
                entity.getTICHDIEM(), entity.getTHANHTIEN(), entity.getTRANGTHAI(), entity.getID_HD());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public List<HOADON> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public HOADON selectById(String id) {
        List <HOADON> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HOADON> selectBySql(String sql, Object... args) {
        List <HOADON> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
                HOADON e = new HOADON();
                e.setID_HD(rs.getInt("ID_HD"));
                e.setMATAIKHOAN(rs.getInt("MATAIKHOAN"));
                e.setMAKH(rs.getInt("MAKH"));
                e.setNGAYLAP(rs.getDate("NGAYLAP"));
                e.setTONGTIEN(rs.getFloat("TONGTIEN"));
                e.setPHANTRAMGG(rs.getInt("PHANTRAMGG"));
                e.setTICHDIEM(rs.getFloat("TICHDIEM"));
                e.setTHANHTIEN(rs.getFloat("THANHTIEN"));
                e.setTRANGTHAI(rs.getInt("TRANGTHAI"));
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    //Tìm kiếm theo tên
    public List<HOADON> selectByKeyword(String keyword) {
        String sql = "select * from HOADON WHERE LIKE ?";
        return this.selectBySql(sql, '%' + keyword + "%");
    }
    
}
