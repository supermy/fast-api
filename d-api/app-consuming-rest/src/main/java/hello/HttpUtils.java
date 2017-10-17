package hello;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import ocr.Ocr;
import ocr.PictureManage;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by moyong on 2017/9/26.
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static boolean proxy = false;
    public static boolean javaScriptEnabled = false;

    private static WebClient webClient = new WebClient(BrowserVersion.CHROME); //单例模式

    /**
     * 爬取网页信息
     */
    public static String pickData(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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
        return null;
    }

    /**
     * 启动无界面浏览器抓取，性能较差，针对 ajax 界面及其他特殊定制的页面数据抓取
     *
     * @param citylist
     * @return
     * @throws IOException
     */
    public static String htmlunit(String citylist) throws IOException {
        //		log.debug("{}",restTemplate.getForEntity(citylist, String.class).getBody());
        URL url = new URL(citylist);

        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        WebRequest request = new WebRequest(url);
        request.setCharset("UTF-8");
        HtmlPage page = webClient.getPage(request);

//        log.debug("{}",page.asXml());
        String js = page.asXml();
//
        int start = js.indexOf("=[{") + 1;
        int end = js.indexOf("}]}") + 2;
        return js.substring(start, end);
    }


    /**
     * 抓取网页，按 xpath 返回数据
     *
     * @param url
     * @param xpath
     * @return
     * @throws IOException
     */
    public static DomElement htmlunit(String url, String xpath) throws IOException {

        HtmlPage page = getHtmlPage(url);

        //是否验证页面，验证页面进行处理
        checkcode(page);


        ///////////////////////////////
        List<HtmlElement> byXPath = page.getByXPath(xpath);

        DomElement divc = page.createElement("div");
        divc.setAttribute("targetData", "ok");

        for (HtmlElement he : byXPath
                ) {
            divc.appendChild(he);
        }
        return divc;

    }

    /**
     * 破解验证码
     * 递归验证，多次破解验证码
     *
     * @param page
     */
    private static void checkcode(HtmlPage page) throws IOException {

        if(!page.asXml().contains("验证码")){
            return;
        }

        log.debug("******************************：begin{} {}","破解验证码",page.getBaseURI());

//        log.debug("******************************：begin \n {}",page.asXml());

        //验证码页面抓取启动 JS
        HttpUtils.javaScriptEnabled=true;
        page = getHtmlPage(page.getBaseURI());

        log.debug("******************************：begin \n {}",page.asText());

        //识别验证码
        HtmlImage valiCodeImg = (HtmlImage) page.getFirstByXPath("//img[@id='ipValidateImg']");
        //正常页面返回继续抓取数据
        if(valiCodeImg==null){
            return ;
        }

        log.debug("验证码元素 {}",valiCodeImg);

        ImageReader imageReader = valiCodeImg.getImageReader();
        BufferedImage bufferedImage = imageReader.read(0);
        //保存验证码到本地图片
        PictureManage.saveImage(bufferedImage,"tmp/checkcode.jpg");
        String valicodeStr = Ocr.checkChodeOcr("tmp/checkcode.jpg");
        log.debug("验证码 {}",valicodeStr);


        //输入验证码
        HtmlElement valiCode = page.getFirstByXPath("//input[@id='IPValidate']");
        valiCode.click();
        valiCode.type(valicodeStr);

        //提交验证码
        HtmlSubmitInput sm = page.getFirstByXPath("//input[@id='IPValidateSubmit']");
        HtmlPage resultPage = sm.click();


        if (resultPage.asXml().contains("验证码") ) {
            log.debug("******************************：{} ,{} ","登录失败",resultPage.asText());

            //递归容易出错
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            HttpUtils.javaScriptEnabled=false;
//            page = getHtmlPage(page.getBaseURI());

//            checkcode(page);//需要多次破解验证码
        } else {
            log.debug("******************************：{} ,{}","登录成功",resultPage.asText());
//            HttpUtils.javaScriptEnabled=false;
//            //页面复原
//            page = getHtmlPage(page.getBaseURI());
        }

        HttpUtils.javaScriptEnabled=false;
        //页面复原
        page = getHtmlPage(page.getBaseURI());




    }

    /**
     * 通过 htmlunit 获取数据
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static HtmlPage getHtmlPage(String url) throws IOException {
        WebClient webClient = getWebClient();


        int count = Integer.MAX_VALUE;

        WebRequest request = getWebRequest(url);

        long start=System.currentTimeMillis();

        HtmlPage page = webClient.getPage(request);

        count = webClient.waitForBackgroundJavaScript(1 * 1000);  //20秒
//        count = webClient.waitForBackgroundJavaScript(1 * 100);  //20秒

        log.debug("抓取网页{}，花费时间 {} 毫秒",url,System.currentTimeMillis()-start);
        //webClient.close();//fixme

        return page;
    }

    /**
     * 配置 web client
     *
     * @return
     */
    public static WebClient getWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        // 启动JS
        webClient.getOptions().setJavaScriptEnabled(javaScriptEnabled); //可以改变静态变量直接配置是否允许 js
        //禁用Css，可避免自动二次请求CSS进行渲染
        webClient.getOptions().setCssEnabled(false);

        //设置Ajax异步处理控制器即启用Ajax支持
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //前者表示当出现Http error时，程序不抛异常继续执行
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        //后者表示当JavaScript执行出现异常时直接无视，否则Java代码会直接抛异常，程序中断。
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        //忽略ssl认证
        webClient.getOptions().setUseInsecureSSL(true);

        webClient.getOptions().setRedirectEnabled(true);

