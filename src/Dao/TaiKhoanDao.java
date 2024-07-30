/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.*;
import java.util.*;
import Entity.TAIKHOAN;
import Utils.XJdbc;

/**
 *
 * @author Tun
 */
public class TaiKhoanDao extends TechZoneDao<TAIKHOAN, String>{
    
    final String Insert_SQL ="insert into TAIKHOAN (MACV, TENDN, TENNV, EMAIL, MATKHAU, DIACHI, DIENTHOAI, NGAYSINH, GIOITINH, TRANGTHAI) values(?,?,?,?,?,?,?,?,?,?)";
    final String Update_SQL="update TAIKHOAN set MACV = ?, TENDN = ?, TENNV = ?, EMAIL = ?, MATKHAU = ?, DIACHI = ?, DIENTHOAI = ?, NGAYSINH = ?, GIOITINH = ?, TRANGTHAI = ? where ID_TK = ?";
    final String Delete_SQL="delete from TAIKHOAN where ID_TK=?";
    final String Select_all_SQL="select * from TAIKHOAN";
    final String Select_ID_SQL="select * from TAIKHOAN where ID_TK=?";

    @Override
    public void insert(TAIKHOAN entity) {
        XJdbc.update(Insert_SQL, entity.getMACV(), entity.getTENDN(), entity.getTENNV(), entity.getEMAIL(), entity.getMATKHAU(),
                entity.getDIACHI(), entity.getDIENTHOAI(), entity.getNGAYSINH(), entity.getGIOITINH(), entity.getTRANGTHAI());
    }
//    private int ID_TK;
//    private int MACV;
//    private String TENDN,TENNV, EMAIL,MATKHAU,DIACHI,DIENTHOAI;
//    private Date NGAYSINH; 
//    private int GIOITINH;
//    private int TRANGTHAI; 
    @Override
    public void update(TAIKHOAN entity) {
        XJdbc.update(Update_SQL, 
                entity.getMACV(), 
                entity.getTENDN(), 
                entity.getTENNV(), 
                entity.getEMAIL(), 
                entity.getMATKHAU(),
                entity.getDIACHI(), 
                entity.getDIENTHOAI(), 
                entity.getNGAYSINH(), 
                entity.getGIOITINH(), 
                entity.getTRANGTHAI(), 
                entity.getID_TK());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public List<TAIKHOAN> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public TAIKHOAN selectById(String id) {
        List <TAIKHOAN> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<TAIKHOAN> selectBySql(String sql, Object... args) {
        List <TAIKHOAN> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
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
        
}
