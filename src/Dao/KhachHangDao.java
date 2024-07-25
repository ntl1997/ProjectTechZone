/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import java.sql.*;
import java.util.*;
import Entity.KHACHHANG;
import Utils.XJdbc;
/**
 *
 * @author Tun
 */
public class KhachHangDao extends TechZoneDao<KHACHHANG, String>{
    
    final String Insert_SQL ="insert into KHACHHANG (HOTEN, DIENTHOAI, DIACHI, TICHDIEM) values(?,?,?,?)";
    final String Update_SQL="update KHACHHANG set HOTEN=?, DIENTHOAI=?, DIACHI=?, TICHDIEM=? where ID_KH = ?";
    final String Delete_SQL="delete from KHACHHANG where ID_KH=?";
    final String Select_all_SQL="select * from KHACHHANG";
    final String Select_ID_SQL="select * from KHACHHANG where ID_KH=?";

    @Override
    public void insert(KHACHHANG entity) {
        XJdbc.update(Insert_SQL, entity.getHOTEN(), entity.getDIENTHOAI(), entity.getDIACHI(), entity.getTICHDIEM());
    }

    @Override
    public void update(KHACHHANG entity) {
        XJdbc.update(Update_SQL, entity.getHOTEN(), entity.getDIENTHOAI(), entity.getDIACHI(), entity.getTICHDIEM(), entity.getID_KH());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public List<KHACHHANG> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public KHACHHANG selectById(String id) {
        List <KHACHHANG> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KHACHHANG> selectBySql(String sql, Object... args) {
        List <KHACHHANG> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
                KHACHHANG e = new KHACHHANG();
                e.setID_KH(rs.getInt("ID_KH"));
                e.setHOTEN(rs.getString("HOTEN"));
                e.setDIENTHOAI(rs.getString("DIENTHOAI"));
                e.setDIACHI(rs.getString("DIACHI"));
                e.setTICHDIEM(rs.getInt("TICHDIEM"));
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }       
}
    
    

