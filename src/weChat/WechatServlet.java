package weChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import test.CheckUtil;

public class WechatServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String nocne=request.getParameter("signature");
		String echostr=request.getParameter("echostr");
		if(CheckUtil.checkSignatur(signature,timestamp,nocne)){
			out.println(echostr);
		}
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> map;
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			map = MessageUtil.xmlToMap(request);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			String message = null;
			if(MessageUtil.MESSAGFE_TEXT.equals(msgType)){
				if("1".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstText());
				}else if ("2".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondText());
				}else if ("3".equals(content)){
					message = MessageUtil.initNewsMessage(toUserName, fromUserName);
				}else if ("?".equals(content) || "？".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}
//				TextMessage text = new TextMessage();
//				text.setFromUserName(toUserName);
//				text.setToUserName(fromUserName);
//				text.setMsgType(msgType);
//				text.setContent("您发送的消息是："+content);
//				message = MessageUtil.textToXml(text);
			}else if(MessageUtil.MESSAGFE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGFE_SUBSCRIBE.equals(msgType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}
			}
			out.println(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
}
