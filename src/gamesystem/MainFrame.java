package gamesystem;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.Env;
import main.Main;

/**
 * メインフレーム
 * 画面遷移はパネルコントローラへ
 * @author kitayamahideya
 *
 */
public class MainFrame extends JFrame implements ComponentListener {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	/**
	 * コンストラクタ
	 * @param title ウィンドウタイトルに表示する文字列
	 */
	public MainFrame(String title) {
		setTitle(title);
		setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int exitConfirmAns = JOptionPane.showConfirmDialog(null,
						"ゲームを終了しますか？" + Env.crlf + "(保存していないセーブデータは失われます)",
						Main.GAME_TITLE, JOptionPane.YES_NO_OPTION);
				if (exitConfirmAns == JOptionPane.YES_OPTION) {
					dispose();
					Main.exitFlag = Main.EXIT_OK;
				}
			}
		});

	}

	@Override
	public void componentHidden(ComponentEvent ce) {}

	@Override
	public void componentMoved(ComponentEvent ce) {}

	@Override
	public void componentResized(ComponentEvent ce) {}

	@Override
	public void componentShown(ComponentEvent ce) {}

}
