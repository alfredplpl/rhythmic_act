package flow;

/**
 * ゲームで使用する総合的な定数を管理します。
 * @author みそかつおでん
 * @version 1.2
 */
public interface GameConst {
	/**
	 * 設定ファイルの場所を表します。
	 */
	public static final String SETTING_PATH="./setting.properties";
	
	/**
	 * １フレームのミリ秒部分に相当します。
	 */
	static final long FRAME_MILI = 16;
	
	/**
	 * １フレームのナノ秒部分に相当します。
	 */
	static final int FRAME_NANO = 700000;
	
	/**
	 * ナノ秒に変換した１フレームの時間に相当します。現在都合により、値が変わっています。
	 */
	//static final long FRAME_TIME = 16700000;
	static final long FRAME_TIME = 16666000;
}
