//package weChat.util;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.util.List;
//import java.util.Map;
//
//import weChat.menu.Button;
//import weChat.menu.ClickButton;
//import weChat.menu.Menu;
//import weChat.menu.ViewButton;
//import weChat.po.AccessToken;
//
//public class WeChatUtil {
//	private static final String APPID= "AppId";
//	private static final String APPSECRET= "APPSECRET";
//
//	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi.bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
//	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
//	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
//	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
//	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
//
//	/**
//	 * get请求
//	 * @param url
//	 * @return
//	 */
//	public static JSONObject doGetStr(String url){
//		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpGet httpGet = new HttpGet(url);
//		JSONObject jsonObject = null;
//		try {
//			HttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			if(entity != null){
//				String result = EntityUtils.toString(entity,"UTF-8");
//				jsonObject = JSONObject.fromObject(result);
//			}
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return jsonObject;
//	}
//	/**
//	 * post请求
//	 * @param url
//	 * @param outStr
//	 * @return
//	 */
//	public static JSONObject doPostStr(String url,String outStr){
//		DefaultHttpClient httpClient = new DefaultHttpClient();
//		HttpPost httpPost = new HttpPost(url);
//		JSONObject jsonObject = null;
//		try {
//			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
//			HttpResponse response = httpClient.execute(httpPost);
//			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
//			jsonObject = JSONObject.fromObject(result);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return jsonObject;
//	}
//
//	public static AccessToken getAccessToken(){
//		AccessToken token = new AccessToken();
//		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
//		JSONObject jsonObject = doGetStr(url);
//		if(jsonObject != null){
//			token.setExpiresIn(jsonObject.getInt("expires_in"));
//			token.setToken(jsonObject.getString("access_token"));
//
//		}
//		return token;
//	}
//
//	public static String upload(String filePath,String accessToken,String type) throws IOException,NoSuchAlgorithmException{
//		File file = new File(filePath);
//		if(!file.exists() || !file.isFile()){
//			throw new IOException("文件不存在");
//		}
//
//		String url = UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE",type);
//
//		URL urlObj = new URL(url);
//		HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
//
//		con.setRequestMethod("POST");
//		con.setDoInput(true);
//		con.setDoOutput(true);
//		con.setUseCaches(false);
//
//		con.setRequestProperty("Connection", "Keep-Alive");
//		con.setRequestProperty("Charset", "UTF-8");
//
//		String BOUNDARY = "------" + System.currentTimeMillis();
//		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("--");
//		sb.append(BOUNDARY);
//		sb.append("\r\n");
//		sb.append("Content-Disposition;form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
//		sb.append("Content-Type:application/octet-stream\r\n\r\n");
//
//		byte[] head = sb.toString().getBytes("UTF-8");
//
//		OutputStream out = new DataOutputStream(con.getOutputStream());
//		out.write(head);
//
//		DataInputStream in = new DataInputStream(new FileInputStream(file));
//		int bytes = 0;
//		byte[] bufferOut = new byte[1024];
//		while((bytes = in.read(bufferOut)) != -1){
//			out.write(bufferOut,0,bytes);
//		}
//
//		in.close();
//
//		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
//
//		out.write(foot);
//		out.flush();
//		out.close();
//
//		StringBuffer buffer = new StringBuffer();
//		BufferedReader reader = null;
//		String result = null;
//		reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		String line = null;
//		while((line = reader.readLine()) != null){
//			buffer.append(line);
//		}
//		if(result == null){
//			result = buffer.toString();
//		}
//		if(reader != null){
//			reader.close();
//		}
//
//		JSONObject jsonObj = JSONObject.fromObject(result);
//		String typeName = "media_id";
//		if(!"image".equals(type)){
//			typeName = type + "_media_id";
//		}
//		String mediaId = jsonObj.getString(typeName);
//		return mediaId;
//	}
//	//组装菜单
//	public static Menu initMenu(){
//		Menu menu = new Menu();
//		ClickButton button11 = new ClickButton();
//		button11.setName("click菜单");
//		button11.setType("click");
//		button11.setKey("11");
//
//		ViewButton button21 = new ViewButton();
//		button21.setName("view菜单");
//		button21.setType("view");
//		button21.setUrl("http://www.baidu.com");
//
//		ClickButton button31 = new ClickButton();
//		button11.setName("扫码事件");
//		button11.setType("scancode_push");
//		button11.setKey("31");
//
//		ClickButton button32 = new ClickButton();
//		button11.setName("地理位置");
//		button11.setType("location_select");
//		button11.setKey("32");
//
//		Button button = new Button();
//		button.setName("菜单");
//		button.setSub_button(new Button[]{button31,button32});
//
//		menu.setButton(new Button[]{button11,button21,button});
//		return menu;
//	}
//
//	public static int createMenu(String token,String menu) throws ParseException,IOException{
//		int result = 0;
//		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
//		JSONObject jsonObject = doPostStr(url,menu);
//		if(jsonObject != null){
//			result = jsonObject.getInt("errcode");
//		}
//
//		return result;
//	}
//
//	public static JSONObject queryMenu(String token) throws ParseException,IOException{
//		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
//		JSONObject jsonObject = doGetStr(url);
//		return jsonObject;
//	}
//
//	public static int deleteMenu(String token) throws ParseException,IOException{
//		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
//		JSONObject jsonObject = doGetStr(url);
//		int result = 0;
//		if(jsonObject != null){
//			result = jsonObject.getInt("errcode");
//		}
//		return result;
//	}
//
//}
