import java.sql.*;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;

public class PhoneManger {
	static Connection con = dbconnect.makeCon();
	static PreparedStatement ps = null;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (con != null) {
			//insert();
			//delete();
			select();
			update();
			select();
		}
	}
	public static void update() {
		String key = sc.nextLine();
		
		ResultSet rs = null;
		String sql = "Select * from Person where phone = ?";
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, key);
			rs = ps.executeQuery(); 
			if (rs.next()) {
				String e = sc.nextLine();
				int a = sc.nextInt();
				String sql2 = "Update Person SET email=?,age=? where phone=?";
				ps=con.prepareStatement(sql2);
				ps.setString(1, e);
				ps.setInt(2, a);
				ps.setString(3, key);
				ps.executeUpdate();
			}
			} catch(SQLException e){
				e.printStackTrace();
			}
		
	}
	
	public static void delete() {
		String sql = "Select * from Person Where phone=?";
		String phone = sc.nextLine();
		ResultSet rs = null;
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, phone);
			rs = ps.executeQuery();
			if(rs != null) {
				String sql1 = "delete from Person where phone=?";
					ps= con.prepareStatement(sql1);
					ps.setString(1, phone);
					ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void select() {
		ResultSet rs = null;
		String sql = "Select * from Person";
		try {
			ps=con.prepareStatement(sql);
			rs = ps.executeQuery();
			System.out.println("========= Phone List ============");
			System.out.println("이름" + "\t"  + "전화번호"+ "\t\t"  +"이메일"+ "\t\t" + "나이");
			while (rs.next()) {
				System.out.print(rs.getString(1));
				System.out.print("\t");
				System.out.print(rs.getString(2));
				System.out.print("\t");
				System.out.print(rs.getString(3));
				System.out.print("\t");
				System.out.print(rs.getInt(4));
				System.out.print("\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void insert() {
		String sql = "insert into Person VALUES(?,?,?,?)";
		String name = sc.nextLine();
		String phone = sc.nextLine();
		String email = sc.nextLine();
		int age = sc.nextInt();
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setString(3, email);
			ps.setInt(4, age);
			int a = ps.executeUpdate();
			System.out.println(a);
			
		} catch(SQLException e){ 
			e.printStackTrace();
		}
		
	}

}
