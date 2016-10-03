package main;

import java.applet.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class Sound implements LineListener {
	private static final int SOUND_NO_SELECTED = -1;
	
	Clip[] bgm; // BGM 
	Clip[] se; // se 

	int bgm_num, se_num;
	private static final int BGM_NUMBER = 4;
	private static final int SE_NUMBER = 1;
	
	private static int nowBgm = SOUND_NO_SELECTED;
	private static int nowSe = SOUND_NO_SELECTED;
	
	FloatControl[] controlBgm, controlSe;

	public Sound() {

		bgm = new Clip[BGM_NUMBER];
		se = new Clip[SE_NUMBER];
		
		controlBgm = new FloatControl[BGM_NUMBER];
		controlSe = new FloatControl[SE_NUMBER];

		while (bgm_num < BGM_NUMBER) {
			File f1 = new File(FilePath.bgmDirPath + "bgm" + bgm_num + ".wav");
			try {
				// オーディオストリームを開く
				AudioInputStream streamBgm = AudioSystem.getAudioInputStream(f1);
				// ストリームのフォーマットを取得
				AudioFormat formatBgm = streamBgm.getFormat();
				// ライン情報を取得
				DataLine.Info info = new DataLine.Info(Clip.class, formatBgm);
				// 空のクリップを作成
				bgm[bgm_num] = (Clip) AudioSystem.getLine(info);
				// クリップのイベントを監視
				bgm[bgm_num].addLineListener(this);
				// オーディオストリームをクリップとして開く
				bgm[bgm_num].open(streamBgm);
				// コントロールを取得
				controlBgm[bgm_num] = (FloatControl)bgm[bgm_num].getControl(FloatControl.Type.MASTER_GAIN);
				// 初期値設定
				controlBgm[bgm_num].setValue((float )Math.log10(Main.saveData.getVolumeBgm()/100)*20);
				// ストリームを閉じる
				streamBgm.close();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bgm_num++;
		}
		
		while (se_num < SE_NUMBER) {
			File f2 = new File(FilePath.seDirPath + "se" + se_num + ".wav");
			try {
				// オーディオストリームを開く
				AudioInputStream streamSe = AudioSystem.getAudioInputStream(f2);
				// ストリームのフォーマットを取得
				AudioFormat formatSe = streamSe.getFormat();
				// ライン情報を取得
				DataLine.Info info = new DataLine.Info(Clip.class, formatSe);
				// 空のクリップを作成
				se[se_num] = (Clip) AudioSystem.getLine(info);
				// クリップのイベントを監視
				se[se_num].addLineListener(this);
				// オーディオストリームをクリップとして開く
				se[se_num].open(streamSe);
				// コントロールを取得
				controlSe[se_num] = (FloatControl)se[se_num].getControl(FloatControl.Type.MASTER_GAIN);
				// 初期値設定
				controlSe[se_num].setValue((float )Math.log10(Main.saveData.getVolumeSe()/100)*20);
				// ストリームを閉じる
				streamSe.close();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			se_num++;
		}

	}

	public void bgmRun(int num) {
		System.out.println(bgm[num].getFrameLength());
		nowBgm = num;
		bgm[nowBgm].setLoopPoints(509740, 1776924);
		bgm[nowBgm].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void bgmStop() {
		if (nowBgm != SOUND_NO_SELECTED) {
			bgm[nowBgm].stop();
			nowBgm = SOUND_NO_SELECTED;
		}
	}

	public void seRun(int num) {
		nowSe = num;
		se[nowSe].start();
		nowSe = SOUND_NO_SELECTED;
	}

	@Override
	public void update(LineEvent event) {
		// ストップか最後まで再生された場合
		if (event.getType() == LineEvent.Type.STOP) {
			Clip clip = (Clip) event.getSource();
			clip.stop();
			clip.setFramePosition(0); // 再生位置を最初に戻す
		}
	}
	
	/**
	 * BGMの音量を調整
	 * @param volumeScale 設定する音量のスケール(0~100)
	 */
	public void setVolumeBgm(double volumeScale) {
		for (int i=0; i<BGM_NUMBER; i++) {
			controlBgm[i].setValue((float)Math.log10(volumeScale/100)*20);
		}
		Main.saveData.setVolumeBgm((int )volumeScale);
	}
	
	/**
	 * 効果音の音量を調整
	 * @param volumeScale 設定する音量のスケール(0~100)
	 */
	public void setVolumeSe(double volumeScale) {
		for (int i=0; i<SE_NUMBER; i++) {
			controlSe[i].setValue((float)Math.log10(volumeScale/100)*20);
		}
		Main.saveData.setVolumeSe((int )volumeScale);
	}
}
