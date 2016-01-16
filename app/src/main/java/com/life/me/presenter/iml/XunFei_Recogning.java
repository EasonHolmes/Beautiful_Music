package com.life.me.presenter.iml;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.life.me.R;
import com.life.me.entity.XunFei_Speak_Bean;
import com.life.me.mutils.SingleGson;

/**
 * Created by cuiyang on 15/9/30.
 */
public class XunFei_Recogning extends java.util.Observable implements RecognizerListener {

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 引擎类型
    public String mEngineType = SpeechConstant.TYPE_CLOUD;
    public XunFei_Speak_Bean result;


    public XunFei_Recogning() {
    }

    public void initRecogn(Context mContext) {
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        mIat.startListening(this);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer(dialog和SpeechRecognizer只一个就可以)
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Log.e(getClass().getName(), "初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam(Context mContext) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");//设置应用领域

        //获取设置界面的数据
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (share.getBoolean(mContext.getResources().getString(R.string.language), true)) {//返回语言的类型
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        } else {
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        }
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");//用于设置语言区域mandarin中文
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "3000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1500");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写UI监听器
     * islast为true是最后的标点。false是返回结果
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!isLast) {
                //取消弹框通知观察者
                mIatDialog.dismiss();
                result = SingleGson.getRequestQueue().fromJson(results.getResultString(), XunFei_Speak_Bean.class);
                setChanged();
                XunFei_Recogning.this.notifyObservers(result);
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Log.e(getClass().getName(), "ui_error-===" + error.getPlainDescription(true));
        }

    };

    /**
     * 听写监听器。
     */
    @Override
    public void onBeginOfSpeech() {
        // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        Log.e(getClass().getName(), "开始说话");
    }

    @Override
    public void onError(SpeechError error) {
        // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
        // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
        Log.e(getClass().getName(), error.getPlainDescription(true));
    }

    @Override
    public void onEndOfSpeech() {
        // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        Log.e(getClass().getName(), "结束说话");
    }

    @Override
    public void onResult(RecognizerResult results, boolean isLast) {
        Log.e(getClass().getName(), "resultttt" + results.getResultString() + "ffffff==" + isLast);
        if (!isLast) {
            //通知观察者
            result = SingleGson.getRequestQueue().fromJson(results.getResultString(), XunFei_Speak_Bean.class);
            setChanged();
            Log.e(getClass().getName(), "tttt222===" + results.getResultString());
            XunFei_Recogning.this.notifyObservers(result);
        }
    }

    @Override
    public void onVolumeChanged(int volume, byte[] data) {
//        Log.e(getClass().getName(), "当前正在说话，音量大小：" + volume);
//        Log.e(getClass().getName(), "返回音频数据：" + data.length);
    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        // 若使用本地能力，会话id为null
        //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
        //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
        //		Log.d(TAG, "session id =" + sid);
        //	}
    }

    public void unConncetion() {
        // 退出时释放连接
        if (mIat != null) {
            mIat.cancel();
            mIat.destroy();
            mIat = null;
            mIatDialog = null;
        }
    }

    public void showDialog() {
        if (mIatDialog != null) mIatDialog.show();
    }

    public void dissDialog() {
        if (mIatDialog != null) mIatDialog.dismiss();
    }
}
