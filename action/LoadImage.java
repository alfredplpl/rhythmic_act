package action;

/**
 * イメージを読み込むのに必要な情報を提供するクラスです。
 * @author みそかつおでん
 *
 */
public interface LoadImage {
	/**
	 * 一枚あたりの横方向のサイズを返します。
	 * @return　一枚あたりの横方向のサイズ
	 */
	public int getSizeX();
	
	/**
	 * 一枚あたりの縦方向のサイズを返します。
	 * @return　一枚あたりの縦方向のサイズ
	 */
	public int getSizeY();
	
	/**
	 * 各状態におけるイメージ枚数を返します。
	 * @return　各状態におけるイメージ枚数
	 */
	public int[] getFrameSize();
}
