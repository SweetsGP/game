package gamesystem;

import java.io.Serializable;

//主人公のステータス設定

public class CharaStatus implements Serializable {
	
	private static final long serialVersionUID = 7077339051727728278L;
	/**
	 * SaveDataIOクラスと連動
	 * ・ここに記述したパラメータはすべてシリアライズ(直列化)されたあと、セーブデータに書き込まれます。(永続化)
	 * ・シリアライズとは、(2次元的な)オブジェクトを、(1次元的な)保存可能なデータ列(バイト列)に変換することです。
	 *   シリアライズすることで、オブジェクトをそのままセーブデータとして外部ファイルに出力することができます。
	 *   詳しいことはググってください。
	 *   重要なことは、次の3つです
	 *   1) "static"指定子および"transient"修飾子をつけたメンバはシリアライズの対象外になります。
	 *      シリアライズの対象外になる情報は、セーブデータに書き込まれないので注意してください。
	 *   2) すべてのメンバはシリアライズ可能である必要があります。
	 *      シリアライズ不可能なメンバはシリアライズの対象外となり、セーブデータには書き込まれません。
	 *      -> 基本的な型の変数(intやStringなど)は、シリアライズ可能です。
	 *      -> その他、クラスのインスタンスなどをメンバに加える場合は、確認が必要です。
	 *         - APIのクラスは基本的にシリアライズ可能です。
	 *           ただし、ストリームを扱うクラスなど(自身でシリアライズされたデータを扱うようなクラス)は、シリアライズ不可能です。
	 *         - 自作のクラスの場合は、"implements Serializable"でシリアライズ可能となります。
	 *           ただし、フィールドの存在しないクラスはシリアライズ可能ではありません。
	 *           (そのようなクラスから生成するオブジェクトは外部ファイルにわざわざ出力して保持する必要もないでしょう。)
	 *           (エラーになるかは不明です。"implements Serializable"自体は"そのクラスがシリアライズ可能であることを保証する"
	 *           ことを表しているだけなので、これを満たすようなコードを書く責任はプログラマにあります。)
	 *   3) このクラスに対して変更を加えた場合は、下のserialVersionUIDを削除して同じものを生成し直してください。
	 *      (Eclipseでは自動的に生成させることができます。)
	 */
	
	private int EnableStatus;	//存在するか / 0: 無効, 1: 有効
	private String CharaName;	//主人公名10文字以内
	private int CharaSex;		//Man:0 Woman:1
	private int Location[] = new int[3];
	private int RepopLocation[] = new int[3];
	private int EventID;
	
	private int volumeBgm;
	private int volumeSe;

	public CharaStatus() {
		this.EnableStatus = 0;
		this.CharaName = "";
		this.CharaSex = -1;
		this.Location[0] = -1;
		this.Location[1] = -1;
		this.Location[2] = -1;
		this.RepopLocation[0] = -1;
		this.RepopLocation[1] = -1;
		this.RepopLocation[2] = -1;
		this.EventID = -1;
		
		this.volumeBgm = 50;
		this.volumeSe = 50;
	}
	
	public CharaStatus(int EnableStatus , String CharaName , int CharaSex
			 , int L_Map , int L_x , int L_y , int RL_Map , int RL_x , int RL_y , int EventID){
		this.EnableStatus = EnableStatus;
		this.CharaName = CharaName;
		this.CharaSex = CharaSex;
		this.Location[0] = L_Map;
		this.Location[1] = L_x;
		this.Location[2] = L_y;
		this.RepopLocation[0] = RL_Map;
		this.RepopLocation[1] = RL_x;
		this.RepopLocation[2] = RL_y;
		this.EventID = EventID;
	}
	
	// ==================================================
	// セッター
	// ==================================================
	public void setEnableStatus(int en) {
		this.EnableStatus = en;
	}
	
	public void setCharaName(String charaName) {
		this.CharaName = charaName;
	}
	
	public void setCharaSex(int charaSex) {
		this.CharaSex = charaSex;
	}
	
	public void setLocation(int[] location) {
		this.Location[0] = location[0];
		this.Location[1] = location[1];
		this.Location[2] = location[2];
	}
	
	public void setLocation(int mapId, int x, int y) {
		this.Location[0] = mapId;
		this.Location[1] = x;
		this.Location[2] = y;
	}
	
	public void setRepopLocation(int[] reLocation) {
		this.RepopLocation[0] = reLocation[0];
		this.RepopLocation[1] = reLocation[1];
		this.RepopLocation[2] = reLocation[2];
	}
	
	public void setRepopLocation(int mapId, int x, int y) {
		this.RepopLocation[0] = mapId;
		this.RepopLocation[1] = x;
		this.RepopLocation[2] = y;
	}
	
	public void setEventID(int id) {
		this.EventID = id;
	}
	
	public void setVolumeBgm(int volumeBgm) {
		this.volumeBgm = volumeBgm;
	}
	
	public void setVolumeSe(int volumeSe) {
		this.volumeSe = volumeSe;
	}
	
	// ==================================================
	// ゲッター
	// ==================================================
	public int getEnableStatus() {
		return this.EnableStatus;
	}
	
	public String getCharaName() {
		return this.CharaName;
	}
	
	public int getCharaSex() {
		return this.CharaSex;
	}
	
	public int[] getLocation() {
		return this.Location;
	}
	
	public int[] getRepopLocation() {
		return this.RepopLocation;
	}
	
	public int getEventID() {
		return this.EventID;
	}
	
	public int getVolumeBgm() {
		return this.volumeBgm;
	}
	
	public int getVolumeSe() {
		return this.volumeSe;
	}

}
