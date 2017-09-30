package hello;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;


/**
 * 重构 JsonBean String Bean 之间的转换
 * Created by moyong on 2017/9/26.
 */
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);


    /**
     * 将bean转换成Json字符串
     * @param bean
     * @return
     */
    public static String toJSONString(Object bean) {
        return new Gson().toJson(bean);
    }

    /**
     * 将Json字符串转换成对象
     * @param json
     * @param beanClass
     * @return
     */
    public static Object toBean(String json, Class beanClass) {
        return new Gson().fromJson(json, beanClass);
    }

    /**
     *
     * 数据与对象的通用格式
     *
     * @param json
     * @return
     */
    public static JsonElement toJson(String json) {
        return  new JsonParser().parse(json);
    }

    /**
     *
     * @param json
     */
    public static JsonArray toJsonArray(String json) {
        //将JsonArray类型的Json字符串解析成对象方法
        JsonArray obj = new JsonParser().parse(json).getAsJsonArray();

        log.debug("{}",obj);

        return obj;
    }

    /**
     *
     * @param json
     */
    public static JsonObject toJsonObject(String json) {
        //将Json字符串转换成JsonObject对象
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();

        log.debug("{}",obj);

        return obj;
    }


}
