package com.itm.utils.beanAndJson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by gaochuang on 2017/4/12.
 */
public class JsonBeanUtil {

    private static final String DEFAULT_CHARSET_NAME = "UTF-8";






    public static void main(String[] args) {


        String jsonStr = "{\"age\":23,\"id\":123,\"name\":\"tt_2009\"," +
                "\"province\":\"上海\",\"sex\":\"男\"}";

        String jsonStr2 = "{\"address\":[\"北京\",\"上海\",\"广州\"]," +
                "\"age\":23,\"id\":123,\"name\":\"tt_2009\",\"province\":\"上海\",\"sex\":\"男\"}";
        SimpleUser suser =(SimpleUser) JSON2SimpleBean(jsonStr);

        ComplexUser ssyy =(ComplexUser) JSON2ComplexBean(jsonStr2);
        System.out.println(ssyy);

        List<Double> ld = new ArrayList<>();
        ld.add(1.01);
        ld.add(3.01);
        ld.add(5.01);
        ld.add(7.01);
        ld.add(10.01);
        SimpleUser suser2 = new SimpleUser();
        suser2.setAge(43);
        suser2.setId(345);
        suser2.setName("ssss");
        suser2.setData(ld);
       // suser2.setProvince("sdfg");
      //  suser2.setSex("xxx");


        System.out.println(suser2+"lllllllll");
        List<SimpleUser> suserq = new ArrayList<>();
       // suserq.add(suser);
        suserq.add(suser2);

        /**
         * 将list<bean> 转化为 json字符串
         */
        System.out.println(serializeListToJson(suserq));
        String ss = serializeListToJson(suserq);

        System.out.println(ss + "jsonsssssssss");


        /**
         * Json字符串 转化为对应的bean
         */
        SimpleUser sdf = deserialize(jsonStr,SimpleUser.class);
        System.out.println(sdf);

    }




    /**
     * 将json格式封装的简单实体类型数据转换成简单类型的javabean
     * @return
     */
    private static Object JSON2SimpleBean( String jsonStr) {

        JSON js = (JSON) JSON.parse(jsonStr);
        SimpleUser jsonBean = JSONObject.toJavaObject(js,SimpleUser.class);
        System.out.println(jsonBean);
        return jsonBean;
    }


    /**
     * 将json格式封装的复杂实体数据转换成复杂类型的javabean
     * @return
     */

    private static Object JSON2ComplexBean(String jsonStr) {
        JSON js = (JSON) JSON.parse(jsonStr);
        ComplexUser jsonBean = JSONObject.toJavaObject(js, ComplexUser.class);
        return jsonBean;
    }


    /**
     * 将list<bean>   Bean  转化为 json字符串
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String serializeListToJson(T object) {
        String json =JSON.toJSONString(object);
        return json.substring(1,json.length()-1);
    }


    /**
     * 将Json字符串转化为 对应的bean
     * @param string
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String string, Class<T> clz) {
        return JSON.parseObject(string, clz);
    }


    /**
     * 从指定路径加载 Json 转化为 Bean
     * @param path
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T load(Path path, Class<T> clz) throws IOException {
        return deserialize(
                new String(Files.readAllBytes(path), DEFAULT_CHARSET_NAME), clz);
    }


    /**
     * 将 Bean 转化为Json 写到指定路径
     * @param path
     * @param object
     * @param <T>
     * @throws IOException
     */
    public static <T> void save(Path path, T object) throws IOException {
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path,
                serializeListToJson(object).getBytes(DEFAULT_CHARSET_NAME),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }




    /**
     * 将json格式封装的列表数据转换成java的List数据
     * @return
     */

    /*private static Object JSON2List() {
        String jsonArray =
                "[{\"age\":23,\"id\":123,\"name\":\"tt_2009_0\",\"province\":\"上海\",\"sex\":\"男\"}," +
                        "{\"age\":24,\"id\":123,\"name\":\"tt_2009_1\",\"province\":\"上海\",\"sex\":\"男\"}," +
                        "{\"age\":32,\"id\":123,\"name\":\"tt_2009_9\",\"province\":\"上海\",\"sex\":\"男\"}]";


       // return JSONArray.toList(JSONArray.fromObject(jsonArray), new SimpleUser(), new JsonConfig());
    }*/



}
