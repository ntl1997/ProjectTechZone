/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import java.sql.*;
import java.util.*;
import Entity.CHITIETHOADON;
import Utils.XJdbc;
/**
 *
 * @author Tun
 */
public class ChiTietHDDao extends TechZoneDao<CHITIETHOADON, String>{
    
    final String Insert_SQL ="insert into CHITIETHOADON (MAHD, MASP, NGAYLAP, GIABAN, SOLUONG) values(?,?,?,?,?)";
    final String Update_SQL="update CHITIETHOADON set MAHD = ?, MASP = ?, NGAYLAP = ?, GIABAN = ?, SOLUONG = ? where ID_HDCT = ?";
    final String Delete_SQL="delete from CHITIETHOADON where ID_HDCT=?";
    final String Select_all_SQL="select * from CHITIETHOADON";
    final String Select_ID_SQL="select * from CHITIETHOADON where ID_HDCT=?";

    @Override
    public void insert(CHITIETHOADON entity) {
        XJdbc.update(Insert_SQL, entity.getMAHD(), entity.getMASP(), entity.getNGAYLAP(), entity.getGIABAN(), entity.getSOLUONG());
    }

    @Override
    public void update(CHITIETHOADON entity) {
        XJdbc.update(Update_SQL, entity.getMAHD(), entity.getMASP(), entity.getNGAYLAP(), entity.getGIABAN(), entity.getSOLUONG(), entity.getID_HDCT());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public List<CHITIETHOADON> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public CHITIETHOADON selectById(String id) {
        List <CHITIETHOADON> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<CHITIETHOADON> selectBySql(String sql, Object... args) {
        List <CHITIETHOADON> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
                CHITIETHOADON e = new CHITIETHOADON();
                e.setID_HDCT(rs.getInt("ID_HDCT"));
                e.setMAHD(rs.getInt("MAHD"));
                e.setMASP(rs.getInt("MASP"));
                e.setNGAYLAP(rs.getDate("NGAYLAP"));
                e.setGIABAN(rs.getFloat("GIABAN"));
                e.setSOLUONG(rs.getInt("SOLUONG"));
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    
}
