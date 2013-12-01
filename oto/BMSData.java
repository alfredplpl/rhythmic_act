package oto;

/**
 * BMSの基本的な情報を格納するクラスを規定するインターフェースです。
 * これらのデータは、文字列、あるいは整数型しか存在せず、
 * 音楽データなどの具体的なデータは含まれません。
 * 
 * @author みそかつおでん
 *
 */
public interface BMSData {
	/**
	 * BMSのタイトルを返します。
	 * @return BMSのタイトル
	 */
	public String getTitle();
	
	/**
	 * BMSの制作者名を返します。
	 * @return BMSの制作者名を
	 */
	public String getArtist();
	
	/**
	 * BMSのジャンルを返します。
	 * @return BMSのジャンル
	 */
	public String getGenre();
	
	/**
	 * BMSのプレイレベルを返します。
	 * @return BMSのプレイレベル
	 */
	public int getPlayLevel();
	
	/**
	 * BMSのBPM(１分あたりに四分音符を打つ回数)を返します。
	 * @return BMSのBPM
	 */
	public int getBPM();
	
	/**
	 * BMSファイルを指すパスを返します。
	 * OtoFactoryなどで使われます。
	 * @return BMSファイルを指すパス
	 */
	public String getPath();
	public String getPath2();
	public int getStartTime();
}
