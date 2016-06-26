package Battle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

import main.FilePath;

public class BlinkTask extends TimerTask{
	private boolean blinkFlag0 = true;
	private boolean blinkFlag1 = true;
	private GameCanvas c = new GameCanvas();
	private int chara;
	public BufferedImage image;
	private int i=0;
	private int j=0;
	private Graphics g;


	public void set(Graphics g, BufferedImage image, int chara){
		this.chara=chara;
		this.image=image;
		this.g=g;

	}

	public int count(int chara){
		if(chara==0){//自分がダメージを受けたとき
			return i;//点滅した回数
		}else{
			return j;
		}
	}

	public void run(/*BufferedImage image, int chara*/){
		if(chara==0){//自分がダメージを受けたとき
			if(blinkFlag0){
				image=c.loadImage(FilePath.battleDirPath+"white.png");
			//	System.out.println("おそ松");
				i++;
			}else{
				image=c.loadImage(FilePath.monstersDirPath+"sample2.jpg");
				i++;
			}
			g.drawImage(image,100,250,c);
			blinkFlag0 = !blinkFlag0;
			if(i==4) this.cancel();
		}else{//相手がダメージを受けたとき
			if(blinkFlag1){
				image=c.loadImage(FilePath.battleDirPath+"white.png");
			//	System.out.println("カラ松");
				j++;
			}else{
				image=c.loadImage(FilePath.monstersDirPath+"sample1.jpg");
				j++;
			}
			g.drawImage(image,550,100,c);
			blinkFlag1 = !blinkFlag1;
			if(j==4) this.cancel();
		}
		//System.out.println(blinkFlag1);
		//System.out.println(j);
	}
}
