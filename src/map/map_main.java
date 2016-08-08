package map;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Battle.Battlemain;
import gamesystem.HasItemsPanel;
import gamesystem.HasMonstersPanel;
import gamesystem.MessageBoard;
import gamesystem.ProductsListPanel;
import main.FilePath;
import main.Main;
import main.PanelController;
import main.Sound;
import npc.NpcLoader;

import java.awt.Color;
import java.awt.Point;

public class map_main extends JPanel implements KeyListener{
	// ループ終了フラグ定数
	public static final int RUNNING_MAP_LOOP = 0;
	public static final int EXIT_MAP_LOOP = 1;
	// ループ終了判定フラグ -> EXIT_MAP_LOOPで各ループを強制的に離脱
	public static int exitMapLoopFlag = RUNNING_MAP_LOOP;

	// 画面遷移管理
	private static PanelController controller;

	JPanel p,p2;
	MessageBoard message_board;
	JLabel button1;
	Point b_point;
	ImageIcon icon;
	
	public Battlemain bp_map;

	static int key_state = 0;
	static int status = 0;

	public int x,y,x_n,y_n;		//current pixel (x:width, y:height)
	public int a,b;		//current point address (a:width, b:height)
	public int map_element;
	public int map_change = 0;
	public int mes_send = 0;
	public String[] message_text;
	public int mes_key = 0;
	public int action_key = 0;
	public int[][] map_info;
	public int[][] npc_ex = {{0,0,0,0},{0,0,0,0}};
	public int npc_units = 0;
	public int npc_tlk = 0;
	public int player_vct = 0;
	public int encounter = 0;

	static map_data md;

	npc npcObjects[];
	Thread t[];
	
	NpcLoader testNpc;

	// メニュー
	public MenuPanel menuPanel;
	public HasMonstersPanel hasMonstersPanel;
	public HasItemsPanel hasItemsPanel;
	public ProductsListPanel productsListPanel;

	public map_main() {}

	public map_main(PanelController c){
		controller = c;

		md = new map_data();

		/* map creating (init)*/
		this.setSize(800,620);
		this.setLayout(null);
		
		this.setFocusable(true);

		p = create_map();
		button1 = create_button(0);
		message_board = new MessageBoard(700, 100);
		message_board.setLocation(50, 500);

		this.addKeyListener(this);

		p.add(button1);
		p.add(message_board);

		/* point address getting */
		Point b_point = button1.getLocation();
		x = b_point.x;
		y = b_point.y;
		a = 0;
		b = 0;
		mes_key = 1;

		npcObjects = new npc[2];
		for(int i = 0 ; i < 2 ; i++){
			npcObjects[i] = null;
		}

		t = new Thread[2];

		// てもちモンスター表示パネル
		hasMonstersPanel = new HasMonstersPanel(controller);
		hasMonstersPanel.setLocation(180, 50);

		// 所持アイテム一覧表示パネル
		hasItemsPanel = new HasItemsPanel(controller);
		hasItemsPanel.setLocation(180, 50);
		
		// 商品一覧表示パネル
		productsListPanel = new ProductsListPanel(controller);
		productsListPanel.setLocation(180, 50);

		/* メニュー */
		menuPanel = new MenuPanel(controller, hasMonstersPanel, hasItemsPanel, productsListPanel);
		menuPanel.setLocation(500, 0);

		p.add(menuPanel);
		p.add(hasMonstersPanel);
		p.add(hasItemsPanel);
		p.add(productsListPanel);

		p.setVisible(false);
		p.setBounds(0, 0, 800, 600);
		this.add(p);
		this.setVisible(false);
	}

