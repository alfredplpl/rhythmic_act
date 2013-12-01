package action;

/**
 * Actionのコンストラクタの代わりに用意されるクラスです。
 * これにより、Actionは直接生成されることなく
 * Actionの中で処理が完結され保護されます。
 * @version 1.1
 * @author みそかつおでん
 */
public final class ActionFactory {
	
	/**
	 * コンストラクタの代わりになります。
	 * @param stage ActionFactoryによって規定されるステージの種類
	 * @return　Actionを実装しているインスタンス
	 * @throws Exception 読み込みに発生した何らかのエラー
	 */
	static public Action makeAction(int stage) throws Exception{
		Action act =null;
		
		act=new SuperMASAO(stage);
		//throw new Exception("エラーチェック");
		return act;
	}

}
