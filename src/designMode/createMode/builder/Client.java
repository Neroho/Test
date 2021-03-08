package designMode.createMode.builder;

/**
 * 类简述
 * <p>
 * 类说明、 详细描述(optional)
 * </p>
 *
 * @author he.jipeng
 * @version 1.0
 * @Copyright
 * @createDate 2021/3/8
 * @see
 * @since
 */
public class Client {
    public static void main(String[] args) {
        Builder builder = new ConcreteBuilder();
        Director director = new Director(builder);
        Product product = director.construct();
        product.show();
    }
}
