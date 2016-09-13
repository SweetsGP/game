package Battle;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import gamesystem.HasItemsPanel;
import gamesystem.HasMonstersPanel;
import gamesystem.MessageBoard;
import main.DataBase;
import main.Main;
import main.PanelController;
import main.Sound;

public class Battlemain extends JPanel implements ActionListener{
	private static PanelController controller;

	JPanel body;
	MessageBoard messageboard;
	GameCanvas canvas = new GameCanvas();

	HasItemsPanel hip;
	HasMonstersPanel hmp;

	JButton button1 = new JButton();
	JButton button2 = new JButton();
	JButton button3 = new JButton();
	JButton button4 = new JButton();
	String monstername[] = {"ゴリチュウ","ヌマクロー"};
	String skill[] = {};
	String pskill[] = {"ひっかく","はかいこうせん","",""};
	int testiryoku[]={40,0,0,0};
	int testmeityu[]={100,100,0,0};
	int etestmeityu =90;
	int iryoku = 40;
	int damage;
	int i = 0;
	int end =0;
	int init_ehp;

	private int turnCount;  // turnCount % 2 が 0 => プレイヤーターン, 1 => 相手ターン

	public static boolean itemUsedFlag = false;
	public static boolean changeMonsterFlag = false;

	ArrayList<HashMap<String,String>> testname, testskill;

	Character hero;
	Character enemy;

