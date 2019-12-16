package com.tjx1014.listener;

import java.util.Timer;
import java.util.TimerTask;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import weixin.popular.api.TicketAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Ticket;
import weixin.popular.bean.Token;

import weixin.popular.config.WeixinConfig;

@Component//被spring容器管理
@Order(1)//如果多个自定义ApplicationRunner，用来标明执行顺序
public class WeixinGetAccessTokenListen  implements ApplicationRunner {

	
	public static String access_token = null;
	public static String jsTicket  = null;



	@Value("${weixin.appId}")
	private String appId;

	@Value("${weixin.appsecret}")
	private String appsecret;

	@Value("${weixin.backUrl}")
	private String backUrl;


	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println(appId);
		//加载配置
		WeixinConfig.APPID = appId;
		WeixinConfig.APPSECRET = appsecret;
		WeixinConfig.url = backUrl;

		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				String appid = WeixinConfig.APPID;
				String secret = WeixinConfig.APPSECRET;
				Token token = new TokenAPI().token(appid, secret);
				access_token = token.getAccess_token();
				//System.out.println("监听器中定时取access_token --------------"+access_token);
				Ticket ticket = new TicketAPI().getTicket(access_token);

				jsTicket = ticket.getTicket(); //jssdk中要用到

			}}, 0, 600*1000);

	}
}
