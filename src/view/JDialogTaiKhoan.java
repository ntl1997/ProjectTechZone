/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package view;

import javax.swing.table.DefaultTableModel;
import Entity.TAIKHOAN;
import Dao.TaiKhoanDao;
import Utils.MsgBox;
import Utils.XDate;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import Dao.ChucVuDao;
import Entity.CHUCVU;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class JDialogTaiKhoan extends javax.swing.JDialog {
    private TaiKhoanDao tkd = new TaiKhoanDao();
    private ChucVuDao cvd = new ChucVuDao();
    int row = -1;
    /**
     * Creates new form NewJDialog
     */
    public JDialogTaiKhoan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null); // Đặt cửa sổ ở trung tâm màn hình
        this.filltableNV();
        this.fillcboCV();
        this.filltblCV();
    }
    
    
    // NHAN VIEN 
    void fillcboCV(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboVaiTroNV.getModel();
        model.removeAllElements();
        try {
            List<CHUCVU> list = cvd.selectAll();
            for(CHUCVU cv: list){
                model.addElement(cv.getTENCV());
            }
        } catch (Exception e) {
            System.out.println("Loi truy van chuc vu" + e);
        }
    }
    
    void filltableNV(){
        DefaultTableModel model = (DefaultTableModel) tblNV.getModel();
        model.setRowCount(0);
        try {
            for(TAIKHOAN tk: tkd.selectAll()){
                Object data[] = {tk.getID_TK(), tk.getTENDN(), tk.getTENNV(), tk.getEMAIL(), tk.getMATKHAU(), tk.getDIACHI(), tk.getDIENTHOAI()
                , tk.getNGAYSINH(), tk.getGIOITINH() == 1 ? "Nam": "Nữ", tk.getTRANGTHAI() == 1? "Hoạt động" : "Ngưng hoạt động"};
                model.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Loi nhap du lieu tai khoan" + e);
        }
    }
    
    public TAIKHOAN getForm(){
        TAIKHOAN tk = new TAIKHOAN();       
        tk.setTENDN(txtTenDNNV.getText());
        tk.setTENNV(txtTenNV.getText());
        tk.setEMAIL(txtEmailNV.getText());
        tk.setMATKHAU(txtMatKhau.getText());
        tk.setDIACHI(txtDiaChiNV.getText());
        tk.setDIENTHOAI(txtSDTNV.getText());
        tk.setNGAYSINH(XDate.toDate(txtNgaySinhNV.getText(), "yyyy/MM/dd"));
        
        if(cboVaiTroNV.getSelectedItem().equals("Nhân viên")){
            tk.setMACV(1);
        }else if(cboVaiTroNV.getSelectedItem().equals("Quản lý")){
            tk.setMACV(2);
        }else{
            tk.setMACV(3);
        }
                       
        if(rdoNam.isSelected()){
            tk.setGIOITINH(1);
        }else{
            tk.setGIOITINH(0);
        }
        if(rdoHD.isSelected()){
            tk.setTRANGTHAI(1);
        }else{
            tk.setTRANGTHAI(0);
        }
        return tk;
    }
    
    public void setForm(TAIKHOAN tk){
        txtTenNV.setText(tk.getTENNV());
        txtTenDNNV.setText(tk.getTENDN());
        txtEmailNV.setText(tk.getEMAIL());
        txtMatKhau.setText(tk.getMATKHAU());
        txtSDTNV.setText(tk.getDIENTHOAI());
        txtNgaySinhNV.setText(XDate.toString(tk.getNGAYSINH(), "yyyy/MM/dd"));
        txtDiaChiNV.setText(tk.getDIACHI());
        if(tk.getGIOITINH() == 1){
            rdoNam.setSelected(true);
        }else{
            rdoNu.setSelected(true);
        }
        if(tk.getTRANGTHAI() == 1){
            rdoHD.setSelected(true);
        }else{
            rdoNHD.setSelected(true);
        }
        
        if(tk.getMACV() == 1){
            cboVaiTroNV.setSelectedItem("Nhân viên");
        }else if(tk.getMACV() == 2){
            cboVaiTroNV.setSelectedItem("Quản lý");
        }else{
            cboVaiTroNV.setSelectedItem("Admin");
        }
    }
    
    public void edit(){
        try {
            Object matk2 = tblNV.getValueAt(this.row, 0);
            int matk = (int) matk2;
            TAIKHOAN tk = tkd.selectById(matk);
            if(tk != null){
                setForm(tk);            
                this.updateStatus();
            }
        } catch (Exception e) {
            System.out.println("Loi truy van du lieu" + e);
        }
    }
    
    public void updateStatus(){
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblNV.getRowCount() - 1);
            
        btnNVFirst.setEnabled(edit && !first);
        btnNVPrev.setEnabled(edit && !first);
        btnNVNext.setEnabled(edit && !last);
        btnNVLast.setEnabled(edit && !last);
    }
    
    public void first(){
        this.row = 0;
        this.edit();
    }
    
    public void prev(){
        if(this.row > 0){
            this.row --;
            this.edit();
        }
    }
    
    public void next(){
        if(this.row < tblNV.getRowCount() - 1){
            this.row ++;
            this.edit();
        }
    }
    
    public void last(){
        this.row = tblNV.getRowCount() - 1;
        this.edit();
    }
    
    public void clearForm(){
        TAIKHOAN tk = new TAIKHOAN();
//        this.setForm(tk);
        this.row = -1;
        this.updateStatus();
    }
    
    public void clear(){
        txtTenNV.setText("");
        txtTenDNNV.setText("");
        txtEmailNV.setText("");
        txtMatKhau.setText("");
        txtSDTNV.setText("");
        txtNgaySinhNV.setText("");
        txtDiaChiNV.setText("");
        rdoNam.setSelected(true);
        rdoHD.setSelected(true);
    }
    
    public void insert(){
        TAIKHOAN tk = getForm();
        try {
            tkd.insert(tk);
            this.filltableNV();
            this.clear();
            MsgBox.alert(this, "Them thanh cong");
        } catch (Exception e) {
            MsgBox.alert(this, "Them that bai");
            System.out.println("Them that bai" + e);
        }
    }
    
    public void update(){
        TAIKHOAN tk = this.getForm();
        try {
            int vitri = tblNV.getSelectedRow();
            String mtk2 = String.valueOf(tblNV.getValueAt(vitri, 0));
            int mtk = Integer.parseInt(mtk2);
            tk.setID_TK(mtk);            
            tkd.update(tk);
            this.filltableNV();
            this.clear();
            MsgBox.alert(this, "Sua thanh cong");
        } catch (Exception e) {
            MsgBox.alert(this, "Sua that bai");
            System.out.println("Sua that bai" + e);
        }
    }
    
    public void delete(){
        if(MsgBox.confirm(this, "Bạn muốn xóa nhân viên này?")){
            try {
                     int matk = (int)(tblNV.getValueAt(this.row, 0));
                     tkd.delete(matk);
                     this.filltableNV();
                     this.clear();
                     MsgBox.alert(this, "Xoa thanh cong");
                 } catch (Exception e) {
                     MsgBox.alert(this, "Xoa that bai");
                     System.out.println("Xóa thất bại" + e);
                 }
        }
    }
    
    //NHAN VIEN END
   
    // CHUC VU
    
    public void filltblCV(){
        DefaultTableModel model = (DefaultTableModel) tblCV.getModel();
        model.setRowCount(0);
        try {
            for(CHUCVU cv: cvd.selectAll()){
                Object data[] = {cv.getID_CV(), cv.getTENCV(), cv.getMOTA()};
                model.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("LOI NHAP DU LIEU CHUC VU: " + e);
        }
    }
    
    public CHUCVU getCVForm(){
        CHUCVU cv = new CHUCVU();
        cv.setTENCV(txtTenCV.getText());
        cv.setMOTA(traMoTaCV.getText());
        return cv;        
    }
    
    public void setCVForm(CHUCVU cv){
        txtTenCV.setText(cv.getTENCV());
        traMoTaCV.setText(cv.getMOTA());
    }
    
    
    public void CVedit(){
        try {
            Object macv2 = tblCV.getValueAt(this.row, 0);
            int macv = (int) macv2;
            CHUCVU cv = cvd.selectById(String.valueOf(macv));
            if(cv != null){
                setCVForm(cv);
            }
        } catch (Exception e) {
            System.out.println("Loi truy van du lieu" + e);
        }
    }
    
    public void CVclearForm(){
        CHUCVU cv = new CHUCVU();
        this.setCVForm(cv);
    }
    
    public void clearCV(){
        txtTenCV.setText("");
        traMoTaCV.setText("");
    }
    
    public void insertCV(){
        CHUCVU cv = this.getCVForm();
        try {
            cvd.insert(cv);
            this.filltblCV();
            this.clearCV();
            MsgBox.alert(this, "THEM CHUC VU THANH CONG");
        } catch (Exception e) {
            System.out.println("THEM CHUC VU THAT BAI" + e);
            MsgBox.alert(this, "THEM CHUC VU THAT BAI");
        }
    }
    
    public void updateCV(){
        CHUCVU cv = this.getCVForm();
        try {
            int vitri = tblCV.getSelectedRow();
            String mcv2 = String.valueOf(tblCV.getValueAt(vitri, 0));
            int mcv = Integer.parseInt(mcv2);
            cv.setID_CV(mcv);            
            cvd.update(cv);
            this.filltblCV();
            this.clearCV();
            MsgBox.alert(this, "SUA CHUC VU THANH CONG");
        } catch (Exception e) {
            System.out.println("SUA CHUC VU THAT BAI" + e);
            MsgBox.alert(this, "SUA CHUC VU THAT BAI");
        }
    }
    
    public void deleteCV(){
        if(MsgBox.confirm(this, "Bạn muốn xóa chức vụ này?")){
            try {
                     String macv = String.valueOf(tblCV.getValueAt(this.row, 0));
                     cvd.delete(macv);
                     this.filltblCV();
                     this.clearCV();
                     MsgBox.alert(this, "Xoa thanh cong");
                 } catch (Exception e) {
                     MsgBox.alert(this, "Xoa that bai");
                     System.out.println("Xóa thất bại" + e);
                 }
        }
    }
    // CHUC VU END
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jToolBar2 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        txtEmailNV = new javax.swing.JTextField();
        txtSDTNV = new javax.swing.JTextField();
        txtNgaySinhNV = new javax.swing.JTextField();
        txtDiaChiNV = new javax.swing.JTextField();
        cboVaiTroNV = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        btnNVFirst = new javax.swing.JButton();
        btnNVPrev = new javax.swing.JButton();
        btnNVNext = new javax.swing.JButton();
        btnNVLast = new javax.swing.JButton();
        btnThemNV = new javax.swing.JButton();
        btnCapNhapNV = new javax.swing.JButton();
        btnLamMoiNV = new javax.swing.JButton();
        btnXoaNV = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        rdoHD = new javax.swing.JRadioButton();
        rdoNHD = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNV = new javax.swing.JTable();
        txtMatKhau = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtTenDNNV = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel5 = new javax.swing.JPanel();
        btnThemCV = new javax.swing.JButton();
        btnCapNhatCV = new javax.swing.JButton();
        btnXoaCV = new javax.swing.JButton();
        btnClearCV = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCV = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtTenCV = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        traMoTaCV = new javax.swing.JTextArea();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));

        jTabbedPane1.setBackground(new java.awt.Color(255, 102, 51));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));

        jToolBar2.setRollover(true);

        jPanel3.setBackground(new java.awt.Color(102, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel1.setText("Tên nhân viên");

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel2.setText("Email");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel3.setText("Mật khẩu");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel4.setText("Số điện thoại");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel5.setText("Ngày sinh");

        txtTenNV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        txtEmailNV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtEmailNV.setMinimumSize(new java.awt.Dimension(64, 25));
        txtEmailNV.setPreferredSize(new java.awt.Dimension(25, 25));

        txtSDTNV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtSDTNV.setMinimumSize(new java.awt.Dimension(64, 25));

        txtNgaySinhNV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtNgaySinhNV.setMinimumSize(new java.awt.Dimension(64, 25));

        txtDiaChiNV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtDiaChiNV.setPreferredSize(new java.awt.Dimension(64, 25));
        txtDiaChiNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiaChiNVActionPerformed(evt);
            }
        });

        cboVaiTroNV.setBackground(new java.awt.Color(0, 153, 255));
        cboVaiTroNV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        cboVaiTroNV.setForeground(new java.awt.Color(255, 255, 255));
        cboVaiTroNV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "Quản lý" }));
        cboVaiTroNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVaiTroNVActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel7.setText("Địa chỉ");

        buttonGroup1.add(rdoNam);
        rdoNam.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");

        buttonGroup1.add(rdoNu);
        rdoNu.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        rdoNu.setText("Nữ");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel8.setText("Giới tính");

        btnNVFirst.setBackground(new java.awt.Color(255, 255, 51));
        btnNVFirst.setText("<<");
        btnNVFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNVFirstActionPerformed(evt);
            }
        });

        btnNVPrev.setBackground(new java.awt.Color(255, 255, 0));
        btnNVPrev.setText("<");
        btnNVPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNVPrevActionPerformed(evt);
            }
        });

        btnNVNext.setBackground(new java.awt.Color(255, 255, 0));
        btnNVNext.setText(">");
        btnNVNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNVNextActionPerformed(evt);
            }
        });

        btnNVLast.setBackground(new java.awt.Color(255, 255, 51));
        btnNVLast.setText(">>");
        btnNVLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNVLastActionPerformed(evt);
            }
        });

        btnThemNV.setBackground(new java.awt.Color(153, 204, 255));
        btnThemNV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnThemNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24_1.png"))); // NOI18N
        btnThemNV.setText("THÊM");
        btnThemNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemNV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNVActionPerformed(evt);
            }
        });

        btnCapNhapNV.setBackground(new java.awt.Color(153, 255, 204));
        btnCapNhapNV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnCapNhapNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/22_1.png"))); // NOI18N
        btnCapNhapNV.setText("Cập nhật");
        btnCapNhapNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapNhapNV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCapNhapNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhapNVActionPerformed(evt);
            }
        });

        btnLamMoiNV.setBackground(new java.awt.Color(153, 153, 153));
        btnLamMoiNV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnLamMoiNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/25_1.png"))); // NOI18N
        btnLamMoiNV.setText("Làm mới");
        btnLamMoiNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLamMoiNV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLamMoiNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiNVActionPerformed(evt);
            }
        });

        btnXoaNV.setBackground(new java.awt.Color(255, 102, 102));
        btnXoaNV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnXoaNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/14.png"))); // NOI18N
        btnXoaNV.setText("XÓA");
        btnXoaNV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXoaNV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNVActionPerformed(evt);
            }
        });

        jComboBox2.setBackground(new java.awt.Color(0, 153, 255));
        jComboBox2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jComboBox2.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp 1", "Sắp xếp 2" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 153, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Tăng");

        jButton2.setBackground(new java.awt.Color(51, 153, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Giảm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setText("Trạng thái");

        buttonGroup2.add(rdoHD);
        rdoHD.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        rdoHD.setSelected(true);
        rdoHD.setText("Hoạt động");

        buttonGroup2.add(rdoNHD);
        rdoNHD.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        rdoNHD.setText("Ngưng hoạt động");

        tblNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID_TK", "Tên DN", "Tên NV", "Email", "Matkhau", "Địa chỉ", "Điện thoại", "Ngày sinh", "Giới tính", "Trạng thái"
            }
        ));
        tblNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNVMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblNV);

        txtMatKhau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txtMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatKhauActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Tên DN");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTenNV, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmailNV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSDTNV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMatKhau, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtDiaChiNV, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cboVaiTroNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNgaySinhNV, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(jLabel16)
                                            .addComponent(txtTenDNNV))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnLamMoiNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnXoaNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnCapNhapNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnThemNV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(55, 55, 55))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoHD)
                                        .addGap(20, 20, 20)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdoNHD)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnNVFirst)
                        .addGap(18, 18, 18)
                        .addComponent(btnNVPrev)
                        .addGap(18, 18, 18)
                        .addComponent(btnNVNext)
                        .addGap(18, 18, 18)
                        .addComponent(btnNVLast))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnThemNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCapNhapNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoaNV))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNgaySinhNV, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel7)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEmailNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(txtDiaChiNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel16)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMatKhau)
                            .addComponent(txtTenDNNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSDTNV, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(cboVaiTroNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnLamMoiNV))
                .addGap(66, 66, 66)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu)
                    .addComponent(rdoHD)
                    .addComponent(rdoNHD)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNVFirst)
                    .addComponent(btnNVPrev)
                    .addComponent(btnNVNext)
                    .addComponent(btnNVLast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jToolBar2.add(jPanel2);

        jTabbedPane1.addTab("Nhân viên", jToolBar2);

        jToolBar1.setRollover(true);

        jPanel5.setBackground(new java.awt.Color(102, 204, 255));

        btnThemCV.setBackground(new java.awt.Color(153, 204, 255));
        btnThemCV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnThemCV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/24_1.png"))); // NOI18N
        btnThemCV.setText("THÊM");
        btnThemCV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThemCV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThemCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemCVActionPerformed(evt);
            }
        });

        btnCapNhatCV.setBackground(new java.awt.Color(153, 255, 204));
        btnCapNhatCV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnCapNhatCV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/22_1.png"))); // NOI18N
        btnCapNhatCV.setText("Cập nhật");
        btnCapNhatCV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCapNhatCV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCapNhatCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatCVActionPerformed(evt);
            }
        });

        btnXoaCV.setBackground(new java.awt.Color(255, 102, 102));
        btnXoaCV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnXoaCV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/14.png"))); // NOI18N
        btnXoaCV.setText("XÓA");
        btnXoaCV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXoaCV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoaCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCVActionPerformed(evt);
            }
        });

        btnClearCV.setBackground(new java.awt.Color(153, 153, 153));
        btnClearCV.setFont(new java.awt.Font("Segoe UI Semibold", 0, 10)); // NOI18N
        btnClearCV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/25_1.png"))); // NOI18N
        btnClearCV.setText("Làm mới");
        btnClearCV.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClearCV.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClearCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearCVActionPerformed(evt);
            }
        });

        jComboBox4.setBackground(new java.awt.Color(0, 153, 255));
        jComboBox4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp xếp 1", "Sắp xếp 2" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(51, 153, 255));
        jButton5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton5.setText("A-Z");

        jButton6.setBackground(new java.awt.Color(51, 153, 255));
        jButton6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jButton6.setText("A-Z");

        tblCV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID_CV", "TÊN CHỨC VỤ", "MÔ TẢ"
            }
        ));
        tblCV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCVMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblCV);

        jButton12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N
        jButton12.setText("search");

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel13.setText("Tên chức vụ");

        txtTenCV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel14.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel14.setText("Mô tả");

        traMoTaCV.setColumns(20);
        traMoTaCV.setRows(5);
        jScrollPane5.setViewportView(traMoTaCV);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 434, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(109, 109, 109)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnCapNhatCV)
                                        .addComponent(btnThemCV, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnXoaCV, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addComponent(btnClearCV, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtTenCV, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(56, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnThemCV)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnCapNhatCV)
                            .addComponent(jLabel13))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(txtTenCV, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnXoaCV)))
                        .addGap(12, 12, 12)
                        .addComponent(btnClearCV))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jToolBar1.add(jPanel5);

        jTabbedPane1.addTab("Chức vụ", jToolBar1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 817, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCapNhatCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatCVActionPerformed
        updateCV();
    }//GEN-LAST:event_btnCapNhatCVActionPerformed

    private void btnXoaCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaCVActionPerformed
        deleteCV();
    }//GEN-LAST:event_btnXoaCVActionPerformed

    private void btnClearCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearCVActionPerformed
        clearCV();
    }//GEN-LAST:event_btnClearCVActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void btnXoaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaNVActionPerformed
        delete();
    }//GEN-LAST:event_btnXoaNVActionPerformed

    private void btnLamMoiNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiNVActionPerformed
        clear();
    }//GEN-LAST:event_btnLamMoiNVActionPerformed

    private void btnCapNhapNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhapNVActionPerformed
        update();
    }//GEN-LAST:event_btnCapNhapNVActionPerformed

    private void btnNVLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNVLastActionPerformed
        last();
    }//GEN-LAST:event_btnNVLastActionPerformed

    private void cboVaiTroNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVaiTroNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboVaiTroNVActionPerformed

    private void txtDiaChiNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaChiNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiaChiNVActionPerformed

    private void txtMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatKhauActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatKhauActionPerformed

    private void btnThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNVActionPerformed
        insert();
    }//GEN-LAST:event_btnThemNVActionPerformed

    private void tblNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNVMouseClicked
        this.row = tblNV.rowAtPoint(evt.getPoint());
        edit();
    }//GEN-LAST:event_tblNVMouseClicked

    private void btnNVFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNVFirstActionPerformed
        first();
    }//GEN-LAST:event_btnNVFirstActionPerformed

    private void btnNVPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNVPrevActionPerformed
        prev();
    }//GEN-LAST:event_btnNVPrevActionPerformed

    private void btnNVNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNVNextActionPerformed
        next();
    }//GEN-LAST:event_btnNVNextActionPerformed

    private void btnThemCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemCVActionPerformed
        insertCV();
    }//GEN-LAST:event_btnThemCVActionPerformed

    private void tblCVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCVMouseClicked
        this.row = tblCV.rowAtPoint(evt.getPoint());
        CVedit();
    }//GEN-LAST:event_tblCVMouseClicked

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
            java.util.logging.Logger.getLogger(JDialogTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDialogTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDialogTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDialogTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JDialogTaiKhoan dialog = new JDialogTaiKhoan(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCapNhapNV;
    private javax.swing.JButton btnCapNhatCV;
    private javax.swing.JButton btnClearCV;
    private javax.swing.JButton btnLamMoiNV;
    private javax.swing.JButton btnNVFirst;
    private javax.swing.JButton btnNVLast;
    private javax.swing.JButton btnNVNext;
    private javax.swing.JButton btnNVPrev;
    private javax.swing.JButton btnThemCV;
    private javax.swing.JButton btnThemNV;
    private javax.swing.JButton btnXoaCV;
    private javax.swing.JButton btnXoaNV;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cboVaiTroNV;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JRadioButton rdoHD;
    private javax.swing.JRadioButton rdoNHD;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblCV;
    private javax.swing.JTable tblNV;
    private javax.swing.JTextArea traMoTaCV;
    private javax.swing.JTextField txtDiaChiNV;
    private javax.swing.JTextField txtEmailNV;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtNgaySinhNV;
    private javax.swing.JTextField txtSDTNV;
    private javax.swing.JTextField txtTenCV;
    private javax.swing.JTextField txtTenDNNV;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables
}
