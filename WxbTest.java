package test;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//https://t.wxb.com/
public class WxbTest {

	private static String cookieStr = null;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static String[] maintype={
		"时事资讯",
		"数码科技",
		"汽车",
		"房产家居",
		"财经理财",
		"生活",
		"情感励志",
		"女性时尚",
		"旅行",
		"运动健康",
		"餐饮美食",
		"搞笑娱乐",
		"明星影视",
		"母婴",
		"文化教育",
		"政务"
	};
	private static String[] ids={
		"1",
		"2",
		"3",
		"4",
		"6",
		"7",
		"8",
		"9",
		"10",
		"11",
		"12",
		"13",
		"14",
		"15",
		"16",
		"18"
	};
	private static int[] pages={
		50,
		4,
		4,
		4,
		10,
		50,
		50,
		50,
		4,
		40,
		16,
		50,
		16,
		15,
		50,
		6
	};

	private static List<NameValuePair> headers = new ArrayList();
	static {
		cookieStr = "PHPSESSID=kbfkc5ho454imubei8cs9b5g75;Hm_lvt_5859c7e2fd49a1739a0b0f5a28532d91=1516938480;Hm_lpvt_5859c7e2fd49a1739a0b0f5a28532d91=1516938480";
		headers.add(new BasicNameValuePair("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36"));
		 headers.add(new BasicNameValuePair("referer",
		 "https://account.wxb.com/"));
		headers.add(new BasicNameValuePair("cookie", cookieStr));
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		login();
		getFirstData();
		getData("-1", "month", "2017-12-15", 1);
	}

	public static void login() {
		String loginUrl = "https://account.wxb.com/login";
		headers.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
		headers.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
		headers.add(new BasicNameValuePair("referer", "https://account.wxb.com/?from=%2F%2Ft.wxb.com"));
		String post = post(loginUrl, "email=18513031949&password=123456", headers);
		headers.remove(2);
		headers.remove(2);
		System.out.println(post);

	}

	public static void getFirstData() throws ClientProtocolException, IOException {
		headers = new ArrayList<>();
		headers.add(new BasicNameValuePair("accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
		headers.add(new BasicNameValuePair("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36"));
		headers.add(new BasicNameValuePair("cookie", cookieStr));
		String url = "https://data.wxb.com/rank?category=-1&page=1";
		headers.add(new BasicNameValuePair("referer", url));
		CloseableHttpResponse resp = getResp(url, headers);
		Header[] allHeaders = resp.getAllHeaders();
		for (Header header : allHeaders) {
			if (header.getName().equals("Set-Cookie")) {
				System.out.println(header.getValue());
				cookieStr = header.getValue().split(" ")[0] + cookieStr;
			}
		}
	}

	// sign 值为 day, week, moth , date为开始时间 dateTime 格式为2018-01-15
	public static void getData(String type, String sign, String dateTime, int page) {
//		String url = "https://data.wxb.com/rank/" + sign + "/";
//		switch (sign) {
//		case "day":
//			url += dateTime;
//			break;
//		case "week":
//			Calendar calendar = Calendar.getInstance();
//			String[] split = dateTime.split("-");
//			calendar.set(calendar.YEAR, Integer.parseInt(split[0]));
//			calendar.set(calendar.MONTH, Integer.parseInt(split[1]));
//			calendar.set(calendar.DAY_OF_MONTH, Integer.parseInt(split[2]) + 6);
//			url = url + dateTime + "/" + calendar.get(calendar.YEAR) + "-"
//					+ (calendar.get(calendar.MONTH) > 10 ? calendar.get(calendar.MONTH)
//							: "0" + calendar.get(calendar.MONTH))
//					+ "-" + (calendar.get(calendar.DAY_OF_MONTH) > 10 ? calendar.get(calendar.DAY_OF_MONTH)
//							: "0" + calendar.get(calendar.DAY_OF_MONTH));
//			break;
//		case "month":
//			url += dateTime.substring(0, dateTime.lastIndexOf("-"));
//			break;
//		default:
//			break;
//		}
//		url += "/" + type + "?sort=&page=" + page + "&page_size=20";
//		System.err.println(url);
//		String url="https://data.wxb.com/rank/day/2018-01-25/1?sort=&page=1&page_size=20";
		headers = new ArrayList<>();
		headers.add(new BasicNameValuePair("accept", "application/json, text/plain, */*"));
		headers.add(new BasicNameValuePair("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36"));
		headers.add(new BasicNameValuePair("cookie", cookieStr));
		headers.add(new BasicNameValuePair("x-requested-with", "XMLHttpRequest"));
		headers.add(new BasicNameValuePair("referer", "https://data.wxb.com/"));
		
		for(int i=0;i<maintype.length;i++){
			System.out.println(maintype[i]);
			for(int j=0;j<=pages[i];j++){
				String url="https://data.wxb.com/rank/day/2018-01-25/"+ids[i]+"?sort=&page="+(j+1)+"&page_size=20";
				String string = get(url, headers);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject fromObject = JSONObject.fromObject(string);
				JSONArray jsonArray = fromObject.getJSONArray("data");
				for (Object object : jsonArray) {
					JSONObject object2 = (JSONObject) object;
					System.err.println("\t"+object2.getString("name")+"\t"+object2.getString("wx_alias"));
				}
			}
		}
//		String string = get(url, headers);
//		JSONObject fromObject = JSONObject.fromObject(string);
//		JSONArray jsonArray = fromObject.getJSONArray("data");
//		for (Object object : jsonArray) {
//			JSONObject object2 = (JSONObject) object;
//			System.err.println("\t"+object2.getString("name")+"\t"+object2.getString("wx_alias"));
//		}
		
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static String post(String url, String params, List<NameValuePair> headers) {
		String content = "";
		// 客户请求超时配置
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10 * 1000)
				.setConnectTimeout(10 * 1000).setConnectionRequestTimeout(10 * 1000)
				.setStaleConnectionCheckEnabled(true).build();
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(defaultRequestConfig);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		String[] splitParams = params.split("&");
		if (!(splitParams.length == 1 && splitParams[0].equals(""))) {
			for (int i = 0; i < splitParams.length; i++) {
				String[] pair = splitParams[i].split("=", 2);
				formparams.add(new BasicNameValuePair(pair[0], pair[1]));
			}
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams);
			httppost.setEntity(uefEntity);
			for (NameValuePair header : headers) {
				httppost.addHeader(header.getName(), header.getValue());
			}
//			System.out.print("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			System.out.println("\t" + response.getStatusLine());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private static String get(String url, List<NameValuePair> headers) {
		String content = "";
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.custom().build();
		// 创建httppost
		HttpGet httpget = new HttpGet(url);
		// 创建参数队列
		try {
			for (NameValuePair header : headers) {
				httpget.addHeader(header.getName(), header.getValue());
			}
			CloseableHttpResponse response = httpclient.execute(httpget);
//			System.out.println("\t" + response.getStatusLine());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private static CloseableHttpResponse getResp(String url, List<NameValuePair> headers)
			throws ClientProtocolException, IOException {
		String content = "";
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.custom().build();
		// 创建httppost
		HttpGet httpget = new HttpGet(url);
		// 创建参数队列
		for (NameValuePair header : headers) {
			httpget.addHeader(header.getName(), header.getValue());
		}
		CloseableHttpResponse response = httpclient.execute(httpget);
		return response;

	}
}
