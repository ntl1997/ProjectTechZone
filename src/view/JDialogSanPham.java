/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package View;

import Entity.LOAIPK;
import Entity.HANGSX;
import Dao.HangSXDao;
import Dao.LoaiPKDao;
import Dao.SanPhamDao;
import Entity.SANPHAM;
import Utils.Auth;
import Utils.MsgBox;
import Utils.XImage;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tun
 */
public class JDialogSanPham extends javax.swing.JDialog {

    JFileChooser fileChooser = new JFileChooser();
    SanPhamDao dao = new SanPhamDao();
    LoaiPKDao lpkdao = new LoaiPKDao();
    HangSXDao hsxdao = new HangSXDao();
    int row = -1;

    /**
     * Creates new form JDialogSanPham
     */
    public JDialogSanPham(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null); // Đặt cửa sổ ở trung tâm màn hình
        setTitle("TechZone Quản Lý Sản Phẩm");
        setIconImage(XImage.getAppIcon());
        this.fillTable();
        this.fillComboBoxDanhMuc();
        this.fillComboBoxHangSX();
        this.filltblHSX();
    }
// SẢN PHẨM START
    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0);
        try {

            for (SANPHAM sp : dao.selectAll()) {
                Object[] data = {
                    sp.getID_SP(),
                    sp.getTENSP(),
                    sp.getMALOAIPK(),
                    sp.getMAHANGSX(),
                    sp.getSOLUONG(),
                    sp.getGIABAN(),
                    sp.getMOTA()
                };
                model.addRow(data);
            }

        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    private void fillComboBoxDanhMuc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboDanhMuc.getModel();
        model.removeAllElements();
        for (LOAIPK lpk : lpkdao.selectAll()) {
            model.addElement(lpk);
        }
        cboDanhMuc.setModel(model);
    }

    private void fillComboBoxHangSX() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboHangSX.getModel();
        model.removeAllElements();
        for (HANGSX hsx : hsxdao.selectAll()) {
            model.addElement(hsx);
        }
        cboHangSX.setModel(model);
    }

    void setForm(SANPHAM sp) {
        txtTenSP.setText(sp.getTENSP());
        txtMoTa.setText(sp.getMOTA());
        txtGiaBan.setText(String.valueOf(sp.getGIABAN()));
        txtSoLuong.setText(String.valueOf(sp.getSOLUONG()));
        if (sp.getHINH() != null) {
            lblAnh.setToolTipText(sp.getHINH());
            lblAnh.setIcon(XImage.read(sp.getHINH()));
        }
        LOAIPK lpk = lpkdao.selectById(sp.getMALOAIPK());
        HANGSX hsx = hsxdao.selectById(sp.getMAHANGSX());
        cboDanhMuc.setSelectedItem(lpk);
        cboHangSX.setSelectedItem(hsx);
    }

    SANPHAM getForm() {
        SANPHAM sp = new SANPHAM();
        sp.setMAHANGSX(((HANGSX) cboHangSX.getSelectedItem()).getID_HANGSX());
        sp.setMALOAIPK(((LOAIPK) cboDanhMuc.getSelectedItem()).getID_LPK());
        sp.setGIABAN(Float.parseFloat(txtGiaBan.getText()));
        sp.setSOLUONG(Integer.parseInt(txtSoLuong.getText()));
        sp.setHINH(lblAnh.getToolTipText());
        sp.setMOTA(txtMoTa.getText());
        sp.setTENSP(txtTenSP.getText());
        return sp;
    }

    void chonAnh() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            XImage.save(file);// Lưu hình vào thư mục logos
            ImageIcon icon = XImage.read(file.getName());//Đọc hình từ logos
            lblAnh.setIcon(icon);
            lblAnh.setToolTipText(file.getName());
        }
    }

    void clearForm() {
        SANPHAM sp = new SANPHAM();
        this.setForm(sp);
        this.row = -1;
        this.updateStatus();
    }

    void updateStatus() {
        boolean edit = (this.row >= 0); // Có 1 dòng được chọn
        boolean first = (this.row == 0); // Dòng đầu tiên
        boolean last = (this.row == tblSanPham.getRowCount() - 1);
        btnThem.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }

    private boolean isVallidate() {
        if (txtTenSP.getText().isBlank()) {
            MsgBox.alert(this, "Bạn không được để trống mã tên sản phẩm");
            return false;
        }

        if (txtMoTa.getText().isBlank()) {
            MsgBox.alert(this, "Bạn không được để trống mô tả");
            return false;
        }
        if (txtSoLuong.getText().isBlank()) {
            MsgBox.alert(this, "Bạn không được để trống số lượng");
            return false;
        }
        if (txtGiaBan.getText().isBlank()) {
            MsgBox.alert(this, "Bạn không được để trống giá bán");
            return false;
        }
        if (lblAnh.getToolTipText() == null) {
            MsgBox.alert(this, "Bạn cần chọn hình");
            return false;
        }
        return true;
    }

    void insert() {
        if (!this.isVallidate()) {
            return;
        }
        SANPHAM sp = getForm();
        try {
            dao.insert(sp);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Thêm mới thất bại");

        }
    }

    void update() {
        if (!this.isVallidate()) {
            return;
        }
        SANPHAM sp = getForm();
        try {
            sp.setID_SP((int) tblSanPham.getValueAt(this.row, 0));
            dao.update(sp);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Câp nhật thành công");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại");

        }
    }

    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa chuyên đề");
        } else if (MsgBox.confirm(this, "Bạn có thực sự muốn xóa chuyên đề này")) {
            int id = Integer.parseInt((String) tblSanPham.getValueAt(this.row, 0));
            try {
                dao.delete(id);
                this.fillTable();
                this.clearForm();
                MsgBox.alert(this, "Xóa thành công");
            } catch (Exception e) {
                MsgBox.alert(this, "Xóa thất bại");
            }
        }
    }

    void edit() {
        int id = (int) tblSanPham.getValueAt(this.row, 0);
        SANPHAM sp = dao.selectById(id);
        this.setForm(sp);
        this.updateStatus();
    }

    void first() {
        this.row = 0;
        this.edit();
    }

    void prev() {
        if (this.row > 0) {
            this.row--;
            this.edit();
        }
    }

    void next() {
        if (this.row < tblSanPham.getRowCount() - 1) {
            this.row++;
            this.edit();
        }
    }

    void last() {
        this.row = tblSanPham.getRowCount() - 1;
        this.edit();
    }
    // SANPHAM END
    
    // HANG SAN XUAT
    void filltblHSX(){
        DefaultTableModel model = (DefaultTableModel) tblHangSX.getModel();
        model.setRowCount(0);
        try {
            for(HANGSX sp: hsxdao.selectAll()){
                Object data[] = {sp.getTENHANGSX(), sp.getMOTA()};
                model.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Loi lay HSX" + e);
        }
    }
    
    public HANGSX getHSXForm(){
        HANGSX hsx = new HANGSX();
        hsx.setTENHANGSX(txtTenHang.getText());
        hsx.setMOTA(txtMoTaHang.getText());
        return hsx;
    }
    
    public void setHSXForm(HANGSX hsx){
        txtTenHang.setText(hsx.getTENHANGSX());
        txtMoTaHang.setText(hsx.getMOTA());
    }
    
    void updateHSXStatus() {
        boolean edit = (this.row >= 0); // Có 1 dòng được chọn
        boolean first = (this.row == 0); // Dòng đầu tiên
        boolean last = (this.row == tblHangSX.getRowCount() - 1);
        
        btnHSXFIRST.setEnabled(edit && !first);
        btnHSXPREV.setEnabled(edit && !first);
        btnHSXNEXT.setEnabled(edit && !last);
        btnHSXLAST.setEnabled(edit && !last);
    }
    
    public void HSXclear(){
        txtTenHang.setText("");
        txtMoTaHang.setText("");
    }
    
    public void HSXinsert(){
        HANGSX hsx = this.getHSXForm();
        try {
            hsxdao.insert(hsx);
            this.filltblHSX();
            this.HSXclear();
            MsgBox.alert(this, "THÊM HÃNG SẢN XUẤT THÀNH CÔNG");
        } catch (Exception e) {
            MsgBox.alert(this, "THÊM HÃNG SẢN XUẤT THẤT BẠI");
            System.out.println("THÊM THẤT BẠI" + e);
        }
    }
    
    public void HSXupdate(){
        HANGSX hsx = this.getHSXForm();
        try {
            int vitri = tblHangSX.getSelectedRow();
            String ten = String.valueOf(tblHangSX.getValueAt(vitri, 0));            
            int id = hsxdao.layIDHSX(ten);
            hsx.setID_HANGSX(id);
            hsxdao.update(hsx);
            this.filltblHSX();
            this.HSXclear();
            MsgBox.alert(this, "CẬP NHẬP HÃNG SẢN XUẤT THÀNH CÔNG");
        } catch (Exception e) {
            MsgBox.alert(this, "CẬP NHẬP THẤT BẠI" + e);
            System.out.println("LỖI KHI CẬP NHẬP: "+ e);
        }
    }
    
    public void HSXdelete(){
        HANGSX hsx = this.getHSXForm();
        if(MsgBox.confirm(this, "BẠN MUỐN XÓA HÃNG SẢN XUẤT NÀY KHÔNG?")){
        try {
            int vitri = tblHangSX.getSelectedRow();
            String ten = String.valueOf(tblHangSX.getValueAt(vitri, 0));
            int id = hsxdao.layIDHSX(ten);
            hsx.setID_HANGSX(id);
            hsxdao.delete(id);
            this.filltblHSX();
            this.HSXclear();
            MsgBox.alert(this, "XÓA HÃNG SẢN XUẤT THÀNH CÔNG");
        } catch (Exception e) {
            MsgBox.alert(this, "XÓA THẤT BẠI" + e);
            System.out.println("LỖI KHI XÓA: "+ e);
        }
    }
    }
    
    void HSXedit() {
        String ten = String.valueOf(tblHangSX.getValueAt(this.row, 0));
        int id = hsxdao.layIDHSX(ten);
        HANGSX hsx = hsxdao.selectById(id);
        setHSXForm(hsx);
        this.updateHSXStatus();
    }
    
    void HSXfirst() {
        this.row = 0;
        this.HSXedit();
    }

    void HSXprev() {
        if (this.row > 0) {
            this.row--;
            this.HSXedit();
        }
    }

    void HSXnext() {
        if (this.row < tblHangSX.getRowCount() - 1) {
            this.row++;
            this.HSXedit();
        }
    }

    void HSXlast() {
        this.row = tblHangSX.getRowCount() - 1;
        this.HSXedit();
    }
    
    // HANG SAN XUAT END
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnHSXFIRST = new javax.swing.JButton();
        btnHSXPREV = new javax.swing.JButton();
        btnHSXNEXT = new javax.swing.JButton();
        btnHSXLAST = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHangSX = new javax.swing.JTable();
        btnCapNhapHangSX = new javax.swing.JButton();
        btnXoaHangSX = new javax.swing.JButton();
        btnLamMoiHangSX = new javax.swing.JButton();
        btnThemHangSX = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        txtTenHang = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtMoTaHang = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtGiaBan = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblAnh = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        btnSua = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        cboSapXep = new javax.swing.JComboBox<>();
        btnTang = new javax.swing.JButton();
        btnGiam = new javax.swing.JButton();
        cboHangSX = new javax.swing.JComboBox<>();
        cboDanhMuc = new javax.swing.JComboBox<>();
        txtMoTa = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        txtTimKiemSP = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton32 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton28 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(255, 102, 51));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(102, 204, 255));

        jLabel16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel16.setText("Tên hãng");

        jLabel17.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel17.setText("MÔ TẢ");

        btnHSXFIRST.setBackground(new java.awt.Color(255, 255, 0));
        btnHSXFIRST.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnHSXFIRST.setText("<<");
        btnHSXFIRST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHSXFIRSTActionPerformed(evt);
            }
        });

        btnHSXPREV.setBackground(new java.awt.Color(255, 255, 0));
        btnHSXPREV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnHSXPREV.setText("<");
        btnHSXPREV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHSXPREVActionPerformed(evt);
            }
        });

        btnHSXNEXT.setBackground(new java.awt.Color(255, 255, 0));
        btnHSXNEXT.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnHSXNEXT.setText(">");
        btnHSXNEXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHSXNEXTActionPerformed(evt);
            }
        });

        btnHSXLAST.setBackground(new java.awt.Color(255, 255, 0));
        btnHSXLAST.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnHSXLAST.setText(">>");
        btnHSXLAST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHSXLASTActionPerformed(evt);
            }
        });

        tblHangSX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "TÊN HÃNG", "MÔ TẢ"
            }
        ));
        tblHangSX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHangSXMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHangSX);

        btnCapNhapHangSX.setBackground(new java.awt.Color(153, 255, 204));
        btnCapNhapHangSX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnCapNhapHangSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/22_1.png"))); // NOI18N
        btnCapNhapHangSX.setText("Cập nhật");
        btnCapNhapHangSX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapNhapHangSX.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCapNhapHangSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhapHangSXActionPerformed(evt);
            }
        });

        btnXoaHangSX.setBackground(new java.awt.Color(255, 102, 102));
        btnXoaHangSX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnXoaHangSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/14.png"))); // NOI18N
        btnXoaHangSX.setText("XÓA");
        btnXoaHangSX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXoaHangSX.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoaHangSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHangSXActionPerformed(evt);
            }
        });

        btnLamMoiHangSX.setBackground(new java.awt.Color(153, 153, 153));
        btnLamMoiHangSX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnLamMoiHangSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/25_1.png"))); // NOI18N
        btnLamMoiHangSX.setText("LÀM MỚI");
        btnLamMoiHangSX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLamMoiHangSX.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLamMoiHangSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiHangSXActionPerformed(evt);
            }
        });

        btnThemHangSX.setBackground(new java.awt.Color(153, 204, 255));
        btnThemHangSX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnThemHangSX.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24_1.png"))); // NOI18N
        btnThemHangSX.setText("THÊM");
        btnThemHangSX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemHangSX.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThemHangSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemHangSXActionPerformed(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        jButton21.setText("search");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jComboBox5.setBackground(new java.awt.Color(0, 153, 255));
        jComboBox5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp 1", "Sắp xếp 2" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jButton34.setBackground(new java.awt.Color(51, 153, 255));
        jButton34.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 255, 255));
        jButton34.setText("Tăng");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(51, 153, 255));
        jButton35.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton35.setForeground(new java.awt.Color(255, 255, 255));
        jButton35.setText("Giảm");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        txtTenHang.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txtMoTaHang.setColumns(20);
        txtMoTaHang.setRows(5);
        jScrollPane5.setViewportView(txtMoTaHang);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel16)
                                    .addComponent(txtTenHang)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnHSXFIRST)
                                .addGap(26, 26, 26)
                                .addComponent(btnHSXPREV)
                                .addGap(18, 18, 18)
                                .addComponent(btnHSXNEXT)
                                .addGap(28, 28, 28)
                                .addComponent(btnHSXLAST)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btnThemHangSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(2, 2, 2))
                            .addComponent(btnCapNhapHangSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXoaHangSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLamMoiHangSX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton34)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton35)))))
                    .addComponent(jScrollPane2))
                .addGap(60, 60, 60))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenHang, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemHangSX, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jButton34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton35))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton21)
                            .addComponent(btnHSXFIRST)
                            .addComponent(btnHSXPREV)
                            .addComponent(btnHSXNEXT)
                            .addComponent(btnHSXLAST)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnCapNhapHangSX, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoaHangSX, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLamMoiHangSX, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("HÃNG SẢN XUẤT", jPanel3);

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel11.setText("Số lượng");

        txtSoLuong.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel12.setText("Giá bán");

        txtGiaBan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtGiaBan.setPreferredSize(new java.awt.Dimension(64, 25));

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel13.setText("Tên sản phẩm");

        jLabel14.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel14.setText("Mô tả");

        lblAnh.setText("Choose Pictures");
        lblAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblAnh.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(153, 204, 255));
        btnThem.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24_1.png"))); // NOI18N
        btnThem.setText("THÊM");
        btnThem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 102, 102));
        btnXoa.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/14.png"))); // NOI18N
        btnXoa.setText("XÓA");
        btnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setBackground(new java.awt.Color(153, 153, 153));
        btnMoi.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/25_1.png"))); // NOI18N
        btnMoi.setText("LÀM MỚI");
        btnMoi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMoi.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnFirst.setBackground(new java.awt.Color(255, 255, 51));
        btnFirst.setText("<<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setBackground(new java.awt.Color(255, 255, 0));
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(255, 255, 0));
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setBackground(new java.awt.Color(255, 255, 51));
        btnLast.setText(">>");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "TÊN SẢN PHẨM", "HÃNG", "LOẠI PHỤ KIỆN", "SỐ LƯỢNG", "GIÁ BÁN", "MÔ TẢ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);
        if (tblSanPham.getColumnModel().getColumnCount() > 0) {
            tblSanPham.getColumnModel().getColumn(0).setResizable(false);
            tblSanPham.getColumnModel().getColumn(0).setPreferredWidth(15);
            tblSanPham.getColumnModel().getColumn(1).setResizable(false);
            tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblSanPham.getColumnModel().getColumn(2).setResizable(false);
            tblSanPham.getColumnModel().getColumn(2).setPreferredWidth(15);
            tblSanPham.getColumnModel().getColumn(3).setResizable(false);
            tblSanPham.getColumnModel().getColumn(3).setPreferredWidth(15);
            tblSanPham.getColumnModel().getColumn(4).setResizable(false);
            tblSanPham.getColumnModel().getColumn(4).setPreferredWidth(15);
            tblSanPham.getColumnModel().getColumn(5).setResizable(false);
            tblSanPham.getColumnModel().getColumn(5).setPreferredWidth(20);
            tblSanPham.getColumnModel().getColumn(6).setResizable(false);
            tblSanPham.getColumnModel().getColumn(6).setPreferredWidth(150);
        }

        btnSua.setBackground(new java.awt.Color(153, 255, 204));
        btnSua.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/22_1.png"))); // NOI18N
        btnSua.setText("Cập nhật");
        btnSua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N

        cboSapXep.setBackground(new java.awt.Color(0, 153, 255));
        cboSapXep.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        cboSapXep.setForeground(new java.awt.Color(255, 255, 255));
        cboSapXep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp 1", "Sắp xếp 2" }));
        cboSapXep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSapXepActionPerformed(evt);
            }
        });

        btnTang.setBackground(new java.awt.Color(51, 153, 255));
        btnTang.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnTang.setForeground(new java.awt.Color(255, 255, 255));
        btnTang.setText("Tăng");
        btnTang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTangActionPerformed(evt);
            }
        });

        btnGiam.setBackground(new java.awt.Color(51, 153, 255));
        btnGiam.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        btnGiam.setForeground(new java.awt.Color(255, 255, 255));
        btnGiam.setText("Giảm");
        btnGiam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGiamActionPerformed(evt);
            }
        });

        cboHangSX.setBackground(new java.awt.Color(51, 153, 255));
        cboHangSX.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        cboHangSX.setForeground(new java.awt.Color(255, 255, 255));

        cboDanhMuc.setBackground(new java.awt.Color(51, 153, 255));
        cboDanhMuc.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        cboDanhMuc.setForeground(new java.awt.Color(255, 255, 255));

        txtMoTa.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txtTenSP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Hãng sản xuât");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Danh mục");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel11)
                                .addComponent(jLabel13)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                .addComponent(cboHangSX, 0, 1, Short.MAX_VALUE)
                                .addComponent(txtTenSP))
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel14)
                                .addComponent(jLabel12)
                                .addComponent(txtGiaBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtMoTa)
                                .addComponent(cboDanhMuc, 0, 130, Short.MAX_VALUE))
                            .addComponent(jLabel2))
                        .addGap(30, 30, 30)
                        .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSua))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTang)
                                    .addComponent(cboSapXep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnGiam))
                                .addGap(15, 15, 15))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnMoi)
                                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnFirst)
                                .addGap(18, 18, 18)
                                .addComponent(btnPrev)
                                .addGap(18, 18, 18)
                                .addComponent(btnNext)
                                .addGap(18, 18, 18)
                                .addComponent(btnLast)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTimKiemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 838, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(23, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboHangSX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(cboSapXep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(btnTang)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnGiam)
                                    .addGap(139, 139, 139))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnMoi)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(6, 6, 6)))
                            .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFirst)
                            .addComponent(btnPrev)
                            .addComponent(btnNext)
                            .addComponent(btnLast)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTimKiemSP)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(241, 241, 241))
        );

        jTabbedPane1.addTab("SẢN PHẨM", jPanel2);

        jPanel4.setBackground(new java.awt.Color(102, 204, 255));

        jLabel18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel18.setText("Loại phụ kiện");

        jTextField11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel19.setText("Mô tả");

        jButton23.setBackground(new java.awt.Color(255, 255, 0));
        jButton23.setText("<<");

        jButton24.setBackground(new java.awt.Color(255, 255, 0));
        jButton24.setText("<");

        jButton25.setBackground(new java.awt.Color(255, 255, 0));
        jButton25.setText(">");

        jButton26.setBackground(new java.awt.Color(255, 255, 0));
        jButton26.setText(">>");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "LOẠI PHỤ KIỆN", "MÔ TẢ"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jButton32.setBackground(new java.awt.Color(153, 255, 204));
        jButton32.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/22_1.png"))); // NOI18N
        jButton32.setText("Cập nhật");
        jButton32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton32.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(255, 102, 102));
        jButton13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/14.png"))); // NOI18N
        jButton13.setText("XÓA");
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton19.setBackground(new java.awt.Color(153, 204, 255));
        jButton19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24_1.png"))); // NOI18N
        jButton19.setText("THÊM");
        jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton20.setBackground(new java.awt.Color(153, 153, 153));
        jButton20.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/25_1.png"))); // NOI18N
        jButton20.setText("Làm mới");
        jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton22.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        jButton22.setText("search");

        jComboBox3.setBackground(new java.awt.Color(0, 153, 255));
        jComboBox3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jComboBox3.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp 1", "Sắp xếp 2" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jButton28.setBackground(new java.awt.Color(51, 153, 255));
        jButton28.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton28.setForeground(new java.awt.Color(255, 255, 255));
        jButton28.setText("Tăng");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton33.setBackground(new java.awt.Color(51, 153, 255));
        jButton33.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.setText("Giảm");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton23)
                        .addGap(18, 18, 18)
                        .addComponent(jButton24)
                        .addGap(18, 18, 18)
                        .addComponent(jButton25)
                        .addGap(18, 18, 18)
                        .addComponent(jButton26))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jLabel18))
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton32, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(51, 51, 51))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jLabel19))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton28)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton33))))
                .addGap(59, 59, 59))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton19)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton32))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jButton28))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton13))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jButton33)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton20)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton26)
                    .addComponent(jButton25)
                    .addComponent(jButton24)
                    .addComponent(jButton23)
                    .addComponent(jButton22))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );

        jTabbedPane1.addTab("LOẠI PHỤ KIỆN", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        this.last();
    }//GEN-LAST:event_btnLastActionPerformed

    private void cboSapXepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSapXepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSapXepActionPerformed

    private void btnTangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTangActionPerformed

    private void btnGiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGiamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGiamActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton35ActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblSanPham.getSelectedRow();
            this.edit();
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.chonAnh();
        }
    }//GEN-LAST:event_lblAnhMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        this.insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        this.clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        this.delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        this.prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        this.next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void tblHangSXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHangSXMouseClicked
        this.row = tblHangSX.rowAtPoint(evt.getPoint());
        HSXedit();
    }//GEN-LAST:event_tblHangSXMouseClicked

    private void btnThemHangSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemHangSXActionPerformed
        HSXinsert();
    }//GEN-LAST:event_btnThemHangSXActionPerformed

    private void btnCapNhapHangSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhapHangSXActionPerformed
        HSXupdate();
    }//GEN-LAST:event_btnCapNhapHangSXActionPerformed

    private void btnXoaHangSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHangSXActionPerformed
        HSXdelete();
    }//GEN-LAST:event_btnXoaHangSXActionPerformed

    private void btnLamMoiHangSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiHangSXActionPerformed
        HSXclear();
    }//GEN-LAST:event_btnLamMoiHangSXActionPerformed

    private void btnHSXFIRSTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHSXFIRSTActionPerformed
        HSXfirst();
    }//GEN-LAST:event_btnHSXFIRSTActionPerformed

    private void btnHSXPREVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHSXPREVActionPerformed
        HSXprev();
    }//GEN-LAST:event_btnHSXPREVActionPerformed

    private void btnHSXNEXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHSXNEXTActionPerformed
        HSXnext();
    }//GEN-LAST:event_btnHSXNEXTActionPerformed

    private void btnHSXLASTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHSXLASTActionPerformed
        HSXlast();
    }//GEN-LAST:event_btnHSXLASTActionPerformed

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
            java.util.logging.Logger.getLogger(JDialogSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogSanPham dialog = new JDialogSanPham(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCapNhapHangSX;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnGiam;
    private javax.swing.JButton btnHSXFIRST;
    private javax.swing.JButton btnHSXLAST;
    private javax.swing.JButton btnHSXNEXT;
    private javax.swing.JButton btnHSXPREV;
    private javax.swing.JButton btnLamMoiHangSX;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTang;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemHangSX;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaHangSX;
    private javax.swing.JComboBox<String> cboDanhMuc;
    private javax.swing.JComboBox<String> cboHangSX;
    private javax.swing.JComboBox<String> cboSapXep;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JTable tblHangSX;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextArea txtMoTaHang;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenHang;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTimKiemSP;
    // End of variables declaration//GEN-END:variables
}
