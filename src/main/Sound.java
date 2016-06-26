package main;

import java.applet.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class Sound implements LineListener {
	Clip[] bgm; // BGM 
	Clip[] se; // se 

	int bgm_num, se_num;
	private static final int BGM_NUMBER = 4;
	private static final int SE_NUMBER = 1;

	public Sound() {

		bgm = new Clip[BGM_NUMBER];
		se = new Clip[SE_NUMBER];

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
				// ストリームを閉じる
				streamSe.close();
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			se_num++;
		}

	}

//	public void bgmRun(int num) {
//	}
//	
//	public void bgmStop(int num) {
//	}
//
//	public void seRun(int num) {
//	}
	public void bgmRun(int num) {
		System.out.println(bgm[num].getFrameLength());
		bgm[num].setLoopPoints(509740, 1776924);
		bgm[num].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void bgmStop(int num) {
		bgm[num].stop();
	}

	public void seRun(int num) {
		se[num].start();
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
}
