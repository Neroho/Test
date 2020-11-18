//package weChat.util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import weChat.po.Image;
//import weChat.po.ImageMessage;
//import weChat.po.Music;
//import weChat.po.MusicMessage;
//import weChat.po.News;
//import weChat.po.NewsMessage;
//import weChat.po.TextMessage;
//
//import com.thoughtworks.xstream.XStream;
//
//public class MessageUtil{
//
//
//	public static final String MESSAGE_NEWS = "news";
//	public static final String MESSAGE_TEXT = "text";
//	public static final String MESSAGE_IMAGE = "image";
//	public static final String MESSAGE_VOICE = "voice";
//	public static final String MESSAGE_VIDEO = "video";
//	public static final String MESSAGE_LINK = "link";
//	public static final String MESSAGE_LOCATION = "location";
//	public static final String MESSAGE_EVENT = "event";
//	public static final String MESSAGE_SUBSCRIBE = "subscribe";
//	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
//	public static final String MESSAGE_CLICK = "CLICK";
//	public static final String MESSAGE_VIEW = "VIEW";
//	public static final String MESSAGE_SCANCODE = "scancode_push";
//
//	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException,DocumentException{
//		Map<String,String> map = new HashMap<String,String>();
//		SAXReader reader = new SAXReader();
//
//		InputStream ins = request.getInputStream();
//		Document doc = reader.read(ins);
//
//		Element root = doc.getRootElement();
//
//		List<Element> list = root.elements();
//
//		for (Element e : list) {
//			map.put(e.getName(),e.getText());
//		}
//		ins.close();
//		return map;
//	}
//
//	public static String textToXml(TextMessage textMessage){
//		XStream xStream = new XStream();
//		xStream.alias("xml", xStream.getClass());
//		return xStream.toXML(textMessage);
//	}
//
//	public static String initText(String toUserName,String fromUserName,String content){
//		TextMessage text = new TextMessage();
//		text.setFromUserName(toUserName);
//		text.setToUserName(fromUserName);
//		text.setMsgType(MessageUtil.MESSAGE_TEXT);
//		text.setCreateTime(new Date().getTime());
//		text.setContent(content);
//		return MessageUtil.textToXml(text);
//	}
//	/**
//	 * 主菜单
//	 * @return
//	 */
//	public static String menuText(){
//		StringBuffer sb = new StringBuffer();
//		sb.append("欢迎您的关注，请按照菜单提示进行操作:\n\n");
//		sb.append("1、课程介绍\n");
//		sb.append("2、慕课网介绍\n\n");
//		sb.append("回复？调出此菜单。");
//		return sb.toString();
//	}
//	public static String firstText(){
//		StringBuffer sb = new StringBuffer();
//		sb.append("课程介绍");
//		sb.append("回复？回到主菜单。");
//		return sb.toString();
//	}
//	public static String secondText(){
//		StringBuffer sb = new StringBuffer();
//		sb.append("慕课网介绍");
//		sb.append("回复？回到主菜单。");
//		return sb.toString();
//	}
//
//	public static String newsMessageToXml(NewsMessage newsMessage){
//		XStream xStream = new XStream();
//		xStream.alias("xml", xStream.getClass());
//		xStream.alias("item", new News().getClass());
//		return xStream.toXML(newsMessage);
//	}
//
//	public static String initNewsMessage(String toUserName,String fromUserName)
//	{
//		String message =null;
//		List<News> newsList = new ArrayList<News>();
//
//
//		News news = new News();
//		news.setTitle("图文消息");
//		news.setDescription("第一条图文消息");
//		news.setPicUrl("http://c.hiphotos.baidu.com/image/pic/item/0824ab18972bd407cf293db177899e510eb30994.jpg");
//		news.setUrl("www.baidu.com");
//
//		newsList.add(news);
//
//		NewsMessage newsMessage = new NewsMessage();
//		newsMessage.setFromUserName(toUserName);
//		newsMessage.setToUserName(fromUserName);
//		newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
//		newsMessage.setCreateTime(new Date().getTime());
//		newsMessage.setArticles(newsList);
//		newsMessage.setArticleCount(newsList.size());
//
//
//		return MessageUtil.newsMessageToXml(newsMessage);
//	}
//
//	/**
//	 * 组装图片消息
//	 * @param toUserName
//	 * @param fromUserName
//	 * @return
//	 */
//	public static String initImageMessage(String toUserName,String fromUserName)
//	{
//		String message =null;
//
//
//		Image image = new Image();
//		image.setMediaId("mediaId");
//
//		ImageMessage imageMessage = new ImageMessage();
//		imageMessage.setFromUserName(toUserName);
//		imageMessage.setToUserName(fromUserName);
//		imageMessage.setMsgType(MessageUtil.MESSAGE_IMAGE);
//		imageMessage.setCreateTime(new Date().getTime());
//
//		return MessageUtil.imageMessageToXml(imageMessage);
//	}
//
//	private static String imageMessageToXml(ImageMessage imageMessage) {
//		XStream xStream = new XStream();
//		xStream.alias("xml", xStream.getClass());
//		xStream.alias("item", new Image().getClass());
//		return xStream.toXML(imageMessage);
//	}
//
//	/**
//	 * 组装音乐消息
//	 * @param toUserName
//	 * @param fromUserName
//	 * @return
//	 */
//	public static String initMusicMessage(String toUserName,String fromUserName)
//	{
//		String message =null;
//
//
//		Music music = new Music();
//		music.setThumbMediaId("mediaId");
//		music.setTitle("音乐消息");
//		music.setDescription("第一条音乐消息");
//		music.setMusicURL("http://c.hiphotos.baidu.com/image/pic/item/0824ab18972bd407cf293db177899e510eb30994.jpg");
//		music.setHQMusicUrl("www.baidu.com");
//
//		MusicMessage musicMessage = new MusicMessage();
//		musicMessage.setFromUserName(toUserName);
//		musicMessage.setToUserName(fromUserName);
//		musicMessage.setMsgType(MessageUtil.MESSAGE_IMAGE);
//		musicMessage.setCreateTime(new Date().getTime());
//
//		return MessageUtil.musicMessageToXml(musicMessage);
//	}
//
//	private static String musicMessageToXml(MusicMessage musicMessage) {
//		XStream xStream = new XStream();
//		xStream.alias("xml", xStream.getClass());
//		xStream.alias("item", new Music().getClass());
//		return xStream.toXML(musicMessage);
//	}
//}
