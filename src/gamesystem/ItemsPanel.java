package gamesystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import gameutil.DBAccess;
import main.PanelController;

public class ItemsPanel extends BasePanel{
	
	public static final int ITEM_NO_SELECTED = -1;
	
	protected DBAccess dba;
	
	protected JPanel pItemList;
	protected JScrollPane scrollItemList;
	protected JButton btnBack;

	protected JButton[] btnItems = null;
	protected int mNumOfItems;
	
	protected int mSelectedItem;
	
	public ItemsPanel() {
		super();
		
		dba = new DBAccess();
		
		mNumOfItems = 0;
		mSelectedItem = ITEM_NO_SELECTED;
		
		this.setSize(300, 540);
		this.setBackground(Color.white);
		this.setBorder(new LineBorder(Color.black, 2, true));
		
		// アイテム一覧表示領域 サイズ可変
		pItemList = new JPanel();
		pItemList.setLayout(null);
		pItemList.setPreferredSize(new Dimension(280, 450));
		pItemList.setOpaque(false);
		scrollItemList = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollItemList.setViewportView(pItemList);
		scrollItemList.setBounds(5, 5, 290, 460);
		scrollItemList.setVisible(false);
		this.add(scrollItemList);

		// もどるボタン
		btnBack = new JButton("もどる");
		btnBack.setFont(new Font("PixelMplus10",Font.PLAIN,13));
		btnBack.setBounds(150, 470, 140, 60);
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mLoopState = LOOP_EXIT;
			}
		});
		this.add(btnBack);
		
		this.setVisible(false);
	}
	
	public ItemsPanel(PanelController pc) {
		this();
		
		this.mController = pc;
	}
	
	/**
	 * パネルの可視化
	 */
	@Override
	public void showPanel() {
		this.setVisible(true);
		scrollItemList.setVisible(true);
		this.requestFocus(true);
	}
	
	// ==================================================
	//  ループ内処理
	// ==================================================
	/**
	 * キー入力受付前の定期処理
	 */
	@Override
	protected void periodicOpBeforeInput() {
		if (mSelectedItem != ITEM_NO_SELECTED) {
			actionItemSelected(mSelectedItem);
			mSelectedItem = ITEM_NO_SELECTED;
		}
	}

	/**
	 * 上矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyUp() {
		moveCursor(CURSOR_INDEX_MINUS);
	}

	/**
	 * 下矢印キー入力受付時の処理
	 */
	@Override
	protected void onPressKeyDown() {
		moveCursor(CURSOR_INDEX_PLUS);
	}

	/**
	 * Zキー入力受付時の処理
	 */
	@Override
	protected void onPressKeyZ() {
		clickOnCursor();
	}

	/**
	 * スペースキー入力受付時の処理
	 */
	@Override
	protected void onPressKeySpace() {
		btnBack.doClick();
	}

	/**
	 * キー入力受付後の定期処理
	 */
	@Override
	protected void periodicOpAfterInput() {
		// no operation
	}
	
	// ==================================================
	//  アイテム選択時処理
	// ==================================================
	/**
	 * アイテム選択時の処理
	 * @param selectedItemIndex 選択されたアイテムのインデックス
	 */
	public void actionItemSelected(int selectedItemIndex) {
		// no operation
	}

}
