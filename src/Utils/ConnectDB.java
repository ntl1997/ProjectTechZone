/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.*;

/**
 *
 * @author luand
 */
public class ConnectDB {
    public static Connection con = null;
    public static Connection getConnect() {
        String strDbUrl = "jdbc:sqlserver://localhost:1436; databaseName=TECHZONE1;user=sa;password=1Secure*Password1;"
                + "encrypt=true;trustServerCertificate=true";
        try {
            con = DriverManager.getConnection(strDbUrl);
            System.out.println("Ket Noi Thanh Cong");
        } catch (SQLException e) {
            System.out.println("Connect Loi: " + e);
        }
        return con;
    }
//    public static void main(String[] args) {
//        ConnectDB.getConnect();
//    }
}
