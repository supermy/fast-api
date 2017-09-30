package hello;

/**
 * 可变参数与泛型
 *
 * Created by moyong on 2017/9/29.
 */
import java.util.ArrayList;
import java.util.List;

public class GenericVarargs {
    /**
     *  静态方法上的泛型：静态方法无法访问类上定义的泛型。如果静态方法操作的引用数据类型不确定的时候，必须要将泛型定义在方法上。
     */
    public static <T> List<T> makeList(T... args){
        List<T> result = new ArrayList<T>();
        for(T item:args)
            result.add(item);
        return result;
    }
    public static void main(String[] args) {
        List ls = makeList("A");
        System.out.println(ls);
        ls = makeList("A","B","C");
        System.out.println(ls);
        ls = makeList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));
        System.out.println(ls);


    }


}