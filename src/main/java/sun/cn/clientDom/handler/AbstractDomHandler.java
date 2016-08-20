package sun.cn.clientDom.handler;


import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.Header;

import sun.cn.clientDom.proxy.Proxy;

/**
 * 抽象实现类
 * @author sunshine
 * @date 2016年8月20日
 * @desc
 */
public abstract class AbstractDomHandler implements CommonHandler{
	
	private Proxy proxy;
	
	private Header[] cookie;
	

	public AbstractDomHandler(){
		properties.put(RETRY_KEY, 3);
		properties.put(CONNECT_TIME_OUT, 5000);
		properties.put(SO_TIME_OUT, 8000);
	}
	
	@Override
	public void setProperties(String key, Object value) {
		properties.put(key, value);
	}
	
	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
	
	public void addGetHeader(HttpGet http,String key,String value){
		http.addHeader(key, value);
	}
	
	public void addPostHeader(HttpPost http,String key,String value){
		http.addHeader(key, value);
	}

	public void setHeaders(String key,Object value){
		headers.put(key, value);
	}

	public Map<String,Object> getHeaders(){
		return headers;
	}
	
	public Header[] getCookie() {
		return cookie;
	}

	public void setCookie(Header[] cookie) {
		this.cookie = cookie;
	}
}
