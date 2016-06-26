package gamesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import Battle.TypeInfo;
import main.DataBase;
import main.Env;
import main.FilePath;
import main.Main;
import main.PanelController;

public class HasMonsterDetailPanel extends JPanel implements KeyListener {
	public static final int RUNNING_HMDP_LOOP = 0;
	public static final int EXIT_HMDP_LOOP = 1;
	
	public static int exitHMDPLoopFlag = RUNNING_HMDP_LOOP;

	private static PanelController controller;

	JLabel lMonsterName, lMonsterImg, lLv, lExp, lType, lHp, lState, lAttack, lDefense, lSpeed,
	lSkill1 = null, lSkill2 = null, lSkill3 = null, lSkill4 = null;
	JLabel[] lSkills = {lSkill1, lSkill2, lSkill3, lSkill4};
	JProgressBar pbHp, pbExp;
	JButton btnBack;
	
	private static int key_state = 0;

	public HasMonsterDetailPanel(PanelController pc) {
		controller = pc;

		this.setLayout(null);
		this.setSize(PanelController.PANEL_WIDTH, PanelController.PANEL_HEIGHT);

		this.setFocusable(true);
		this.addKeyListener(this);

		lMonsterName = new JLabel();
		lMonsterName.setBorder(new LineBorder(Color.cyan, 5, true));
		lMonsterName.setBackground(Color.white);
		lMonsterName.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lMonsterName.setBounds(10, 10, 250, 90);
		this.add(lMonsterName);

		lMonsterImg = new JLabel();
		lMonsterImg.setBorder(new LineBorder(Color.cyan, 5, true));
		lMonsterImg.setBackground(Color.white);
		lMonsterImg.setBounds(10, 100, 380, 190);
		this.add(lMonsterImg);
		
		lLv = new JLabel();
		lLv.setBorder(new LineBorder(Color.cyan, 5, true));
		lLv.setBackground(Color.white);
		lLv.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lLv.setBounds(400, 10, 90, 90);
		this.add(lLv);
		
		lExp = new JLabel();
		lExp.setBorder(new LineBorder(Color.cyan, 5, true));
		lExp.setBackground(Color.white);
		lExp.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lExp.setVerticalAlignment(JLabel.TOP);
		lExp.setBounds(490, 10, 290, 90);
		this.add(lExp);
		
		pbExp = new JProgressBar();
//		pbExp.setForeground(Color.green);
		pbExp.setBounds(510, 60, 250, 30);
		this.add(pbExp);
		
		lType = new JLabel();
		lType.setBorder(new LineBorder(Color.cyan, 5, true));
		lType.setBackground(Color.white);
		lType.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lType.setBounds(260, 10, 130, 90);
		this.add(lType);

		lHp = new JLabel();
		lHp.setBorder(new LineBorder(Color.cyan, 5, true));
		lHp.setBackground(Color.white);
		lHp.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lHp.setVerticalAlignment(JLabel.TOP);
		lHp.setBounds(10, 300, 240, 90);
		this.add(lHp);
		
		pbHp = new JProgressBar();
//		pbHp.setForeground(Color.green);
		pbHp.setBounds(30, 350, 200, 30);
		this.add(pbHp);

		lState = new JLabel();
		lState.setBorder(new LineBorder(Color.cyan, 5, true));
		lState.setBackground(Color.white);
		lState.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lState.setBounds(250, 300, 140, 90);
		this.add(lState);

		lAttack = new JLabel();
		lAttack.setBorder(new LineBorder(Color.cyan, 5, true));
		lAttack.setBackground(Color.white);
		lAttack.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lAttack.setBounds(10, 400, 190, 90);
		this.add(lAttack);

		lDefense = new JLabel();
		lDefense.setBorder(new LineBorder(Color.cyan, 5, true));
		lDefense.setBackground(Color.white);
		lDefense.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lDefense.setBounds(200, 400, 190, 90);
		this.add(lDefense);

		lSpeed = new JLabel();
		lSpeed.setBorder(new LineBorder(Color.cyan, 5, true));
		lSpeed.setBackground(Color.white);
		lSpeed.setFont(new Font("PixelMplus10", Font.PLAIN, 28));
		lSpeed.setBounds(10, 490, 190, 90);
		this.add(lSpeed);

		for (int i = 0; i < 4; i++) {
			lSkills[i] = new JLabel();
			lSkills[i].setBorder(new LineBorder(Color.cyan, 5, true));
			lSkills[i].setBackground(Color.white);
			lSkills[i].setFont(new Font("PixelMplus10", Font.PLAIN, 28));
			lSkills[i].setBounds(400, 110 + 90*i, 380, 90);
			this.add(lSkills[i]);
		}

		btnBack = new JButton("もどる");
		btnBack.setBounds(600, 500, 140, 60);
		btnBack.setFont(new Font("PixelMplus10", Font.PLAIN, 13));
		btnBack.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				exitHMDPLoopFlag = EXIT_HMDP_LOOP;
				requestFocus(false);
				setVisible(false);
				if (PanelController.state == PanelController.STATE_MAP) {
					controller.showMapPanel();
				} else if (PanelController.state == PanelController.STATE_BATTLE) {
					controller.showBattlemainPanel();
				}
			}
		});
		this.add(btnBack);

		this.setVisible(false);
	}

	/**
	 * てもちモンスター1体の詳細情報を表示 (各コンポーネントに情報をセット)
	 * @param hasMonsterInfo
	 */
	public void showDetailOfHasMonster(HashMap<String, String> hasMonsterInfo) {
		ArrayList<HashMap<String, String>> monsterBaseInfo = null;
		ArrayList<HashMap<String, String>> nextExpInfo = null;
		ArrayList<HashMap<String, String>> skillInfo = null;
		
		try {
			monsterBaseInfo = DataBase.wExecuteQuery("select * from testmonsters where monsterId = "
							+ hasMonsterInfo.get("monsterId"));
			nextExpInfo = DataBase.wExecuteQuery("select * from testexp where level = "
					+ Integer.toString(Integer.parseInt(hasMonsterInfo.get("level"))+1));
		} catch (SQLException sqle) {

		}
		
		// 名前
		lMonsterName.setText(hasMonsterInfo.get("name"));
		// 画像
		ImageIcon iconMonster = new ImageIcon(FilePath.monstersDirPath + monsterBaseInfo.get(0).get("img"));
		lMonsterImg.setIcon(iconMonster);
		// レベル
		lLv.setText("Lv:" + hasMonsterInfo.get("level"));
		// 経験値
		lExp.setText("EXP:" + hasMonsterInfo.get("exp") + "/" + nextExpInfo.get(0).get("exp"));
		pbExp.setValue((Integer.parseInt(hasMonsterInfo.get("exp")) * 100)
				/ Integer.parseInt(nextExpInfo.get(0).get("exp")));
		// タイプ/属性
		if (!monsterBaseInfo.get(0).get("type2").equals("0")) {
			lType.setText("<html>" + TypeInfo.typeName[Integer.parseInt(monsterBaseInfo.get(0).get("type1"))] + "<br />"
					+ TypeInfo.typeName[Integer.parseInt(monsterBaseInfo.get(0).get("type2"))] + "</html>");
		} else {
			lType.setText(TypeInfo.typeName[Integer.parseInt(monsterBaseInfo.get(0).get("type1"))]);
		}
		// HP
		lHp.setText("HP:" + hasMonsterInfo.get("hp") + "/" + Integer.parseInt(hasMonsterInfo.get("maxhp")));
		pbHp.setValue((Integer.parseInt(hasMonsterInfo.get("hp")) * 100)
				/ Integer.parseInt(hasMonsterInfo.get("maxhp")));
		// 状態異常
		if (!hasMonsterInfo.get("state").equals("")) {
			lState.setText(hasMonsterInfo.get("state"));
		}
		// 攻撃力
		lAttack.setText("こうげき:" + hasMonsterInfo.get("attack"));
		// 防御力
		lDefense.setText("ぼうぎょ:" + hasMonsterInfo.get("defense"));
		// すばやさ
		lSpeed.setText("すばやさ:" + hasMonsterInfo.get("speed"));
		// 技
		for (int i = 0; i < 4; i++) {
			if (!hasMonsterInfo.get("skill" + Integer.toString(i+1) + "Id").equals("")
										&& !hasMonsterInfo.get("skill" + Integer.toString(i+1) + "Point").equals("")) {
				try {
					skillInfo = DataBase.wExecuteQuery("select * from testskills where skillId = "
							+ hasMonsterInfo.get("skill" + Integer.toString(i+1) + "Id"));
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				}
				lSkills[i].setText(skillInfo.get(0).get("name") + Env.crlf
						+ "SP:" + hasMonsterInfo.get("skill" + Integer.toString(i+1) + "Point")
						+ "/" + skillInfo.get(0).get("sp"));
			}
		}

		this.setVisible(true);
		this.requestFocus(true);
	}
	
	public void loop() {
		exitHMDPLoopFlag = RUNNING_HMDP_LOOP;
		key_state = 0;
		
		while(true) {
			if (Main.exitFlag != Main.RUNNING || exitHMDPLoopFlag != RUNNING_HMDP_LOOP) {
				break;
			}
			
			switch (key_state) {
			case 7:  // SPACE
				btnBack.doClick();
				break;
			default:
				break;
			}
			key_state = 0;
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;
		
		/* キーコードの格納 */
		key = e.getKeyCode();

		switch(key) {
//		case KeyEvent.VK_UP:
//			key_state = 1;
//			break;
//		case KeyEvent.VK_DOWN:
//			key_state = 2;
//			break;
//		case KeyEvent.VK_LEFT:
//			key_state = 3;
//			break;
//		case KeyEvent.VK_RIGHT:
//			key_state = 4;
//			break;
//		case KeyEvent.VK_Z:
//			key_state = 5;
//			break;
//		case KeyEvent.VK_X:
//			key_state = 6;
//			break;
		case KeyEvent.VK_SPACE:
			key_state = 7;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}

