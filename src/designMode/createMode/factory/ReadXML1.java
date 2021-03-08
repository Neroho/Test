package designMode.createMode.factory;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

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
public class ReadXML1 {
    //该方法用于从XML配置文件中提取具体类类名，并返回一个实例对象
    public static Object getObject() {
        try {
            //创建文档对象
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFactory.newDocumentBuilder();
            Document doc;
            doc = builder.parse(new File("src/designMode/factory/config1.xml"));
            //获取包含类名的文本节点
            NodeList nl = doc.getElementsByTagName("className");
            Node classNode = nl.item(0).getFirstChild();
            String cName = "designMode.createMode.factory." + classNode.getNodeValue();
            //System.out.println("新类名："+cName);
            //通过类名生成实例对象并将其返回
            Class<?> c = Class.forName(cName);
            Object obj = c.newInstance();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
