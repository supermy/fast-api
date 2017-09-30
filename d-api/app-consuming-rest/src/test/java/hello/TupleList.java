package hello;

/**
 * Created by moyong on 2017/9/29.
 */

import java.util.ArrayList;

class ThreeTuple2<A,B,C>{
    public final A first;
    public final B second;
    private final C three;
    public ThreeTuple2(A a,B b,C c){
        first = a;
        second = b;
        three = c;
    }
    public String toString(){
        return "(" + first + "," + second + "," + three + ")";
    }
}

/**
 * 构建复杂模型如list元组
 *
 * @param <A>
 * @param <B>
 * @param <C>
 */
public class TupleList<A,B,C> extends ArrayList<ThreeTuple2<A,B,C>> {

    static ThreeTuple2<Integer,String,Character> h(Integer a,String b,Character c){
        return new ThreeTuple2<Integer,String,Character>(a,b,c);
    }

    public static void main(String[] args) {
        TupleList<Integer,String,Character> ts = new TupleList<Integer,String,Character>();
        ts.add(h(99,"东方不败",'a'));
        ts.add(h(88,"黄金时代",'b'));
        ts.add(h(77,"时代广场",'c'));
        for(ThreeTuple2<Integer,String,Character> ttp:ts)
            System.out.println(ttp);
    }
}