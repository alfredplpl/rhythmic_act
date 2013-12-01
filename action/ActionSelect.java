package action;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *アクションゲームの難易度を選択させるためのインターフェースです。
 *この画面は、難易度の一覧と難易度の詳細、操作方法によって構成されています。
 *ユーザは、
 *KeyTable.ACT_LEFTで難易度選択のフレームを右方向に移動させ、
 *KeyTable.ACT_RIGHTでフレームを左方向に移動させることができます。
 *またKeyTable.ACT_UPに押すことで難易度を決定することができます。
 *具体的な配置や全体のイメージは別途図を参照してください。
 *@version 1.1
 */
public interface ActionSelect {
	/**
	 * アクションゲームの難易度の選択画面として表示できる最大幅です。
	 */
	public static final int dispX=1024;

	/**
	 * アクションゲームの難易度の選択画面として表示できる縦の最大の長さです。
	 */
	public static final int dispY=384;
	
	
	/**
	 * 難易度の選択を続けるかどうかを返します。難易度を決定した場合、falseを返すようにしてください。
	 * @return 続けるかどうか
	 */
	public abstract boolean isContinue();
	
	/**
	 * 難易度の決定やフレームの移動を行います。
	 * @param key 各キーの状態
	 */
	public abstract void act(KeyTable key);
	
	/**
	 * 直接領域を描画します。指定された領域以上を描画してしようとしても描画されないので、
	 * はみ出るとか考えずに描画してください（必要なオフスクリーン描画をしっかりしてください）。
	 * @param g 直接領域を描画するグラフィックス
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * 状態の初期化を行う。
	 *
	 */
	public abstract void reset();
	
	/**
	 * 選択した難易度を返します。
	 * @return　Actionインターフェースにて定義された難易度に対応する整数型の値。選択されていない場合は0
	 */
	public int getDifficulty();
}
 
