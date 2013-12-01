package oto;
import java.io.*;
import java.util.Properties;

import flow.GameConst;
public final class OtoFactory {
	//static String BMS_PATH = "BMS";
	static final int DataMax = 300;
	
	public static Oto makeOto(BMSData BMS){
		return new MusicGame(BMS);
		//return new BM08();
	}
	
	public static BMSInfomation[] getBMSList(){
		Properties set=new Properties();
		try {
			set.load((new FileInputStream(GameConst.SETTING_PATH)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		File file1=new File(set.getProperty("bmsinfomation.folder"));
		
		//File file1 = new File(BMS_PATH);
		
		if(!file1.exists()) System.out.println("îhå≠é∏îs");
		String[] list1 = file1.list();
		if(list1 == null) System.out.println("Ç ÇÈÇ€");
		BMSInfomation[] output,temp = new BMSInfomation[DataMax];
		String path,path2;
		int count = 0,st = 0;
		for(int i=0;i<list1.length;i++){
			if(list1[i].indexOf(".",0) == -1){
				File file2 = new File(file1.getPath() + "\\" + list1[i]);
				String[] list2 = file2.list();
				for(int j=0;j<list2.length;j++){
					if(list2[j].lastIndexOf(".",list2[j].length()) != -1){
						if(list2[j].indexOf("bms",
							list2[j].lastIndexOf(".",list2[j].length())) != -1){
							path2 = null;
							for(int k=0;k<list2.length;k++){
								if(list2[k].lastIndexOf("preview_", 8) != -1 &&
										list2[k].lastIndexOf(".mp3") >= (list2[k].length() - 5)){
									path2 = file1.getPath() + "\\" + list1[i] + "\\" + list2[k];
									st = StrToInt.strToInt(list2[k].substring(9, 11));
								}
							}
							path = file1.getPath() + "\\" + list1[i] + "\\" + list2[j];
							
							temp[count] = loadData(path,path2,st);
							count++;
						}
					}
				}
			}
		}
		output = new BMSInfomation[count];
		for(int i=0;i<count;i++){
			output[i] = temp[i];
		}
		return output;
	}
	static BMSInfomation loadData(String path,String path2,int startTime){
		BMSInfomation dat = new BMSInfomation();
		try{
			dat.setPath(path);
			BufferedReader br =
				new BufferedReader(new FileReader(path));
			String line;
			dat.setStartTime(startTime);

			dat.setPath2(path2);
			for(int n=0;n<30&&(line = br.readLine()) != null;n++){
				if(line.length()!=0 && line.charAt(0) == '#'){
					String code,arg;
					int i;
					//System.out.println("a");
					for(i=1;i<line.length() && line.charAt(i) != ' ';i++){
					}

					//System.out.println("t");
					code = line.substring(1,i);
					//System.out.println("r");
					arg = line.substring(i).trim();
					if("ARTIST".equals(code)){
						//System.out.println("b");
						dat.setArtist(arg);
					} else if("GENRE".equals(code)){
						//System.out.println("c");
						dat.setGenre(arg);
					} else if("TITLE".equals(code)){
						//System.out.println("d");
						dat.setTitle(arg);
					} else if("BPM".equals(code)){
						//System.out.println("e");
						dat.setBPM(StrToInt.strToInt(arg));
					} else if("PLAYLEVEL".equals(code)){
						//System.out.println("f");
						dat.setPlayLevel(StrToInt.strToInt(arg));
					}
					
				}
			}
			br.close();
			
		} catch (Exception e){
			System.out.println("ó·äO" + e + "Ç™î≠ê∂ÇµÇ‹ÇµÇΩ");
		}
		
		
		return dat;
	}
	
}
