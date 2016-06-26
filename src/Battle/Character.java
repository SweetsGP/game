package Battle;

public class Character {
	public int hp;
	public int atk;
	public int def;
	public int spd;
	public int exp;
	public String name;
	public int skillID[] = new int [4];
	public int type;
	public int lv;
	public int getNO;

	public Character(){}
	public Character(int atk , int def , int hp, String name,int exp,int type,int lv ,int[] skillID,int getNO){
		this.atk = atk;
		this.def = def;
		this.hp = hp;
		this.name = name;
		this.exp = exp;
		this.type = type;
		this.lv = lv;
		this.skillID = skillID;
		this.getNO = getNO;
	}
	
	public Character(int atk,int def ,int hp ,String name){
		this.atk = atk;
		this.def = def;
		this.hp = hp;
		this.name = name;
	}
	
	public Character(int atk,int def ,int hp ,String name, int speed){
		this.atk = atk;
		this.def = def;
		this.spd = speed;
		this.hp = hp;
		this.name = name;
	}

}
