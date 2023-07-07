
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HelloSwing extends JFrame implements ActionListener {
	JTextField tf1;
	JTextField[] in_tf;
	JTable table;
	DefaultTableModel dtm;
	Connection con = null;
	PreparedStatement ps = null;
	
	
	public HelloSwing() {
		this.setTitle("난 프레임으로 탄생한 HelloSwing 객체입니다.");
		/*this.setSize(500,500);
		this.setLocation(500, 300);
		Container c = this.getContentPane();
		c.setBackground(Color.blue);*/
		this.setBounds(700, 300, 700, 700);
		this.setLayout(null);
		
		JPanel p1 = new JPanel(); //FlowLayout
		JPanel p2 = new JPanel();
		JPanel insert = new JPanel();
		insert.setSize(300,500);
		
		p2.setBackground(Color.red);
		p1.setBackground(Color.orange);
		insert.setBackground(Color.green);
		
		Object[] title = {"이름","전화번호","이메일","나이"};
		Object[][] value = new Object[0][4];
		dtm = new DefaultTableModel(value,title);
		
		table= new JTable(dtm);
		JScrollPane p3 = new JScrollPane(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JButton b1 = new JButton("전체조회");
		JButton b2 = new JButton("추가");
		JButton b3 = new JButton("검색");
		JButton b4 = new JButton("수정");
		JButton b5 = new JButton("삭제");
		
		b1.addActionListener(this);b3.addActionListener(this);b5.addActionListener(this);
		b2.addActionListener(this);b4.addActionListener(this);
		

		p1.add(b1);p1.add(b2);p1.add(b3);p1.add(b4);p1.add(b5);
		
		/*this.add(b1,BorderLayout.NORTH); 
		this.add(b2,BorderLayout.EAST);
		this.add(b3,BorderLayout.WEST);
		this.add(b4,BorderLayout.SOUTH);
		this.add(b5);*/
		JLabel ft1_la1 = new JLabel ("검색/삭제할 이름 : ");
		tf1=new JTextField(20);
		p2.add(ft1_la1);
		p2.add(tf1);
		
		JLabel[] in_la = new JLabel[4];
		in_la[0] = new JLabel ("이름 : ");
		in_la[1] = new JLabel ("전화번호 : ");
		in_la[2] = new JLabel ("이메일 : ");
		in_la[3] = new JLabel ("나이 : ");
		in_tf = new JTextField[4];
		
		for (int i = 0; i < in_tf.length; i++) {
		    in_tf[i] = new JTextField(20); // 각 요소마다 새로운 JTextField 객체 생성
		   insert.add(in_la[i]); insert.add(in_tf[i]); 
		}
		
		p1.setBounds(10, 10, 400, 40);
		p2.setBounds(10, 60, 400, 40);
		insert.setBounds(10, 110, 500, 60);
		p3.setBounds(10,180,670,400);
		this.add(p1); //버튼 패널 
		this.add(p2);
		this.add(insert); //정보 추가 패널
		this.add(p3); //스크롤 패널 
	
		this.setVisible(true);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				for (int i = 0; i < table.getColumnCount(); i++) { 
					String n = (String) table.getValueAt(row, i);
					in_tf[i].setText(n);
				}
				tf1.setText((String)table.getValueAt(row,0));
			}
		});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HelloSwing();

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		
		if (cmd.equals("전체조회")) {
			display(1);
		}
		
		else if(cmd.equals("추가")) {
			if (!in_tf[0].getText().equals("")&&!in_tf[1].getText().equals("")) {
				insert();
				display(1);
				}
			}
		else if(cmd.equals("검색")) {
			if(!tf1.getText().equals("")){
				display(2);
			}
		}
		
		else if(cmd.equals("삭제")) {
			if(!tf1.getText().equals(""))
				delete();
			display(1);
		}
		else if(cmd.equals("수정")) {
			if (!in_tf[1].getText().equals(""))
				update();
			display(1);
		}
	}
	
	public void update() {
		String sql = "Select * from Person Where phone=?";
		ResultSet rs = null;
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, tf1.getText());
			rs = ps.executeQuery();
			if(!rs.next() && !in_tf[0].getText().equals("")) {
				sql = "Update Person SET name=?,email=?,age=? WHERE phone=?";
				ps= con.prepareStatement(sql);
				
				if  (in_tf[3].getText().equals("")&&in_tf[2].getText().equals("")) {
					ps.setString(1, in_tf[0].getText());
					ps.setString(2, null);
					ps.setString(3, null);
					ps.setString(4, in_tf[1].getText());
					}
				else if (in_tf[2].getText().equals("")) {
					ps.setString(1, in_tf[0].getText());
					ps.setString(2, null);
					ps.setInt(3, Integer.parseInt(in_tf[3].getText()));
					ps.setString(4, in_tf[1].getText());
					}
					
				else if (in_tf[3].getText().equals("")) {
					ps.setString(1, in_tf[0].getText());
					ps.setString(2, in_tf[2].getText());
					ps.setString(3, null);
					ps.setString(4, in_tf[1].getText());
				}
				
				else {
					ps.setString(1, in_tf[0].getText());
					ps.setString(2, in_tf[2].getText());
					ps.setInt(3, Integer.parseInt(in_tf[3].getText()));
					ps.setString(4, in_tf[1].getText());
				}
				ps.executeUpdate();
			}
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void delete() {
		String sql = "Select * from Person Where name=?";
		ResultSet rs = null;
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, tf1.getText());
			rs = ps.executeQuery();
			if(rs != null) {
				sql = "delete from Person where name=?";
				ps= con.prepareStatement(sql);
				ps.setString(1, tf1.getText());
				ps.executeUpdate();
		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ResultSet search() {
		dtm.setRowCount(0); //= dtm.setNumRows(0);
		con=makeCon();
		ResultSet rs = null;
		String sql = "Select * from Person where name = ?";
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, tf1.getText());
			rs = ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rs;
		}
		

	}
	
	public void display(int key) {
		dtm.setRowCount(0); //= dtm.setNumRows(0);
		con=makeCon();
		ResultSet rs = null;
		if (key == 1)
			rs = select();
		else 
			rs = search();
		try {
			String info[] = new String[4];
			while(rs.next()) {
				info[0] = rs.getString(1);
				info[1] = rs.getString(2);
				info[2] = rs.getString(3);
				info[3] = rs.getString(4);
				dtm.addRow(info);
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	public void insert() {
		con = makeCon();
		ResultSet rs = null;
		try {
			String sql = "Select * from Person where phone=?";
			ps=con.prepareStatement(sql);
			ps.setString(1, in_tf[1].getText());
			rs = ps.executeQuery();
			if (!rs.next()) {
				if(!in_tf[3].getText().equals("")) {
					sql = "insert into Person VALUES(?,?,?,?)";
				}else {
					sql = "insert into Person(name, phone, email) VALUES(?,?,?)";
				}
				ps=con.prepareStatement(sql);
				ps.setString(1, in_tf[0].getText());
				ps.setString(2, in_tf[1].getText());
				ps.setString(3, in_tf[2].getText());
				if(!in_tf[3].getText().equals(""))
					ps.setInt(4, Integer.parseInt(in_tf[3].getText()));
				int a = ps.executeUpdate();
				
			}
		} catch(SQLException e){ 
			e.printStackTrace();
		}
		
	}
	
	public ResultSet select() {
		ResultSet rs = null;
		String sql = "Select * from Person";
		try {
			ps=con.prepareStatement(sql);
			rs = ps.executeQuery();
			return rs;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rs;
		}
		
	}
	public Connection makeCon(){
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



	
	
	//클래스 끝
}
