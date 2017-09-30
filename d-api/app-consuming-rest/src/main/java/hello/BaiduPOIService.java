package hello;


import com.gargoylesoftware.htmlunit.html.DomElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;


@Service
public class BaiduPOIService {
    private static final Logger log = LoggerFactory.getLogger(BaiduPOIService.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private RocksDB db;


    /**
     * 嵌入式数据存储
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        try {
            db.put(key, value);
        } catch (RocksDBException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 嵌入式数据获取
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        try {
            return db.get(key);
        } catch (RocksDBException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 数据是否存在
     *
     * @param key
     * @return
     */
    public boolean keyMayExist(byte[] key) {
        StringBuilder retValue = new StringBuilder();
        boolean exists = db.keyMayExist("key".getBytes(), retValue);
        log.debug("{}", retValue);
        return exists;
    }

    /**
     * 获取图吧的数据
     *
     * @throws IOException
     */
    public void getMapBarPoi() throws IOException, RocksDBException {

        Map<String, String> cityList = getCommonByUrlXPath(String.format("http://poi.mapbar.com"), "//dl[@id='city_list']/dd");

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();


        log.debug("{}", String.format("city num : %d", cityList.keySet().size()));

        for (String cityUrl : cityList.keySet()) {

            log.debug("{}", cityUrl);

            //某个城市分类列表
            Map<String, String> classList = getCommonByUrlXPath(cityUrl, "//div[@class='isortBox']/a");

//            if (classList.size()<=0) {
//                   log.debug("{}","------------------");
//                   log.debug("{}",classList);
//            }else
//                   log.debug("{}",String.format("city num : %d", classList.keySet().size()));


            for (String classUrl : classList.keySet()) {


                for (int i = 1; i < 10; i++) {

                    String classUrlPage = classUrl.substring(0, classUrl.length() - 1) + "_" + i;
                    //某个分类景点列表
                    Map<String, String> itemList = getCommonByUrlXPath(classUrlPage, "//div[@class='sortC']/dl");


                    //新的 page 没有数据跳出循环
                    if (itemList.keySet().size() <= 0) {
                        break;
                    }

                    for (String itemUrl : itemList.keySet()) {

                        Map<String, String> item = new HashMap<String, String>();
                        item.put("city", cityList.get(cityUrl)); //name
                        item.put("class", classList.get(classUrl)); //name
                        item.put("name", itemList.get(itemUrl));//name

                        item.put("url", itemUrl);
                        item.put("class_url", classUrlPage);
                        item.put("city_url", cityUrl);

                        String text = getCommonTextByUrlXPath(itemUrl, "//div[@class='infoPhoto']/ul", item);

                        log.debug("POI 项目的信息 ：{}", text);
//                           log.debug("{}",item);

                        list.add(item);
                    }

                }

            }
        }

        log.debug("{}", list.size());
//           log.debug("{}",list);
    }

    /**
     * 文本信息
     *
     * @param poiurl
     * @param xpath
     * @param item
     * @return
     * @throws IOException
     * @throws RocksDBException
     */
    public String getCommonTextByUrlXPath(String poiurl, String xpath, Map<String, String> item) throws IOException, RocksDBException {
        //倒排主键作为 key
        byte[] urlKey = TableUtil.reverseUrl(poiurl).getBytes();

        byte[] json = db.get(urlKey);
        String jsonsstring = json == null ? "" : new String(json);
        log.debug("{}", String.format("::::::rocksdb data %s:  %s = %s", poiurl, new String(urlKey), jsonsstring));

        StringBuilder buf = new StringBuilder();

        if (StringUtils.isNotBlank(jsonsstring)) {

            buf.append(jsonsstring);

            Map<String, String> stringStringMap = (Map<String, String>) JsonUtils.toBean(jsonsstring, HashMap.class);

            //清除 info 数据为空的 值
            if (StringUtils.isAllEmpty(stringStringMap.get("info"))) {
                db.delete(urlKey);
            }


        } else {

            //数据采集抓取
            DomElement xpath1 = HttpUtils.htmlunit(poiurl, xpath);

            String text = xpath1.asText().replaceAll("\\n", "|"); //清除 \s是指空白，包括空格、换行、tab缩进等所有的空白 换行符用| fixme good
            text = text.replaceAll("\\s+", " "); //清除 \s是指空白，包括空格、换行、tab缩进等所有的空白

            if (StringUtils.isNotEmpty(text)) {
                item.put("info", text);
                log.debug("{}", String.format("::::::save to rocksdb data  %s = %s", new String(urlKey), item));
                //数据持久化到 Rocksdb
                db.put(urlKey, JsonUtils.toJSONString(item).getBytes());
            } else {
                log.debug("{}", String.format("::::::抓取数据失败 %s: %s = %s", poiurl, new String(urlKey), item));
            }
            try {

                int s = random();
                Thread.sleep(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return buf.toString();
    }

    /**
     * href 节点信息
     *
     * @param url
     * @param xpath
     * @return
     * @throws IOException
     * @throws RocksDBException
     */
    public Map<String, String> getCommonByUrlXPath(String url, String xpath) throws IOException, RocksDBException {
        Map<String, String> result = new HashMap<String, String>();

        //倒排主键作为 key
        byte[] urlkey = TableUtil.reverseUrl(url).getBytes();

        log.debug("{}", new String(urlkey));
        byte[] json = db.get(urlkey);
        String jsonsstring = json == null ? "" : new String(json);
        log.debug("{}", String.format("::::::rocksdb data %s: %s = %s", url, new String(urlkey), jsonsstring));

        if (StringUtils.isNotBlank(jsonsstring)) {
            result = (Map<String, String>) JsonUtils.toBean(jsonsstring, HashMap.class);
        } else {
            //数据采集抓取
            DomElement xpath1 = HttpUtils.htmlunit(url, xpath);
            result = HttpUtils.saveH5HrefToMap(xpath1);

            log.debug("{}", String.format("::::::save to rocksdb %s: %s = %s", url, new String(urlkey), result));

            if (result.size() > 0) {
                //数据持久化到 Rocksdb
                log.debug("{}", JsonUtils.toJSONString(result));
                db.put(urlkey, JsonUtils.toJSONString(result).getBytes());
            }
            try {

                int s = random();
                Thread.sleep(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private int random() {
        int max = 500;
        int min = 100;
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    private int random(int min,int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }
    /**
     * 根据百度 api 获取 poi 数据，每个区域最多返回400个结果，每页最多返回20个记录
     */
    public void getPoiByApi() throws IOException, RocksDBException {
        //POI列表 按区域
        String byBoxApi = String.format("http://api.map.baidu.com/place/v2/search?query=%s&page_size=20&page_num=%d&scope=1&bounds=%s&output=json&ak=%s", "美食", 0, "39.915,116.404,39.975,116.414", "2GeRN9G0VqDwhVn7ZiBBbuQSxcfUi5jv");
        //        JsonElement cityListByBox = getJsonPoiByBaiduApi(byBoxApi,"results");


        //城市列表
        String citylist = "http://webmap0.map.bdstatic.com/wolfman/static/common/pkg/SelCity-pkg_043d2cd.js";
        JsonElement cityList = getJsonPoiByBaiduApi(citylist, null, "=[{", "1", "}]}", "2");
        JsonArray citys = cityList.getAsJsonArray();

        //分类列表
        List<String> classList = BaiduClassCode.getClassList();


        for (String classObj : classList
                ) {

            for (JsonElement obj : citys
                    ) {

                if (obj.getAsJsonObject().get("type").getAsString().equals("C")) {
                    log.debug(" obj:{} ;type: {} ; == {}", obj, obj.getAsJsonObject().get("type"), obj.getAsJsonObject().get("type").getAsString().equals("C"));
                    log.debug("  {}", obj.getAsJsonObject().get("name").getAsString());

                    //分页查询
                    for (int i = 0; i < 20; i++) {
                        String byCityApi = String.format("http://api.map.baidu.com/place/v2/search?query=%s&page_size=20&page_num=%d&scope=1&region=%s&output=json&ak=%s", classObj, i, obj.getAsJsonObject().get("name").getAsString(), "2GeRN9G0VqDwhVn7ZiBBbuQSxcfUi5jv");
                        //POI 列表 按行政区域
                        JsonElement cityListByRegion = getJsonPoiByBaiduApi(byCityApi, "results");
                        log.debug("{}",cityListByRegion );

                        //逐个项目查询
                        for (JsonElement byDatail : cityListByRegion.getAsJsonArray()
                                ) {
                            //POI 详情
                            String byDatailUrl = String.format("http://api.map.baidu.com/place/v2/detail?uid=%s&output=json&scope=2&ak=%s", byDatail.getAsJsonObject().get("uid").getAsString(), "2GeRN9G0VqDwhVn7ZiBBbuQSxcfUi5jv");
                            JsonElement poiDetailByUidApi = getJsonPoiByBaiduApi(byDatailUrl, "result");
                            log.debug("详单数据：{}", poiDetailByUidApi);
                        }
                    }
                }
            }

        }
    }


    /**
     * 兼容数据与数据对象的 json 数据格式
     *
     * @param url
     * @return
     * @throws IOException
     * @throws RocksDBException
     */
    public JsonElement getJsonPoiByBaiduApi(String url, String dataname, String... substring) throws IOException, RocksDBException {
        JsonElement jsonElements = null;

        //倒排主键作为 key
        byte[] urlkey = TableUtil.reverseUrl(url).getBytes();
        log.debug("{}", new String(urlkey));
        byte[] json = db.get(urlkey);
        String jsonsstring = json == null ? "" : new String(json);
        log.debug("{}", String.format("::::::rocksdb data %s: %s = %s", url, new String(urlkey), jsonsstring));

//        db.delete(urlkey);  //清理测试数据时使用

//        log.debug("{}", null != jsonsstring);
//        log.debug("{}", jsonsstring);
//        log.debug("{}", "null".trim().equalsIgnoreCase(jsonsstring));

        if (StringUtils.isNotBlank(jsonsstring) && !"null".equalsIgnoreCase(jsonsstring)) {
            jsonElements = JsonUtils.toJson(jsonsstring);
        } else {
            //数据采集抓取

            String json2 = HttpUtils.pickData(url);

            if (substring.length >= 4) {
//                int start1 = json2.indexOf("=[{") + 1;
//                int end1 = json2.indexOf("}]}") + 2;
                int start1 = json2.indexOf(substring[0]) + new Integer(substring[1]);
                int end1 = json2.indexOf(substring[2]) + new Integer(substring[3]);
                log.debug("json 数据进行预处理 {}", json2.substring(start1, end1));
                json2 = json2.substring(start1, end1);
            }


            log.debug("{}", String.format("::::::save to rocksdb %s: %s = %s", url, new String(urlkey), json2));

            if (StringUtils.isNotEmpty(json2)) {
                //数据持久化到 Rocksdb
                jsonElements = JsonUtils.toJson(json2); //相当于json格式验证
                if(jsonElements.getAsJsonObject().get("status").getAsString().equalsIgnoreCase("401")){
                    log.debug("$$$$$$ 抓取数据错误 {}",jsonElements.getAsJsonObject().get("message").getAsString());
                    try {
                        Thread.sleep(10000);
                        return new JsonArray();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (StringUtils.isNotEmpty(dataname)) {
                    jsonElements = jsonElements.getAsJsonObject().get(dataname);
                }
                log.debug("{}", JsonUtils.toJSONString(jsonElements));
                db.put(urlkey, JsonUtils.toJSONString(jsonElements).getBytes());
            }

            //增加访问时间间隔
            try {

                int s1 = random(800,1200);
                Thread.sleep(s1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return jsonElements;
    }


}
