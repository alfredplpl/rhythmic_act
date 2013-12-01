package actionenemy;

/**
 * 敵の種類を規定するクラスです。
 * 敵を生成するタイミングは、
 * 音楽ゲームから降ってくる場合とアクションゲームで生成される場合の
 * 2種類あり、それぞれコンストラクタを決める必要があります。
 * 詳しくはパッケージの説明をご覧ください。
 * 
 * @version 1.1
 * @author みそかつおでん
 *
 */
public interface EnemyType {
	/**
	 * 雑魚Aの番号
	 */
	public static int ZACO_A=1;
	
	/**
	 * 雑魚Bの番号
	 */
	public static int ZACO_B=2;
	
	/**
	 * 雑魚Cの番号
	 */
	public static int ZACO_C=3;
}
