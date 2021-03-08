package designMode.createMode.prototype;

/**
 * @描述
 * 原型模式
 * @创建人 Nero
 * @创建时间 2021/3/7
 * @see
 * @since
 */
public class PrototypeTest {
    public static void main(String[] args) {
        Realizetype obj1 = new Realizetype();
        try {
            Realizetype obj2 = (Realizetype) obj1.clone();
            System.out.println("obj1 == obj2?" + (obj1 == obj2));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }
}


class Realizetype implements Cloneable {
    Realizetype() {
        System.out.println("具体原型创建成功");
    }

    public Object clone() throws CloneNotSupportedException {
        System.out.println("具体原型复制成功");
        return (Realizetype) super.clone();
    }
}
