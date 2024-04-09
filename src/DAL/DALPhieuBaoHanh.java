package DAL;

import DTO.DTOPhieuBaoHanh;
import java.util.ArrayList;
import java.sql.*;

public class DALPhieuBaoHanh {
    private Connection c;
    private PreparedStatement p = null;
    private Statement stm = null;
    public ArrayList <DTOPhieuBaoHanh> pbhList = new ArrayList<DTOPhieuBaoHanh>();
    
    public boolean open(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbUrl = "jdbc:sqlserver://localhost:1433;databaseName = QLBDT;encrypt=false";
            String usename = "sa";
            String password = "123456";
            c = DriverManager.getConnection(dbUrl,usename,password);
            return true;
        }catch(Exception ex){
            System.out.println(ex);
            return false;
        }
    }
    
    public void close(){
        try{
            if(c!=null){
                c.close();
            }
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }
    
    public ArrayList<DTOPhieuBaoHanh> getallpbhlist(){
        if(open()){
            try{
                pbhList.clear();
                String sql = "SELECT * FROM PHIEU_BAO_HANH";
                stm = c.createStatement();
                ResultSet rs = stm.executeQuery(sql);
                while(rs.next()){
                    DTOPhieuBaoHanh pbh = new DTOPhieuBaoHanh();
                    pbh.setMaBaoHanh(rs.getString("MA_BAO_HANH"));
                    pbh.setNgayLap(rs.getDate("NGAY_LAP"));
                    pbh.setNoiDung(rs.getString("NOI_DUNG"));
                    pbh.setChiPhi(rs.getFloat("CHI_PHI"));
                    pbhList.add(pbh);
                }
            }catch(SQLException ex){
                System.out.println(ex);
            }finally{
                close();
            }
        }
        return pbhList;
    }
    
    public boolean hasPBH(String MaBaoHanh){
        boolean result = false;
        if(open()){
            try{
                String sql = "SELECT * FROM PHIEU_BAO_HANH WHERE MA_BAO_HANH = ?";
                p = c.prepareStatement(sql);
                p.setString(1, MaBaoHanh);
                ResultSet rs = p.executeQuery();
                result = rs.next();
            }catch(SQLException ex){
                System.out.println(ex);
            }finally{
                close();
            }
        }
        return result;
    }
    
    public DTOPhieuBaoHanh timtheompbh(String MaBaoHanh){
        try{
            if(open()){
                String sql = "SELECT * FROM PHIEU_BAO_HANH WHERE MA_BAO_HANH = ?";
                p = c.prepareStatement(sql);
                p.setString(1, MaBaoHanh);
                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    Date ngayLap = rs.getDate("NGAY_LAP");
                    String NoiDung = rs.getString("NOI_DUNG");
                    float Gia = rs.getFloat("CHI_PHI");
                    DTOPhieuBaoHanh pbh = new DTOPhieuBaoHanh(MaBaoHanh,NoiDung,ngayLap,Gia);
                    return pbh;
                }
            }
        }catch(SQLException ex){
            System.out.println(ex);
        }finally{
            close();
        }
        return null;
    }
    
    public boolean themPBH(DTOPhieuBaoHanh pbh){
        boolean result = false;
        if(open()){
            try{
                String sql = "INSERT INTO PHIEU_BAO_HANH VALUES(?, ?, ?, ?)";
                p = c.prepareStatement(sql);
                p.setString(1,pbh.getMaBaoHanh());
                p.setDate(2, pbh.getNgayLap());
                p.setString(3, pbh.getNoiDung());
                p.setFloat(4, pbh.getChiPhi());
                if(p.executeUpdate() >= 1){
                    result = true;
                }
            }catch(SQLException ex){
                System.out.println(ex);
            }finally{
                close();
            }
        }
        return result;
    }
    
    public boolean xoaPBH(String MaBaoHanh){
        boolean result = false;
        if(open()){
            try{
                String sql = "DELETA FROM PHIEU_BAO_HANH WHERE MA_BAO_HANH = ?";
                p = c.prepareStatement(sql);
                p.executeUpdate();
                p.setString(1, MaBaoHanh);
                if(p.executeUpdate() >= 1){
                    result = true;
                }
            }catch(SQLException ex){
                System.out.println(ex);
            }finally{
                close();
            }
        }
        return result;
    }
    
    public boolean suaPBH(DTOPhieuBaoHanh pbh){
        boolean result = false;
        if(open()){
            try{
                String SQL = "UPDATE PHIEU_BAO_HANH SET  MA_BAO_HANH = ?, NGAY_LAP = ?, NOI_DUNG = ?, CHI_PHI = ? WHERE MA_BAO_HANH = ? ";
                p = c.prepareStatement(SQL);
                p.setString(1, pbh.getMaBaoHanh());
                p.setDate(2, pbh.getNgayLap());
                p.setString(3, pbh.getNoiDung());
                p.setFloat(4, pbh.getChiPhi());
                p.setString(5, pbh.getMaBaoHanh());
                if(p.executeUpdate() >= 1){
                    result = true;
                }
            }catch(SQLException ex){
                System.out.println(ex);
            }finally{
                close();
            }
        }
        return result;
    }
}
