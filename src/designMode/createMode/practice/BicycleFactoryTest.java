package designMode.createMode.practice;

import java.math.BigDecimal;

/**
 * 类简述
 * <p>
 * 创造型模式实践
 * </p>
 *
 * @author he.jipeng
 * @version 1.0
 * @Copyright
 * @createDate 2021/3/8
 * @see
 * @since
 */
public class BicycleFactoryTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Bicycle a;
        String cName = "designMode.createMode.practice.YadeaFactory";
        BicycleFactory bf = (BicycleFactory) Class.forName(cName).newInstance();
        bf.bicycle().show();
        bf.eletricBicycle().show();
    }
}

interface Bicycle {
    public void show();
}


class AimaBicycle implements Bicycle {
    @Override
    public void show() {
        System.out.println("aimaBicycle show up");
    }
}

class YadeaBicycle implements Bicycle {
    @Override
    public void show() {
        System.out.println("YadeaBicycle show up");
    }
}

class EletricBicycle implements Bicycle {
    @Override
    public void show() {
        System.out.println("EletricBicycle show up");
    }
}
//自行车厂
interface BicycleFactory {
    public Bicycle bicycle();
    public EletricBicycle eletricBicycle();

}
//具体工厂：爱玛工厂
class AimaFactory implements BicycleFactory {
    @Override
    public Bicycle bicycle() {
        System.out.println("爱玛自行车生产了！");
        return new AimaBicycle();
    }

    @Override
    public EletricBicycle eletricBicycle() {
        return null;
    }
}
//具体工厂：雅迪工厂
class YadeaFactory implements BicycleFactory {
    @Override
    public Bicycle bicycle() {
        System.out.println("雅迪自行车生产了！");
        return new YadeaBicycle();
    }

    @Override
    public EletricBicycle eletricBicycle() {
        System.out.println("雅迪电动自行车生产了！");
        return new EletricBicycle();
    }
}