package itemhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gamesystem.MenuEvent;
import main.DataBase;
import main.Main;
import main.PanelController;

class BellOfBike extends ItemHandler {
	// 自転車のベル
	
	protected ItemHandlingAreaPanel ihap;
	
	protected JLabel lsound;
	protected JButton btnBack;
	
	BellOfBike(ItemHandlingAreaPanel p) {
		ihap = p;
		
		lsound = new JLabel("チリンチリーン♪");
		lsound.setBounds(10, 150, 380, 30);
		ihap.add(lsound);
		
		btnBack = new JButton("もどる");
		btnBack.setBounds(200, 530, 140, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ihap.requestFocus(false);
				ihap.setVisible(false);
			}
		});
		ihap.add(btnBack);
	}
	
	@Override
	int getItemFlag() {
		return itemFlag;
	}
	
	@Override
	void itemEffect() throws ItemHandlingFailedException {
		ihap.setVisible(true);
		ihap.requestFocus(true);
	}
	
	@Override
	void itemThrowAway() throws ItemHandlingFailedException {
		JOptionPane.showMessageDialog(null, "自転車のベルはチャンピオンになるためには必須のアイテムでありますからゆえ捨てることはできないでごいまするだっちゃ",
				Main.GAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

}


