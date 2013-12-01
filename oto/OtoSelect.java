package oto;

import java.awt.Graphics;

import keyio.KeyTable;


/**
 *音楽ゲームの曲を選択させるためのインターフェースです。
 *この画面は、曲リストと曲の詳細、操作方法によって構成されています。
 *ユーザは、
 *KeyTable.OTO_2で曲リストを上方向にずらし、
 *KeyTable.OTO_4で曲リストを下方向にずらすことができます。
 *またKeyTable.OTO_3に押すことで曲を決定することができます。
 *具体的な配置や全体のイメージは別途図を参照してください。
 *@version 1.1
 */
public interface OtoSelect {
	/**
	 * 曲の選択画面として表示できる最大幅です。
	 */
	public static final int dispX=1024;

	/**
	 * 曲の選択画面として表示できる縦の最大の長さです。
	 */
	public static final int dispY=384;
	
	/**
	 * 曲の選択を続けるかどうかを返します。曲を決定した場合、falseを返すようにしてください。
	 * @return 続けるかどうか
	 */
	public abstract boolean isContinue();
	
	/**
	 * 曲の決定や曲リストの操作を行います。
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
	 * 選択画面で選択した曲を返します。
	 * @return 選択されたBMSの情報。選択されていない場合はnull
	 */
	public abstract BMSData getSelectedBMS();
	public abstract void finalize();
}
 
