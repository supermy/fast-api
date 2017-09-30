/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import static org.assertj.core.api.Assertions.assertThat;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * RocksDB 用于数据暂存
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {
	private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);


	@Autowired
	BaiduPOIService bps;

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void contextLoads() {
		assertThat(restTemplate).isNotNull();
	}

	@Autowired
	private RocksDB db;

	/**
	 * 测试 Rocksdb crud
	 * @throws RocksDBException
	 */
	@Test
	public void rocksdb() throws RocksDBException {
		byte[] aaa = "com.mapbar.poi:http".getBytes();
		db.put(aaa,"11111".getBytes());
		assertThat(db.get(aaa)).isNotNull();

		StringBuilder buf=new StringBuilder();
		log.debug("{}",db.keyMayExist(aaa,buf));

		log.debug("{}",new String(db.get(aaa)));
		log.debug("{}",buf);

		byte[] bbb = "bbb".getBytes();
		StringBuilder buf1=new StringBuilder();
		log.debug("{}",db.keyMayExist(bbb,buf1));

		log.debug("{}",StringUtils.isEmpty(db.get(bbb)));
		log.debug("{}",org.apache.commons.lang3.StringUtils.isAllEmpty(buf1));



	}


	/**
	 * 通过百度 API 获取 poi 信息
	 */
	@Test
	public void baiduPoidDtaByAPI() throws IOException, RocksDBException {
		bps.getPoiByApi();


	}

	/**
	 * 模拟查询获取百度地图 poi 数据
	 *
	 * 执行抓取数据
	 *
	 *
	 */
	@Test
	public void baidupoidata() {

		String keyWord="沈阳";
		String  url = "http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&da_par=direct&pcevaname=pc4.1&qt=s&da_src=searchBox.button&wd=" + keyWord + "&c=289&pn=" + 0;
		String poiurl= String.format("http://map.baidu.com/?newmap=1&reqflag=pcmap&biz=1&from=webmap&da_par=direct&pcevaname=pc4.1&qt=s&da_src=searchBox.button&wd=%s&c=%d&pn=%d", "人民广场",289,0);
		log.debug("{}",poiurl);
		String s = HttpUtils.pickData(poiurl);
		JsonObject jo = JsonUtils.toJsonObject(s);
		log.debug("{}",jo);

	}

	/**
	 * 获取代理 IP 地址
	 */
	@Test
	public void getProxyIP() throws IOException, RocksDBException {
		HttpUtils.proxy=true;
		List<String> obj = HttpUtils.getBJProxyIP();
		log.debug("{}",obj);
	}


	/**
	 * 通过图吧，提取所有节点信息
	 */
	@Test
	public void getFullItemList() throws IOException, RocksDBException {
//		HttpUtils.proxy=true;
		bps.getMapBarPoi();
		db.close();
	}

	//
