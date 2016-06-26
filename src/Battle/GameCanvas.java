package Battle;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.DataBase;
import main.FilePath;

public class GameCanvas extends Canvas{
	
	private static final long serialVersionUID = 1L;
	BufferedImage me;//ウィンドウに表示するイメージ
	BufferedImage en;
	BufferedImage mehp;
	BufferedImage enhp;
	


	//本来ならDBから情報をとってきて入れる
	double maxmhp=50;
	int mhp=50;
	int mexp=89;
	int mtype=1;
	int mlv = 5;
	String mname="カラ松";
	int mhpwid;
	
	ArrayList<HashMap<String,String >> testname;
	//testname 
	
	
	double maxehp=50;
	int ehp=50;
	int eexp=96;
	int etype=2;
	int elv = 5;
	String ename="おそ松";
	int ehpwid;
	
	int normdamage=10;
	double mhprate=1;//初期HP残量割合はMAXなので１
	double ehprate=1;

	//コンストラクタ
	public GameCanvas(){
		setSize(800,500);
		setBounds(0,0,800,500);
		
		try {
			testname= DataBase.wExecuteQuery("SELECT name FROM testmonsters WHERE monsterID = 1");
		} catch(SQLException e) {
			
		}
		mname = testname.get(0).get("name");
		me=loadImage(FilePath.monstersDirPath + "sample2.jpg");//表示用のイメージを取り込み
		en=loadImage(FilePath.monstersDirPath + "sample1.jpg");
		mehp=loadImage(FilePath.battleDirPath + "HP.png");
		enhp=loadImage(FilePath.battleDirPath + "HP.png");
	}

	//イメージを読み込み戻り値として返す 引数=>ファイルパスに変更しました
	public BufferedImage loadImage(String filePath){
		try{
			FileInputStream in = new FileInputStream(filePath);//FileInputStreamを作る
			BufferedImage rv = ImageIO.read(in);//イメージを取り込む
			in.close();//閉じる
			return rv;//戻り値に読み込んだイメージをセット
		}catch(IOException e){
			//エラー時の処理（エラーを表示）しnullを返す
			System.out.println("Err e="+e);//エラーを表示
			return null;//nullを返す
		}
	}

	//表示の必要があれば実行されるメソッド
	@Override
	public void paint(Graphics g){

		Graphics2D g2=(Graphics2D)g;

		g=this.getGraphics();
		super.paint(g);
		
		mhpwid=(int)(64*mhprate);
		ehpwid=(int)(64*ehprate);

		//変数piの画像を表示
		g.drawImage(me,100,250,this);
		g.drawImage(en,550,100,this);

		//枠を表示
		g2.draw(new RoundRectangle2D.Double(280.0d, 280.0d, 100.0d, 120.0d, 10.0d, 10.0d));
		g2.draw(new RoundRectangle2D.Double(420.0d, 130.0d, 100.0d, 120.0d, 10.0d, 10.0d));

		//情報を表示
		draw_info(g,1);
		draw_info(g,2);
		g.drawImage(set_hpcolor(mhprate),290,325,mhpwid,64,this);
		g.drawImage(set_hpcolor(ehprate),430,175,ehpwid,64,this);
	}

	public void draw_info(Graphics g, int target){
		if(target==1){//プレイヤーのモンスター情報を表示
			g.drawString("　←　"+mname, 255, 300);
			g.drawString("　　　Lv:"+mlv,255,320);
			//g.drawString("　　　HP:"+mhp, 255, 340);
			g.drawString("　　　HP:",255,350);
			set_string_color(g,mhprate);
			g.drawString(""+mhp,312,350);
			g.setColor(Color.black);
			g.drawString("/"+(int)maxmhp,335,350);
			g.drawString("　　　EXP:"+mexp, 255, 390);
		}else{//相手のモンスター情報を表示
			g.drawString(ename, 430, 150);
			g.drawString(" →　", 520, 150);
			g.drawString("Lv:"+elv,430,170);
			//g.drawString("HP:"+ehp, 430, 190);
			g.drawString("HP:",430,200);
			set_string_color(g,ehprate);
			g.drawString(""+ehp,450,200);
			g.setColor(Color.black);
			g.drawString("/"+(int)maxehp,470,200);
			g.drawString("EXP:"+eexp, 430, 240);
		}

	}
	
	public BufferedImage set_hpcolor(double hprate){
		BufferedImage hp_pic;
		if(hprate>0.5){
			hp_pic=loadImage(FilePath.battleDirPath + "HP.png");
		}else if(hprate<=0.5 && hprate>0.25){
			hp_pic=loadImage(FilePath.battleDirPath + "HPyellow.png");
		}else{
			hp_pic=loadImage(FilePath.battleDirPath + "HPred.png");
		}
		return hp_pic;
	}
	
	public void set_string_color(Graphics g, double hprate){
		if(hprate>0.5){
			g.setColor(Color.black);
		}else if(hprate<=0.5 && hprate>0.25){
			g.setColor(Color.orange);
		}else{
			g.setColor(Color.red);
		}
	}


	public int hp_check(int etype, int mtype, int hp, int normdamage){
		//のこりHP計算（本来はタイプ相性によって0.5倍や1.5倍）
		hp=hp-normdamage;
		return hp;
	}


	public int exp_check(int exp, int getexp){
		exp+=getexp;
		return exp;
	}
}