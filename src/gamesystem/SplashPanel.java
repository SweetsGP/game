package gamesystem;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.PanelController;

public class SplashPanel extends JPanel {
	
	private JLabel lNowLoading;
	
	public SplashPanel() {
		super();
		
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);
		
		lNowLoading = new JLabel("Now Loading...");
		lNowLoading.setBounds(600, 500, 150, 50);
		lNowLoading.setFont(new Font("PixelMplus10", Font.PLAIN, 18));
		this.add(lNowLoading);
		
		this.setVisible(false);
	}
	
	public void showPanel() {
		this.setVisible(true);
	}

}