	public Battlemain(PanelController c){
		controller = c;

		setLayout(null);
		setBackground(Color.gray);
		setSize(PanelController.PANEL_WIDTH,PanelController.PANEL_HEIGHT);

		setFocusable(true);

		try {
			testname = DataBase.wExecuteQuery("SELECT name FROM testhasmonsters WHERE orderNum = 1");
			testskill = DataBase.wExecuteQuery("SELECT * FROM testskills");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		String ename = testname.get(0).get("name");
		String mname = "カラ松";//(仮)本来はDBからもってくる
		init_ehp = 50;//(仮)本来はDBからもってくる
		enemy = new Character(10,7,50,ename,10);
		hero = new Character(20,7,50,mname,10);

		pskill[2] = testskill.get(0).get("name");
		pskill[3] = testskill.get(1).get("name");

		testiryoku[2] = Integer.parseInt(testskill.get(0).get("power"));
		testiryoku[3] = Integer.parseInt(testskill.get(1).get("power"));

		testmeityu[2] = Integer.parseInt(testskill.get(0).get("hitrate"));
		testmeityu[3] = Integer.parseInt(testskill.get(1).get("hitrate"));
		testmeityu[3] = 75;

		messageboard = new MessageBoard();
		messageboard.setLocation(0, 500);

		body = create_graphicboard();
		button1 = set_button("たたかう");
		button2 = set_button("どうぐ");
		button3 = set_button("てもち");
		button4 = set_button("にげる");
		button1.setBounds(600,500,100,50);
		button2.setBounds(700,500,100,50);
		button3.setBounds(600,550,100,50);
		button4.setBounds(700,550,100,50);
		canvas.ehp = enemy.hp;

		messageboard.showMessage("戦闘開始！");

		body.add(canvas);
		add(button1);
		add(button2);
		add(button3);
		add(button4);
		add(messageboard);
		add(body);

		hip = new HasItemsPanel(controller);
		hip.setLocation(300, 10);
		add(hip);

		hmp = new HasMonstersPanel(controller);
		hmp.setLocation(300, 10);
		add(hmp);

		turnCount = 0;

		setVisible(false);
	}

	public void showBattlemain() {
		this.requestFocus(true);
		this.setVisible(true);
	}

	public void loop(Sound s){
		/**
		 * 戦闘開始
		 */
		s.bgmRun(3);
		canvas.ehp = enemy.hp;
		wait(1);

		messageboard.setMessage(enemy.name + "　が　あらわれた!");
		wait(1);

		i = 0;
		end = 0;

		// 素早さ判定
		turnCount = 0;
		if (spdjudge(hero.spd, enemy.spd) == 2) {
			turnCount++;
		}

		do{
			if(Main.exitFlag != Main.RUNNING) {
				break;
			}

			// turnCountのオーバフロー防止
			if (turnCount == 100) {
				turnCount = 0;
			}

			// プレイヤーターンの場合は、行動選択肢提示
			if (turnCount % 2 == 0) {
				messageboard.setMessage(hero.name + "　は　どうする？");

				button1.setText("たたかう");
				button2.setText("どうぐ");
				button3.setText("てもち");
				button4.setText("にげる");
			}

			/**
			 * プレイヤーのターン
			 */
			MY_TURN:
			while(true){
				if(Main.exitFlag != Main.RUNNING) {
					break;
				}

				// 相手ターンの場合はプレイヤーターンのループ終了
				if (turnCount % 2 == 1) {
					break;
				}

				/**
				 * 行動選択
				 */
				do {
					if(i == 1){  // たたかう
						button1.setText(pskill[0]);
						button2.setText(pskill[1]);
						button3.setText(pskill[2]);
						button4.setText(pskill[3]);
						i = 0;

					}else if(i == 2) {  // どうぐ
						mainViewSetEnabled(false);

						hip.showHasItems();
						hip.loop();

						mainViewSetEnabled(true);
						i = 0;
						if(itemUsedFlag) {
							// アイテムを使用した場合は、自ターン終了
							itemUsedFlag = false;
							turnCount++;
							break MY_TURN;
						}else{
							break;
						}

					}else if(i == 3) {  // にげる
						end = escape(5,5);
						i = 0;
						turnCount++;
						break MY_TURN;

					}else if(i == 4){  // てもち
						mainViewSetEnabled(false);

						hmp.showHasMonsters();
						hmp.loop();

						mainViewSetEnabled(true);
						i = 0;
						if(changeMonsterFlag) {
							// アイテムを使用した場合は、自ターン終了
							changeMonsterFlag = false;
							turnCount++;
							break MY_TURN;
						}else{
							break;
						}
					}
				} while(false);

				/**
				 * 技選択
				 */
				if (10 < i && i < 15) {
					int j = i - 11;

					// 技(j+1)の処理
					if (j == 1) {
						damage = attack(hero.name,enemy.name,pskill[j],iryoku,enemy.hp,hero.atk,enemy.def);
							// ↑ のdamageは、攻撃時に点滅させるためだけにつけています、damage計算が目的ではありません。
						damage = 9999;  // 強制終了
					} else {
						damage = attack(hero.name,enemy.name,pskill[j],iryoku,enemy.hp,hero.atk,enemy.def);
					}

					if(hit(testmeityu[j])==0){
						wait(1);
						messageboard.setMessage(enemy.name + "　に　" + damage + "　の　ダメージ!");

						enemy.hp = enemy.hp - damage;

						canvas.repaint();
						if(enemy.hp<0){
							enemy.hp =0;
						}
					}else{
						wait(1);
						messageboard.setMessage("しかし、"+hero.name+"の　こうげきは　はずれた！");
					}

					canvas.ehp = enemy.hp;
					canvas.ehprate=enemy.hp/canvas.maxehp;
					System.out.println("残りHP："+ enemy.hp );
					i = 0;
					turnCount++;
					break;
				}

				try{
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/* turnCountのインクリメントが完了 */

			/**
			 * 終了判定/相手ターン処理
			 */
			if (end == 1 || end == 2) {
				// 終了
			} else if (enemy.hp <= 0) {
				// 敵戦闘不能
				end = 1;
				wait(1);
				messageboard.setMessage(enemy.name +"　は　たおれた！");
			} else {
				// 相手ターン処理
				wait(1);
				damage = attack(enemy.name,hero.name,"こうげき",40,hero.hp,enemy.atk,hero.def);
				if(hit(etestmeityu)==0){
					wait(1);
					messageboard.setMessage(hero.name + "　に　" + damage + "の　ダメージ！");
					hero.hp = hero.hp - damage;
					if(hero.hp<0){
						hero.hp =0;
					}
				}else{
					wait(1);
					messageboard.setMessage("しかし、"+enemy.name+"の　こうげきは　はずれた！");
				}

				canvas.mhp = hero.hp;
				canvas.mhprate=hero.hp/canvas.maxmhp;
				canvas.repaint();
				wait(1);
				if(hero.hp <=0){
					end = 1;
				} else {
					turnCount++;
				}
			}

		}while(end < 1);

		wait(1);

		/**
		 * 終了処理
		 */
		if(end ==1){
			messageboard.setMessage("30　の　けいけんち　を　てにいれた！");
			hero.exp = hero.exp + 30;
			canvas.repaint();
		}

		/*敵モンスターのパラメータリセット*/
		enemy.hp = init_ehp;
		canvas.ehprate = 1;

		s.bgmStop();

		requestFocus(false);
//		controller.showTitlePanel();
		controller.showMapPanel();
	}

	public void mainViewSetEnabled(boolean b) {
		button1.setEnabled(b);
		button2.setEnabled(b);
		button3.setEnabled(b);
		button4.setEnabled(b);
		body.setEnabled(b);
		canvas.setEnabled(b);
		messageboard.setEnabled(b);

		body.setVisible(b);
		canvas.setVisible(b);
		messageboard.setVisible(b);
	}

	public int spdjudge(int hspd,int espd){
		if(hspd > espd) return 1;
		else if(hspd < espd) return 2;
		else{
			double p = Math.random();
			if(p > 0.5){
				return 1;
			}else{
				return 2;
			}
		}
	}

	public int hit(int hitrate){
		double p = Math.random();
		p = p*100;
		System.out.println(p);
		if(hitrate > p)return(0);
		else return(1);
	}

	public int escape(int hlv, int elv){  //逃走確率計算
		double p = Math.random();
		int s=  (hlv - elv)/20;
		int r;
		p = p + s;
		if(p > 0.5){
			messageboard.setMessage("うまく にげきれた！");
			r =2;
		}else{
			messageboard.setMessage("にげられなかった！");
			r = 0;
		}
		return(r);
	}

	@Override
	public void actionPerformed(ActionEvent e){
		String cmdName=e.getActionCommand();

		if("たたかう".equals(cmdName)){
			i = 1;
		}
		else if("どうぐ".equals(cmdName)){
			i = 2;
		}
		else if("にげる".equals(cmdName)){
			i = 3;
		}
		else if("てもち".equals(cmdName)){
			i = 4;
		}

		else if(pskill[0].equals(cmdName)){
			i = 11;
		}
		else if(pskill[1].equals(cmdName)){
			i = 12;
		}
		else if(pskill[2].equals(cmdName)){
			i = 13;
		}
		else if(pskill[3].equals(cmdName)){
			i = 14;
		}
	}

	public int attack(String hname,String ename,String waza , int iryoku,int hp, int atk, int def){
		messageboard.setMessage(hname + "　の　" + waza+ "!");

		//wait(1);

		/*点滅メソッド*/
		BlinkTask task0 = new BlinkTask();//タイマータスクのインスタンス
		Timer timer0 = new Timer();//タイマーのインスタンス

		BlinkTask task1 = new BlinkTask();//タイマータスクのインスタンス
		Timer timer1 = new Timer();//タイマーのインスタンス

		//if(ename.equals(this.hero.name)){//自分がダメージを受ける
		if(turnCount % 2 == 1){//相手ターン
			task0.set(canvas.getGraphics(), canvas.me, 0);
			timer0.schedule(task0, 0, 100);
		}else{//相手がダメージを受ける
			task1.set(canvas.getGraphics(), canvas.en, 1);
			timer1.schedule(task1, 0, 100);
		}


		int damage = Damage(atk,def,iryoku);

		return damage;
	}

	public int waza(Object obj){
		if(obj == button1){
			return 0;
		}
		else if(obj == button2){
			return 1;
		}
		else if(obj == button3){
			return 2;
		}else{
			return 3;
		}
	}

	public int Damage(int str,int def,int iryoku){
		int r;

		r = str - def;
		r =r * iryoku / 100;
		if(r<0)r=0;

		return(r);
	}

	public JPanel create_graphicboard(){
		JPanel m = new JPanel();
		m.setSize(800, 500);
		m.setBounds(0,0,800,500);
		m.setBackground(Color.white);
		return m;
	}

	public JButton set_button(String s){
		JButton b = new JButton();
		b.setBackground(Color.WHITE);
		LineBorder border = new LineBorder(Color.black,2,true);
		b.setBorder(border);
		b.setSize(100,50);
		b.setFont(new Font("PixelMplus12",Font.PLAIN,13));
		b.setForeground(Color.black);
		b.setHorizontalAlignment(JLabel.CENTER);
		b.setVerticalAlignment(JLabel.CENTER);
		b.setText(s);
		b.addActionListener(this);
		return b;
	}

	static void wait(int second){
		try{
			Thread.sleep(second * 1000);
		}catch(Exception e)  {
			System.out.println(e.getMessage());
		}
	}
}
