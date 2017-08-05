import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class EHandlerMainClass {
	private static ArrayList<String> strs;
	private static ArrayList<String> result;
	private static final String filePath = "F://desktopbackup//txts//movies.txt";
	public static void main(String[] args) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
		String line = "";
		strs = new ArrayList<String>();
		result = new ArrayList<String>();
		while ((line = bufferedReader.readLine()) != null) {
			line = line.replaceAll(" ", "");
			line = line.replaceAll("��", "(");
			line = line.replaceAll("��", ")");
			line = line.replaceAll("��", ":");
			if (strs.indexOf(line) == -1) {
				strs.add(line);
			}
		}
		bufferedReader.close();
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
		int length = strs.size();
		for (int i = length / 2 - 1; i >= 0; i--) {
			heapify(i);
		}
		for (int i = length - 1; i > 0; i--) {
			exchange(i, 0);
			result.add(strs.get(i));
			strs.remove(i);
			heapify(0);
		}
		result.add(strs.get(0));
		for (int i = length - 1; i >= 0; i--) {
			bufferedWriter.write(result.get(i));
			bufferedWriter.write("\r\n");
		}
	    bufferedWriter.close();
	}

	public static boolean smallerThan(int i1, int i2) {
		String[] S1 = converterToSpell(strs.get(i1)).split(",");
		String[] S2 = converterToSpell(strs.get(i2)).split(",");
		String s1 = S1[0];
		String s2 = S2[0];
		if (s1.charAt(s1.length() - 1) <= '9' && s2.charAt(s2.length() - 1) <= '9') {
			if (s1.indexOf(':') != -1 && s2.indexOf(':') != -1) {
				String ss1 = s1.substring(0, s1.indexOf(':'));
				String ss2 = s2.substring(0, s2.indexOf(':'));
				if (ss1.equals(ss2)) {
					if (s1.charAt(s1.length() - 1) < s2.charAt(s2.length() - 1)) {
						return true;
					}
					else {
						return false;
					}
				}
			}
			else {
				String ss1 = s1.substring(0, s1.length() - 1);
				String ss2 = s2.substring(0, s2.length() - 1);
				if (ss1.equals(ss2)) {
					if (s1.charAt(s1.length() - 1) < s2.charAt(s2.length() - 1)) {
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		int length1 = s1.length();
		int length2 = s2.length();
		int length = length1 > length2 ? length2 : length1;
		for (int i = 0; i < length; i++) {
			if (s1.charAt(i) < s2.charAt(i)) {
				return true;
			}
			else if (s1.charAt(i) > s2.charAt(i)) {
				return false;
			}
		}
		if (length1 > length2) {
			return false;
		}
		return true;
	}
	
	public static void exchange(int i, int j) {
		String temp = strs.get(i);
		strs.set(i, strs.get(j));
		strs.set(j, temp);
	}
	
	public static void heapify(int index) {
		int l = 2 * index + 1;
		int r = l + 1;
		int largest = index;
		int length = strs.size();
		if (l < length && smallerThan(index, l)) {
			largest = l;
		}
		if (r < length && smallerThan(largest, r)) {
			largest = r;
		}
		if (largest != index) {
			exchange(largest, index);
			heapify(largest);
		}
	}
	
	/** 
     * ����ת��λ����ȫƴ��Ӣ���ַ����䣬�����ַ���ʧ 
     * ֧�ֶ����֣����ɷ�ʽ�磨�ص���:zhongdangcen,zhongdangcan,chongdangcen 
     * ,chongdangshen,zhongdangshen,chongdangcan�� 
     *  
     * @param chines 
     *            ���� 
     * @return ƴ�� 
     */  
    public static String converterToSpell(String chines) {  
        StringBuffer pinyinName = new StringBuffer();  
        char[] nameChar = chines.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        for (int i = 0; i < nameChar.length; i++) {  
            if (nameChar[i] > 128) {  
                try {  
                    // ȡ�õ�ǰ���ֵ�����ȫƴ  
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(  
                            nameChar[i], defaultFormat);  
                    if (strs != null) {  
                        for (int j = 0; j < strs.length; j++) {  
                            pinyinName.append(strs[j]);  
                            if (j != strs.length - 1) {  
                                pinyinName.append(",");  
                            }  
                        }  
                    }  
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                    e.printStackTrace();  
                }  
            } else {  
                pinyinName.append(nameChar[i]);  
            }  
            pinyinName.append(" ");  
        }  
        // return pinyinName.toString();  
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));  
    }  
  
    /** 
     * ȥ���������ظ����� 
     *  
     * @param theStr 
     * @return 
     */  
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {  
        // ȥ���ظ�ƴ�����ƴ���б�  
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();  
        // ���ڴ���ÿ���ֵĶ����֣�ȥ���ظ�  
        Map<String, Integer> onlyOne = null;  
        String[] firsts = theStr.split(" ");  
        // ����ÿ�����ֵ�ƴ��  
        for (String str : firsts) {  
            onlyOne = new Hashtable<String, Integer>();  
            String[] china = str.split(",");  
            // �����ִ���  
            for (String s : china) {  
                Integer count = onlyOne.get(s);  
                if (count == null) {  
                    onlyOne.put(s, new Integer(1));  
                } else {  
                    onlyOne.remove(s);  
                    count++;  
                    onlyOne.put(s, count);  
                }  
            }  
            mapList.add(onlyOne);  
        }  
        return mapList;  
    }  
  
    /** 
     * ���������ƴ��������ϲ�����(�Ƽ�ʹ��) 
     *  
     * @return 
     */  
    private static String parseTheChineseByObject(  
            List<Map<String, Integer>> list) {  
        Map<String, Integer> first = null; // ����ͳ��ÿһ��,�����������  
        // ����ÿһ�鼯��  
        for (int i = 0; i < list.size(); i++) {  
            // ÿһ�鼯������һ����ϵ�Map  
            Map<String, Integer> temp = new Hashtable<String, Integer>();  
            // ��һ��ѭ����firstΪ��  
            if (first != null) {  
                // ȡ���ϴ������˴μ��ϵ��ַ���������  
                for (String s : first.keySet()) {  
                    for (String s1 : list.get(i).keySet()) {  
                        String str = s + s1;  
                        temp.put(str, 1);  
                    }  
                }  
                // ������һ���������  
                if (temp != null && temp.size() > 0) {  
                    first.clear();  
                }  
            } else {  
                for (String s : list.get(i).keySet()) {  
                    String str = s;  
                    temp.put(str, 1);  
                }  
            }  
            // ������������Ա��´�ѭ��ʹ��  
            if (temp != null && temp.size() > 0) {  
                first = temp;  
            }  
        }  
        String returnStr = "";  
        if (first != null) {  
            // ����ȡ������ַ���  
            for (String str : first.keySet()) {  
                returnStr += (str + ",");  
            }  
        }  
        if (returnStr.length() > 0) {  
            returnStr = returnStr.substring(0, returnStr.length() - 1);  
        }  
        return returnStr;  
    }
  
}  
