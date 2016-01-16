package com.life.me.presenter.iml;


import com.life.me.app.BaseActivity;

/**
 * Created by cuiyang on 15/10/1.
 */
public abstract class Music_Presenter extends BaseActivity {





//    /**
//     * 天天动听的接口需要转编码 中文会乱码
//     * q{0}=需要搜索的歌曲或歌手
//     * <p>
//     * page{1}=查询的页码数
//     * <p>
//     * size{2}=当前页的返回数量
//     * 讯飞的结果中有可能会有空格要去掉
//     * <p>
//     * <p>
//     * http://mobilecdn.kugou.com/new/app/i/search.php?cmd=302&keyword=%E8%90%A7%E6%95%AC%E8%85%BE&with_res_tag=1  搜索框联想
//     */
//    public void getMusic_Result(final String code, Context mContext, Music_Model callback) {
//        try {
//            final String finalcode = Utils.urlEncode(code);
//            HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
//                    "http://trackercdn.kugou.com/i/v2/?album_id=" + 500954 + "&pid=2&mid=22714590359938611205692132001742552671&cmd=26&token=&key=b565ce7b4af8a0c3fa2bbcf37ce07ef9&hash=9ced8d3f686877f165649993b0ffaf24&behavior=play&version=7995&appid=1005&module=&vipType=0&userid=0&with_res_tag=1", new HttpUtils.RequestCallBack() {
//                        @Override
//                        public void success(String result) {
//                            Log.e(getClass().getName(), "dfdfdf==" + result);
//                            Log.e(getClass().getName(),"fffffff===="+result.substring(23,result.length()-23));
//                            MusicBean bean = SingleGson.getRequestQueue().fromJson(result.substring(23,result.length()-21), MusicBean.class);
//                            callback.getMusic(bean.getUrl().get(0),bean.getFileName(),"");
//                        }
//                    });
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * 天天动听的接口需要转编码 中文会乱码
//     * 拿歌曲图片
//     * 关键词中有可能会有空格要去掉
//     */
//    public void getMusic_Img(Context mContext, String Keyword, Music_Model callback) {
//        try {
//            final String finalcode = Utils.urlEncode(Keyword);
//            HttpUtils.getSingleton().getResultForHttpGet(SingleRequestQueue.getRequestQueue(mContext),
//                    "http://lp.music.ttpod.com/pic/down?artist=" + finalcode.replaceAll(" ", "").trim() + "",
//                    new HttpUtils.RequestCallBack() {
//                        @Override
//                        public void success(String result) {
//                            MusicImgBean bean = SingleGson.getRequestQueue().fromJson(result, MusicImgBean.class);
//                            if (bean.getMsg() == null) {
//                                callback.getMusicImg(bean.getData().getSingerPic());
//                            } else {
//                                callback.error("没有找到对应图片哦");
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//        }
//    }

}
