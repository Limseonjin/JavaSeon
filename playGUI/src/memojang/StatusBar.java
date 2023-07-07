package memojang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

public class StatusBar extends JPanel {
	JPanel leftBar, rightBar, midBar;
	JLabel left,mid,right;
	JTextArea ta;
	public StatusBar(JTextArea ta) {
		this.ta = ta;
		
		left = new JLabel();
		mid = new JLabel();
		right = new JLabel();
		
		/*
		//날짜 생성 
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd aa HH:mm:ss");
		left.setText(sd.format(d).toString());
		
		//커서 위치값 가져와서 mid 레이블 표시
		int x=0,y=0;
		try {
			y =ta.getCaretPosition();
			x = ta.getLineOfOffset(y);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mid.setText("행 : "+x+"열 : "+y);
		
		//글짜 크기 비율 
		Font ft = ta.getFont();
		int rate = ft.getSize()/12* 100 ;
		right.setText(rate+"%");
		*/
	
		leftBar = new JPanel();
		leftBar.setBackground(new Color(204,204,204));
		midBar = new JPanel();
		midBar.setBackground(new Color(153,153,153));
		rightBar = new JPanel();
		rightBar.setBackground(new Color(102,102,102));
		
		
	
		this.setLayout(new BorderLayout());
		
		leftBar.add(left);
		midBar.add(mid);
		rightBar.add(right);
		
		this.setLayout(new GridLayout(1,3));
		this.add(leftBar);
		this.add(midBar);
		this.add(rightBar);
		ta.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent ce) {
				// TODO Auto-generated method stub
				int x=0, y=0;
				try {
					int cor = ta.getCaretPosition(); // 커서 y값(열) 위치 - 누적됨 +1
					x = ta.getLineOfOffset(cor);  // 커서의 x값
					int startcor = ta.getLineStartOffset(x); //x의 위치에서의 텍스트 개수만큼 누적됨
					y = cor - startcor;  //커서 위치 - 텍스트 개수
	
				} catch (BadLocationException e) {
					e.printStackTrace();
				} 
				
				mid.setText("행 : " + (x+1) + " 열 : " + (y+1));	
			}
			
		});
		
		Timer timer = new Timer(3000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}}); 
		timer.start();
	}
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    // 날짜를 생성해서 left 레일블에 표시
	    Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd aa HH:mm:ss");				
		left.setText(sd.format(d));
		
		// 커서의 위치값 가져와서 mid 레이블에 표시	
			
		
		// 글짜크기 비율값 계산해서 right 레이블에 표시
		Font ft = ta.getFont();
		double rate = ft.getSize()/12.0*100 ;
		right.setText(String.format("%.0f",rate)+"%");
	}
	
	

	
}
