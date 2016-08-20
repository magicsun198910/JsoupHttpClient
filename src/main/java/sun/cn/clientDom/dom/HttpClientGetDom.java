package sun.cn.clientDom.dom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.SSLHandshakeException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import sun.cn.clientDom.handler.AbstractDomHandler;
import sun.cn.clientDom.proxy.Proxy;


/**
 * 
 *@description:通过httpClient获取Jsoup节点
 * @author:sunla
 * @date:2014年5月15日下午2:45:43
 */
public class HttpClientGetDom extends AbstractDomHandler{
	
	private static Logger logger=Logger.getLogger(HttpClientGetDom.class);
	
	/**
	 * 不能通过外部实例化
	 */
	private HttpClientGetDom(){
		super();
	}
	
	@Override
	public String getStrFromUrl(String docUrl, String charset) {
		String result=null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
    	HttpGet httpget=null;
    	HttpResponse response; 
        try {
            httpget = new HttpGet(docUrl);
            if(this.getProxy()!=null){
            	setHttpProxy(httpclient, this.getProxy(),docUrl);
            }
            for(String key:headers.keySet()){
            	httpget.addHeader(key, String.valueOf(headers.get(key)));
            }
            HttpParams params = httpclient.getParams();
            params.setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
            /**设置连接超时*/
            HttpConnectionParams.setConnectionTimeout(params, Integer.valueOf(properties.get(CONNECT_TIME_OUT).toString()));
            HttpConnectionParams.setSoTimeout(params, Integer.valueOf(properties.get(SO_TIME_OUT).toString()));
            /**重试机制*/
            HttpRequestRetryHandler myRetryHandler=new HttpRequestRetryHandler() {
				public boolean retryRequest(IOException ex, int executionCount, HttpContext context) {
					/**如果超过3次就放弃*/
					if(executionCount>Integer.valueOf(properties.get(RETRY_KEY).toString())){
						return false;
					}
					/**如果服务器丢掉了连接，那么就重试*/
					if (ex instanceof NoHttpResponseException) {
						return true;
						}
					/**不要重试SSL握手异常*/
					if (ex instanceof SSLHandshakeException) {
						return false;
						}
					return false;
				}
			};
				httpclient.setHttpRequestRetryHandler(myRetryHandler);
				response=httpclient.execute(httpget);
				int code=response.getStatusLine().getStatusCode();
				if(code==200){
					HttpEntity entity=response.getEntity();
					this.setCookie(response.getHeaders("Set-Cookie"));
					result=EntityUtils.toString(entity,charset.equals("")?DEFAULT_CHARSET:charset);
				}else if(code==404){
					result=null;
					logger.error(String.format("url is %s and status is %s", docUrl,404));
				}else if(code==403){
					logger.error(String.format("url is %s and status is %s", docUrl,403));
				}
        }catch(ClientProtocolException e){
        	e.printStackTrace();
        }catch(ConnectTimeoutException e){
        	e.toString();
        }catch(IOException e){
        	e.toString();
        } finally {
        	httpget.abort();
            httpclient.getConnectionManager().shutdown();
        }
		return result;
	}

	@Override
	public Document parseInputStream(InputStream is, String charset, String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document parseHtmlStr(String htmlStr, String charset) {
		Document doc=null;
		if(htmlStr!=null&&htmlStr.length()>0){
			doc=Jsoup.parse(htmlStr);
		}
		return doc;
	}

	@Override
	public Proxy setProxy(String ip, int port) {
		Proxy p=new Proxy();
		p.setHost(ip);
		p.setPort(String.valueOf(port));
		this.setProxy(p);
		return p;
	}
	
	private void setHttpProxy(HttpClient httpclient,Proxy hp,String url){
		HttpHost proxy = new HttpHost(hp.getHost(), Integer.valueOf(hp.getPort()), "http"); 
        httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        logger.info("===使用代理===ip:"+hp.getHost()+"===="+url);
	}

	@Override
	public String postStrFromUrl(String url, String charset,
			HttpEntity entityModel) {
		String result="";
		int code=0;
		JSONObject resultObj = new JSONObject();
		DefaultHttpClient httpclient = new DefaultHttpClient();
    	HttpPost httppost=null;
    	HttpResponse response; 
        try {
        	httppost = new HttpPost(url);
        	httppost.setEntity(entityModel);
        	for(String key:headers.keySet()){
        		httppost.addHeader(key, String.valueOf(headers.get(key)));
            }
        	 if(this.getProxy()!=null){
             	setHttpProxy(httpclient, this.getProxy(),url);
             }
            HttpParams params = httpclient.getParams();
            params.setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
            /**设置连接超时*/
            HttpConnectionParams.setConnectionTimeout(params, Integer.valueOf(properties.get(CONNECT_TIME_OUT).toString()));
            HttpConnectionParams.setSoTimeout(params, Integer.valueOf(properties.get(SO_TIME_OUT).toString()));
            /**重试机制*/
            HttpRequestRetryHandler myRetryHandler=new HttpRequestRetryHandler() {
				public boolean retryRequest(IOException ex, int executionCount, HttpContext context) {
					/**如果超过3次就放弃*/
					if(executionCount>Integer.valueOf(properties.get(RETRY_KEY).toString())){
						return false;
					}
					/**如果服务器丢掉了连接，那么就重试*/
					if (ex instanceof NoHttpResponseException) {
						return true;
						}
					/**不要重试SSL握手异常*/
					if (ex instanceof SSLHandshakeException) {
						return false;
						}
					return false;
				}
			};
				httpclient.setHttpRequestRetryHandler(myRetryHandler);
				response=httpclient.execute(httppost);
				code=response.getStatusLine().getStatusCode();
				if(code==200){
					HttpEntity entity=response.getEntity();
					this.setCookie(response.getHeaders("Set-Cookie"));
					result=EntityUtils.toString(entity,charset);
				}else if(code==404){
					logger.error(String.format("url is %s and status is %s", url,404));
				}else if(code==400){
					logger.error(String.format("url is %s and status is %s", url,400));
				}
        }catch(ClientProtocolException e){
        	e.toString();
        }catch(ConnectTimeoutException e){
        	e.toString();
        }catch(IOException e){
        	e.toString();
        }finally {
        	httppost.abort();
            httpclient.getConnectionManager().shutdown();
        }
        resultObj.put("result", result);
		resultObj.put("code", code);
		return resultObj.toString();
	}

	@Override
	public String postFile(String postUrl, File file, String desc) {
		String result="";
		HttpClient httpclient = new DefaultHttpClient();
		try{
        HttpPost post = new HttpPost(postUrl);
        FileBody fileBody = new FileBody(file);
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", fileBody);
        entity.addPart("desc", new StringBody(desc));
        post.setEntity(entity);
        HttpParams params = httpclient.getParams();
        params.setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        /**设置连接超时*/
        HttpConnectionParams.setConnectionTimeout(params, Integer.valueOf(properties.get(CONNECT_TIME_OUT).toString()));
        HttpConnectionParams.setSoTimeout(params, Integer.valueOf(properties.get(SO_TIME_OUT).toString()));
        HttpResponse response = httpclient.execute(post);
        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){
            HttpEntity entitys = response.getEntity();
            if (entity != null) {
                result=EntityUtils.toString(entitys);
            }
        }
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 单例
	 * @return
	 * HttpClientGetDom
	 */
	public static HttpClientGetDom getInstance(){
		return DomInstance.getDom();
	}
	
	private static class DomInstance{
		
		private static HttpClientGetDom dom = new HttpClientGetDom();
		
		public static HttpClientGetDom getDom(){
			return dom;
		}
		
	}
}
