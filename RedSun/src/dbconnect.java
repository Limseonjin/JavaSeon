import java.sql.*;

public class dbconnect {
	public static Connection makeCon(){
		// TODO Auto-generated method stub
		String url = "jdbc:mysql://localhost:3306/app?serverTimezone=Asia/Seoul";
		String user = "root";
		String password = "1234";
		
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
						
		}  catch (Exception e) {
			e.printStackTrace();return con;
		}
		return con;
	}

}
