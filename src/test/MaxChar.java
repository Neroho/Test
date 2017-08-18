/**
 * 
 */
package test;


/**
 * @author Nero
 * @创建日期:2016-8-30
 */
public class MaxChar {
	private  int max;
	private  char[] c;
	
	public void getMaxChar(String str)
	{
		char cm = '0';
		c= str.toCharArray();
		System.out.println(System.currentTimeMillis());
		for(int i = 0;i < str.length();i++)
		{
			int count = 1;
			for(int j = i+1 ; j < str.length();j++)
			{
				if(c[i] == c[j])
				{
					count++;
				}
				if(max < count)
				{
					max = count;
					cm = c[i];
				}
			}		
		}
		System.out.println(System.currentTimeMillis());
		System.out.println("in string " + cm + " is the max char , it has " + max 
				+ " times in the string");
		
	}
	public static void main(String[] args) {
		MaxChar mc = new MaxChar();
		String str = "abcdefghijklmnopqrstuvwxyzasdfghjklqwertyuiopzxcvbnmqzawsxedcrfvtgbyhnumjik,opxdrcfvtgybhunjimkgvbhjnkml,;cfvgbhnjmk,xsdcfvgbhnj mkasdfdakjhcbauicbanbrfjhabocubalrnfbaluiclanbdvabueblcnablyabfvajbnaoyrvdbkahvobrvjbnabfvejffladnfljaskdhfqiuehkjncclkajxncp	iheprjadlknqpeiuhakcv,mnbiiquhpfqhejknldvlaiuriuerhjanlvknprhpialvblvuipuhjlakvbliufhpiuhfajnurhpuhrpajbjqvljbald" +
					 "ajhdfiuhrfjafiubrijabdlkjpiu";
		mc.getMaxChar(str);
	}
	
	
	
	
}
