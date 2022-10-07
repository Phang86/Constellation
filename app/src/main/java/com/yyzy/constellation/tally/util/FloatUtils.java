package com.yyzy.constellation.tally.util;

import java.math.BigDecimal;

public class FloatUtils {

    public static float div(float v1,float v2){
        //除法运算，保留四位小数
        float v3 = v1/v2;
        BigDecimal bl = new BigDecimal(v3);
        float v4 = bl.setScale(4, 4).floatValue();
        //再将浮点数进行百分比
        float num = v4*100;
        BigDecimal decimal = new BigDecimal(num);
        float v5 = decimal.setScale(2, 4).floatValue();
        return v5;
    }
}
