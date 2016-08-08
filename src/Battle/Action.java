package Battle;

public class Action {
	public int attack(int atk,int def,int hp,int htype,int etype,int skillID[]){
		//4つのわざを表示
		//選択後、ダメージ計算メソッドに投げる
		return hp;
	}

	public int escape(int hlv, int elv){
		double p = Math.random();
		int s=  (hlv - elv)/20;
		int r;
		p = p + s;
		if(p > 0.5){
			System.out.println("うまく にげきれた！");
			r =1;
		}else{
			System.out.println("にげられなかった！");
			r = 0;
		}
		return(r);
	}
}
//hoge