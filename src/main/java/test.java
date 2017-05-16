import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;

/**
 * Created by gaochuang on 2017/4/6.
 */
public class test {

    public static void main(String[] args){
     /*   ObjectMapper  ed = new ObjectMapper();
        ObjectReader ous = new ObjectReader(ed);
        String str2 = "{\"et\":\"kanqiu_client_join\",\"vtm\":1435898329434,\"body\":{\"client\":\"866963024862254\",\"client_type\":\"android\",\"room\":\"NBA_HOME\",\"gid\":\"\",\"type\":\"\",\"roomid\":\"\"},\"time\":1435898329}" ;

        JsonNode ss=null;
        try {
             ss = ous.readTree(str2);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(ss.has("et")){
            System.out.println(ss.get("et").textValue());
        }
*/


        String str2 = "{\"et\":\"kanqiu_client_join\",\"vtm\":1435898329434,\"body\":{\"client\":\"866963024862254\",\"client_type\":\"android\",\"room\":\"NBA_HOME\",\"gid\":\"\",\"type\":\"\",\"roomid\":\"\"},\"time\":1435898329}" ;

        ObjectMapper m = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = m.readTree(str2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode weaNode = rootNode.get("et");
        System.out.println(weaNode);
        System.out.println(weaNode.get("vtm"));
    }

}
