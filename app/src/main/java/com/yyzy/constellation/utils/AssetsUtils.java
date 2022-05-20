package com.yyzy.constellation.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.squareup.picasso.Picasso;
import com.yyzy.constellation.entity.StarInfoEntity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//读取Assets文件夹当中的数据
public class AssetsUtils {

    private static Map<String,Bitmap> logeImgMap;
    private static Map<String,Bitmap> contentLogoImgMap;


    //读取Assets文件夹中的内容
    public static String getJsonFromAssets(Context context, String fileName){
        //获取Assets中的管理器
        AssetManager am = context.getResources().getAssets();
        //获取输入流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = am.open(fileName);
            //读取内容存放到内存流当中
            int hasRead = 0;
            byte[] buf = new byte[1024];
            while (true){
                hasRead = is.read(buf);
                if (hasRead == -1) {
                    break;
                }
                baos.write(buf,0,hasRead);
            }
            String str = baos.toString();
            is.close();
            return str;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //读取Assets文件夹当中的图片
    public static Bitmap getBitmapFromAssets(Context context, String fileName){
        Bitmap bitmap = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    //将Assets文件夹下的图片一起读取，放置到内存当中，便于管理
    public static void saveBitmapFromAssets(Context context, StarInfoEntity starInfoEntity){
        logeImgMap = new HashMap<>();
        contentLogoImgMap = new HashMap<>();
        List<StarInfoEntity.StarinfoDTO> dtoList = starInfoEntity.getStarinfo();
        for (int i = 0; i < dtoList.size(); i++) {
            String logoname = dtoList.get(i).getLogoname();
            String fileName = "xzlogo/"+logoname+".png";
            Bitmap bitmapFromAssets = getBitmapFromAssets(context, fileName);
            logeImgMap.put(logoname,bitmapFromAssets);

            String contentname = "xzcontentlogo/"+logoname+".png";
            Bitmap bitmap = getBitmapFromAssets(context, contentname);
            contentLogoImgMap.put(logoname,bitmap);
        }
    }

    public static Map<String, Bitmap> getContentLogoImgMap() {
        return contentLogoImgMap;
    }

    public static Map<String, Bitmap> getLogeImgMap() {
        return logeImgMap;
    }
}
