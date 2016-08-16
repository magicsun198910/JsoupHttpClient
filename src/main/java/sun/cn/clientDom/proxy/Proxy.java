package sun.cn.clientDom.proxy;

import java.io.Serializable;

/**
 * http代理类
 * @author sunshine
 * @date 2016年8月17日
 * @desc
 */
public class Proxy implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String host;
	
	private String port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	

}
