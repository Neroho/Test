package designMode.structuralPattern.proxy;

/**
 * 类简述
 * <p>
 * 代理模式
 * </p>
 *
 * @author he.jipeng
 * @version 1.0
 * @Copyright
 * @createDate 2021/3/8
 * @see
 * @since
 */
public class ProxyTest {
    public static void main(String[] args) {
        Proxy proxy = new Proxy();
        proxy.work();
    }
}

//抽象主题
interface Subject {
    void work();
}

class RealSubject implements Subject {
    @Override
    public  void work() {
        System.out.println("realSubject work");
    }
}

class Proxy implements Subject {
    private  RealSubject realSubject;

    @Override
    public void work() {
        if(realSubject == null){
            realSubject = new RealSubject();
        }

        System.out.println("proxy work");
        preWork();
        realSubject.work();
        postWork();
    }

    public void preWork() {
        System.out.println("proxy preWork");
    }

    public void postWork() {
        System.out.println("proxy postWork");
    }
}