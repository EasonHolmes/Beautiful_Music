package com.life.me.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.life.me.R;
import com.life.me.entity.CacheBean;
import com.life.me.entity.MusicBean;
import com.life.me.entity.MusicImgBean;
import com.life.me.model.Music_Model;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.SingleGson;
import com.life.me.mutils.SingleRequestQueue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.spec.ECField;
import java.util.List;

/**
 * Created by cuiyang on 15/10/1.
 */
public class Music_Presenter {
    /**
     * 天天动听的接口需要转编码 中文会乱码
     * q{0}=需要搜索的歌曲或歌手
     * <p>
     * page{1}=查询的页码数
     * <p>
     * size{2}=当前页的返回数量
     * 讯飞的结果中有可能会有空格要去掉
     */
    public void getMusic_Result(String code, Context mContext, Music_Model callback) {
        try {
            String temp = URLEncoder.encode(code, "UTF-8");
            final String finalTemp = temp;
            HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
                    "http://so.ard.iyyin.com/s/song_with_out?q={" + temp.replaceAll(" ", "").trim() + "}&page={1}&size={1}", new HttpUtils.RequestCallBack() {
                        @Override
                        public void success(String result) {
                            Log.e(getClass().getName(), "dfdfdf==" + result);
                            MusicBean bean = SingleGson.getRequestQueue().fromJson(result, MusicBean.class);
                            List<MusicBean.DataEntity> data = bean.getData();
                            if (data == null || data.size() == 0) {
                                callback.error("没有找到对应歌曲哦");
                                return;
                            }
                            for (int i = 0; i < data.size(); i++) {
                                //有些是乱七八糟的歌。首先优先歌名匹配 再pick_count大于3000一般是正确的从高往低的拿越高越准
                                if ((data.get(i).getSong_name().contains(finalTemp) && data.get(i).getPick_count() > 200)
                                        || data.get(i).getPick_count() > 10000 || data.get(i).getPick_count() > 8000
                                        || data.get(i).getPick_count() > 6000 || data.get(i).getPick_count() > 4000
                                        || data.get(i).getPick_count() > 2000) {
                                    //get(0)是压缩品质1是标准品质3是超高品质
                                    String MusicUrl;
                                    SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(mContext);
                                    if (share.getBoolean(mContext.getResources().getString(R.string.quality), true)) {
                                        MusicUrl = data.get(i).getAudition_list().get(0).getUrl();
                                    } else {
                                        MusicUrl = data.get(i).getAudition_list().get(1).getUrl();
                                    }
                                    String singerName = data.get(i).getSinger_name();
                                    String songName = data.get(i).getSong_name();
                                    callback.getMusic(MusicUrl, singerName, songName);
                                    return;
                                } else {
                                    callback.error("没有找到对应歌曲哦");
                                }
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    /**
     * 天天动听的接口需要转编码 中文会乱码
     * 拿歌曲图片
     * 关键词中有可能会有空格要去掉
     */
    public void getMusic_Img(Context mContext, String Keyword, Music_Model callback) {
        try {
            String temp = URLEncoder.encode(Keyword, "UTF-8");
            HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
                    "http://lp.music.ttpod.com/pic/down?artist={" + temp.replaceAll(" ", "").trim() + "}",
                    new HttpUtils.RequestCallBack() {
                        @Override
                        public void success(String result) {
                            MusicImgBean bean = SingleGson.getRequestQueue().fromJson(result, MusicImgBean.class);
                            if (bean.getMsg() == null) {
                                callback.getMusicImg(bean.getData().getSingerPic());
                            } else {
                                callback.error("没有找到对应图片哦");
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }

    /**
     * 制作蒙板
     */
    public Bitmap blur(Bitmap bkg, Context mContext) {
        try {
            RenderScript rs = RenderScript.create(mContext);
            Allocation overlayAlloc = Allocation.createFromBitmap(rs, bkg);
            ScriptIntrinsicBlur blur =
                    ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
            blur.setInput(overlayAlloc);
            blur.setRadius(19);
            blur.forEach(overlayAlloc);
            overlayAlloc.copyTo(bkg);
            return bkg;
        } catch (Exception e) {
            Log.e(getClass().getName(),"blur_error=="+e.getMessage());
        }
        return null;
    }


}
