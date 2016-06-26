package itemhandler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import gamesystem.ControlBox;
import gamesystem.HasMonstersPanel;
import gamesystem.MenuEvent;
import main.PanelController;

public class TargetMonstersPanel extends HasMonstersPanel {
	private int selectedTarget = 0;

	public TargetMonstersPanel(PanelController c) {
		super();
		
		this.hasCb = false;
		
		this.setLayout(null);
		this.setSize(300, 480);
		this.setBackground(Color.white);
		this.setBorder(new LineBorder(Color.black, 2, true));
		
		this.setFocusable(true);
		
		this.setVisible(false);
	}
	
	@Override
	public void onClickBtnMonster(ActionEvent e) {
		for(int i = 0; i < 6; i++) {
			if (e.getSource() == btnMonsters[i]) {
				selectedTarget = i + 1;
				break;
			}
		}
	}
	
	public int getSelectedMonster() {
		return selectedTarget;
	}
	
	public void setSelectedMonster(int select) {
		selectedTarget = select;
	}

}
