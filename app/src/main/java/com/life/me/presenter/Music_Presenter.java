package com.life.me.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.life.me.entity.MusicBean;
import com.life.me.entity.MusicImgBean;
import com.life.me.model.Music_Model;
import com.life.me.mutils.HttpUtils;
import com.life.me.mutils.SingleGson;
import com.life.me.mutils.SingleRequestQueue;

import java.util.List;

/**
 * Created by cuiyang on 15/10/1.
 */
public class Music_Presenter {


    /**
     * q{0}=需要搜索的歌曲或歌手
     * <p>
     * page{1}=查询的页码数
     * <p>
     * size{2}=当前页的返回数量
     * 讯飞的结果中有可能会有空格要去掉
     */
    public void getMusic_Result(String code, Context mContext, Music_Model callback) {
        HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
                "http://so.ard.iyyin.com/s/song_with_out?q={" + code.replaceAll(" ", "").trim() + "}&page={1}&size={1}", new HttpUtils.RequestCallBack() {
                    @Override
                    public void success(String result) {
                        MusicBean bean = SingleGson.getRequestQueue().fromJson(result, MusicBean.class);
                        List<MusicBean.DataEntity> data = bean.getData();
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getPick_count() > 10000 || data.get(i).getPick_count() > 8000
                                    || data.get(i).getPick_count() > 6000 || data.get(i).getPick_count() > 4000
                                    || data.get(i).getPick_count() > 3000) {//有些是乱七八糟的歌。pick_count大于3000一般是正确的从高往低的拿越高越准
                                //get(0)是压缩品质1是标准品质3是超高品质
                                String MusicUrl = data.get(i).getUrl_list().get(0).getUrl();
                                String singerName = data.get(i).getSinger_name();
                                String songName = data.get(i).getSong_name();
                                callback.getMusic(MusicUrl, singerName, songName);
                                return;
                            }
                        }
                    }
                });
    }

    /**
     * 拿歌曲图片
     * 关键词中有可能会有空格要去掉
     */
    public void getMusic_Img(Context mContext, String Keyword, Music_Model callback) {
        try {
            HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
                    "http://lp.music.ttpod.com/pic/down?artist={" + Keyword.replaceAll(" ", "").trim() + "}",
                    result -> callback.getMusicImg(SingleGson.getRequestQueue().fromJson(result, MusicImgBean.class).
                            getData().getSingerPic() + ""));
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
        }
        return null;
    }


}
