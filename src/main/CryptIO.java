package main;

import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 暗号化データ入出力クラス
 * @author kitayamahideya
 *
 */
public class CryptIO {
	// 暗号化に用いるアルゴリズム
	private static final String CRYPT_ALGORITHM = "Blowfish";
	// 暗号化キー
	private static final String CRYPT_DEFAULT_KEY = "TestPass";
	// バイナリバッファのサイズ
	private static final int CASHE_SIZE = 2000;
	
	// 秘密鍵の仕様
	private SecretKeySpec sksSpec = null;
	// 暗号化機構
	private Cipher cipher = null;
	// 暗号化データ出力ストリーム
	private CipherOutputStream encryptOutputStream = null;
	private FileOutputStream fos = null;
	// 復号化データ入力ストリーム
	private CipherInputStream decryptInputStream = null;
	private FileInputStream fis = null;
	
	/**
	 * コンストラクタ(キーはデフォルト)
	 */
	public CryptIO() {
		try {
			// 秘密鍵仕様をキーから策定
			sksSpec = new SecretKeySpec(CRYPT_DEFAULT_KEY.getBytes(), CRYPT_ALGORITHM);
			// 暗号化機構インスタンス取得
			cipher = Cipher.getInstance(CRYPT_ALGORITHM);
		} catch (NoSuchAlgorithmException nsae) {
		} catch (NoSuchPaddingException nspe) {}
	}
	
	/**
	 * コンストラクタ(キーを設定)
	 * @param key
	 */
	public CryptIO(String key) {
		try {
			// 秘密鍵仕様をキーから策定
			sksSpec = new SecretKeySpec(key.getBytes(), CRYPT_ALGORITHM);
			// 暗号化機構インスタンス取得
			cipher = Cipher.getInstance(CRYPT_ALGORITHM);
		} catch (NoSuchAlgorithmException nsae) {
		} catch (NoSuchPaddingException nspe) {}
	}

	//==================================================
	// 暗号化メソッド
	//==================================================
	/**
	 * 暗号化用ファイル出力ストリームをオープンする
	 *   (このメソッドで返したストリームは、ファイル出力後、"必ず"closeEncryptFileStream()でクローズすること)
	 * @param encryptFileName 出力する暗号化バイナリファイル名
	 */
	public void openEncryptFileStream(String encryptFileName) {
		// 暗号化機構を'暗号化'モードで初期化
		try {
			cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
		} catch (InvalidKeyException ike) {}
		
		// 暗号化データ出力ストリーム作成
		try {
			fos = new FileOutputStream(encryptFileName);
			// ファイル出力ストリームをラップして暗号化データ出力ストリームを用意
			encryptOutputStream = new CipherOutputStream(fos, cipher);
		} catch (FileNotFoundException fnfe) {
			System.err.println(fnfe.getMessage());
		}

	}
	
	/**
	 * 暗号化用ファイル出力ストリームを取得する
	 * @return
	 */
	public CipherOutputStream getEncryptFileStream() {
		return encryptOutputStream;
	}
	
	/**
	 * バイト配列を暗号化データ出力ストリームに書き込む
	 * @param b 書き込みたいバイト配列
	 */
	public void writeToEncryptFileStream(byte[] b) {
		try {
			encryptOutputStream.write(b);
		} catch(IOException e) {
			
		}
		
	}
	
	/**
	 * バイト配列を暗号化データ出力ストリームに書き込む
	 * @param b 書き込みたいバイト配列
	 * @param off オフセット(先頭位置)
	 * @param len 長さ
	 */
	public void writeToEncryptFileStream(byte[] b, int off, int len) {
		try {
			encryptOutputStream.write(b, off, len);
		} catch(IOException e) {
			
		}
		
	}
	
