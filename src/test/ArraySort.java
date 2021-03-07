package test;

public class ArraySort {
    public static void main(String[] args) {
        int maxValue = 0;
        for(int i =0 ; i < 8 ; i++){
            for(int j = 0 ; j < 8; j ++){
                if((i*7 + j*2) <= 50 && (i + j * 3) <=23)
                {
                    int tmp = i*30000 + j * 20000;
                    maxValue = Math.max(maxValue,tmp);
                    System.out.println("最大值为：" +maxValue +",当前值为："+tmp +" ,需要兑换 "+ i + " 个心心相印 兑换" + j+" 个盛典现场");
                    if(maxValue == tmp)
                        System.out.println("最大值为：" +maxValue +",需要兑换 "+ i + " 个心心相印 兑换" + j+" 个盛典现场");
                }
            }
        }
    }


}
