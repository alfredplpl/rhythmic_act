package keyio;

import java.awt.event.KeyEvent;

/**
 * キー入力を管理するクラスに必要な定数、またはメソッドを定義しています。
 * このインターフェースを実装するクラスは、リスナーになることが必要とされます。
 * 詳しくはパッケージの説明をご覧ください。
 * <b>1.1から定数値が変わりました。</b>
 * @author みそかつおでん
 * @version 1.1
 *
 */
public interface KeyTable{
	/**
	 * 音楽ゲームで一番左のレーンに相当するキー番号です。
	 */
	public static final int OTO_1 = KeyEvent.VK_Z;
	
	/**
	 * 音楽ゲームで二番目のレーンに相当するキー番号です。
	 */
	public static final int OTO_2 = KeyEvent.VK_X;
	
	/**
	 * 音楽ゲームで三番目のレーンに相当するキー番号です。
	 */
	public static final int OTO_3 = KeyEvent.VK_C;
	
	/**
	 * 音楽ゲームで四番目のレーンに相当するキー番号です。
	 */
	public static final int OTO_4 = KeyEvent.VK_V;
	
	/**
	 * 予約しているだけで実装はしません。
	 */
	public static final int OTO_5 = KeyEvent.VK_B;
	
	/**
	 * 予約しているだけで実装はしません。
	 */
	public static final int OTO_6 = KeyEvent.VK_N;
	
	/**
	 * 予約しているだけで実装はしません。
	 */
	public static final int OTO_7 = KeyEvent.VK_M;
	
	/**
	 * 予約しているだけで実装はしません。
	 */
	public static final int OTO_8 = KeyEvent.VK_SHIFT;
	
	/**
	 * アクションゲームで左へ行くことに相当するキー番号です。
	 */
	public static final int ACT_LEFT = KeyEvent.VK_LEFT;
	
	/**
	 * アクションゲームで右へ行くことに相当するキー番号です。
	 */
	public static final int ACT_RIGHT = KeyEvent.VK_RIGHT;
	
	/**
	 * アクションゲームでジャンプすることに相当するキー番号です。
	 */
	public static final int ACT_UP = KeyEvent.VK_UP;
	
	/**
	 * アクションゲームでしゃがむことに相当するキー番号です。
	 * まだ、実装しなくても結構です。
	 */
	public static final int ACT_CIRLE = KeyEvent.VK_BACK_SLASH;
	
	/**
	 * アクションゲームでしゃがむことに相当するキー番号です。
	 * まだ、実装しなくても結構です。
	 */
	public static final int ACT_CROSS = KeyEvent.VK_SLASH;
	
	
	/**
	 * アクションゲームでしゃがむことに相当するキー番号です。
	 * まだ、実装しなくても結構です。
	 */
	public static final int ACT_DOWN = KeyEvent.VK_DOWN;
	
	
	/**
	 * 決定を意味します。
	 */
	public static final int OK = KeyEvent.VK_ENTER;
	
	/**
	 * キャンセルを意味します。
	 */
	public static final int CANCEL = KeyEvent.VK_SPACE;
	
	/**
	 * 終了を意味します。
	 */
	public static final int EXIT = KeyEvent.VK_ESCAPE;
	
	/**
	 * 引数に対応するキーが押されているかどうか判定します。
	 * ただし、押したことや離したことについては判定できません。
	 * @param keycode　KeyTableで規定されている変数
	 * @return 指定したキーが押されているかどうか
	 */
	public boolean isPressing(int keycode);

}
