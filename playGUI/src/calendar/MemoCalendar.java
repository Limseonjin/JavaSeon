package calendar;
// originally uploaded to http://blog.naver.com/azure0777

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class MemoCalendar extends CalendarDataManager{ // CalendarDataManager의 GUI + 메모기능 + 시계
	// 창 구성요소와 배치도
	JFrame mainFrame;
	ImageIcon icon = new ImageIcon (Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
	
	JPanel calOpPanel,calPanel,infoPanel,memoPanel, frameBottomPanel,memoSubPanel;
	JButton todayBut, lYearBut, lMonBut, nMonBut, nYearBut,saveBut,delBut,clearBut;
	JLabel todayLab,curMMYYYYLab,infoClock,selectedDate;
	
	JTextArea memoArea;//x
	
	JScrollPane memoAreaSP; 
	JTable jt;
	DefaultTableModel dtm;
	
	
	ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();

	JButton weekDaysName[];
	JButton dateButs[][] = new JButton[6][7];
	listenForDateButs lForDateButs = new listenForDateButs(); 
	JLabel bottomInfo = new JLabel("Welcome to Memo Calendar!");
	
	String del_key ="";
	//상수, 메세지
	final String WEEK_DAY_NAME[] = { "SUN", "MON", "TUE", "WED", "THR", "FRI", "SAT" };
	final String title = "메모 달력 ver 1.0";
	final String SaveButMsg1 = "를 MemoData폴더에 저장하였습니다.";
	final String SaveButMsg2 = "메모를 먼저 작성해 주세요.";
	final String SaveButMsg3 = "<html><font color=red>ERROR : 파일 쓰기 실패</html>";
	final String DelButMsg1 = "메모를 삭제하였습니다.";
	final String DelButMsg2 = "작성되지 않았거나 이미 삭제된 memo입니다.";
	final String DelButMsg3 = "<html><font color=red>ERROR : 파일 삭제 실패</html>";
	final String ClrButMsg1 = "입력된 메모를 비웠습니다.";

	
	public MemoCalendar(){ //구성요소 순으로 정렬되어 있음. 각 판넬 사이에 빈줄로 구별
		
		mainFrame = new JFrame(title);
		mainFrame.setSize(800,500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setIconImage(icon.getImage());
		try{
			UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//LookAndFeel Windows 스타일 적용
			SwingUtilities.updateComponentTreeUI(mainFrame) ;
		}catch(Exception e){
			bottomInfo.setText("ERROR : LookAndFeel setting failed");
		}
		
		calOpPanel = new JPanel();
		todayBut = new JButton("Today");
		todayBut.setToolTipText("Today");
		todayBut.addActionListener(lForCalOpButtons);
		todayLab = new JLabel(today.get(Calendar.MONTH)+1+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR));
		
		lYearBut = new JButton("<<");
		lYearBut.setToolTipText("Previous Year");
		lYearBut.addActionListener(lForCalOpButtons);
		
		lMonBut = new JButton("<");
		lMonBut.setToolTipText("Previous Month");
		lMonBut.addActionListener(lForCalOpButtons);
		
		curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
		
		nMonBut = new JButton(">");
		nMonBut.setToolTipText("Next Month");
		nMonBut.addActionListener(lForCalOpButtons);
		
		nYearBut = new JButton(">>");
		nYearBut.setToolTipText("Next Year");
		nYearBut.addActionListener(lForCalOpButtons);
		
		calOpPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints calOpGC = new GridBagConstraints();
			calOpGC.gridx = 1;
			calOpGC.gridy = 1;
			calOpGC.gridwidth = 2;
			calOpGC.gridheight = 1;
			calOpGC.weightx = 1;
			calOpGC.weighty = 1;
			calOpGC.insets = new Insets(5,5,0,0);
			calOpGC.anchor = GridBagConstraints.WEST;
			calOpGC.fill = GridBagConstraints.NONE;
		calOpPanel.add(todayBut,calOpGC);
			calOpGC.gridwidth = 3;
			calOpGC.gridx = 2;
			calOpGC.gridy = 1;
		calOpPanel.add(todayLab,calOpGC);
			calOpGC.anchor = GridBagConstraints.CENTER;
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 1;
			calOpGC.gridy = 2;
		calOpPanel.add(lYearBut,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 2;
			calOpGC.gridy = 2;
		calOpPanel.add(lMonBut,calOpGC);
			calOpGC.gridwidth = 2;
			calOpGC.gridx = 3;
			calOpGC.gridy = 2;
		calOpPanel.add(curMMYYYYLab,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 5;
			calOpGC.gridy = 2;
		calOpPanel.add(nMonBut,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 6;
			calOpGC.gridy = 2;
		calOpPanel.add(nYearBut,calOpGC);
		
		calPanel=new JPanel();
		weekDaysName = new JButton[7];
		for(int i=0 ; i<CAL_WIDTH ; i++){
			weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
			weekDaysName[i].setBorderPainted(false);
			weekDaysName[i].setContentAreaFilled(false);
			weekDaysName[i].setForeground(Color.WHITE);
			
			if(i == 0)
				weekDaysName[i].setBackground(new Color(200, 50, 50));
			else if (i == 6) 
				weekDaysName[i].setBackground(new Color(50, 100, 200));
			else
				weekDaysName[i].setBackground(new Color(150, 150, 150));
			
			weekDaysName[i].setOpaque(true);
			weekDaysName[i].setFocusPainted(false);
			calPanel.add(weekDaysName[i]);
		}
		for(int i=0 ; i<CAL_HEIGHT ; i++){
			for(int j=0 ; j<CAL_WIDTH ; j++){
				dateButs[i][j]=new JButton();
				dateButs[i][j].setBorderPainted(false);
				dateButs[i][j].setContentAreaFilled(false);
				dateButs[i][j].setBackground(Color.WHITE);
				dateButs[i][j].setOpaque(true);
				dateButs[i][j].addActionListener(lForDateButs);
				calPanel.add(dateButs[i][j]);
			}
		}
		calPanel.setLayout(new GridLayout(0,7,2,2));
		calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		showCal(); // 달력을 표시
						
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoClock = new JLabel("", SwingConstants.RIGHT);
		infoClock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		infoPanel.add(infoClock, BorderLayout.NORTH);
		selectedDate = new JLabel("<Html><font size=3>"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR)+"&nbsp;(Today)</html>", SwingConstants.LEFT);
		selectedDate.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
						
		memoPanel=new JPanel();
		memoPanel.setBorder(BorderFactory.createTitledBorder("Memo"));
	
		//DB연결을 위한 테이블 구조 변경 
		Object[] title = {"아이디","메모"};
		Object[][] value = new Object[0][2];
		dtm = new DefaultTableModel(value,title);
		jt = new JTable(dtm);
		//TableColumnModel cM = jt.getColumnModel();
		//cM.getColumn(0).setPreferredWidth(50);
		jt.getColumn("아이디").setPreferredWidth(50);
		jt.getColumn("메모").setPreferredWidth(200);
		jt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int row = jt.getSelectedRow();
				del_key = jt.getValueAt(row,0).toString();
				
			}	
		});
		
		memoAreaSP = new JScrollPane(jt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		readMemo();
		
		memoSubPanel=new JPanel();
		saveBut = new JButton("Add"); //일정 추가 부분  
		saveBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String dd =calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon;
				String memo = JOptionPane.showInputDialog(null, "새로운 일정을 입력하세요");
				
				if(!memo.equals("")) {
					DBConnect dc = new DBConnect();
					dc.insert(dd, memo);	
				}
					
				showCal();
				readMemo();
			}					
		});
		delBut = new JButton("Delete");
		delBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {				
				if(!del_key.equals("")){
					DBConnect dc = new DBConnect();
					dc.delete(Integer.parseInt(del_key));	
					showCal();
					readMemo();
					bottomInfo.setText(DelButMsg1);
				}
				else 
					bottomInfo.setText(DelButMsg2);					
			}					
		});
		
		
		clearBut = new JButton("Clear");
		clearBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				dtm.setRowCount(0);
				bottomInfo.setText(ClrButMsg1);
			}
		});
		memoSubPanel.add(saveBut);
		memoSubPanel.add(delBut);
		memoSubPanel.add(clearBut);
		memoPanel.setLayout(new BorderLayout());
		memoPanel.add(selectedDate, BorderLayout.NORTH);
		memoPanel.add(memoAreaSP,BorderLayout.CENTER);
		memoPanel.add(memoSubPanel,BorderLayout.SOUTH);

		//calOpPanel, calPanel을  frameSubPanelWest에 배치
		JPanel frameSubPanelWest = new JPanel();
		Dimension calOpPanelSize = calOpPanel.getPreferredSize();
		calOpPanelSize.height = 90;
		calOpPanel.setPreferredSize(calOpPanelSize);
		frameSubPanelWest.setLayout(new BorderLayout());
		frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
		frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

		//infoPanel, memoPanel을  frameSubPanelEast에 배치
		JPanel frameSubPanelEast = new JPanel();
		Dimension infoPanelSize=infoPanel.getPreferredSize();
		infoPanelSize.height = 65;
		infoPanel.setPreferredSize(infoPanelSize);
		frameSubPanelEast.setLayout(new BorderLayout());
		frameSubPanelEast.add(infoPanel,BorderLayout.NORTH);
		frameSubPanelEast.add(memoPanel,BorderLayout.CENTER);

		Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
		frameSubPanelWestSize.width = 500;
		frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);
		
		//뒤늦게 추가된 bottom Panel..
		frameBottomPanel = new JPanel();
		frameBottomPanel.add(bottomInfo);
		
		//frame에 전부 배치
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
		mainFrame.add(frameSubPanelEast, BorderLayout.CENTER);
		mainFrame.add(frameBottomPanel, BorderLayout.SOUTH);
		mainFrame.setVisible(true);

		focusToday(); //현재 날짜에 focus를 줌 (mainFrame.setVisible(true) 이후에 배치해야함)
		
		//Thread 작동(시계, bottomMsg 일정시간후 삭제)
		ThreadConrol threadCnl = new ThreadConrol();
		threadCnl.start();	
	}
	
	private void focusToday(){
		if(today.get(Calendar.DAY_OF_WEEK) == 1)
			dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
		else
			dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
	}
	
	private void readMemo(){
		try{
			
			String key = calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon;
			DBConnect dc = new DBConnect();
			ResultSet rs = dc.select(key);
			
			if (rs != null) {
				dtm.setRowCount(0);
				String[] info = new String[3];
				while (rs.next()) {
					info[0] = Integer.toString(rs.getInt("id"));
					info[1] = rs.getString("day_content");
					dtm.addRow(info);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void showCal(){
		DBConnect dc = new DBConnect();
		ResultSet rs = dc.select();
		
		for(int i=0;i<CAL_HEIGHT;i++){
			for(int j=0;j<CAL_WIDTH;j++){
				String fontColor="black";
				
				if(j==0) fontColor="red";
				else if(j==6) fontColor="blue";
				
				if(calDates[i][j] != 0) {
					String key = calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDates[i][j]<10?"0":"")+calDates[i][j];
					try {
						rs.beforeFirst();
						while(rs.next()) {
							if(rs.getString(1).equals(key)){
								dateButs[i][j].setText("<html><b><i><font color="+fontColor+">"+calDates[i][j]+"</font></i></b></html>");
								break;
							}
							else dateButs[i][j].setText("<html><font color="+fontColor+">"+calDates[i][j]+"</font></html>");
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				JLabel todayMark = new JLabel("<html><font color=green>*</html>");
				dateButs[i][j].removeAll();
				if(calMonth == today.get(Calendar.MONTH) &&
						calYear == today.get(Calendar.YEAR) &&
						calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
					dateButs[i][j].add(todayMark);
					dateButs[i][j].setToolTipText("Today");
				}
				
				if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
				else dateButs[i][j].setVisible(true);
			}
		}
	}
	private class ListenForCalOpButtons implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == todayBut){
				setToday();
				lForDateButs.actionPerformed(e);
				focusToday();
			}
			else if(e.getSource() == lYearBut) moveMonth(-12);
			else if(e.getSource() == lMonBut) moveMonth(-1);
			else if(e.getSource() == nMonBut) moveMonth(1);
			else if(e.getSource() == nYearBut) moveMonth(12);
			
			curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
			showCal();
		}
	}
	private class listenForDateButs implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int k=0,l=0;
			for(int i=0 ; i<CAL_HEIGHT ; i++){
				for(int j=0 ; j<CAL_WIDTH ; j++){
					if(e.getSource() == dateButs[i][j]){ 
						k=i;
						l=j;
					}
				}
			}
	
			if(!(k ==0 && l == 0)) calDayOfMon = calDates[k][l]; //today버튼을 눌렀을때도 이 actionPerformed함수가 실행되기 때문에 넣은 부분

			cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
			
			String dDayString = new String();
			int dDay=((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
			if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) 
					&& (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
					&& (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today"; 
			else if(dDay >=0) dDayString = "D-"+(dDay+1);
			else if(dDay < 0) dDayString = "D+"+(dDay)*(-1);
			
			selectedDate.setText("<Html><font size=3>"+(calMonth+1)+"/"+calDayOfMon+"/"+calYear+"&nbsp;("+dDayString+")</html>");
			
			readMemo();
		}
	}
	
	private class ThreadConrol extends Thread{
		public void run(){
			boolean msgCntFlag = false;
			int num = 0;
			String curStr = new String();
			while(true){
				try{
					today = Calendar.getInstance();
					String amPm = (today.get(Calendar.AM_PM)==0?"AM":"PM");
					String hour;
							if(today.get(Calendar.HOUR) == 0) hour = "12"; 
							else if(today.get(Calendar.HOUR) == 12) hour = " 0";
							else hour = (today.get(Calendar.HOUR)<10?" ":"")+today.get(Calendar.HOUR);
					String min = (today.get(Calendar.MINUTE)<10?"0":"")+today.get(Calendar.MINUTE);
					String sec = (today.get(Calendar.SECOND)<10?"0":"")+today.get(Calendar.SECOND);
					infoClock.setText(amPm+" "+hour+":"+min+":"+sec);

					sleep(1000);
					String infoStr = bottomInfo.getText();
					
					if(infoStr != " " && (msgCntFlag == false || curStr != infoStr)){
						num = 5;
						msgCntFlag = true;
						curStr = infoStr;
					}
					else if(infoStr != " " && msgCntFlag == true){
						if(num > 0) num--;
						else{
							msgCntFlag = false;
							bottomInfo.setText(" ");
						}
					}		
				}
				catch(InterruptedException e){
					System.out.println("Thread:Error");
				}
			}
		}
	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MemoCalendar();
			}
		});
	}
}