	/**
	 * 文字列を暗号化データ出力ストリームに書き込む
	 * @param s 書き込みたい文字列
	 */
	public void writeStringToEncryptFileStream(String s) {
		byte[] sb;
		
		try {
			sb = s.getBytes("UTF-8");
			encryptOutputStream.write(sb);
		} catch (UnsupportedEncodingException uee) {
		} catch (IOException ioe) {
			
		}
	}
	
	/**
	 * 整数を暗号化データ出力ストリームに書き込む
	 * @param in 書き込みたい整数
	 */
	public void writeIntToEncryptFileStream(int in) {
		byte[] inb;
		
		inb = ByteBuffer.allocate(4).putInt(in).array();
		try {
			encryptOutputStream.write(inb);
		} catch (IOException ioe) {
			
		}
	}

	/**
	 * 暗号化用ファイル出力ストリームをクローズする
	 *   (直近でopenEncryptFileStream()によってオープンされたもの)
	 */
	public void closeEncryptFileStream() {
		try {
			if (encryptOutputStream != null) {
				encryptOutputStream.close();
			}
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			
		}
	}

	//==================================================
	// 復号化メソッド
	//==================================================
	/**
	 * 復号化用ファイル入力ストリームをオープンする
	 *   (このメソッドで返したストリームは、複合データ取り出し後、"必ず"closeDecryptFileStream()でクローズすること)
	 * @param decryptFileName 復号化したいバイナリファイル名
	 */
	public void openDecryptFileStream(String decryptFileName) {
		// 暗号化機構を'復号化'モードで初期化
		try {
			cipher.init(Cipher.DECRYPT_MODE, sksSpec);
		} catch (InvalidKeyException ike) {}
		
		// 復号化データ入力ストリーム作成
		try {
			fis = new FileInputStream(decryptFileName);
			// ファイル入力ストリームをラップして復号化データ入力ストリームを用意
			decryptInputStream = new CipherInputStream(fis, cipher);

		} catch (FileNotFoundException fnfe) {
			System.err.println(fnfe.getMessage());
		}
	}
	
	/**
	 * 復号化用ファイル入力ストリームを取得する
	 * @return
	 */
	public CipherInputStream getDecryptFileStream() {
		return decryptInputStream;
	}
	
	/**
	 * バイト配列(全内容分)を復号化データ入力ストリームから読み込む
	 * @return 読み込んだバイト配列
	 */
	public byte[] readFromDecryptFileStream() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[CASHE_SIZE];
		
		try {
			while(true) {
				int readLen = decryptInputStream.read(b);
				if(readLen < 0) {
					break;
				}
				baos.write(b, 0, readLen);
			}
		} catch(IOException e) {
			
		}
		
		return baos.toByteArray();
	}
	
	/**
	 * 文字列(全内容分)を復号化データ入力ストリームから読み込む
	 * @return 読み込んだ文字列
	 */
	public String readStringFromDecryptFileStream() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(decryptInputStream));
			StringBuffer sb = new StringBuffer();
			
			int c;
			while ((c = br.read()) != -1) {
				sb.append((char) c);
			}
			
			return sb.toString();
		} catch (IOException ioe) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException ioe) {}
		}
	}
	
	/**
	 * 整数(1つ分)を復号化データ入力ストリームから読み込む
	 * @return in 読み込んだ整数を格納
	 */
	public int readIntFromDecryptFileStream() {
		int in = 0;
		byte[] rb = new byte[4];
		
		try {
			decryptInputStream.read(rb);
			in = ByteBuffer.wrap(rb).getInt();
		} catch (IOException ioe) {
			
		}
		
		return in;
	}

	/**
	 * 復号化用ファイル入力ストリームをクローズする
	 *   (直近でopenDecryptFileStream()によってオープンされたもの)
	 */
	public void closeDecryptFileStream() {
		try {
			if (decryptInputStream != null) {
				decryptInputStream.close();
			}
			if (fis != null) {
				fis.close();
			}
		} catch (Exception e) {
			
		}
	}

}


