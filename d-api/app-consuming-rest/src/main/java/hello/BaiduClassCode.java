package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moyong on 2017/9/26.
 */
public class BaiduClassCode {
    private static final Logger log = LoggerFactory.getLogger(BaiduClassCode.class);

    //使用现在返回的adcode，这个code为国家统一标准，且持续维护
     public static String  citycode="美食\t中餐厅、外国餐厅、小吃快餐店、蛋糕甜品店、咖啡厅、茶座、酒吧\n" +
            "酒店\t星级酒店、快捷酒店、公寓式酒店\n" +
            "购物\t购物中心、超市、便利店、家居建材、家电数码、商铺、集市\n" +
            "生活服务\t通讯营业厅、邮局、物流公司、售票处、洗衣店、图文快印店、照相馆、房产中介机构、公用事业、维修点、家政服务、殡葬服务、彩票销售点、宠物服务、报刊亭、公共厕所\n" +
            "丽人\t美容、美发、美甲、美体\n" +
            "旅游景点\t公园、动物园、植物园、游乐园、博物馆、水族馆、海滨浴场、文物古迹、教堂、风景区\n" +
            "休闲娱乐\t度假村、农家院、电影院、KTV、剧院、歌舞厅、网吧、游戏场所、洗浴按摩、休闲广场\n" +
            "运动健身\t体育场馆、极限运动场所、健身中心\n" +
            "教育培训\t高等院校、中学、小学、幼儿园、成人教育、亲子教育、特殊教育学校、留学中介机构、科研机构、培训机构、图书馆、科技馆\n" +
            "文化传媒\t新闻出版、广播电视、艺术团体、美术馆、展览馆、文化宫\n" +
            "医疗\t综合医院、专科医院、诊所、药店、体检机构、疗养院、急救中心、疾控中心\n" +
            "汽车服务\t汽车销售、汽车维修、汽车美容、汽车配件、汽车租赁、汽车检测场\n" +
            "交通设施\t飞机场、火车站、地铁站、长途汽车站、公交车站、港口、停车场、加油加气站、服务区、收费站、桥\n" +
            "金融\t银行、ATM、信用社、投资理财、典当行\n" +
            "房地产\t写字楼、住宅区、宿舍\n" +
            "公司企业\t公司、园区、农林园艺、厂矿\n" +
            "政府机构\t中央机构、各级政府、行政单位、公检法机构、涉外机构、党派团体、福利机构、政治教育机构";

    public static void main(String[] args) throws IOException, InterruptedException {

        log.debug("百度的分类列表 {}",getClassList());

    }

    public static List<String> getClassList() {
        List<String> results =new ArrayList<String>();

        String[] split = BaiduClassCode.citycode.split("\n");
        for (int i = 0; i <split.length ; i++) {

            String[] line = split[i].split("\t");
            System.out.println(line[0]);
            String[] classline = line[1].split("、");
            for (String linedata:classline
                 ) {
                System.out.println(linedata);
                results.add(linedata);
            }
        }

        return results;
    }

}
