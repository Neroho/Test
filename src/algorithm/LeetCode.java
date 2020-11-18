package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LeetCode {
/*
* 
* 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
示例 1:

输入: "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
示例 2:

输入: "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
示例 3:

输入: "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。

来源：力扣（LeetCode）
链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
*/
	//暴力解法
	 public int lengthOfLongestSubstring(String s) {
	        if(s.equals(" ") || s.length() == 1){
	            return 1;
	        }
	        if(s == null || s.equals("")){
	            return 0;
	        }
	        StringBuffer sBuffer = new StringBuffer();
	        sBuffer.append(s.charAt(0));
	        List<String> list = new ArrayList();
	        int len = s.length();
	        int lager = 0;
	        for(int i = 1 ;i < len ; i++){
	            char a = s.charAt(i);
	            int x = 0;
	            while(x < sBuffer.length()){
	                if(a == sBuffer.charAt(x)){
	                    list.add(String.valueOf(sBuffer));
	                    if(lager < sBuffer.length()){
	                        lager = sBuffer.length();
	                    }
	                    sBuffer.delete(0,x + 1 );
	                    sBuffer.append(a);
	                    break;
	                }else if(x == sBuffer.length() - 1){
	                    sBuffer.append(a);
	                    x ++;
	                }
	                x ++;
	            }
	            if(i == len -1){
	            	list.add(String.valueOf(sBuffer));
	            	if(lager < list.get(list.size()-1).length()){
	            		lager = list.get(list.size()-1).length();
	            	}
	            }
	        }
	        
	        return lager;
	    }
	 //滑动窗口 分段处理 找出最长的对 左边界在有重复是右移，右边界持续右移  需要持续到整个字符串遍历完
	 public int lengthOfLongestSubstring1(String s) {
	        Set<Character> set = new HashSet();
	        int len = s.length();
	        int lager = 0,i = 0,j = 0;
	        
	        while(i < len && j < len){
	            if(!set.contains(s.charAt(j))){
	            	//不重复的字符加入到set中，右边界右移，重新计算不重复字符串的最大值
	                set.add(s.charAt(j++));
	                lager = Math.max(lager, j - i);
	            }else{
	            	//将重复的字符从set中移除，然后左边界向右移动一位
	                set.remove(s.charAt(i++));
	            }
	        }
	        return lager;
	    }
	 //优化滑动窗口
	 public int lengthOfLongestSubstring2(String s) {
	        Map<Character,Integer> map = new HashMap();
	        int len = s.length();
	        int lager = 0;
	        s.indexOf(s.charAt(4),5);
	        for(int i = 0 , j = 0 ; j < len ;j ++){
	            if(map.containsKey(s.charAt(j))){
	                i = Math.max(map.get(s.charAt(j)), i);
	            }
	            System.out.println(i);
	            lager = Math.max(lager, j - i + 1);
	            map.put(s.charAt(j), j+1);
	        }
	        return lager;
	    }
	 
	 public int lengthOfLongestSubstring3(String s) {
			int i = 0;
			int flag = 0;
			int length = 0;
			int result = 0;
			while (i < s.length()) {
				int pos = s.indexOf(s.charAt(i),flag);
				System.out.println(pos+ " " + i + " " +  flag + " " + length + " " + result);
				
				if (pos < i) {
					if (length > result) {
						result = length;
					}
					if (result >= s.length() - pos - 1) {
						return result;
					}
					length = i - pos - 1;
					flag = pos + 1;
				}
				length++;
				i++;
			}
			return length;
		}
	 
	 public double findMedianSortedArrays(int[] nums1, int[] nums2) {
	        int m1 = nums1 != null ? nums1.length:0;
	        int m2 = nums2 != null ? nums2.length:0;
	        int len = m1 + m2;
	        int mind = len % 2 ;
		        double result = len / 2;
		        List<Integer> list = new ArrayList();
		        for(int i =0; i < m1;i++){
		                list.add(nums1[i]);
		        }
	            for(int i =0; i < m2;i++){
		                list.add(nums2[i]);
		        }
		        list.sort(null);
		        if(mind == 0){
		        	mind = (int)result;
	                System.out.println(list.get(mind-1));
	                System.out.println(list.get(mind));
		            result =(list.get(mind-1) + list.get(mind)) / 2.0;
		        }else{
		        	mind = (int) Math.ceil(result);
		            result = list.get(mind);
		        }

	        return result;
	    }
	 
	 public double findMedianSortedArrays1(int[] nums1, int[] nums2) {
	        int len = nums1.length + nums2.length;
			int pre = 0;
			int right = 0;
			int mid = len / 2;//题目中有序数组 保证了中间的数字在奇偶情况中都是中位数 
			int index1 = 0;
			int index2 = 0;
			for(int i = 0; i <= mid; i++) {
				pre = right;
				if(index1 < nums1.length && (index2 >= nums2.length || nums1[index1] < nums2[index2])) {
					right = nums1[index1];
					index1++;
				} else {
					right = nums2[index2];
					index2++;
				}
			}
			double result = 0;
			if(len % 2 == 1) {
				result = right;
			} else {
				result = (right + pre) / 2.0;
			}
			return result;
	    }
	 
	 public String longestPalindrome(String s) {
		    if (s == null || s.length() < 1) return "";
		    int start = 0, end = 0;
		    for (int i = 0; i < s.length(); i++) {
		        int len1 = expandAroundCenter(s, i, i);
		        int len2 = expandAroundCenter(s, i, i + 1);
		        int len = Math.max(len1, len2);
		        if (len > end - start) {
		            start = i - (len - 1) / 2;
		            end = i + len / 2;
		        }
		    }
		    return s.substring(start, end + 1);
		}

		private int expandAroundCenter(String s, int left, int right) {
		    int L = left, R = right;
		    while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
		        L--;
		        R++;
		    }
		    return R - L - 1;
		}
		
		public String longestPalindrome1(String s) {
		    int length = s.length();
		    boolean[][] P = new boolean[length][length];
		    int maxLen = 0;
		    String maxPal = "";
		    for (int len = 1; len <= length; len++) //遍历所有的长度
		        for (int start = 0; start < length; start++) {
		            int end = start + len - 1;
		            if (end >= length) //下标已经越界，结束本次循环
		                break;
		            P[start][end] = (len == 1 || len == 2 || P[start + 1][end - 1]) && s.charAt(start) == s.charAt(end); //长度为 1 和 2 的单独判断下
		            if (P[start][end] && len > maxLen) {
		                maxPal = s.substring(start, end + 1);
		            }
		        }
		    return maxPal;
		}
		public String longestPalindrome4(String s) {
	        if (s == null || s.length() == 0) {
	            return "";
	        }
//	         保存起始位置，测试了用数组似乎能比全局变量稍快一点
	        int[] range = new int[2];
	        char[] str = s.toCharArray();
	        for (int i = 0; i < s.length(); i++) {
//	             把回文看成中间的部分全是同一字符，左右部分相对称
//	             找到下一个与当前字符不同的字符
	            i = findLongest(str, i, range);
	        }
	        return s.substring(range[0], range[1] + 1);
	    }
	    
	    public static int findLongest(char[] str, int low, int[] range) {
//	         查找中间部分
	        int high = low;
	        while (high < str.length - 1 && str[high + 1] == str[low]) {
	            high++;
	        }
//	         定位中间部分的最后一个字符
	        int ans = high;
//	         从中间向左右扩散
	        while (low > 0 && high < str.length - 1 && str[low - 1] == str[high + 1]) {
	            low--;
	            high++;
	        }
//	         记录最大长度
	        if (high - low > range[1] - range[0]) {
	            range[0] = low;
	            range[1] = high;
	        }
	        return ans;
	    }
	    
	public String longestPalindrome2(String s) {
        int len = s.length();
        len = len > 1000 ? 1000 : len;
        if(len <= 1){
            return s;
        }
        int larger = 0;
        String result = "";
        for(int i = 0 ; i < len ; i ++){
            for(int j = i + 1; j < len;j ++){
                if(s.charAt(i) == s.charAt(j) && (j -i + 1 > larger)){
                    for(int m = i , n = j  ;m <= n ;m ++){
                        if(s.charAt(m) == s.charAt(n) && (m == n || m == n - 1)){
                            if(larger < j - i + 1){
                                larger = j - i + 1;
                                result = s.substring(i,j+1); 
                            }
                        }else if(s.charAt(m) != s.charAt(n)){
                            break;
                        }
                        n--;
                    }
                }else if(j == len -1){
                    if(larger < 1){
                        larger = 1;
                        result = s.substring(0,1); 
                    }
                }
            }
        }
            return result;
    }
	 
	public static void main(String[] args) {
		int a = (new LeetCode()).lengthOfLongestSubstring3( "absngjslduwhtnx");
		System.out.println(a);
	}
}
