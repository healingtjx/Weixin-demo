package com.tjx1014.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tjx1014.listener.WeixinGetAccessTokenListen;
import com.tjx1014.util.URLManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import weixin.popular.api.MenuAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.EventMessage;
import weixin.popular.bean.MenuButtons;
import weixin.popular.bean.MenuButtons.Button;
import weixin.popular.bean.User;
import weixin.popular.bean.message.TextMessage;
import weixin.popular.util.XMLConverUtil;


/**
 * 微信开发首页
 * 
 * @author lgh
 * 
 * 
 */
// 创建对象
@Controller
// 形成映射
@RequestMapping("/weixin")
public class WeinxinIndexController {


	/**
	 * 刷新菜单
	 * @return
	 */
	@ResponseBody
	@GetMapping("/update_menu")
	public String updateMenu(){
		Button btnOne = new Button();// 第一个按钮
		btnOne.setType("view");
		btnOne.setName("百度");
		btnOne.setKey("MENU_ONE");
		btnOne.setUrl("https://www.baidu.com");

		Button btnTwo = new Button();// 第一个按钮
		btnTwo.setType("view");
		btnTwo.setName("谷歌");
		btnTwo.setKey("MENU_ONE");
		btnTwo.setUrl("https://www.google.cn");

		Button btnThree = new Button();// 第一个按钮
		btnThree.setType("view");
		btnThree.setName("必应");
		btnThree.setKey("MENU_ONE");
		btnThree.setUrl("https://cn.bing.com/");

		List list = new ArrayList(); // 多个menu集合

		list.add(btnOne);
		list.add(btnTwo);
		list.add(btnThree);
		MenuButtons menus = new MenuButtons();
		Button[] arrayButton = new Button[list.size()];
		list.toArray(arrayButton);// list转为数组（类型为Button）
		menus.setButton(arrayButton);
		new MenuAPI()
				.menuCreate(WeixinGetAccessTokenListen.access_token, menus);
		return "refresh success";
	}


	@RequestMapping
	public String index(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ServletInputStream inputStream = request.getInputStream();
		String echostr = request.getParameter("echostr");
		// 首次请求申请验证,返回echostr
		if (echostr != null) {
			response.getWriter().print(echostr);
			return null;
		}
		try {
			// 获取公众号ID
			EventMessage eventMessage = XMLConverUtil.convertToObject(
					EventMessage.class, inputStream);


			String msgType = eventMessage.getMsgType();
			String weixinhao = eventMessage.getFromUserName();
			UserAPI userAPI = new UserAPI();
			Integer flag = -1;
			User user = userAPI.userInfo(
					WeixinGetAccessTokenListen.access_token, weixinhao);

			String Event = eventMessage.getEvent();
			// ----------------- 关注-----------------------
			if ("subscribe".equals(Event)) {

			}
			// ----------------- 取消-----------------------
			if ("unsubscribe".equals(Event)) {

			}

			// --------------------智能回答系统-----------------------------------
			if ("text".equals(msgType)) {
				//回复消息
				TextMessage textMessage = new TextMessage(weixinhao, "恭喜调通demo!!");
				MessageAPI messageAPI = new MessageAPI();
				messageAPI.messageCustomSend(WeixinGetAccessTokenListen.access_token, textMessage);
			}
			// -----------------------------------------------------------------

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




	


}
