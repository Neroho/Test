package designMode.createMode.singleton;

/**
 * @描述
 * 双重检查锁模式
 * 懒汉模式的变种，线程安全,且性能更优
 * @创建人 Nero
 * @创建时间 2021/3/6
 * @see
 * @since
 */
public class DoubleCheckSingleton {
    private volatile static DoubleCheckSingleton instance;
    private DoubleCheckSingleton() {}

    public static DoubleCheckSingleton getInstance() {
        if(instance == null){
            synchronized (DoubleCheckSingleton.class) {
                if(instance == null) {
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }
}
