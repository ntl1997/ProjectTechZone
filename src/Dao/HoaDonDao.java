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
public class HoaDonDao extends TechZoneDao<HOADON, Integer>{
    
    final String Insert_SQL ="insert into HOADON (MATAIKHOAN, NGAYLAP, TONGTIEN, PHANTRAMGG, THANHTIEN, TRANGTHAI,"
            + "TenKH, SDT, DiaChi, HinhThucTT, LyDo) values(?,?,?,?,?,?,?,?,?,?,?)";
    final String Update_SQL="update HOADON set MATAIKHOAN = ?, NGAYLAP = ?, TONGTIEN = ?, PHANTRAMGG = ?, THANHTIEN = ?, TRANGTHAI = ?,"
            + "TenKH =?, SDT=?, DiaChi=?, HinhThucTT=?, LyDo=? where ID_HD = ?";
    final String Delete_SQL="delete from HOADON where ID_HD=?";
    final String Select_all_SQL="select * from HOADON";
    final String Select_ID_SQL="select * from HOADON where ID_HD=?";
    private final String SELECT_BY_STATUS = "SELECT * FROM HoaDon WHERE TrangThai = ?";
    private final String SELECT_YEAR = "SELECT DISTINCT YEAR(NGAYLAP) AS Nam FROM HoaDon";

//    private int ID_HD; 
//    private int MATAIKHOAN;
//    private Date NGAYLAP; 
//    private float TONGTIEN; 
//    private int PHANTRAMGG; 
//    private float THANHTIEN; 
//    private int TRANGTHAI;
//    private String TenKH;
//    private String SDT;
//    private String DiaChi;
//    private boolean HinhThucTT;
//    private String LyDo;
    @Override
    public void insert(HOADON entity) {
        XJdbc.update(Insert_SQL, 
                entity.getMATAIKHOAN(), 
                entity.getNGAYLAP(), 
                entity.getTONGTIEN(), 
                entity.getPHANTRAMGG(),  
                entity.getTHANHTIEN(), 
                entity.getTRANGTHAI(),
                entity.getTenKH(),
                entity.getSDT(),
                entity.getDiaChi(),
                entity.isHinhThucTT(),
                entity.getLyDo()
                );
    }

    @Override
    public void update(HOADON entity) {
        XJdbc.update(Update_SQL,
                entity.getMATAIKHOAN(), 
                entity.getNGAYLAP(), 
                entity.getTONGTIEN(), 
                entity.getPHANTRAMGG(),  
                entity.getTHANHTIEN(), 
                entity.getTRANGTHAI(),
                entity.getTenKH(),
                entity.getSDT(),
                entity.getDiaChi(),
                entity.isHinhThucTT(),
                entity.getLyDo(),
                entity.getID_HD()
        );
    }
    @Override
    public void delete(Integer id) {
                XJdbc.update(Delete_SQL, id);

    }

    @Override
    public HOADON selectById(Integer id) {
        List <HOADON> list = selectBySql(Select_ID_SQL,id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }
    @Override
    public List<HOADON> selectAll() {
        return selectBySql(Select_all_SQL);
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
                e.setNGAYLAP(rs.getDate("NGAYLAP"));
                e.setTONGTIEN(rs.getFloat("TONGTIEN"));
                e.setPHANTRAMGG(rs.getInt("PHANTRAMGG"));
                e.setTHANHTIEN(rs.getFloat("THANHTIEN"));
                e.setTRANGTHAI(rs.getInt("TRANGTHAI"));
                e.setTenKH(rs.getString("TenKH"));
                e.setSDT(rs.getString("SDT"));
                e.setDiaChi(rs.getString("DiaChi"));
                e.setHinhThucTT(rs.getBoolean("HinhThucTT"));
                e.setLyDo(rs.getString("LyDo"));
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
    // Select theo trạng thái hóa đơn
    public List<HOADON> selectByStatus(int status) {
        return selectBySql(SELECT_BY_STATUS, status);
    }
        // Select cac nam co doanh thu
    public List<Integer> selectYear() {
        List<Integer> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.query(SELECT_YEAR)) {
            while (rs.next()) {                
                list.add(rs.getInt("Nam"));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
