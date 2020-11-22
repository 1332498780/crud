package com.cn.tfe;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChineseTest {

    Logger log = LoggerFactory.getLogger(ChineseTest.class);

    @Test
    public void testChinese() throws BadHanyuPinyinOutputFormatCombination {

        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
         outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
         outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
         outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        String str = "你是一个大好人";
        StringBuilder builder = new StringBuilder();
        for(char c : str.toCharArray()){
            String[] res = PinyinHelper.toHanyuPinyinStringArray(c,outputFormat);
            builder.append(res[0]).append(' ');
        }
        log.info(builder.toString());
    }
}
