package Dao;

import Utils.XJdbc;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ThongKeDAO {

    // Lay doanh thu ngay hom nay
    public double getDoanhThuToday() {
        String sql = "{CALL sp_DoanhThu_now}";
        return getDoanhThu(sql);
    }

    // Lay doanh thu theo khoang ngay
    public double getDoanhThuByDate(Date start, Date end) {
        String sql = "{CALL sp_DoanhThu_date(?, ?)}";
        return getDoanhThu(sql, start, end);
    }

    // Lay doanh thu thang nay
    public double getDoanhThuThisMonth() {
        String sql = "{CALL get_doanhThu_thisMonth}";
        return getDoanhThu(sql);
    }

    // Lay doanh thu nam nay
    public double getDoanhThuThisYear() {
        String sql = "{CALL get_doanhThu_thisYear}";
        return getDoanhThu(sql);
    }

    // Lay thong tin chi tiet doanh thu trong nam
    public List<Object[]> getDoanhThuDetail(int year) {
        String sql = "{CAll get_doanhThu_detail(?)}";
        String[] cols = {"Thang", "SoLuongBan", "DoanhThu"};
        return getListArray(sql, cols, year);
    }

    // Dem so hoa don trong ngay hom nay
    public int countHoaDonToday(int status) {
        String sql = "{CALL count_hoaDon_today(?)}";
        return countHoaDon(sql, status);
    }

    // Dem so hoa don theo khoang ngay
    public int countHoaDonByDate(int status, Date start, Date end) {
        String sql = "{CALL count_hoaDon_byDate(?, ?, ?)}";
        return countHoaDon(sql, status, start, end);
    }

    // Dem so hoa don thang nay
    public int countHoaDonThisMonth(int status) {
        String sql = "{CALL count_hoaDon_thisMonth(?)}";
        return countHoaDon(sql, status);
    }

    // Dem so hoa don nam nay
    public int countHoaDonThisYear(int status) {
        String sql = "{CALL count_hoaDon_thisYear(?)}";
        return countHoaDon(sql, status);
    }

    // Lay danh sach top 10 san pham ban chay nhan trong thang
    public List<Object[]> getTop10SanPham(int month, int year) {
        String sql = "{CALL get_top10_sanPham(?, ?)}";
        String[] cols = {"MaSP", "TenSP", "TENHANGSX", "SoLuongBan"};
        return getListArray(sql, cols, month, year);
    }

    // Lay doanh thu tu ResultSet va tra ve double
    private double getDoanhThu(String sql, Object... args) {
        try (ResultSet rs = XJdbc.query(sql, args)) {
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Dem hoa don
    private int countHoaDon(String sql, Object... args) {
        try (ResultSet rs = XJdbc.query(sql, args)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Lay thong tin bang tu ResultSet va tra ve ArrayList
    private List<Object[]> getListArray(String sql, String[] cols, Object... args) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
