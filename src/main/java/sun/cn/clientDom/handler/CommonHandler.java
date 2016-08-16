package sun.cn.clientDom.handler;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.jsoup.nodes.Document;

import com.google.common.collect.Maps;

import sun.cn.clientDom.proxy.Proxy;

/**
 * 公用抽取转换接口
 * @author sunshine
 * @date 2016年8月17日
 * @desc
 */
public interface CommonHandler {
	
	final Calendar sf=Calendar.getInstance();
	/**默认字符集*/
	static final String DEFAULT_CHARSET="utf-8";
	/**重试次数*/
	static final String RETRY_KEY="retry";
	/**连接超时*/
	static final String CONNECT_TIME_OUT="connectTimeOut";
	
	static final String SO_TIME_OUT="soTimeOut";
	/**来源*/
	static final String REFERER="Referer";
	
	static final String USER_AGENT="User-Agent";
	/**参数集合*/
	final Map<String,Object> properties=Maps.newConcurrentMap();
	/**head头信息参数集合*/
	final Map<String,Object> headers=Maps.newConcurrentMap();
	
	/**
	 * 
	 * @Description: 获取请求页面文本
	 * @param url 链接地址
	 * @param charset 文件编码
	 * @return
	 * @return InputStream
	 */
	String getStrFromUrl(String url,String charset);
	
	/**
	 * 
	 * @Description: post方式获取请求数据 
	 * @param url
	 * @param proxy
	 * @param charset
	 * @param params 参数集合
	 * @return
	 * @return String
	 */
	String postStrFromUrl(String url,String charset,HttpEntity entity);
	
	/**
	 * 
	 * @Description: 解析输入流获取文档 
	 * @param is 输入流
	 * @param charset 文件编码
	 * @param uri 
	 * @return
	 * @return Document
	 */
	Document parseInputStream(InputStream is,String charset,String uri);
	
	/**
	 * 
	 * @Description: 解析html文本获取文档 
	 * @param htmlStr
	 * @param charset
	 * @return
	 * @return Document
	 */
	Document parseHtmlStr(String htmlStr,String charset);
	
	
	/**
	 * 
	 * @Description: 设置代理 
	 * @param ip
	 * @param port
	 * @return
	 * @return Proxy
	 */
	Proxy setProxy(String ip,int port);
	
	/**
	 * 
	 * @Description: 设置key value 
	 * @param key
	 * @param value
	 * @return void
	 */
	void setProperties(String key,Object value);
	
	/**
	 * 
	 * @Description: 设置头信息 
	 * @param key
	 * @param value
	 * @return void
	 */
	void setHeaders(String key,Object value);
	
	Map<String,Object> getHeaders();
	
	/**
	 * 
	 * @Description: 上传文件 
	 * @param postUrl 请求uri
	 * @param file 文件
	 * @param desc 文件描述
	 * @return
	 * @return String
	 */
	String postFile(String postUrl,File file,String desc);
}
