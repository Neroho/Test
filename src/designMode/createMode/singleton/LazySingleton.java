package designMode.createMode.singleton;

/**
 * @描述
 * 懒汉模式
 * 类加载时没有生成单例，只有当第一次调用 getlnstance 方法时才去创建这个单例
 * @创建人 Nero
 * @创建时间 2021/3/6
 * @see
 * @since
 */
public class LazySingleton {
    //保证 instance 在所有线程中同步
    private static volatile LazySingleton instance = null;

    private LazySingleton() {
        //private 避免类在外部被实例化
    }

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            return new LazySingleton();
        }
        return instance;
    }
}
