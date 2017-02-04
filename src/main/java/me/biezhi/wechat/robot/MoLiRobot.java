package me.biezhi.wechat.robot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSONKit;
import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.Constant;

public class MoLiRobot implements Robot {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Robot.class);

	private String apiUrl;

	public MoLiRobot() {
		String api_key = Constant.config.get("itpk.api_key");
		String api_secret = Constant.config.get("itpk.api_secret");
		if(StringKit.isNotBlank(api_key) && StringKit.isNotBlank(api_secret)){
			this.apiUrl = Constant.ITPK_API + "?api_key=" + api_key + "&api_secret=" + api_secret;
		}
	}

	@Override
	public String talk(String msg) {
		if(null == this.apiUrl){
			return "机器人未配置";
		}
		String url = apiUrl + "&question=" + msg;
		String result = HttpRequest.get(url).connectTimeout(3000).body();
		
		if (msg.indexOf(Constant.XIAO_HUA) != -1) {
			//出现bug，字符串转char数组时第一个为空字符
			StringBuilder sBuilder = new StringBuilder(result);
			sBuilder.deleteCharAt(0);
			
			JSONObject jsonresult = JSONKit.parseObject(sBuilder.toString());
			String title = jsonresult.getString("title");
			String content = jsonresult.getString("content");
			result = "标题: "+title+"\r\n"+content;
		}
		
		return result;
	}

}
