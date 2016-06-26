package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.PanelController;

/**
 * ある項目選択時の次の操作の選択肢を提供します
 * @author kitayamahideya
 *
 */
public class ControlBox extends JPanel {
	private static final int CONTROL_NO_SELECTED = -1;
	private static final int CONTROL_BACK = -2;
	
	private PanelController controller;
	
	JButton btnBack;
	JButton btnControls[];
	
	private int numOfControls;
	
	private int lastSelected;
	
	/**
	 * コンストラクタ
	 * @param c
	 * @param numOfBtn 項目数
	 * @throws IllegalArgumentException numOfBtn < 1 のとき
	 */
	public ControlBox(PanelController c, int numOfBtn) throws IllegalArgumentException {
		if (numOfBtn < 1) {
			throw new IllegalArgumentException();
		}
		
		controller = c;
		numOfControls = numOfBtn;
		lastSelected = CONTROL_NO_SELECTED;
		
		this.setLayout(null);
		this.setSize(200, 60 + 60*numOfControls);
		this.setBackground(Color.white);
		this.setBorder(new LineBorder(Color.black, 2, true));
		
		btnControls = new JButton[numOfControls];
		for (int i=0; i<numOfControls; i++) {
			btnControls[i] = new JButton();
			btnControls[i].setFont(new Font("PixelMplus10",Font.PLAIN,13));
			btnControls[i].setBorderPainted(false);
			btnControls[i].setBounds(10, 10 + 60*i, 180, 50);
			btnControls[i].addActionListener(new ActionListener(){
				private int iter;
				
				public ActionListener setParam(int j) {
					this.iter = j;
					return this;
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					lastSelected = this.iter;
				}
			}.setParam(i));
			this.add(btnControls[i]);
		}
		
		btnBack = new JButton("もどる");
		btnBack.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBack.setBounds(10, 10 + 60*numOfControls, 180, 50);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lastSelected = CONTROL_BACK;
//				requestFocus(false);
//				setVisible(false);
			}
		});
		this.add(btnBack);
		
		this.setVisible(false);
	}
	
	/**
	 * 各項目名を設定する
	 * @param controlNames controlNames[0] => btnControls[0]の名前
	 * @throws IllegalArgumentException contorlNamesがnullのとき
	 *   もしくは contorlNamesのサイズがnumOfControlsより小さいとき
	 */
	public void setControlNames(String[] controlNames) throws IllegalArgumentException {
		if (Objects.equals(controlNames, null) || controlNames.length < numOfControls) {
			throw new IllegalArgumentException();
		}
		
		for (int i=0; i<numOfControls; i++) {
			btnControls[i].setText(controlNames[i]);
		}
	}
	
	/**
	 * コントロールボックス内の全ボタンの有効化/無効化
	 * @param b true -> 有効化 / false -> 無効化
	 */
	public void setEnabledInner(boolean b) {
		for (int i=0; i<numOfControls; i++) {
			btnControls[i].setEnabled(b);
		}
		
		btnBack.setEnabled(b);
	}
	
	/**
	 * 与えられた番号の項目位置にカーソル表示をセットする
	 * @param cursorLocation カーソル表示を設置したい項目の位置番号
	 */
	public void setCursor(int cursorLocation) {
		for (int i = 0; i < numOfControls; i++) {
			if (i == cursorLocation) {
				btnControls[i].setBorderPainted(true);
				btnControls[i].setBorder(new LineBorder(Color.BLUE, 2, true));
			} else {
				btnControls[i].setBorderPainted(false);
			}
		}
		this.repaint();
	}
	
	/**
	 * 最後に選択した項目の番号を取得する
	 * @return 選択した項目の番号(btnControlsのインデックス)を返す
	 */
	public int getSource() {
		int r = lastSelected;
		lastSelected = CONTROL_NO_SELECTED;
		return r;
	}
	
	/**
	 * 引数で与えられた番号の項目を選択した処理(クリック)を行う
	 * @param btn クリックしたとしたい項目の番号
	 */
	public void emurateClick(int btn) {
		if (btn < numOfControls) {
			btnControls[btn].doClick();
		}
	}
}
