package encrypt.aes;

import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * AES加解密实现类
 *
 * @author zhangmeng
 *
 */
public class AESEncryptParamSVImpl {

    /*
     * AES解密
     */
    public static String decrypt(String data, String key) throws Exception {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)){
            return data;
        }

        return SecurityUtils.decodeAES256HexUpper(data, SecurityUtils.decodeHexUpper(key));
    }


    /*
     * AES加密
     */
    public static String encrypt(String data, String key) throws Exception {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)){
            return data;
        }

        return SecurityUtils.encodeAES256HexUpper(data, SecurityUtils.decodeHexUpper(key));
    }


    public static void main(String[] args) {
        try {
            String key = "d8f92665e236bbbbed5d7be2bebfe867";
//			String content = "570D64A4E746C8AF4B88B93C56ACEF4240A264B682D6F79888F05CFE4E5AE558286CBF80B1C0E5CA80471148F9B760813290984F250020699998DEC286B93299BA0A1D9EEA5C296E3554E837026C034067B3CD5899D3C151C7FC6F3514947180";
//			String encodeStr = encrypt(content, key);
//			System.out.println(encodeStr);
            String str = "DEDE861E8127B4E960FF50BCC2FF20E8227077B225331746E9149D8643553E61A5EA26216ECC8B6B69FCD67D073571D9A7A03ACF48FF59EBE179A52EFA9B4B3755B5A37C8B9FE8D566759A68A73A7E56";
//			str = encodeStr;

            String jsonStr = "{\"appSecret\":\"xx\",\"templateId\":481,\"templateData\":{\"money\":{\"value\":\"第2发送\"},\"receivables\":{\"value\":\"第2发送\"},\"status\":{\"value\":\"第2发送\"},\"details\":{\"value\":\"第2发送\"}},\"linkUrl\":\"https://www.baidu.com/\",\"skipApplet\":\"0\",\"userId\":\"oxHv85iLA5IqF7VTZ2v4uJywQvkg\",\"appId\":44}";
            String decodeStr = decrypt(str, key);
            String encodeStr = encrypt(jsonStr,key);
            System.out.println(decodeStr);
            System.out.println(encodeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public static void jar() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入密钥：");
            String key = scanner.nextLine();
            for (;;) {
                System.out.println("请输入需要进行的操作编码(1:加密，2：解密，3：修改密钥，4：退出");
                String oper = scanner.nextLine();
                if ("1".equals(oper)) {
                    System.out.println("请输入加密的字符串：");
                    String content = scanner.nextLine();
                    System.out.println("当前密钥为：" + key);
                    System.out.println("加密结果为：" + encrypt(content, key));
                } else if ("2".equals(oper)) {
                    System.out.println("请输入解密的字符串：");
                    String content = scanner.nextLine();
                    System.out.println("当前密钥为：" + key);
                    System.out.println("解密结果为：" + decrypt(content, key));
                } else if ("3".equals(oper)) {
                    System.out.println("当前密钥为：" + key);
                    System.out.println("请输入新的密钥：");
                    key = scanner.nextLine();
                    System.out.println("新的密钥为：" + key);
                } else if ("4".equals(oper)) {
                    return;
                } else {
                    System.out.println("输如的操作有误，输入的操作为：" + oper);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
