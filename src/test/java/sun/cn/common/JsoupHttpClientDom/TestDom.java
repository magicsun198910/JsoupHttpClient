package sun.cn.common.JsoupHttpClientDom;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import sun.cn.clientDom.dom.HttpClientGetDom;

/**
 * 单元测试
 * @author sunshine
 * @date 2016年8月20日
 * @desc
 */
public class TestDom {
	
	@Test
	public void test(){
		HttpClientGetDom dom = HttpClientGetDom.getInstance();
		String str = dom.getStrFromUrl("http://www.baidu.com", "utf-8");
		Document c = dom.parseHtmlStr(str, "utf-8");
		Element e = c.getElementById("su");
		System.out.println(str);
		System.out.println(e.attr("value"));
	}
	

}
