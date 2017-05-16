package com.itm.utils.beanAndJson;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
/*import org.json.JSONArray;
import org.json.JSONObject;*/
import com.alibaba.fastjson.JSONObject;
/*import net.sf.json.JSONArray;
import net.sf.json.JSONObject;*/
import org.apache.avro.data.Json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaochuang on 2017/4/12.
 */
public class jsontoBean {

    /*public static void main(String[] args) {
        String str="{\"student\":[{\"name\":\"leilei\",\"age\":23},{\"name\":\"leilei02\",\"age\":23}]}";
        Student stu = null;
        List list = null;
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            StudentList studentList=objectMapper.readValue(str, StudentList.class);

            list=studentList.getStudent();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(Student s:list){
            System.out.println(s.getName()+"   "+s.getAge());
        }
    }
*/


    public static void main(String[] args) {
        String jsonStr = "{\"age\":23,\"id\":123,\"name\":\"tt_2009\"," +
                "\"province\":\"上海\",\"sex\":\"男\"}";
        JSON js = (JSON) JSON.parse(jsonStr);
        SimpleUser sr = JSONObject.toJavaObject(js,SimpleUser.class);
        System.out.println(sr);

    }
   /* *//**
     * 将json格式封装的简单实体类型数据转换成简单类型的javabean
     * @return
     *//*
    private static Object JSON2SimpleBean() {
        String jsonStr = "{\"age\":23,\"id\":123,\"name\":\"tt_2009\"," +
                "\"province\":\"上海\",\"sex\":\"男\"}";
        JSONObject jsonBean = JSONObject.fromObject(jsonStr);
        return JSONObject.toBean(jsonBean, SimpleUser.class);
    }

    *//**
     * 将json格式封装的复杂实体数据转换成复杂类型的javabean
     * @return
     *//*
    private static Object JSON2ComplexBean() {
        String jsonStr = "{\"address\":[\"北京\",\"上海\",\"广州\"]," +
                "\"age\":23,\"id\":123,\"name\":\"tt_2009\",\"province\":\"上海\",\"sex\":\"男\"}";
        JSONObject jsonBean = JSONObject.fromObject(jsonStr);
        return JSONObject.toBean(jsonBean, ComplexUser.class);
    }

    *//**
     * 将json格式封装的列表数据转换成java的List数据
     * @return
     *//*
    private static Object JSON2List() {
        String jsonArray =
                "[{\"age\":23,\"id\":123,\"name\":\"tt_2009_0\",\"province\":\"上海\",\"sex\":\"男\"}," +
                        "{\"age\":24,\"id\":123,\"name\":\"tt_2009_1\",\"province\":\"上海\",\"sex\":\"男\"}," +
                        "{\"age\":32,\"id\":123,\"name\":\"tt_2009_9\",\"province\":\"上海\",\"sex\":\"男\"}]";

        return JSONArray.toList(JSONArray.fromObject(jsonArray), new SimpleUser(), new JsonConfig());
    }



    *//**
     * 将json格式封装的字符串数据转换成java中的Map数据
     * @return
     *//*
    private static Map<String, SimpleUser> JSON2Map() {
        Map<String, SimpleUser> map = new HashMap<String, SimpleUser>();
        String jsonMapStr =
                "{\"tt_2009_4\":{\"age\":27,\"id\":123,\"name\":\"tt_2009_4\",\"province\":\"上海\",\"sex\":\"男\"}," +
                        "\"tt_2009_6\":{\"age\":29,\"id\":123,\"name\":\"tt_2009_6\",\"province\":\"上海\",\"sex\":\"男\"}," +
                        "\"tt_2009_0\":{\"age\":23,\"id\":123,\"name\":\"tt_2009_0\",\"province\":\"上海\",\"sex\":\"男\"}}";
        JSONObject jsonMap = JSONObject.fromObject(jsonMapStr);
        Iterator<String> it = jsonMap.keys();
        while(it.hasNext()) {
            String key = (String) it.next();
            SimpleUser u = (SimpleUser) JSONObject.toBean(
                    JSONObject.fromObject(jsonMap.get(key)),
                    SimpleUser.class);
            map.put(key, u);
        }
        return map;
    }*/




}