	public void loop(Sound s){
		// ループ終了フラグ
		exitMapLoopFlag = RUNNING_MAP_LOOP;

		npc_units = 0;

		this.setVisible(true);
		this.requestFocus(true);
		p.setVisible(true);
		key_state = 0;

		s.bgmRun(0);

		try{
			if(status == 0){
				npc_units = 0;

				this.setVisible(true);
				p.setVisible(true);
				key_state = 0;

				message();
				
				for(int i = 0 ; i < 2 ; i++){
					if(Objects.equals(npcObjects[i], null)) {
						npcObjects[i] = null;
					}

					npcObjects[i] = new npc(i);
					npc_units++;
					
					// test code
					if (i == 1) {
//						testNpc = new NpcLoader("Merchant");
//						testNpc.create(1);
						testNpc = new NpcLoader("Inn");
						testNpc.create(2);
					}

					t[i] = new Thread(npcObjects[i]);
					t[i].start();
				}
			}
			
			/* object moving treatment */
			while(true){
				if (Main.exitFlag != Main.RUNNING || exitMapLoopFlag != RUNNING_MAP_LOOP) {
					break;
				}

				if(a == 11 && b == 0){
					map_change = 1;
					status = 1;
					mes_key = 2;

					p.getGraphics();

					message();
					move_button(1);

					this.add(p);

					b_point = button1.getLocation();
					x = b_point.x;
					y = b_point.y;
					a = 11;
					b = 15;

					key_state = 0;

					map_to_map();

					this.setVisible(true);
				}else{
					if(encounter >= md.Max_enc ){
						action_key = -1;
						message();
						
						status = 3;
						button1.setVisible(false);
						p.setVisible(false);
						
						s.bgmStop();
						controller.showBattlemainPanel();
						bp_map = (Battlemain )controller.getPanelInstance(PanelController.BATTLE);
						bp_map.loop(s);

						p.setVisible(true);
						button1.setVisible(true);
						this.requestFocus(true);
						
						s.bgmRun(0);

						status = 0;
						key_state = 0;
						action_key = 0;
						status = 0;
						encounter = 0;
					}
				}

				if(key_state == 0){	//no treatment

				}else if(key_state == 1){	//up
					icon = new ImageIcon(FilePath.mapDirPath + "up.png");
					player_vct = 1;
					button1.setIcon(icon);
					if((a-1 != -1) && (map_point(map_info[a-1][b]) != 0) ){
						npc_check();
					}
					key_state = 0;
				}else if(key_state == 2){	//down
					icon = new ImageIcon(FilePath.mapDirPath + "down.png");
					player_vct = 2;
					button1.setIcon(icon);
					if((a+1 != 12) && (map_point(map_info[a+1][b]) != 0) ){
						npc_check();
					}
					key_state = 0;
				}else if(key_state == 3){	//left
					icon = new ImageIcon(FilePath.mapDirPath + "left.png");
					player_vct = 3;
					button1.setIcon(icon);
					if((b-1 != -1) && (map_point(map_info[a][b-1]) != 0) ){
						npc_check();
					}
					key_state = 0;
				}else if(key_state == 4){	//right
					icon = new ImageIcon(FilePath.mapDirPath + "right.png");
					player_vct = 4;
					button1.setIcon(icon);
					if((b+1 != 16) && (map_point(map_info[a][b+1]) != 0) ){
						npc_check();
					}
					key_state = 0;
				}else if(key_state == 5){	//Z action
					status = 2;
					action();
					status = 0;
					key_state = 0;
				}else if(key_state == 6){	//Battle 
					key_state = 0;
					//not developed yet
				}else if(key_state == 7){	//SPACE action (Menu)
					menuPanel.setMenuType(MenuPanel.DEFAULT_MENU);
					pushSpaceKey();
					key_state = 0;
				}

				/* waiting time */
				Thread.sleep(10);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		for(int i = 0 ; i < 2 ; i++){
			npcObjects[i].shutdown();
		}

		s.bgmStop();

		p.setVisible(false);
		this.setVisible(false);
	}
	
	public void pushSpaceKey() {
		status = 2;
		button1.setVisible(false);
		menuPanel.showMenu();
		button1.setVisible(true);
		status = 0;
	}

	/* getting map_point address */

	public JPanel create_map(){

		JPanel p = new JPanel() {	//background making
			@Override
			public void paintComponent(Graphics g){
				int i,j;

				Graphics2D g2 = (Graphics2D)g;
				BufferedImage readImage = null;

				if(map_change == 0){
					map_info = md.mp1;
				}else{
					map_info = md.mp2;
				}
				try{
					for(j=0;j<=11;j++){
						for(i=0;i<=15;i++){
							map_element = map_info[j][i];
							//System.out.println(map_element);
							switch(map_element){
							case 1:		//field
								readImage = ImageIO.read(new File(FilePath.mapDirPath + "field.png"));
								g2.drawImage(readImage, null, i*50, j*50);
								break;
							case 2:		//river
								readImage = ImageIO.read(new File(FilePath.mapDirPath + "tile.png"));
								g2.drawImage(readImage, null, i*50, j*50);
								break;
							}
							readImage = null;
						}
					}
				}catch(Exception e){

				}
			}
		};
		p.setLayout(null);

		/* object making */
		return p;
	}

	public JLabel create_button(int change){
		JLabel button1 = new JLabel(new ImageIcon(FilePath.mapDirPath + "down.png"));
		if(change == 0){
			button1.setBounds(12, 12, 25, 25);
		}else{
			button1.setBounds(764, 564, 25, 25);
		}

		return button1;
	}

	public void move_button(int change){
		if(change == 0){
			button1.setBounds(12, 12, 25, 25);
		}else{
			button1.setBounds(764, 564, 25, 25);
		}
	}

	public int map_point (int map_info){
		switch(map_info){
		case 1:
			return 1;
		case 2:
			return 0;
		}
		/* no reach */
		return 1;
	}

	public void hide_panel(JPanel hide){
		hide.setEnabled(false);
		hide.setVisible(false);
	}

	public void emerge_panel(JPanel emerge){
		emerge.setEnabled(true);
		emerge.setVisible(true);
	}

	public void motion(int vector){
		int i = vector;
		int move_stop = 25;
		int rand = (int)(Math.random() * 5);
		try{
			switch(i){
			case 1:
				while(move_stop != 0 ){
					y = y - 2;
					button1.setBounds(x, y, 25, 25);
					p.repaint();
					move_stop--;
					Thread.sleep(15);
				}
				a--;
				break;
			case 2:
				while(move_stop != 0 ){
					y = y + 2;
					button1.setBounds(x, y, 25, 25);
					p.repaint();
					move_stop--;
					Thread.sleep(15);
				}
				a++;
				break;
			case 3:
				while(move_stop != 0 ){
					x = x - 2;
					button1.setBounds(x, y, 25, 25);
					p.repaint();
					move_stop--;
					Thread.sleep(15);
				}
				b--;
				break;
			case 4:
				while(move_stop != 0 ){
					x = x + 2;
					button1.setBounds(x, y, 25, 25);
					p.repaint();
					move_stop--;
					Thread.sleep(15);
				}
				b++;
				break;
			}
			b_point = button1.getLocation();
			if(a == 11 && b == 0){}else{
				encounter += rand;
			}
		}catch(InterruptedException e){}
	}

	public void npc_check(){
		int j;
		switch(key_state){
		case 1:
			j = 0;
			while(j < npc_units){
				if((npc_ex[j][1] != b || a - npc_ex[j][0] != 1) && j == npc_units - 1){
					motion(1);
				}
				j++;
			}
			break;
		case 2:
			j = 0;
			while(j < npc_units){
				if((npc_ex[j][1] != b || npc_ex[j][0] - a != 1) && j == npc_units - 1){
					motion(2);
				}
				j++;
			}
			break;
		case 3:
			j = 0;
			while(j < npc_units){
				if((npc_ex[j][0] != a || b - npc_ex[j][1]  != 1) && j == npc_units - 1){
					motion(3);
				}
				j++;
			}
			break;
		case 4:
			j = 0;
			while(j < npc_units){
				if((npc_ex[j][0] != a || npc_ex[j][1] - b != 1) && j == npc_units - 1){
					motion(4);
				}
				j++;
			}

			break;
		}

	}

	public void g_message(){
		switch(mes_key){
		case 1:
			mes_send = md.message_long[1];
			message_text = md.message_text_1;
			break;
		case 2:
			mes_send = md.message_long[2];
			message_text = md.message_text_2;
			break;
		default:
			break;
		}
	}

	public void a_message(){
		switch(action_key){
		case 1:
			mes_send = md.action_long[2];
			if(npc_tlk == 0){
				message_text = md.action_n_text_1;
			}else{
				message_text = md.action_n_text_2;
			}
			break;
		case 2:
			mes_send = md.action_long[2];
			if(npc_tlk == 0){
				message_text = md.action_n_text_1;
			}else{
				message_text = md.action_n_text_2;
			}
			break;
		case 3:
			mes_send = md.action_long[2];
			if(npc_tlk == 0){
				message_text = md.action_n_text_1;
			}else{
				message_text = md.action_n_text_2;
			}
			break;
		case 4:
			mes_send = md.action_long[2];
			if(npc_tlk == 0){
				message_text = md.action_n_text_1;
			}else{
				message_text = md.action_n_text_2;
			}
			break;
		case 8:
			mes_send = md.action_long[1];
			message_text = md.action_text_1;
			break;
		default:
			break;
		}
	}

	public void e_message(){
		switch(mes_key){
		case 1:
			mes_send = md.encounter_long[1];
			message_text = md.encounter_text_1;
			break;
		case 2:
			mes_send = md.encounter_long[1];
			message_text = md.encounter_text_1;
			break;
		default:
			break;
		}
	}

	public void message(){
		int i = 1;

		if(action_key > 0){
			a_message();
		}
		else if(action_key == -1){
			e_message();
		}else{
			g_message();
		}
		button1.setVisible(false);
		try{
			if (npc_tlk == 1) {
				emerge_panel(message_board);
				testNpc.talk(message_board);
			}else{
				message_board.setMessage(message_text[i]);
				i++;
				Thread.sleep(100);
				emerge_panel(message_board);
				key_state = 0;

				while(i < mes_send){
					if (Main.exitFlag != Main.RUNNING || exitMapLoopFlag != RUNNING_MAP_LOOP) {
						break;
					}

					if(key_state == 5){
						message_board.setMessage(message_text[i]);
						key_state = 0;
						i++;
					}
					Thread.sleep(10);
				}
			}
		}catch(Exception e){} 
		key_state = 0;
		try{
			while(true){
				if (Main.exitFlag != Main.RUNNING || exitMapLoopFlag != RUNNING_MAP_LOOP) {
					break;
				}

				if(key_state == 5){
					hide_panel(message_board);
					break;
				}
				Thread.sleep(10);
			}
		}catch(Exception e){} 
		key_state = 0;
		button1.setVisible(true);
	}

	public void map_to_map(){

		button1.setVisible(false);
		emerge_panel(message_board);
		key_state = 0;

		try{
			message_board.setMessage("Please wait");
			key_state = 0;
			Thread.sleep(2000);
		}catch(Exception e){} 

		key_state = 0;
		hide_panel(message_board);

		key_state = 0;
		button1.setVisible(true);
	}

	public void action(){
		int i;
		action_key = 8;
		switch(player_vct){
		case 0:
			message();
			action_key = 0;
			break;
		case 1:
			i = 0;
			while(i < npc_units){
				if(npc_ex[i][1] == b && a - npc_ex[i][0] == 1){
						action_key = 1;
						npc_tlk = i;
						break;
				}
				i++;
			}
			message();
			action_key = 0;
			break;
		case 2:
			i = 0;
			while(i < npc_units){
				if(npc_ex[i][1] == b  && npc_ex[i][0] - a == 1){
						action_key = 2;
						npc_tlk = i;
						break;
				}
				i++;
			}
			message();
			action_key = 0;
			break;
		case 3:
			i = 0;
			while(i < npc_units){
				if(npc_ex[i][0] == a && b - npc_ex[i][1]  == 1){
						action_key = 3;
						npc_tlk = i;
						break;
				}
				i++;
			}
			message();
			action_key = 0;
			break;
		case 4:
			i = 0;
			while(i < npc_units){
				if(npc_ex[i][0] == a && npc_ex[i][1] - b == 1){
						action_key = 4;
						npc_tlk = i;
						break;
				}
				i++;
			}
			message();
			action_key = 0;
			break;
		}
		npc_tlk = 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key;

		/* getting key_code */
		key = e.getKeyCode();

		/* each key_action */
		switch(key){
		case KeyEvent.VK_UP:
			key_state = 1;
			break;
		case KeyEvent.VK_DOWN:
			key_state = 2;
			break;
		case KeyEvent.VK_LEFT:
			key_state = 3;
			break;
		case KeyEvent.VK_RIGHT:
			key_state = 4;
			break;
		case KeyEvent.VK_Z:
			key_state = 5;
			break;
		case KeyEvent.VK_X:
			key_state = 6;
			break;
		case KeyEvent.VK_SPACE:
			key_state = 7;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}