//        webClient.setJavaScriptTimeout(35000);


//启动cookie管理
        webClient.setCookieManager(new CookieManager());


        webClient.getCurrentWindow().setInnerHeight(6000);


        if (proxy) {
            log.debug("::: 使用代理{}", proxy);

            List<String> bjProxyIP = null;
            proxy = false;
            try {
                bjProxyIP = getBJProxyIP();
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.debug("proxy ip list: {}", bjProxyIP);


            int random = random(1, bjProxyIP.size() - 1);
            String[] ip = bjProxyIP.get(random).split(":");

            log.debug("proxy ip : {}", ip[0]);
            log.debug("proxy ip : {}", ip[1]);

            ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();

            proxyConfig.setProxyHost(ip[0]);
            proxyConfig.setProxyPort(new Integer(ip[1]));
        }

        return webClient;
    }

    /**
     * 配置请求协议
     *
     * @return
     * @throws MalformedURLException
     */
    public static WebRequest getWebRequest(String url1) throws MalformedURLException {
        URL uri = new URL(url1);
        WebRequest request = new WebRequest(uri);

        request.setCharset("UTF-8");
//        request.setCharset("GBK");
        String refer = url1;
        request.setAdditionalHeader("Referer", refer);//设置请求报文头里的refer字段
        ////设置请求报文头里的User-Agent字段
        request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        //request.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        //request.setAdditionalHeader("Cookie", "QN99=6118; QN1=dXrggVnDkACkKOK2BkRWAg==; csrfToken=3lPL7C3LnMskdEMvGs7b95vAzbvI1Uo4; QN269=89AC52F274C911E7B545FA163ED697DB; QN170=39.155.134.146_666c64_0_176uI3oYCEwdwD8vgHI%2FCgGdjBGjXf%2Bj7FpTXL%2BtV7c%3D; QunarGlobal=10.86.213.123_-414fd2bc_15ea3d99667_-44b8|1505988610489; QN621=1490067914133%3DDEFAULT; QN205=ap_20100201; QN277=ap_20100201; _i=ueHd8K8HN509vgNYPc6cTyj6UoXX; _vi=8LqmeG6BYrwON4A-0q5bdOowQSu0v-OwL7R4G-sGYWABL1pTPHhuZ_lcDruK7XKnNAvoQXDrSP_3G2xUAhcVpQNk5ovOAwpM1nLX-UKkbL5Avu9Wd6pD9mct8ysJ-pe1e0Sb5Bq2w_8FbNjGzChQ9hdDKezYPpA8NtKB0O-8CObc");
//        request.setCharset(java.nio.charset.Charset.forName("utf-8"));


        return request;
    }


    /**
     * 提取 html 中的 a 标签 herf 信息到 Map
     *
     * @param xpath
     * @return
     */
    public static HashMap<String, String> saveH5HrefToMap(DomElement xpath) {
        HashMap<String, String> hm = new HashMap<String, String>();//todo RocksDB 永久存储

        DomNodeList domNodeList = xpath.getElementsByTagName("a");
        for (int i = 0; i < domNodeList.size(); i++) {
            DomElement domElement = (DomElement) domNodeList.get(i);
            String href = domElement.getAttribute("href");
            String nodeValue = domElement.getFirstChild().getNodeValue();

            if (hm.containsKey(href)) {
                log.debug("{}", String.format("重复数据 %s =%s", href, nodeValue));
            } else
                hm.put(href, nodeValue);

        }
        return hm;
    }


    public static List<String> getBJProxyIP() throws IOException {
        String url = "http://www.66ip.cn/areaindex_1/1.html";
        String xpath1 = "//div[@align='center']/table/tbody/tr/td[1]";
        String xpath2 = "//div[@align='center']/table/tbody/tr/td[2]";

        DomElement ipport = HttpUtils.htmlunit(url, xpath1 + "|" + xpath2);

        StringBuilder ipportList = getTextNode(ipport);
//        log.debug("{}",ipportList);

//        Map<String,String> obj=new HashMap<String,String>();
        List<String> obj = new ArrayList<String>();
        String[] ipports = ipportList.toString().split(";");
        String key = null;
        for (int i = 0; i < ipports.length; i++) {
            if (i % 2 == 0) {
                key = ipports[i];
            } else {
                if (!key.equalsIgnoreCase("ip"))
                    obj.add(key + ":" + ipports[i]);
//                    obj.put(key,ipports[i]);
            }
        }
        return obj;
    }

    /**
     * 主要用于提取文本信息
     *
     * @param xpath
     */
    public static StringBuilder getTextNode(DomElement xpath) {

        StringBuilder buf = new StringBuilder();

        NodeWalker walker = new NodeWalker(xpath);
        while (walker.hasNext()) {

            Node currentNode = walker.nextNode();
            short nodeType = currentNode.getNodeType();
            String nodeName = currentNode.getNodeName();

            //特殊标签跳过下面解析程序
            if (nodeType == Node.TEXT_NODE) {
                String nodeValue = currentNode.getNodeValue();
                buf.append(nodeValue).append(";");
                walker.skipChildren();
            }
        }

        return buf;
    }

    private static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }
}
