/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;
import java.sql.*;
import java.util.*;
import Entity.CHUCVU;
import Utils.XJdbc;
/**
 *
 * @author Tun
 */
public class ChucVuDao extends TechZoneDao<CHUCVU, String> {
    
    final String Insert_SQL ="insert into CHUCVU (TENCV, MOTA) values(?,?)";
    final String Update_SQL="update CHUCVU set TENCV=?, MOTA=? where ID_CV = ?";
    final String Delete_SQL="delete from CHUCVU where ID_CV=?";
    final String Select_all_SQL="select * from CHUCVU";
    final String Select_ID_SQL="select * from CHUCVU where ID_CV=?";

    @Override
    public void insert(CHUCVU entity) {
        XJdbc.update(Insert_SQL, entity.getTENCV(), entity.getMOTA());
    }

    @Override
    public void update(CHUCVU entity) {
        XJdbc.update(Update_SQL, entity.getTENCV(), entity.getMOTA(), entity.getID_CV());
    }

    @Override
    public void delete(String id) {
        XJdbc.update(Delete_SQL, id);
    }

    @Override
    public List<CHUCVU> selectAll() {
        return selectBySql(Select_all_SQL);
    }

    @Override
    public CHUCVU selectById(String id) {
        List <CHUCVU> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<CHUCVU> selectBySql(String sql, Object... args) {
        List <CHUCVU> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while(rs.next()){
                CHUCVU e = new CHUCVU();
                e.setID_CV(rs.getInt("ID_CV"));
                e.setTENCV(rs.getString("TENCV"));
                e.setMOTA(rs.getString("MOTA"));                              
                list.add(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    
}
