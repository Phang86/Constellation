package com.yyzy.constellation.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String setContent(){
        String content = "此应用软件是一款星座配对多功能软件，主要包括：星座介绍、星配分析、星座配对、星座运势、出行天气、聚合新闻、姓氏查询、生活记账等一些日常功能。";
        return content;
    }

    public final static  List<String> URLS = Arrays.asList(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201507%2F04%2F20150704104529_LGAuQ.thumb.700_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665570591&t=39775d38143604b60af3f4bf4ce67640"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg2.doubanio.com%2Fview%2Frichtext%2Flarge%2Fpublic%2Fp241851172.jpg&refer=http%3A%2F%2Fimg2.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665570683&t=d5c0074c33839bccd3007ed7dcf88b64"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg3.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp267785241.jpg&refer=http%3A%2F%2Fimg3.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665570735&t=32281d2ea4ca2cce59f1cc1642023d93"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp262296599.jpg&refer=http%3A%2F%2Fimg1.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665570981&t=3bb8a2275db835a59a94ed2af4eefd6d"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg3.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp262319980.jpg&refer=http%3A%2F%2Fimg3.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571080&t=159a92ed9e60bd34f788e5a8a81ec875"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg3.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp262296613.jpg&refer=http%3A%2F%2Fimg3.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571133&t=30357f34e0fb30e389330f4c86077036"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202004%2F05%2F20200405210120_jlclt.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571269&t=3088ad47b497010ca102813010c71e9f"
            ,"https://pics0.baidu.com/feed/562c11dfa9ec8a1341ff8eb379531f88a1ecc050.jpeg?token=bf25c6431081b10b5a1ef5576682e04a"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp370906244.jpg&refer=http%3A%2F%2Fimg9.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571363&t=d0aa2c9c5daf5804cdac90d0b3383e89"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202005%2F03%2F20200503165321_MQXcA.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571508&t=6298e8553cb4834015d9066f1347560e"
            ,"https://pics5.baidu.com/feed/0ff41bd5ad6eddc4c062ad95b58b38fa536633aa.jpeg?token=5743bd81a8502cd2a443076cfd55dc42"
            ,"https://pics0.baidu.com/feed/562c11dfa9ec8a1341ff8eb379531f88a1ecc050.jpeg?token=bf25c6431081b10b5a1ef5576682e04a"
            ,"https://pics7.baidu.com/feed/72f082025aafa40fa8e307332f348d4879f0198b.jpeg?token=1a2f0dc71ac53f0e2993afede7b99eb7"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp511465309.jpg&refer=http%3A%2F%2Fimg1.doubanio.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571734&t=b6fc8b8206c9cf540201817c040715f1"
            ,"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202005%2F03%2F20200503033930_Srus4.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1665571778&t=642a275f0714dcce2b2a5f8cfe875f79");

}

