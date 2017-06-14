package com.cgbsoft.lib.utils.net;

import android.text.TextUtils;

import com.cgbsoft.lib.base.mvp.model.BaseResult;
import com.cgbsoft.lib.utils.exception.ApiException;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-09-01
 * Time: 20:27
 * FIXME
 * Rx处理服务器返回
 */
class RxResultHelper {
    static Observable.Transformer<ResponseBody, String> filterResultToString() {
        return tObservabe -> tObservabe.flatMap(new Func1<ResponseBody, Observable<String>>() {
            @Override
            public Observable<String> call(ResponseBody responseBody) {
                String string = "";
                try {
                    string = responseBody.string();
                    System.out.println("-----string=" + string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return createData(string);
            }
        });
    }

    static <T> Observable.Transformer<BaseResult<T>, T> handleResult() {
        return tObservable -> tObservable.flatMap(
                new Func1<BaseResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseResult<T> entity) {
                        if(entity != null && TextUtils.isEmpty(entity.message) && entity.result != null && !"".equals(entity.result)) {
                            return createData(entity.result);
                        }else {
                            //todo 测试用
                           return Observable.error(new ApiException("5000", "error"));
                        }
//                        if (entity.isOk()) {
//                            return createData(entity.result);
//                        } else {
//                            if (SettingPreference.isLogin(BaseApplication.getContext())
//                                    && SettingPreference.getUserInfoData(BaseApplication.getContext()) != null) {
//                                SettingPreference.setRecommendState(BaseApplication.getContext(), SettingPreference.getUserInfoData(BaseApplication.getContext()).getId(), false);
//                            }
//
//                            if (TextUtils.equals(entity.code, "400_8") || TextUtils.equals(entity.code, "400_5") || TextUtils.equals(entity.code, "401")
//                                    || TextUtils.equals(entity.code, "503")) {
//                                Toast.makeText(BaseApplication.getContext(), !TextUtils.equals(entity.code, "400_8") ? "登录失效,请重新登录..." : "账号已被查封", Toast.LENGTH_SHORT).show();
//                                if (!Utils.isOpenLoginPage()) {
//                                    SettingPreference.saveUserInfoComplete(BaseApplication.getContext(), false);
//                                    SettingPreference.quitLogin(BaseApplication.getContext());
//                                    RxBus.get().post(CLOSE_MAIN_PAGE, true);
//                                    RxBus.get().post(WELCOME_FINISH_OBSERVABLE, true);
//
//                                    Intent intent = new Intent(BaseApplication.getContext(), LoginBaseActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.putExtra("isStartOpen", true);
//                                    BaseApplication.getContext().startActivity(intent);
//                                }
//                                Observable.error(new ApiException(entity.code, "登录失效,请重新登录..."));
//                            } else if (entity.code.equals("301")) {
//                                Observable.error(new ApiException(entity.code, "该手机号已被注册"));
//                            } else if (entity.code.equals("315")) {
//                                Observable.error(new ApiException(entity.code, entity.message));//不允许评论
//                            } else if (entity.code.equals("330")) {
//                                Observable.error(new ApiException(entity.code, "请输入正确手机号码"));
//                            } else if (entity.code.equals("303")) {
//                                Observable.error(new ApiException(entity.code, "验证码失效"));
//                            } else if (entity.code.equals("321")) {
//                                Observable.error(new ApiException(entity.code, "密码有误"));
//                            } else if (entity.code.equals("333")) {
//                                Observable.error(new ApiException(entity.code, "重了,\n" +
//                                        "\" + \"制作人最怕抄袭"));
//
//                            } else if (entity.code.equals("351")) {
//                                Observable.error(new ApiException(entity.code, "手机号已被绑定"));
//                            } else if (entity.code.equals("335")) {
//                                Observable.error(new ApiException(entity.code, "注册获取验证码已超3次，请明天再试"));
//                            } else if (entity.code.equals("353")) {
//                                Observable.error(new ApiException(entity.code, "种草莓次数用光，每人每天只能种3次"));
//                            } else if (entity.code.equals("400_3")) {
//                                Observable.error(new ApiException(entity.code, "账号错误"));
//                            } else if (entity.code.equals("400_4")) {
//                                Observable.error(new ApiException(entity.code, "账号或密码错误"));
//                            } else if (entity.code.equals("400_6")) {
//                                Observable.error(new ApiException(entity.code, "用户未注册"));
//                            } else if (TextUtils.equals("343", entity.code)) {
//                                Observable.error(new ApiException(entity.code, "不能转发自己的视频"));
//                            } else if (TextUtils.equals("344", entity.code)) {
//                                Observable.error(new ApiException(entity.code, "已经转发过该视频"));
//                            } else if (TextUtils.equals("345", entity.code)) {
//                                Observable.error(new ApiException(entity.code, "该视频不存在"));
//                            } else if (TextUtils.equals("-1", entity.code)) {
//                                Observable.error(new ApiException(entity.code, entity.code));
//                            } else {
//                                if (TextUtils.equals(entity.message, "value == null")) {
//                                    Observable.error(new ApiException(entity.code, "未知错误 007"));
//                                } else if (entity.message.contains("failed to connect to") || entity.message.contains("timeout") || entity.message.contains("008")) {
//                                    Observable.error(new ApiException(entity.code, "网络不给力，请重新尝试"));
//                                } else
//                                    Observable.error(new ApiException(entity.code, entity.message));
//                            }
//                        }
//                        return Observable.empty();
//                        return createData(entity.result);
                    }
                }

        );
    }

    private static <T> Observable<T> createData(T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}