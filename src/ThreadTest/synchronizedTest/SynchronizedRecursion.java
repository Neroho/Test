package ThreadTest.synchronizedTest;

/**
 * 类简述
 * <p>
 * 可重入粒度测试：递归调用本方法
 * </p>
 *
 * @author Nero
 * @version 1.0
 * @Copyright
 * @createDate 2020/11/18 16:11
 */
public class SynchronizedRecursion {
    private int a = 0;

    public static void main(String[] args) {
        SynchronizedRecursion synchronizedRecursion = new SynchronizedRecursion();
        synchronizedRecursion.method2();
    }

    private synchronized  void method1() {
        System.out.println("method1:"+a);
        a++;
        if(a > 3)
            return;
        method1();
    }

    private synchronized  void method2() {
        System.out.println("method2:"+a);
        a++;
        method1();
    }
}
