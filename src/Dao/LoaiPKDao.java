/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.*;
import java.util.*;
import Entity.LOAIPK;
import Utils.XJdbc;

/**
 *
 * @author Tun
 */
public class LoaiPKDao extends TechZoneDao<LOAIPK, Integer> {

    final String Insert_SQL = "insert into LOAIPK (TENLPK, MOTA) values(?,?)";
    final String Update_SQL = "update LOAIPK set TENLPK=?, MOTA=? where ID_LPK = ?";
    final String Delete_SQL = "delete from LOAIPK where ID_LPK=?";
    final String Select_all_SQL = "select * from LOAIPK";
    final String Select_ID_SQL = "select * from LOAIPK where ID_LPK=?";

    @Override
    public void insert(LOAIPK entity) {
        XJdbc.update(Insert_SQL, entity.getTENLPK(), entity.getMOTA());
    }

    @Override
    public void update(LOAIPK entity) {
        XJdbc.update(Update_SQL, entity.getTENLPK(), entity.getMOTA(), entity.getID_LPK());
    }

    @Override
    public void delete(Integer id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public LOAIPK selectById(Integer id) {
        List<LOAIPK> list = selectBySql(Select_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<LOAIPK> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public List<LOAIPK> selectBySql(String sql, Object... args) {
        List<LOAIPK> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                LOAIPK e = new LOAIPK();
                e.setID_LPK(rs.getInt("ID_LPK"));
                e.setTENLPK(rs.getString("TENLPK"));
                e.setMOTA(rs.getString("MOTA"));
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int layIDLPK(String mota){
        final String LayIDLPK = "SELECT ID_LPK FROM LOAIPK WHERE MOTA = ?";
        int idLPK = 0;
        try {
            Object[] args = { mota }; 
            ResultSet rs = XJdbc.query(LayIDLPK, args);        
            if (rs.next()) {
            idLPK = rs.getInt("ID_LPK");
        }
        } catch (Exception e) {
            System.out.println("LAY THAT BAI: " + e);
        }
        return idLPK;
    }
}
