package oto;

import java.awt.Image;

/**
 * 上から振ってくる敵を受け渡すためのクラスを規定します。
 * @author みそかつおでん
 *
 * @version 1.1
 */
public interface Note {
	
	/**
	 * 敵のタイプを指定します。なお、敵の番号はEnemyTypeで規定されます。
	 * @return EnemyTypeで規定される定数
	 */
	public int getType();
	
	/**
	 * 落ちてくる速度を指定します。時間はフレーム単位で、
	 * 適当なタイミングで減速するように落ちてきてください。
	 * 
	 * @return　落ちてくる速度
	 */
	public double getVelocity();
	
	/**
	 * 落ちてくる敵の画像を渡します。
	 * なお、渡すのは参照だけで、（ディープ）コピーする必要はありません。
	 * @return　敵のイメージ
	 */
	public Image getImage();
	
	/**
	 * 落ちてくるオブジェクトの中心のX座標を返します
	 * @return 落ちてくるオブジェクトの中心のX座標
	 */
	public double getX();
}
