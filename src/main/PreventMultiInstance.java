package main;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 二重起動のチェック
 * (https://gist.github.com/seraphy/5502048)
 */
public class PreventMultiInstance implements Closeable {
	
	// ロックファイルの名前
	public static final String LOCK_FILE_NAME = ".lock";
	
	// ロックファイルの出力ストリーム
	private FileOutputStream fos;
	
	// 排他ロックするためのロックオブジェクト
	private FileLock lock;
	
	/**
	 * コンストラクタ
	 * 構築時にはロックファイルは作成しますがロックはかけられていません。
	 * @param baseDir ロックファイルを置くディレクトリ
	 * @throws IOException 失敗
	 */
	public PreventMultiInstance(File baseDir) throws IOException {
		if (baseDir == null) {
			throw new IllegalArgumentException();
		}
		
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		
		// ロックファイル生成
		File lockFile = new File(baseDir, LOCK_FILE_NAME);
		this.fos = new FileOutputStream(lockFile);
	}
	
	/**
	 * 同じデータ保存先を示して複数起動していないかチェックする
	 * ロックファイルは引数で指定されたディレクトリ上に作成される
	 *
	 * @param baseDir ロックファイルを配置するディレクトリ
	 * @return 問題なければtrue、排他されている場合はfalse
	 */
	public boolean tryLock() throws IOException {
		if (this.lock != null) {
			return false;
		}
		
		// ロック取得試行
		try {
			FileChannel channel = fos.getChannel();
			FileLock lock = channel.tryLock();
			if (lock != null) {
				// ロック取得成功
				this.lock = lock;
				return true;
			}
			
		} catch (IOException e) {
			throw new IOException("Failed to control a lock file.", e);
		}
		
		// ロック取得失敗
		return false;
	}
	
	/**
	 * ロックされている状態であるかチェック
	 * @return ロックされている状態であればtrue
	 */
	public boolean isLocked() {
		return lock != null;
	}
	
	/**
	 * ロックをリリースする
	 * @throws IOException
	 */
	public void release() throws IOException {
		FileLock lock = this.lock;
		
		if (lock != null) {
			lock.release();
			this.lock = null;
		}
	}
	
	/**
	 * ロックされていればロックをリリースし、
	 * ロックファイルをクローズする
	 */
	@Override
	public void close() throws IOException {
		try {
			release();
		} catch (IOException e) {
			
		}
		
		if (fos != null) {
			fos.close();
		}
	}
}
