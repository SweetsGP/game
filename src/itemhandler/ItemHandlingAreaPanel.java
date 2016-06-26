package itemhandler;

import javax.swing.JPanel;

import main.PanelController;

public class ItemHandlingAreaPanel extends JPanel {
	/* package_private */ PanelController controller;  // 各アイテムハンドラからアクセス可
	
	public ItemHandlingAreaPanel(PanelController pc) {
		controller = pc;
		
		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH / 2, PanelController.PANEL_HEIGHT);

		this.setFocusable(true);
		
		this.setVisible(false);
	}
	
	public ItemHandlingAreaPanel(PanelController pc, int width, int height) {
		controller = pc;
		
		this.setLayout(null);
		this.setSize(width, height);

		this.setFocusable(true);
		
		this.setVisible(false);
	}
	
	/**
	 * このパネル領域上のすべてのコンポーネントを除去
	 */
	public void clear() {
		this.removeAll();
	}

}
