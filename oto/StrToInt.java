package oto;

public class StrToInt {
	
	static int strToInt36(String moji){
		int value = 0;
		for(int i=0;i<moji.length();i++){
			value *= 36;
			if(moji.charAt(i) <='9' && moji.charAt(i) >= '0'){
				value += moji.charAt(i) - '0';
			}else if(moji.charAt(i) <='Z' && moji.charAt(i) >= 'A'){
				value += moji.charAt(i) - 'A' + 10;
			}else value += moji.charAt(i) - 'a' + 10;
		}
		return value;
	}
	static int strToInt16(String moji){
		int value = 0;
		for(int i=0;i<moji.length();i++){
			value *= 16;
			if(moji.charAt(i) <='9' && moji.charAt(i) >= '0'){
				value += moji.charAt(i) - '0';
			}else if(moji.charAt(i) <='F' && moji.charAt(i) >= 'A'){
				value += moji.charAt(i) - 'A' + 10;
			}else value += moji.charAt(i) - 'a' + 10;
		}
		return value;
	}
	static int strToInt(String moji){
		int value = 0;
		for(int i=0;i<moji.length();i++){
			value *= 10;
			value += moji.charAt(i) - '0';
		}
		return value;
	}
}
