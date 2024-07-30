/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.*;
import java.util.*;
import Entity.HANGSX;
import Utils.XJdbc;

/**
 *
 * @author Tun
 */
public class HangSXDao extends TechZoneDao<HANGSX, Integer> {

    final String Insert_SQL = "insert into HANGSX (TENHANGSX, MOTA) values(?,?)";
    final String Update_SQL = "update HANGSX set TENHANGSX=?, MOTA=? where ID_HANGSX = ?";
    final String Delete_SQL = "delete from HANGSX where ID_HANGSX=?";
    final String Select_all_SQL = "select * from HANGSX";
    final String Select_ID_SQL = "select * from HANGSX where ID_HANGSX=?";

    @Override
    public void insert(HANGSX entity) {
        XJdbc.update(Insert_SQL, entity.getTENHANGSX(), entity.getMOTA());
    }

    @Override
    public void update(HANGSX entity) {
        XJdbc.update(Update_SQL, entity.getTENHANGSX(), entity.getMOTA(), entity.getID_HANGSX());
    }

    @Override
    public void delete(Integer id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public HANGSX selectById(Integer id) {
        List<HANGSX> list = selectBySql(Select_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HANGSX> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public List<HANGSX> selectBySql(String sql, Object... args) {
        List<HANGSX> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                HANGSX e = new HANGSX();
                e.setID_HANGSX(rs.getInt("ID_HANGSX"));
                e.setTENHANGSX(rs.getString("TENHANGSX"));
                e.setMOTA(rs.getString("MOTA"));
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
