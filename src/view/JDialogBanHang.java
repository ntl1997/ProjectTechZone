/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package View;

import java.util.*;
import Dao.ChiTietHDDao;
import Dao.HangSXDao;
import Dao.HoaDonDao;
import Dao.LoaiPKDao;
import Dao.SanPhamDao;
import Entity.CHITIETHOADON;
import Entity.HOADON;
import Entity.SANPHAM;
import Utils.Auth;
import Utils.MsgBox;
import Utils.XDate;
import Utils.XImage;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tun
 */
public class JDialogBanHang extends javax.swing.JDialog {

    SanPhamDao spdao = new SanPhamDao();
    LoaiPKDao lpkdao = new LoaiPKDao();
    HangSXDao hsxdao = new HangSXDao();
    HoaDonDao hddao = new HoaDonDao();
    ChiTietHDDao ctdao = new ChiTietHDDao();

    /**
     * Creates new form JDialogBanHang
     */
    public JDialogBanHang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        setTitle("TechZone Quản lý bán hàng");
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null); // Đặt cửa sổ ở trung tâm màn hình
        this.fillTableSanPham();
        this.fillTableHoaDon();
        // Sự kiện lắng nghe txtGiamGia khi người dùng ấn Enter
        txtGiamGia.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    applyDiscount();
                }
            }
        });
    }

    // Đổ dữ liệu lên bảng Sản Phẩm
    private void fillTableSanPham() {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);

        for (SANPHAM sp : spdao.selectAll()) {
            Object data[] = {
                sp.getID_SP(),
                sp.getTENSP(),
                sp.getGIABAN(),
                sp.getSOLUONG(),
                lpkdao.selectById(sp.getMALOAIPK()).getTENLPK(),
                hsxdao.selectById(sp.getMAHANGSX()).getTENHANGSX()
            };
            model.addRow(data);
        }
        tblSanPham.setModel(model);
    }

    // Đổ dữ liệu lên bảng Giỏ Hàng
    private void fillTableGioHang(int row) {
        int maHD = (int) tblHoaDon.getValueAt(row, 0);
        DefaultTableModel model = (DefaultTableModel) tblGioHang.getModel();
        model.setRowCount(0);

        List<CHITIETHOADON> list = ctdao.selectByMaHD(maHD);
        for (CHITIETHOADON cthd : list) {
            SANPHAM sp = spdao.selectById(cthd.getMASP());
            Object[] data = {
                cthd.getMASP(),
                sp.getTENSP(),
                cthd.getGIABAN(),
                cthd.getSOLUONG(),
                cthd.getGIABAN() * cthd.getSOLUONG()
            };
            model.addRow(data);
        }
        tblGioHang.setModel(model);
    }

    // Đổ dữ liệu vào bảng hóa đơn
    private void fillTableHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        List<HOADON> list = hddao.selectByStatus(0);
        for (HOADON hd : list) {
            Object data[] = {
                hd.getID_HD(),
                Auth.user.getTENNV(),
                hd.getTenKH(),
                hd.getTRANGTHAI() == 0 ? "Pending" : "Complete-Cancel",
                hd.getNGAYLAP()
            };
            model.addRow(data);
        }
        tblHoaDon.setModel(model);
    }

    private void applyDiscount() {
        try {
            int hdRow = tblHoaDon.getSelectedRow();
            if (hdRow < 0) {
                MsgBox.alert(this, "Vui lòng chọn hóa đơn");
                return;
            }

            float phanTramGG = Float.parseFloat(txtGiamGia.getText());
            float tongTien = Float.parseFloat(txtTongTien.getText());
            float soTienDuocGiam = tongTien * (phanTramGG / 100);
            float khachCanTra = tongTien - soTienDuocGiam;

            txtTruTienGiamGia.setText(String.valueOf(soTienDuocGiam));
            txtKhachCanTra.setText(String.valueOf(khachCanTra));

        } catch (NumberFormatException e) {
            MsgBox.alert(this, "Vui lòng nhập số hợp lệ cho giảm giá");
        }
    }

    private void taoHoaDon() {
        try {
            HOADON hd = getForm();
            hddao.insert(hd);
            this.fillTableHoaDon();
            this.clearForm();
            MsgBox.alert(this, "Thêm hóa đơn thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm hóa đơn thất bại");
            e.printStackTrace();
        }

    }

    // Thêm sản phẩm vào giỏ hàng
    private void addToCart(int hdRow, int spRow) {
        int maHD = (int) tblHoaDon.getValueAt(hdRow, 0);
        int maSP = (int) tblSanPham.getValueAt(spRow, 0);
        int slTon = (int) tblSanPham.getValueAt(spRow, 3);

        boolean isInCart = false;

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        for (int i = 0; i < tblGioHang.getRowCount(); i++) {
            int maSP_GioHang = (int) tblGioHang.getValueAt(i, 0);
            int sl_GioHang = (int) tblGioHang.getValueAt(i, 3);

            if (maSP_GioHang == maSP) {
                isInCart = true;

                if (sl_GioHang < slTon) {
                    CHITIETHOADON cthd = ctdao.selectByMaHD_MaSP(maHD, maSP);
                    int soluong = sl_GioHang + 1;
                    cthd.setSOLUONG(soluong);
                    ctdao.update(cthd);
                } else {
                    MsgBox.alert(this, "Sản phẩm này hiện đang hết hàng");
                }
                break; // Thoát khỏi vòng lặp khi đã cập nhật số lượng
            }
        }

        // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
        if (!isInCart) {
            CHITIETHOADON cthd = new CHITIETHOADON();
            float giaBan = (float) tblSanPham.getValueAt(spRow, 2);
            cthd.setMAHD(maHD);
            cthd.setMASP(maSP);
            cthd.setSOLUONG(1);
            cthd.setGIABAN(giaBan);
            Date ngayTao = XDate.toDate(LocalDate.now().toString(), "yyyy-MM-dd");
            cthd.setNGAYLAP(ngayTao);
            ctdao.insert(cthd);
        }

        // Cập nhật lại bảng giỏ hàng và tổng tiền
        this.fillTableGioHang(hdRow);
        updateThanhTien(hdRow);
        setForm(hdRow);
    }

    // XÓA SẢN PHẨM KHỎI GIỎ HÀNG
    private void removeToCart(int hdRow, int spRow) {
        int maHD = (int) tblHoaDon.getValueAt(hdRow, 0);
        int maSP = (int) tblGioHang.getValueAt(spRow, 0);
        ctdao.deleteByMaHD_MaSP(maHD, maSP);
        fillTableGioHang(hdRow);
        updateThanhTien(hdRow);
        setForm(hdRow);
    }

    // XÓA TRẮNG BẢNG GIỎ HÀNG
    public void clearGioHang() {
        DefaultTableModel model = (DefaultTableModel) tblGioHang.getModel();
        model.setRowCount(0);
        tblGioHang.setModel(model);
    }

    // CẬP NHẬT TRẠNG THÁI HÓA ĐƠN (Completed, Cancel)
    private void updateStatus(int row, int status, String args) {
        int maHD = (int) tblHoaDon.getValueAt(row, 0);
        HOADON hd = hddao.selectById(maHD);

        hd.setTRANGTHAI(status);
        boolean HT_ThanhToan = cboHinhThucTT.getSelectedIndex() == 1;
        hd.setHinhThucTT(HT_ThanhToan);
        hd.setLyDo(args);
        hddao.update(hd);

        if (status == 1) {
            MsgBox.alert(this, "Thanh toán thành công!");
        } else if (status == 2) {
            MsgBox.alert(this, "Hủy hóa đơn thành công!");
        }
        // Đổ lại Hóa đơn, Sản Phẩm lên bảng
        fillTableHoaDon();
        fillTableSanPham();
        clearGioHang();
        clearForm();
    }

    // Cập nhật lại thành tiền khi thêm sản phẩm vào giỏ hàng
    private void updateThanhTien(int row) {
        int maHD = (int) tblHoaDon.getValueAt(row, 0);
        HOADON hd = hddao.selectById(maHD);
        float thanhTien = getThanhTien();
//        float giamGia = Float.parseFloat(txtGiamGia.getText());
//        hd.setTHANHTIEN(thanhTien * (1 - (giamGia / 100)));
        float giamGia = txtGiamGia.getText().isBlank() ? 0 : Float.parseFloat(txtGiamGia.getText());
        hd.setTHANHTIEN(thanhTien * (1 - (giamGia / 100)));

        hddao.update(hd);
    }

    // Lấy tổng thành tiền trước giảm giá
    private float getThanhTien() {
        float thanhTien = 0;
        for (int i = 0; i < tblGioHang.getRowCount(); i++) {
            thanhTien += (float) tblGioHang.getValueAt(i, 4);
        }
        return thanhTien;
    }

    HOADON getForm() {
//        System.out.println("Mã tài khoản: " + Auth.user.getID_TK());
        HOADON hd = new HOADON();
        hd.setMATAIKHOAN(Auth.user.getID_TK());
        hd.setSDT(txtSDT.getText().isBlank() ? "Không có" : txtSDT.getText());
        hd.setTenKH(txtTenKH.getText().isBlank() ? "Khách lẻ" : txtTenKH.getText());
        hd.setDiaChi(txtDiaChi.getText().isBlank() ? "Không có" : txtDiaChi.getText());
//        hd.setPHANTRAMGG(txtGiamGia.getText().isBlank() ? 0 : Integer.parseInt(txtGiamGia.getText()));
        boolean ht_ThanhToan = cboHinhThucTT.getSelectedIndex() == 1;
        hd.setHinhThucTT(ht_ThanhToan);
//        hd.setTHANHTIEN(0);
//        hd.setTONGTIEN(0);  // Khởi tạo TONGTIEN với giá trị 0
        Date ngayTao = XDate.toDate(LocalDate.now().toString(), "yyyy-MM-dd");
        hd.setNGAYLAP(ngayTao);
        hd.setTRANGTHAI(0);
        return hd;
    }

    private void setForm(int row) {
        int maHD = (int) tblHoaDon.getValueAt(row, 0);
        HOADON hd = hddao.selectById(maHD);

        double thanhTien = getThanhTien();
        double truTienGiamGia = thanhTien * (hd.getPHANTRAMGG() / 100);
        double khachCanTra = thanhTien - truTienGiamGia;
        txtTenKH.setText(hd.getTenKH());
        txtTenKH.setEnabled(false);

        txtSDT.setText(hd.getSDT());
        txtSDT.setEnabled(false);

        txtDiaChi.setText(hd.getDiaChi());
        txtDiaChi.setEnabled(false);

        txtGiamGia.setText(String.valueOf(hd.getPHANTRAMGG()));
//        txtGiamGia.setEnabled(false);

        txtTongTien.setText(String.valueOf(thanhTien));
        txtGiamGia.setText(String.valueOf(truTienGiamGia));
        txtKhachCanTra.setText(String.valueOf(khachCanTra));
    }

    // Xóa trắng form
    public void clearForm() {
        txtTenKH.setEnabled(true);
        txtTenKH.setText("");

        txtSDT.setEnabled(true);
        txtSDT.setText("");

        txtDiaChi.setEnabled(true);
        txtDiaChi.setText("");

//        txtGiamGia.setEnabled(true);
        txtGiamGia.setText("");

        txtKhachCanTra.setText("");
        txtTienKhachDua.setText("");
        txtTienThua.setText("");
        
        txtTongTien.setText("");
        txtTruTienGiamGia.setText("");
    }

    // Kiểm tra các thông tin khi đặt hàng
    private boolean isValidate() {
        if (txtTenKH.getText().isBlank()) {
            MsgBox.alert(this, "Bạn cần thêm tên khách hàng");
            return false;
        } else if (txtSDT.getText().isBlank()) {
            MsgBox.alert(this, "Bạn cần thêm số điện thoại khách hàng");
            return false;
        } else if (txtDiaChi.getText().isBlank()) {
            MsgBox.alert(this, "Bạn cần thêm địa chỉ khách hàng");
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        btnScan = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtTruTienGiamGia = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtKhachCanTra = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cboHinhThucTT = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        txtTienThua = new javax.swing.JTextField();
        btnHuy = new javax.swing.JButton();
        btnTaoHD = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        btnDatHang = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        spnSoLuong = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(740, 603));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("DANH SÁCH SẢN PHẨM");

        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("QUÉT MÃ SẢN PHẨM");

        jButton12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        jButton12.setText("search");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("BÁN HÀNG");

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MÃ SP", "TÊN SP", "GIÁ TIỀN", "SỐ LƯỢNG", "THÀNH TIỀN"
            }
        ));
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblGioHang);

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("GIỎ HÀNG");

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("DS HÓA ĐƠN");

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MÃ HD", "TÊN NV", "TÊN KH", "TRẠNG THÁI", "NGÀY TẠO"
            }
        ));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        btnScan.setBackground(new java.awt.Color(0, 255, 204));
        btnScan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        btnScan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/12_1.png"))); // NOI18N
        btnScan.setText("QUÉT");

        jLabel14.setText("TÊN KH");

        jLabel15.setText("SĐT");

        jLabel16.setText("ĐỊA CHỈ");

        jLabel17.setText("GIẢM GIÁ (%)");

        txtGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiamGiaActionPerformed(evt);
            }
        });

        jLabel18.setText("Tổng tiền:");

        jLabel19.setText("Trừ tiền giảm giá");

        jLabel20.setText("Khách cần trả");

        jLabel21.setText("HT Thanh Toán");

        cboHinhThucTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản" }));
        cboHinhThucTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHinhThucTTActionPerformed(evt);
            }
        });

        jLabel22.setText("Tiền khách đưa");

        jLabel23.setText("Tiền thừa");

        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTienKhachDuaKeyPressed(evt);
            }
        });

        btnHuy.setBackground(new java.awt.Color(255, 102, 102));
        btnHuy.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnHuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/14_1.png"))); // NOI18N
        btnHuy.setText("HỦY HĐ");
        btnHuy.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnHuy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHuy.setPreferredSize(new java.awt.Dimension(100, 60));
        btnHuy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnTaoHD.setBackground(new java.awt.Color(0, 204, 255));
        btnTaoHD.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnTaoHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/13.png"))); // NOI18N
        btnTaoHD.setText("TẠO HÓA ĐƠN");
        btnTaoHD.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnTaoHD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTaoHD.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(153, 255, 153));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/credit-card.png"))); // NOI18N
        btnThanhToan.setText("THANH TOÁN");
        btnThanhToan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThanhToan.setPreferredSize(new java.awt.Dimension(100, 50));
        btnThanhToan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnDatHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/13.png"))); // NOI18N
        btnDatHang.setText("ĐẶT HÀNG");
        btnDatHang.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDatHang.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(37, 37, 37)
                            .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel23)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnTaoHD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnHuy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDatHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTruTienGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addGap(62, 62, 62)
                                        .addComponent(txtKhachCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTruTienGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtKhachCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(cboHinhThucTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTaoHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDatHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "MÃ SP", "TÊN SP", "ĐƠN GIÁ", "SỐ LƯỢNG", "DANH MỤC", "HÃNG"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblSanPham);
        if (tblSanPham.getColumnModel().getColumnCount() > 0) {
            tblSanPham.getColumnModel().getColumn(0).setResizable(false);
            tblSanPham.getColumnModel().getColumn(1).setResizable(false);
            tblSanPham.getColumnModel().getColumn(2).setResizable(false);
            tblSanPham.getColumnModel().getColumn(3).setResizable(false);
            tblSanPham.getColumnModel().getColumn(4).setResizable(false);
            tblSanPham.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 157, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24_1.png"))); // NOI18N
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bin.png"))); // NOI18N
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnScan)
                                .addGap(84, 84, 84))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(51, 51, 51)))
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(spnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(btnThem)
                                .addGap(29, 29, 29)
                                .addComponent(btnXoa))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel12)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(268, 268, 268)
                                    .addComponent(jLabel9)))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnXoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(spnSoLuong))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addComponent(jLabel11)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnScan)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnThem)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        // TODO add your handling code here:
        this.taoHoaDon();
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            HOADON hd = hddao.selectById((int) tblHoaDon.getValueAt(row, 0));

            float khachCanTra = Float.parseFloat(txtKhachCanTra.getText());
            float tongTien = Float.parseFloat(txtTongTien.getText());
            int phanTramGG = Integer.parseInt(txtGiamGia.getText());

            hd.setTHANHTIEN(khachCanTra);
            hd.setTONGTIEN(tongTien);
            hd.setPHANTRAMGG(phanTramGG);

            hddao.update(hd);
            updateStatus(row, 1, "");
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        // TODO add your handling code here:
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            String lyDoHuy = MsgBox.prompt(this, "Lý do hủy:");
            if (lyDoHuy != null) {
                updateStatus(row, 2, lyDoHuy);
            }
        }
    }//GEN-LAST:event_btnHuyActionPerformed

    private void txtGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiamGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiamGiaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        int spRow = (int) tblSanPham.getSelectedRow();
        int hdRow = (int) tblHoaDon.getSelectedRow();
        if (spRow >= 0 && hdRow >= 0) {
            addToCart(hdRow, spRow);
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        int spRow = (int) tblSanPham.getSelectedRow();
        int hdRow = (int) tblHoaDon.getSelectedRow();
        if (spRow >= 0 && hdRow >= 0) {
            removeToCart(hdRow, spRow);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            this.fillTableGioHang(row);
            setForm(row);
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void cboHinhThucTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHinhThucTTActionPerformed
        // TODO add your handling code here:
        if (cboHinhThucTT.getSelectedIndex() == 1) {
            txtTienKhachDua.setEnabled(false);
            txtTienThua.setText("");
        } else if (cboHinhThucTT.getSelectedIndex() == 0) {
            txtTienKhachDua.setEnabled(true);
        }
    }//GEN-LAST:event_cboHinhThucTTActionPerformed

    private void txtTienKhachDuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtTongTien.getText().isBlank()) {
                MsgBox.alert(this, "Bạn chưa chọn hóa đơn thanh toán");
            } else {
                try {
                    float tienKhachDua = Float.parseFloat(txtTienKhachDua.getText());
                    float khachCanTra = Float.parseFloat(txtKhachCanTra.getText());
                    float tienThua = tienKhachDua - khachCanTra;
                    txtTienThua.setText(String.valueOf(tienThua));
                } catch (NumberFormatException e) {
                    MsgBox.alert(this, "Mời nhập đúng số tiền (VNĐ)");
                }
            }
        }
    }//GEN-LAST:event_txtTienKhachDuaKeyPressed

    private void tblGioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblGioHang.getModel();
        model.addTableModelListener((TableModelEvent e) -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int ghRow = tblGioHang.getSelectedRow();
                int hdRow = tblHoaDon.getSelectedRow();

                if (ghRow >= 0) {
                    int maSP = (int) tblGioHang.getValueAt(ghRow, 0);
                    int maHD = (int) tblHoaDon.getValueAt(hdRow, 0);
                    int sl_gioHang = Integer.parseInt(model.getValueAt(ghRow, 3).toString());
                    int soLuongTon = spdao.selectById(maSP).getSOLUONG();
                    CHITIETHOADON hdct = ctdao.selectByMaHD_MaSP(maHD, maSP);

                    if (sl_gioHang < soLuongTon) {
                        hdct.setSOLUONG(sl_gioHang);
                        ctdao.update(hdct);
                    } else {
                        MsgBox.alert(tblGioHang, "Không có đủ hàng!");
                    }

                    fillTableGioHang(hdRow);
                    updateThanhTien(hdRow);
                    setForm(hdRow);
                }
            }
        });
    }//GEN-LAST:event_tblGioHangMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JDialogBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogBanHang dialog = new JDialogBanHang(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDatHang;
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnScan;
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboHinhThucTT;
    private javax.swing.JButton jButton12;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner spnSoLuong;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtKhachCanTra;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienThua;
    private javax.swing.JTextField txtTongTien;
    private javax.swing.JTextField txtTruTienGiamGia;
    // End of variables declaration//GEN-END:variables
}
