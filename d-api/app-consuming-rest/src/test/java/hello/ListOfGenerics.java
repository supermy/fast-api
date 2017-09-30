package hello;

import java.util.ArrayList;
import java.util.List;

/**
 * 不能创建泛型数组。一般的解决方案是任何想要创建泛型数组的地方都使用ArrayList:
 *
 * Created by moyong on 2017/9/29.
 */
public class ListOfGenerics<T> {
    private List<T> array = new ArrayList<T>();

    public void add(T item) {
        array.add(item);
    }

    public T get(int index) {
        return array.get(index);
    }

}