package action;

/**
 * 音楽ゲームで発生させられたイベントを間接的に受け渡す
 * インターフェースです。
 * 今はまだ実装する必要はありません。
 * 
 * @author みそかつおでん
 *
 */
public interface ActionEvent {
	
	public static int NO_EFFCT=0;
	
	public static int JUDGE_BONUS=5;
	
	/**
	 * 音楽ゲームで発生させられたActionEventの種類を受け取ります。
	 * @return ActionEventで定義されるアクションタイプ
	 */
	public int getEventType();
	
	
}
