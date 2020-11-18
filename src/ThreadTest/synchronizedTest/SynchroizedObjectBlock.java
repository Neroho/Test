package ThreadTest.synchronizedTest;

public class SynchroizedObjectBlock implements Runnable {
    static SynchroizedObjectBlock instance = new SynchroizedObjectBlock();
    Object lock1 = new Object();
    Object lock2 = new Object();
    @Override
    public void run() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName()+ "lock1");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "运行结束");
        }

        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName()+ "lock2");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "运行结束");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        while(t1.isAlive() || t2.isAlive()){

        }
    }
}
