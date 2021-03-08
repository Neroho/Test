package designMode.createMode.singleton;

/**
 * @描述
 * 饿汉模式
 * 类一旦加载就创建一个单例，保证在调用 getInstance 方法之前单例已经存在了。
 * @创建人 Nero
 * @创建时间 2021/3/6
 * @see
 * @since
 */
public class HungrySingleton {
    private static final HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {

    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
