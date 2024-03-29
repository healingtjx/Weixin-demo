package weixin.popular.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import weixin.popular.bean.QrcodeTicket;
import weixin.popular.bean.Token;
import weixin.popular.client.JsonResponseHandler;
import weixin.popular.config.WeixinConfig;


/**
 * 二维码API
 * @author LiYi
 *
 */
public class QrcodeAPI extends BaseAPI{


	public static void main(String[] args) throws UnsupportedEncodingException {
		Token token = new TokenAPI().token(WeixinConfig.APPID, WeixinConfig.APPSECRET);
		QrcodeTicket qrcodeTicket = new QrcodeAPI().qrcodeCreate(token.getAccess_token(), "{\"expire_seconds\": 1800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}");
		String url = new QrcodeAPI().showqrcode(qrcodeTicket.getTicket());
		System.out.println(url);
	}
	
	public static String create(String id){
		
		Token token = new TokenAPI().token(WeixinConfig.APPID, WeixinConfig.APPSECRET);
		QrcodeTicket qrcodeTicket = new QrcodeAPI().qrcodeCreate(token.getAccess_token(), "{\"expire_seconds\": 1800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}");
//		
	
		return new QrcodeAPI().showqrcode(qrcodeTicket.getTicket());
//		return null;
	}
	
	
	
	/**
	 * 创建二维码
	 * @param access_token
	 * @param qrcodeJson json 数据 例如{"expire_seconds": 1800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
	 * @return
	 */
	private QrcodeTicket qrcodeCreate(String access_token,String qrcodeJson){
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(jsonHeader)
										.setUri(BASE_URI+"/cgi-bin/qrcode/create")
										.addParameter("access_token", access_token)
										.setEntity(new StringEntity(qrcodeJson,Charset.forName("utf-8")))
										.build();
		return localHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(QrcodeTicket.class));
	}

	/**
	 * 创建二维码
	 * @param access_token
	 * @param expire_seconds 	该二维码有效时间，以秒为单位。 最大不超过1800秒。
	 * @param action_name		二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
	 * @param scene_id			场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	 * @return
	 */
	private QrcodeTicket qrcodeCreate(String access_token,Integer expire_seconds,String action_name,long scene_id){
		
		return qrcodeCreate(access_token,String.format("{"+(expire_seconds==null?"%1$s":"\"expire_seconds\": %1$s, ")+"\"action_name\": \"%2$s\", \"action_info\": {\"scene\": {\"scene_id\": %3$d}}}",
													expire_seconds==null?"":expire_seconds,action_name,scene_id));
	}

	/**
	 * 创建临时二维码
	 * @param access_token
	 * @param expire_seconds 不超过1800秒
	 * @param scene_id		  场景值ID，32位非0整型
	 * @return
	 */
	public QrcodeTicket qrcodeCreateTemp(String access_token,int expire_seconds,long scene_id){
		return qrcodeCreate(access_token,expire_seconds,"QR_SCENE",scene_id);
	}

	/**
	 * 创建持久二维码
	 * @param access_token
	 * @param scene_id	场景值ID 1-100000
	 * @return
	 */
	public QrcodeTicket qrcodeCreateFinal(String access_token,int scene_id){
		return qrcodeCreate(access_token,null,"QR_LIMIT_SCENE",scene_id);
	}

	/**
	 * 下载二维码
	 * 视频文件不支持下载
	 * @param ticket  内部自动 UrlEncode
	 * @return
	 */
	public String showqrcode(String ticket){
		HttpUriRequest httpUriRequest = RequestBuilder.get()
				.setUri(QRCODE_DOWNLOAD_URI + "/cgi-bin/showqrcode")
				.addParameter("ticket", ticket)
				.build();
		return httpUriRequest.getURI().toString();
		//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
		/*try {
			URL reqURL = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket ); //创建URL对象
			HttpsURLConnection httpsConn = (HttpsURLConnection)reqURL.openConnection();
			InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream());

			 //读取服务器的响应内容并显示
			int respInt = insr.read();
			 while( respInt != -1){
			System.out.print((char)respInt);
			respInt = insr.read();
			 }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
