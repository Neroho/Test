package designMode.prototype;

import digitalDigest.messageDigest.SHA;
import org.apache.hadoop.util.hash.Hash;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @描述
 * 带原型管理器的原型模式
 * @创建人 Nero
 * @创建时间 2021/3/7
 * @see
 * @since
 */
public class ProtoTypeShape {
    public static void main(String[] args) {
        ProtoTypeManager pm = new ProtoTypeManager();
        Shape obj1 = (Circle) pm.getShape("Circle");
        obj1.countArea();
        Shape obj2 = (Shape) pm.getShape("Square");
        obj2.countArea();
    }
}

interface Shape extends Cloneable {
    //拷贝
    Object clone();
    //计算面积
    void countArea();
}

class Circle implements Shape {

    @Override
    public Object clone() {
        Circle w = null;
        try{
            w = (Circle) super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.out.println("拷贝圆失败!");
        }
        return w;
    }

    @Override
    public void countArea() {
        int r = 0;
        System.out.println("这是一个圆，请输入圆的半径：");
        Scanner input = new Scanner(System.in);
        r = input.nextInt();
        System.out.println("圆的面积=" + 3.1415 * r * r );
    }
}

class Square implements Shape {

    @Override
    public Object clone() {
        Square b = null;
        try {
            b = (Square) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("拷贝正方形失败!");
        }
        return b;
    }

    @Override
    public void countArea() {
        int a = 0;
        System.out.print("这是一个正方形，请输入它的边长：");
        Scanner input = new Scanner(System.in);
        a = input.nextInt();
        System.out.println("该正方形的面积=" + a * a + "\n");
    }
}

class ProtoTypeManager {
    private HashMap<String,Shape> hm = new HashMap<>();

    public ProtoTypeManager() {
        hm.put("Circle",new Circle());
        hm.put("Square",new Square());
    }

    public void addShape(String key,Shape obj) {
        hm.put(key,obj);
    }

    public Shape getShape(String key){
        return (Shape) hm.get(key);
    }
}