//	/**
//	 * 通过图吧，提取所有政府部门信息
//	 */
//	@Test
//	public void getFullDepartmentList() throws IOException {
//		String poiurl= String.format("http://poi.mapbar.com");
//		log.debug("{}",poiurl);
//		//        String xpath="//div[@class='m-airfly-lst']/div";
//
//		DomElement xpath = HttpUtils.htmlunit(poiurl, "//dl[@id='city_list']/dd");
//
//		HashMap<String, String> cityList = saveH5HrefToMap(xpath);
//
//		log.debug("{}",cityList);
////		printNode(xpath);
//
//	}

	/**
	 * 通过图吧，查询所有的城市
	 */
	@Test
	public void F1MapbarCityData() throws IOException {
		String poiurl= String.format("http://poi.mapbar.com");
		log.debug("{}",poiurl);
		//        String xpath="//div[@class='m-airfly-lst']/div";

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//dl[@id='city_list']/dd");

		HashMap<String, String> cityList = HttpUtils.saveH5HrefToMap(xpath);

		log.debug("{}",cityList);
//		printNode(xpath);

	}

	/**
	 * 某个城市的分类信息列表
	 * 按分类提取-
	 */
	@Test
	public void F2MapbarClassByCityData() throws IOException {
//		http://poi.mapbar.com/shanghai/110_1/
		//200页面
		String poiurl= String.format("http://poi.mapbar.com/alashanmeng/");
		log.debug("{}",poiurl);

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//div[@class='isortBox']/a");

		HashMap<String, String> classList = HttpUtils.saveH5HrefToMap(xpath);

		log.debug("{}",classList);

//		printNode(xpath);

	}

	/**
	 * 获取某城市某分类的景点信息列表
	 *
	 * @throws IOException
	 */
	@Test
	public void F3Mapbar4JingdianByCityJDdata() throws IOException {
//		http://poi.mapbar.com/shanghai/110_1/
		//200页
		String poiurl= String.format("http://poi.mapbar.com/beijing/910/");
		log.debug("{}",poiurl);

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//div[@class='sortC']/dl");

		HashMap<String, String> jdList = HttpUtils.saveH5HrefToMap(xpath);

		log.debug("{}",jdList);
//		printNode(xpath);

	}


	/**
	 * 获取某个景点的详细信息
	 * @throws IOException
	 */
	@Test
	public void F4Mapbar4InfoBySingleJdData() throws IOException {

		String poiurl= String.format("http://poi.mapbar.com/beijing/MAPAFATOQNTEXBSPHWFPZOZ");
		log.debug("{}",poiurl);

		// //div[@class='infoPhoto']/ul
//		信息更新时间：2017年9月8日
//		地址： 北京 朝阳区 西大望路63号阳光财富大厦传奇生活广场B1层
//		电话： 010-58496604,15321959262 我来添加
//		所属分类：游乐园

		// //div[@id='rightPanel']/p|//div[@id='rightPanel']/h2
		// 交通数据

//		getHrefByPath("//div[@class='infoPhoto']/ul|//div[@id='rightPanel']/h2", poiurl);

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//div[@class='infoPhoto']/ul");


		log.debug("{}",xpath.asText());


//		printTextNode(xpath);

	}

	/**
	 * 获取单个城市的城区列表
	 *
	 */
	@Test
	public void F2_1MapbarDepartListByCitydata() throws IOException {

		String poiurl= String.format("http://poi.mapbar.com/beijing/");
		log.debug("{}",poiurl);

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//dl[@class='other'][1]");

		HashMap<String, String> departCityList = HttpUtils.saveH5HrefToMap(xpath);

		log.debug("{}",departCityList);

//		printNode(xpath);

	}


	/**
	 * 获取某个城区的政府部门列表
	 *
	 */
	@Test
	public void F2_2MapbarByDeparmentChilddata() throws IOException {

		String poiurl= String.format("http://poi.mapbar.com/beijing/dongcheng.html");
		log.debug("{}",poiurl);

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//div[@class='sortRow'][1]/div[@class='sortBox']");

		HashMap<String, String> items = HttpUtils.saveH5HrefToMap(xpath);

		log.debug("{}",items);


//		printNode(xpath);

	}


	/**
	 * 地址不存在测试
	 *
	 */
	@Test
	public void faillData() throws IOException {

		String poiurl= String.format("http://poi.mapbar.com/weizhi");
		log.debug("{}",poiurl);

		DomElement xpath = HttpUtils.htmlunit(poiurl, "//div[@class='sortRow'][1]/div[@class='sortBox']");

		log.debug("-------------");
		log.debug("{}",xpath.hasChildNodes());


		HashMap<String, String> items = HttpUtils.saveH5HrefToMap(xpath);

		log.debug("{}",items.keySet().size()<=0);



	}


	/**
	 * 打印节点 主要用于 A的标签与链接
	 * @param xpath
	 */
	@Deprecated
	public void printNode(DomElement xpath) {
		NodeWalker walker = new NodeWalker(xpath);

//		StringBuilder sb=new StringBuilder();

		while (walker.hasNext()) {

			Node currentNode = walker.nextNode();

			short nodeType = currentNode.getNodeType();
			String nodeName = currentNode.getNodeName();

			if (nodeType == Node.COMMENT_NODE) {
				walker.skipChildren();
			}

			if ("script".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}
			if ("style".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}

			//特殊标签跳过下面解析程序
			if (nodeType == Node.TEXT_NODE) {
				walker.skipChildren();

			}

			//nodeType == Node.ELEMENT_NODE
			if ("a".equalsIgnoreCase(nodeName)) {
				DomElement currentNode1 = (DomElement) currentNode;
				String nodeValue = currentNode1.getFirstChild().getNodeValue();
				String href = currentNode1.getAttribute("href");

				log.debug("{}",nodeValue);
				log.debug("{}",href);

				walker.skipChildren();
			}


		}
	}

	/**
	 * 主要用于提取文本信息
	 * @param xpath
	 */
	@Deprecated
	public void printTextNode(DomElement xpath) {
		NodeWalker walker = new NodeWalker(xpath);

//		StringBuilder sb=new StringBuilder();

		while (walker.hasNext()) {

			Node currentNode = walker.nextNode();

			short nodeType = currentNode.getNodeType();
			String nodeName = currentNode.getNodeName();

			if (nodeType == Node.COMMENT_NODE) {
				walker.skipChildren();
			}

			if ("script".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}
			if ("style".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}

			//nodeType == Node.ELEMENT_NODE
			if ("a".equalsIgnoreCase(nodeName)) {
				String nodeValue = currentNode.getFirstChild().getNodeValue();
				log.debug("{}",nodeValue);
				walker.skipChildren();
			}

			//特殊标签跳过下面解析程序
			if (nodeType == Node.TEXT_NODE) {
				String nodeValue = currentNode.getNodeValue();
				log.debug("{}",nodeValue);
				walker.skipChildren();
			}



		}
	}

	/**
	 * 获取百度地图的城市列表
	 */
	@Test
	public void httpclient() {
		//省分与所有城市列表，城市有所属省分 pid
		String citylist = "http://webmap0.map.bdstatic.com/wolfman/static/common/pkg/SelCity-pkg_043d2cd.js";
		//String json1 = htmlunit(citylist);
		String s = HttpUtils.pickData(citylist);
		int start1 = s.indexOf("=[{") + 1;
		int end1 = s.indexOf("}]}") + 2;
//		log.debug("{}",s.substring(start1, end1));
		String json2 = s.substring(start1, end1);
		JsonUtils.toJsonArray(json2);

		String poi= String.format("http://api.map.baidu.com/place/v2/search?page_size=20&page_num=%d&query=%s&scope=2&region=%s&output=json&ak=2GeRN9G0VqDwhVn7ZiBBbuQSxcfUi5jv",0,"银行","北京");
		String json3 = HttpUtils.pickData(poi);
		JsonUtils.toJsonObject(json3);
		log.debug("{}","================================");
		log.debug("{}",poi);
		log.debug("{}",json3);
	}



}
