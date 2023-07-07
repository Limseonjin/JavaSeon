package seonjingame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class mains3 extends JPanel {
	JButton jb1, jb2;
	mains1 win;
	String img_url = "C:\\JavaSeonjin\\playGUI\\src\\img\\";

    public mains3(mains1 win) {
    	this.win = win;
    	 
    	this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    	JPanel xset = new JPanel();
    	xset.setLayout(new BoxLayout(xset,BoxLayout.X_AXIS));
    	
    	JLabel titleN = new JLabel();
    	titleN.setText("곰돌이 먹이 짝 맞추기");
    	add(titleN);
    	
    	ImageIcon gifIcon = new ImageIcon(img_url+"title2.gif");
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setPreferredSize(new Dimension(800, 500));
        
        add(gifLabel);
        add(xset);
        
		jb1 = new JButton("게임시작");
		jb2 = new JButton("끝내기");
		xset.add(jb1);
		xset.add(jb2);
		
		jb1.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				win.change("jp2");
			}
			
		});
		}

}

