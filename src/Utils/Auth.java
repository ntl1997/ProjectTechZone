/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import Entity.TAIKHOAN;

public class Auth {
    public static TAIKHOAN user = null;
    public static void clear(){
        Auth.user=null;
    }
    public static boolean isLogin(){
        return Auth.user != null;
    }
//    public static int isManager(){
//        return Auth.isLogin() && user.isTr();
//    }
}

