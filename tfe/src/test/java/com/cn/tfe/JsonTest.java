package com.cn.tfe;

import com.cn.tfe.entity.CommonRes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.util.List;

public class JsonTest {

    private static final Logger log = LoggerFactory.getLogger(JsonTest.class);

    @Test
    public void test(){
        String str = "{\"from\":\"en\",\"to\":\"zh\",\"trans_result\":[{\"src\":\"dog\",\"dst\":\"狗\",\"src_tts\":\"https://fanyi-api.baidu.com/api/trans/api/tts?query=dog&appid=20201103000607261&lang=en&sign=e86a373925f13d3057e950477ee21721\",\"dst_tts\":\"https://fanyi-api.baidu.com/api/trans/api/tts?query=%E7%8B%97&appid=20201103000607261&lang=zh&sign=9a5ea161acc0ce1d19a5d3cfa7012089\",\"dict\":\"{\\\"lang\\\":\\\"1\\\",\\\"word_result\\\":{\\\"edict\\\":{\\\"item\\\":[{\\\"tr_group\\\":[{\\\"tr\\\":[\\\"a member of the genus Canis (probably descended from the common wolf) that has been domesticated by man since prehistoric times\\\",\\\"occurs in many breeds\\\"],\\\"example\\\":[\\\"the dog barked all night\\\"],\\\"similar_word\\\":[\\\"domestic dog\\\",\\\"Canis familiaris\\\"]},{\\\"tr\\\":[\\\"metal supports for logs in a fireplace\\\"],\\\"example\\\":[\\\"the andirons were too hot to touch\\\"],\\\"similar_word\\\":[\\\"andiron\\\",\\\"firedog\\\",\\\"dog-iron\\\"]},{\\\"tr\\\":[\\\"a hinged catch that fits into a notch of a ratchet to move a wheel forward or prevent it from moving backward\\\"],\\\"example\\\":[],\\\"similar_word\\\":[\\\"pawl\\\",\\\"detent\\\",\\\"click\\\"]},{\\\"tr\\\":[\\\"a smooth-textured sausage of minced beef or pork usually smoked\\\",\\\"often served on a bread roll\\\"],\\\"example\\\":[],\\\"similar_word\\\":[\\\"frank\\\",\\\"frankfurter\\\",\\\"hotdog\\\",\\\"hot dog\\\",\\\"wiener\\\",\\\"wienerwurst\\\",\\\"weenie\\\"]},{\\\"tr\\\":[\\\"someone who is morally reprehensible\\\"],\\\"example\\\":[\\\"you dirty dog\\\"],\\\"similar_word\\\":[\\\"cad\\\",\\\"bounder\\\",\\\"blackguard\\\",\\\"hound\\\",\\\"heel\\\"]},{\\\"tr\\\":[\\\"informal term for a man\\\"],\\\"example\\\":[\\\"you lucky dog\\\"],\\\"similar_word\\\":[]},{\\\"tr\\\":[\\\"a dull unattractive unpleasant girl or woman\\\"],\\\"example\\\":[\\\"she got a reputation as a frump\\\",\\\"she's a real dog\\\"],\\\"similar_word\\\":[\\\"frump\\\"]}],\\\"pos\\\":\\\"noun\\\"},{\\\"tr_group\\\":[{\\\"tr\\\":[\\\"go after with the intent to catch\\\"],\\\"example\\\":[\\\"The policeman chased the mugger down the alley\\\",\\\"the dog chased the rabbit\\\"],\\\"similar_word\\\":[\\\"chase\\\",\\\"chase after\\\",\\\"trail\\\",\\\"tail\\\",\\\"tag\\\",\\\"give chase\\\",\\\"go after\\\",\\\"track\\\"]}],\\\"pos\\\":\\\"verb\\\"}],\\\"word\\\":\\\"dog\\\"},\\\"zdict\\\":\\\"\\\",\\\"simple_means\\\":{\\\"word_name\\\":\\\"dog\\\",\\\"from\\\":\\\"original\\\",\\\"word_means\\\":[\\\"\\\\u72d7\\\",\\\"\\\\u72ac\\\",\\\"\\\\u516c\\\\u72d7\\\",\\\"\\\\u516c\\\\u72d0\\\",\\\"\\\\u516c\\\\u72fc\\\",\\\"\\\\u8d5b\\\\u72d7\\\",\\\"\\\\u56f0\\\\u6270\\\\uff0c\\\\u6298\\\\u78e8\\\\uff0c\\\\u7ea0\\\\u7f20\\\",\\\"\\\\u8ddf\\\\u8e2a\\\",\\\"\\\\u5c3e\\\\u968f\\\"],\\\"exchange\\\":{\\\"word_third\\\":[\\\"dogs\\\"],\\\"word_ing\\\":[\\\"dogging\\\"],\\\"word_done\\\":[\\\"dogged\\\"],\\\"word_pl\\\":[\\\"dogs\\\"],\\\"word_past\\\":[\\\"dogged\\\"]},\\\"tags\\\":{\\\"core\\\":[\\\"\\\\u9ad8\\\\u8003\\\",\\\"CET4\\\",\\\"\\\\u8003\\\\u7814\\\"],\\\"other\\\":[\\\"BEC\\\"]},\\\"symbols\\\":[{\\\"ph_en\\\":\\\"d\\\\u0252\\\\u0261\\\",\\\"ph_am\\\":\\\"d\\\\u0254\\\\u02d0\\\\u0261\\\",\\\"parts\\\":[{\\\"part\\\":\\\"n.\\\",\\\"means\\\":[\\\"\\\\u72d7\\\",\\\"\\\\u72ac\\\",\\\"\\\\u516c\\\\u72d7\\\",\\\"\\\\u516c\\\\u72d0\\\",\\\"\\\\u516c\\\\u72fc\\\",\\\"\\\\u8d5b\\\\u72d7\\\"]},{\\\"part\\\":\\\"v.\\\",\\\"means\\\":[\\\"(\\\\u957f\\\\u671f)\\\\u56f0\\\\u6270\\\\uff0c\\\\u6298\\\\u78e8\\\\uff0c\\\\u7ea0\\\\u7f20\\\",\\\"\\\\u8ddf\\\\u8e2a\\\",\\\"\\\\u5c3e\\\\u968f\\\"]}],\\\"ph_other\\\":\\\"\\\"}]}}}\"}]}";

//        log.info(""+JsonParser.parseString(str).isJsonObject());
        String dictStr = JsonParser.parseString(str).getAsJsonObject()
                .getAsJsonArray("trans_result").get(0).getAsJsonObject()
                .getAsJsonPrimitive("dict").getAsString();
        JsonObject resObj = JsonParser.parseString(dictStr).getAsJsonObject();
        log.info(""+resObj.isJsonObject());
//        log.info(""+dictObj.isJsonPrimitive());
    }

    @Test
    public void fromJsonTest(){
        String str = "[{\"dst\":\"小狗\",\"phonetic\":\"xiao gou\",\"dstTts\":\"http://\",\"word\":\"paddy\"}]";
        Gson gson = new Gson();
        log.info(gson.fromJson(str,new TypeToken<List<CommonRes>>(){}.getType()).toString());
    }

    @Test
    public void testPrimitiveType(){
        String str = "{\"name\":\"\"}";
        JsonElement ele = JsonParser.parseString(str).getAsJsonObject().get("name");
        log.info(ele.isJsonPrimitive()+"");
    }
}
