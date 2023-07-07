package calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnect {
	Connection con = null;
	PreparedStatement ps = null;
	
	public Connection makeCon(){
		// TODO Auto-generated method stub
		String url = "jdbc:mysql://localhost:3306/app?serverTimezone=Asia/Seoul";
		String user = "root";
		String password = "1234";
		
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
			return con;
						
		}  catch (Exception e) {
			e.printStackTrace();return con;
		}
	}
	/////////////////////////////////////////////////////////////////////////////
	public ResultSet select(String key) {
		ResultSet rs = null;
		con = makeCon();
		
		String sql = "Select * from dayday where day_num = ?";
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, key);
			rs = ps.executeQuery();
			return rs;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rs;
		}

	}
	/////////////////////////////////////////////////////////////////
	public ResultSet select() {
		ResultSet rs = null;
		con = makeCon();
		
		String sql = "Select Distinct day_num from dayday";
		try {
			ps=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			return rs;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rs;
		}

	}
	////////////////////////////////////////////////////////////////////
	public void insert(String dd, String memo) {
		con = makeCon();
		
		String sql = "insert into dayday values(null, ?, ?)";
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, dd);
			ps.setString(2, memo);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void delete (int key) {
		con = makeCon();
		String sql = "delete from dayday where id = ?";
		try {
			ps=con.prepareStatement(sql);
			ps.setInt(1, key);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		

}

