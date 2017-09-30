package hello;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

/**
 * Created by moyong on 2017/9/26.
 */
public class BaiduCityCode {
    //使用现在返回的adcode，这个code为国家统一标准，且持续维护
     public static String  citycode="100 拉萨市\n" +
            "101 那曲地区\n" +
            "102 日喀则地区\n" +
            "103 阿里地区\n" +
            "104 昆明市\n" +
            "105 楚雄彝族自治州\n" +
            "106 玉溪市\n" +
            "107 红河哈尼族彝族自治州\n" +
            "108 普洱市\n" +
            "109 西双版纳傣族自治州\n" +
            "110 临沧市\n" +
            "111 大理白族自治州\n" +
            "112 保山市\n" +
            "113 怒江傈僳族自治州\n" +
            "114 丽江市\n" +
            "115 迪庆藏族自治州\n" +
            "116 德宏傣族景颇族自治州\n" +
            "117 张掖市\n" +
            "118 武威市\n" +
            "119 东莞市\n" +
            "120 东沙群岛\n" +
            "121 三亚市\n" +
            "122 鄂州市\n" +
            "123 乌海市\n" +
            "124 莱芜市\n" +
            "125 海口市\n" +
            "126 蚌埠市\n" +
            "1277 河南省直辖县级行政单位\n" +
            "1277 济源市\n" +
            "127 合肥市\n" +
            "128 阜阳市\n" +
            "129 芜湖市\n" +
            "130 安庆市\n" +
            "131 北京市\n" +
            "132 重庆市\n" +
            "133 南平市\n" +
            "134 泉州市\n" +
            "135 庆阳市\n" +
            "136 定西市\n" +
            "137 韶关市\n" +
            "138 佛山市\n" +
            "139 茂名市\n" +
            "140 珠海市\n" +
            "141 梅州市\n" +
            "142 桂林市\n" +
            "143 河池市\n" +
            "144 崇左市\n" +
            "145 钦州市\n" +
            "146 贵阳市\n" +
            "147 六盘水市\n" +
            "148 秦皇岛市\n" +
            "149 沧州市\n" +
            "150 石家庄市\n" +
            "151 邯郸市\n" +
            "152 新乡市\n" +
            "153 洛阳市\n" +
            "154 商丘市\n" +
            "155 许昌市\n" +
            "156 襄樊市\n" +
            "157 荆州市\n" +
            "158 长沙市\n" +
            "159 衡阳市\n" +
            "160 镇江市\n" +
            "161 南通市\n" +
            "162 淮安市\n" +
            "163 南昌市\n" +
            "164 新余市\n" +
            "165 通化市\n" +
            "166 锦州市\n" +
            "167 大连市\n" +
            "168 乌兰察布市\n" +
            "169 巴彦淖尔市\n" +
            "170 渭南市\n" +
            "171 宝鸡市\n" +
            "172 枣庄市\n" +
            "173 日照市\n" +
            "174 东营市\n" +
            "175 威海市\n" +
            "176 太原市\n" +
            "177 文山壮族苗族自治州\n" +
            "178 温州市\n" +
            "179 杭州市\n" +
            "180 宁波市\n" +
            "181 中卫市\n" +
            "182 临夏回族自治州\n" +
            "183 辽源市\n" +
            "184 抚顺市\n" +
            "185 阿坝藏族羌族自治州\n" +
            "186 宜宾市\n" +
            "187 中山市\n" +
            "188 亳州市\n" +
            "189 滁州市\n" +
            "190 宣城市\n" +
            "191 廊坊市\n" +
            "192 宁德市\n" +
            "193 龙岩市\n" +
            "194 厦门市\n" +
            "195 莆田市\n" +
            "196 天水市\n" +
            "197 清远市\n" +
            "198 湛江市\n" +
            "199 阳江市\n" +
            "200 河源市\n" +
            "201 潮州市\n" +
            "202 来宾市\n" +
            "203 百色市\n" +
            "204 防城港市\n" +
            "205 铜仁地区\n" +
            "206 毕节地区\n" +
            "207 承德市\n" +
            "208 衡水市\n" +
            "209 濮阳市\n" +
            "210 开封市\n" +
            "211 焦作市\n" +
            "212 三门峡市\n" +
            "213 平顶山市\n" +
            "214 信阳市\n" +
            "215 鹤壁市\n" +
            "216 十堰市\n" +
            "217 荆门市\n" +
            "218 武汉市\n" +
            "219 常德市\n" +
            "220 岳阳市\n" +
            "221 娄底市\n" +
            "222 株洲市\n" +
            "223 盐城市\n" +
            "224 苏州市\n" +
            "225 景德镇市\n" +
            "226 抚州市\n" +
            "227 本溪市\n" +
            "228 盘锦市\n" +
            "229 包头市\n" +
            "230 阿拉善盟\n" +
            "231 榆林市\n" +
            "232 铜川市\n" +
            "233 西安市\n" +
            "234 临沂市\n" +
            "235 滨州市\n" +
            "236 青岛市\n" +
            "237 朔州市\n" +
            "238 晋中市\n" +
            "239 巴中市\n" +
            "240 绵阳市\n" +
            "241 广安市\n" +
            "242 资阳市\n" +
            "243 衢州市\n" +
            "244 台州市\n" +
            "245 舟山市\n" +
            "246 固原市\n" +
            "247 甘南藏族自治州\n" +
            "248 内江市\n" +
            "249 曲靖市\n" +
            "250 淮南市\n" +
            "251 巢湖市\n" +
            "252 黄山市\n" +
            "253 淮北市\n" +
            "254 三明市\n" +
            "255 漳州市\n" +
            "256 陇南市\n" +
            "257 广州市\n" +
            "258 云浮市\n" +
            "259 揭阳市\n" +
            "260 贺州市\n" +
            "261 南宁市\n" +
            "262 遵义市\n" +
            "263 安顺市\n" +
            "264 张家口市\n" +
            "265 唐山市\n" +
            "266 邢台市\n" +
            "267 安阳市\n" +
            "268 郑州市\n" +
            "269 驻马店市\n" +
            "270 宜昌市\n" +
            "271 黄冈市\n" +
            "272 益阳市\n" +
            "273 邵阳市\n" +
            "274 湘西土家族苗族自治州\n" +
            "275 郴州市\n" +
            "276 泰州市\n" +
            "277 宿迁市\n" +
            "278 宜春市\n" +
            "279 鹰潭市\n" +
            "280 朝阳市\n" +
            "281 营口市\n" +
            "282 丹东市\n" +
            "283 鄂尔多斯市\n" +
            "284 延安市\n" +
            "285 商洛市\n" +
            "286 济宁市\n" +
            "287 潍坊市\n" +
            "288 济南市\n" +
            "289 上海市\n" +
            "290 晋城市\n" +
            "2911 澳门特别行政区\n" +
            "2912 香港特别行政区\n" +
            "291 南充市\n" +
            "292 丽水市\n" +
            "293 绍兴市\n" +
            "294 湖州市\n" +
            "295 北海市\n" +
            "296 海南省直辖县级行政单位\n" +
            "297 赤峰市\n" +
            "298 六安市\n" +
            "299 池州市\n" +
            "300 福州市\n" +
            "301 惠州市\n" +
            "302 江门市\n" +
            "303 汕头市\n" +
            "304 梧州市\n" +
            "305 柳州市\n" +
            "306 黔南布依族苗族自治州\n" +
            "307 保定市\n" +
            "308 周口市\n" +
            "309 南阳市\n" +
            "310 孝感市\n" +
            "311 黄石市\n" +
            "312 张家界市\n" +
            "313 湘潭市\n" +
            "314 永州市\n" +
            "315 南京市\n" +
            "316 徐州市\n" +
            "317 无锡市\n" +
            "318 吉安市\n" +
            "319 葫芦岛市\n" +
            "320 鞍山市\n" +
            "321 呼和浩特市\n" +
            "322 吴忠市\n" +
            "323 咸阳市\n" +
            "324 安康市\n" +
            "325 泰安市\n" +
            "326 烟台市\n" +
            "327 吕梁市\n" +
            "328 运城市\n" +
            "329 广元市\n" +
            "330 遂宁市\n" +
            "331 泸州市\n" +
            "332 天津市\n" +
            "333 金华市\n" +
            "334 嘉兴市\n" +
            "335 石嘴山市\n" +
            "336 昭通市\n" +
            "337 铜陵市\n" +
            "338 肇庆市\n" +
            "339 汕尾市\n" +
            "33 嘉峪关市\n" +
            "340 深圳市\n" +
            "341 贵港市\n" +
            "342 黔东南苗族侗族自治州\n" +
            "343 黔西南布依族苗族自治州\n" +
            "344 漯河市\n" +
            "345 湖北省直辖县级行政单位\n" +
            "346 扬州市\n" +
            "347 连云港市\n" +
            "348 常州市\n" +
            "349 九江市\n" +
            "34 金昌市\n" +
            "350 萍乡市\n" +
            "351 辽阳市\n" +
            "352 汉中市\n" +
            "353 菏泽市\n" +
            "354 淄博市\n" +
            "355 大同市\n" +
            "356 长治市\n" +
            "357 阳泉市\n" +
            "358 马鞍山市\n" +
            "359 平凉市\n" +
            "35 白银市\n" +
            "360 银川市\n" +
            "361 玉林市\n" +
            "362 咸宁市\n" +
            "363 怀化市\n" +
            "364 上饶市\n" +
            "365 赣州市\n" +
            "366 聊城市\n" +
            "367 忻州市\n" +
            "368 临汾市\n" +
            "369 达州市\n" +
            "36 兰州市\n" +
            "370 宿州市\n" +
            "371 随州市\n" +
            "372 德州市\n" +
            "373 恩施土家族苗族自治州\n" +
            "37 酒泉市\n" +
            "38 大兴安岭地区\n" +
            "39 黑河市\n" +
            "40 伊春市\n" +
            "41 齐齐哈尔市\n" +
            "42 佳木斯市\n" +
            "43 鹤岗市\n" +
            "44 绥化市\n" +
            "45 双鸭山市\n" +
            "46 鸡西市\n" +
            "47 七台河市\n" +
            "48 哈尔滨市\n" +
            "49 牡丹江市\n" +
            "50 大庆市\n" +
            "51 白城市\n" +
            "52 松原市\n" +
            "53 长春市\n" +
            "54 延边朝鲜族自治州\n" +
            "55 吉林市\n" +
            "56 四平市\n" +
            "57 白山市\n" +
            "58 沈阳市\n" +
            "59 阜新市\n" +
            "60 铁岭市\n" +
            "61 呼伦贝尔市\n" +
            "62 兴安盟\n" +
            "63 锡林郭勒盟\n" +
            "64 通辽市\n" +
            "65 海西蒙古族藏族自治州\n" +
            "66 西宁市\n" +
            "67 海北藏族自治州\n" +
            "68 海南藏族自治州\n" +
            "69 海东地区\n" +
            "70 黄南藏族自治州\n" +
            "71 玉树藏族自治州\n" +
            "72 果洛藏族自治州\n" +
            "73 甘孜藏族自治州\n" +
            "74 德阳市\n" +
            "75 成都市\n" +
            "76 雅安市\n" +
            "77 眉山市\n" +
            "78 自贡市\n" +
            "79 乐山市\n" +
            "80 凉山彝族自治州\n" +
            "81 攀枝花市\n" +
            "82 和田地区\n" +
            "83 喀什地区\n" +
            "84 克孜勒苏柯尔克孜自治州\n" +
            "85 阿克苏地区\n" +
            "86 巴音郭楞蒙古自治州\n" +
            "87 新疆直辖县级行政单位\n" +
            "87 新疆维吾尔自治区直辖县级行政单位\n" +
            "88 博尔塔拉蒙古自治州\n" +
            "89 吐鲁番地区\n" +
            "90 伊犁哈萨克自治州\n" +
            "91 哈密地区\n" +
            "92 乌鲁木齐市\n" +
            "93 昌吉回族自治州\n" +
            "94 塔城地区\n" +
            "95 克拉玛依市\n" +
            "96 阿勒泰地区\n" +
            "97 山南地区\n" +
            "98 林芝地区\n" +
            "99 昌都地区\n";

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] split = BaiduCityCode.citycode.split("\n");
        for (int i = 0; i <split.length ; i++) {

            String[] split1 = split[i].split(" ");
            System.out.println(split1[0]);
            System.out.println(split1[1]);

        }
    }

}