package map;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import main.FilePath;
import main.Main;
import main.PanelController;

public class npc extends map_main implements Runnable {
	private static final int NPC_ALIVE = 0;
	private static final int NPC_DEAD = 1;
	private static final int NPC_WAIT = 2;
	private static final int NPC_BATTLE = 3;
	private int isAlive;

	private int status;

	public JButton npc_b;
	public Point n_point;

	public npc(int status) {
		super();

		this.status = status;
		this.isAlive = NPC_ALIVE;

		npc_set();

		n_point = npc_b.getLocation();
		x_n = n_point.x;
		y_n = n_point.y;

		PanelController.mp.p.add(npc_b);
		//		m.p.add(npc_b);

		this.setFocusable(false);
	}

	@Override
	public void run() {
		if (this.isAlive == NPC_ALIVE) {
			npc_motion();
		}
	}

	public void shutdown() {
		this.isAlive = NPC_DEAD;
		npc_b.setVisible(false);
		PanelController.mp.p.repaint();
	}

	public void npc_motion() {
		int vector;
		int move;

		n_point = npc_b.getLocation();
		x_n = n_point.x;
		y_n = n_point.y;

		try{
			while(true){
				if (Main.exitFlag != Main.RUNNING || exitMapLoopFlag != RUNNING_MAP_LOOP) {
					break;
				}

				vector = (int)(Math.random() * 4);
				//			System.out.println(vector);

				if(map_main.status  == NPC_DEAD){
					System.out.println("Thread finished working");
					npc_b.setVisible(false);
					break;
				}else if(map_main.status == NPC_WAIT){
					while(map_main.status == NPC_WAIT){
						Thread.sleep(1000);
					}
				}else if(map_main.status == NPC_BATTLE){
					this.setVisible(false);
					while(map_main.status == NPC_BATTLE){
						Thread.sleep(1000);
					}
					this.setVisible(true);
				}

				PanelController.mp.p.repaint();
				//				m.p.repaint();
				Thread.sleep(1000);

				switch(vector){
				case 0:
					if(PanelController.mp.npc_ex[this.status][0] - PanelController.mp.a == 1 
					|| (y_n - 50 < 50) || md.mp1[PanelController.mp.npc_ex[this.status][0]-1][PanelController.mp.npc_ex[this.status][1]] != 1) {
						//					if(m.npc_ex[this.status][0] - m.a == 1 || (y_n - 50 < 50) || md.mp1[m.npc_ex[this.status][0]-1][m.npc_ex[this.status][1]] != 1) {
						//System.out.println("壁か画面外を阻止");
					}else{
						for(move = 50;move > 0;move--){
							npc_b.setBounds(x_n, y_n - 1, 25, 25);
							PanelController.mp.p.repaint();
							//							m.p.repaint();
							n_point = npc_b.getLocation();
							y_n = n_point.y;
							Thread.sleep(5);
						}
						PanelController.mp.npc_ex[this.status][0]--;
						//						m.npc_ex[this.status][0]--;
					}
					break;
				case 1:
					if(PanelController.mp.a - PanelController.mp.npc_ex[this.status][0] == 1 
					|| (y_n + 50 > 600) || md.mp1[PanelController.mp.npc_ex[this.status][0]+1][PanelController.mp.npc_ex[this.status][1]] != 1) {
						//					if(m.a - m.npc_ex[this.status][0] == 1 || (y_n + 50 > 600) || md.mp1[m.npc_ex[this.status][0]+1][m.npc_ex[this.status][1]] != 1) {
						//System.out.println("壁か画面外を阻止");
					}else{
						for(move = 50;move > 0;move--){
							npc_b.setBounds(x_n, y_n + 1, 25, 25);
							PanelController.mp.p.repaint();
							//							m.p.repaint();
							n_point = npc_b.getLocation();
							y_n = n_point.y;
							Thread.sleep(5);
						}
						PanelController.mp.npc_ex[this.status][0]++;
						//						m.npc_ex[this.status][0]++;
					}	
					break;
				case 2:
					if(PanelController.mp.npc_ex[this.status][1] - PanelController.mp.b == 1 
					|| (x_n - 50 < 50) || md.mp1[PanelController.mp.npc_ex[this.status][0]][PanelController.mp.npc_ex[this.status][1]-1] != 1) {
						//						if(m.npc_ex[this.status][1] - m.b == 1 || (x_n - 50 < 50) || md.mp1[m.npc_ex[this.status][0]][m.npc_ex[this.status][1]-1] != 1) {
						//System.out.println("壁か画面外を阻止");
					}else{
						for(move = 50;move > 0;move--){
							npc_b.setBounds(x_n - 1, y_n, 25, 25);
							PanelController.mp.p.repaint();
							//							m.p.repaint();
							n_point = npc_b.getLocation();
							x_n = n_point.x;
							Thread.sleep(5);
						}
						PanelController.mp.npc_ex[this.status][1]--;
						//						m.npc_ex[this.status][1]--;
					}
					break;
				case 3:
					if(PanelController.mp.npc_ex[this.status][1] - PanelController.mp.b == 1 
					|| (x_n + 50 > 800) || md.mp1[PanelController.mp.npc_ex[this.status][0]][PanelController.mp.npc_ex[this.status][1]+1] != 1) {
						//						if(m.npc_ex[this.status][1] - m.b == 1 || (x_n + 50 > 800) || md.mp1[m.npc_ex[this.status][0]][m.npc_ex[this.status][1]+1] != 1) {
						//System.out.println("壁か画面外を阻止");
					}else{
						for(move = 50;move > 0;move--){
							npc_b.setBounds(x_n + 1, y_n, 25, 25);
							PanelController.mp.p.repaint();
							//							m.p.repaint();
							n_point = npc_b.getLocation();
							x_n = n_point.x;
							Thread.sleep(5);
						}
						PanelController.mp.npc_ex[this.status][1]++;
						//						m.npc_ex[this.status][1]++;
					}
					break;
				}
				if(map_main.status  == 1){
					System.out.println("Thread finished working");
					npc_b.setVisible(false);
					break;
				}
				//				System.out.println(x_n +":"+ y_n);
				Thread.sleep(2000);

			}
		}catch(Exception e){}
	}

	public void npc_set(){
		if(this.status == 0){
			npc_b = new JButton(new ImageIcon(FilePath.mapDirPath + "npc1.png"));
			npc_b.setBounds(764, 564, 25, 25);
			PanelController.mp.npc_ex[this.status][0] = 11;
			PanelController.mp.npc_ex[this.status][1] = 15;
		}else{
			npc_b = new JButton(new ImageIcon(FilePath.mapDirPath + "npc2.png"));
			npc_b.setBounds(764, 12, 25, 25);
			PanelController.mp.npc_ex[this.status][0] = 0;
			PanelController.mp.npc_ex[this.status][1] = 15;
		}
	}

}

