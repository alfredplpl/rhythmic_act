package action;

import java.awt.Graphics;
import java.awt.Image;

import keyio.KeyTable;

import oto.*;

/**
 * 下側に表示されるアクションゲームのインターフェースです。
 * このゲームは、フレーム単位でゲームを動かします。
 * このインターフェースを実装したインスタンスを生成するときは、
 * ActionFactory.makeAction()を使います。
 * <s>また、draw()のために、このインターフェースを実装する際、
 * Image型のバッファを用意しなければなりません。</s>
 *　drawDirectのGraphicsをつかって直接描画してください。
 * 画像の初期化処理は必要ありません。
 * なお、このアクションゲームの横の可動範囲は、
 * 見えている範囲ではなく、スコア表示部程度になります。
 * 
 * @version 1.4
 * @author みそかつおでん
 *
 */
public interface Action {
	/**
	 * アクションゲームの表示部分のサイズの一部です。
	 * 横方向のピクセルサイズを表します。
	 */
	public static final int dispX=1024;
	
	/**
	 * アクションゲームの表示部分のサイズの一部です。
	 * 縦方向のピクセルサイズを表します。
	 */
	public static final int dispY=384;
	
	/**
	 * このゲームで使用するステージ用データのパスを示します。
	 */
	static final String STAGE_PATH = "./stage/";
	
	/**
	 * 難易度が低いステージを表します。
	 */
	static final int EASY = 1;
	
	/**
	 * 難易度が普通であるステージを表します。
	 */
	static final int NORMAL = 2;
	
	/**
	 * 難易度が高いステージを表します。
	 */
	static final int HARD = 3;
	
	/**
	 * 外部ファイルであることを示します。
	 */
	static final int EXTERNAL =10;
	
	/**
	 * このメソッドによって、アクションゲームを１フレーム進めます。
	 * もし、今までに一度も呼び出されていない状態で呼び出された場合、
	 * ゲームの開始を意味します。ただし初期化はコンストラクタで行って
	 * ください。
	 * keytableについてはkeyioを参照してください。
	 * @param note 敵（譜面）を表します。
	 * @param keytable　キー入力を受け取ります。
	 */
	public void act(Note[] note,KeyTable keytable);
	
	/**
	 * 音楽ゲームによって発生させられるイベントをうけとります。
	 * 引数がnullの場合、何も発生しません。
	 * @param event　音楽ゲームによって発生させられるイベント
	 */
	public void setEvent(OtoEvent[] event);
	
	/**
	 * アクションゲーム中に発生した音楽ゲーム用イベントを渡します。
	 * 何もなかった場合、nullを返します。
	 * @return 音楽ゲームに発生させるイベント
	 */
	public ActionEvent[] getEvent();
	
	/**
	 * このメソッドは、１フレームの間に生じた得点を返します。
	 * 得点が生じた場合はその得点を、
	 * 特に生じなかった場合は0を返します。
	 * 
	 * @return 直前に処理したアクションゲームの得点
	 */
	public int getFrameScore();
	
	/**
	 * このメソッドによって、直前に処理したアクションゲームの様子
	 * を表す画像が得られます。
	 * なお、このインターフェースを実装する
	 * クラスは、このメソッドが呼ばれたときに描画を行ってください。
	 * @deprecated drawDirectメソッドによって置き換えてください。この
	 * メソッドは使われません。
	 * @return 直前に処理したアクションゲームの様子
	 */
	public Image draw();

	/**
	 * このメソッドによって、直前に処理したアクションゲームの様子
	 * を引数のグラフィックコンテキストで描画します。
	 * なお、このインターフェースを実装する
	 * クラスは、このメソッドが呼ばれたときに描画を行ってください。
	 * @param g ゲーム描画領域のグラフィックコンテキスト
	 */
	public void drawDirect(Graphics g);
	
	/**
	 * ゲーム中に移動した距離を返します。
	 * @return 移動した距離
	 */
	public double getWorldDistance();
}
