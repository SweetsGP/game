package gamesystem;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

import main.PanelController;

import javax.swing.JLabel;
import javax.swing.JButton;

/**
 * 設定画面クラス
 * @author kitayamahideya
 *
 */
public class SettingsPanel extends JPanel {
	private PanelController controller;
	private JLabel pSettings;
	private JButton btnA,btnB,btnC, btnBack;
	
	/**
	 * コンストラクタ
	 * @param pc 画面遷移管理インスタンス
	 */
	public SettingsPanel(PanelController pc) {
		controller = pc;
		
		this.setName("Title");
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH,PanelController.PANEL_HEIGHT);

		pSettings = new JLabel("Settings");
		pSettings.setBounds(100, 5, 400, 40);
		pSettings.setFont(new Font("PixelMplus10-Regular", Font.PLAIN, 30));
		this.add(pSettings);

		btnA = new JButton("A");
		btnA.setBounds(20, 50, 150, 40);
		btnA.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){

			}
		});
		this.add(btnA);
		
		btnB = new JButton("B");
		btnB.setBounds(20, 100, 150, 40);
		btnB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){

			}
		});
		this.add(btnB);
		
		btnC = new JButton("C");
		btnC.setBounds(200, 50, 150, 40);
		btnC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){

			}
		});
		this.add(btnC);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(200, 100, 150, 40);
		btnBack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				controller.showTitlePanel();
			}
		});
		this.add(btnBack);
	}

}
