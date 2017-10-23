package com.boredream.weibo.tinker;

import android.os.Environment;
import android.util.Log;

import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.utils.CollectionUtils;
import com.boredream.weibo.BaseApplicationLike;
import com.boredream.weibo.entity.HotPatchInfo;
import com.boredream.weibo.net.WbHttpRequest;
import com.tencent.tinker.lib.tinker.TinkerApplicationHelper;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LoadTinkerManager {

    private static final String TAG = "LoadTinkerManager";
    private static final String PATCH_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";

    private static volatile LoadTinkerManager instance = null;

    public static LoadTinkerManager getInstance() {
        if (instance == null) {
            synchronized (LoadTinkerManager.class) {
                if (instance == null) {
                    instance = new LoadTinkerManager();
                }
            }
        }
        return instance;
    }

    private LoadTinkerManager() {
        // private
    }

    public void checkUpdatePatch() {
        final int versionCode = AppUtils.getAppVersionCode(BaseApplicationLike.instance.getApplication());

        Map<String, Object> params = new HashMap<>();
        params.put("where", "{\"apkVersionCode\":" + versionCode + "}");

        WbHttpRequest.getInstance().getApiService()
                .checkUpdateHotPatch(params)
                .subscribeOn(Schedulers.io())
                .compose(LcRxCompose.<HotPatchInfo>handleListResponse())
                .flatMap(new Function<ArrayList<HotPatchInfo>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(@NonNull ArrayList<HotPatchInfo> hotPatchInfos) throws Exception {
                        if (CollectionUtils.isEmpty(hotPatchInfos)) {
                            Log.i(TAG, "no patch list for apkVersion : " + versionCode);
                            return Observable.empty();
                        }

                        HashMap<String, String> configs = TinkerApplicationHelper.getPackageConfigs(BaseApplicationLike.instance);
                        // 本地已经打好的热更新版本号
                        int localPatchVersionCode = -1;
                        if(configs != null) {
                            String localPatchVersion = configs.get("patchVersion");
                            if(localPatchVersion != null) {
                                Log.i(TAG, "local patch version = " + localPatchVersion);
                                localPatchVersionCode = Integer.parseInt(localPatchVersion.replace(".", ""));
                            }
                        }

                        HotPatchInfo newestHotPatchInfo = null;
                        for (HotPatchInfo hotPatchInfo : hotPatchInfos) {
                            Integer version = Integer.parseInt(hotPatchInfo.getHotPatchVersion().replace(".", ""));
                            // 如果服务端的个补丁版本大过本地，则可以使用
                            if(version > localPatchVersionCode) {

                                // 找到最新版本号的补丁信息
                                if(newestHotPatchInfo == null || version > Integer.parseInt(
                                        newestHotPatchInfo.getHotPatchVersion().replace(".", ""))) {
                                    newestHotPatchInfo = hotPatchInfo;
                                }
                            }
                        }

                        if(newestHotPatchInfo == null) {
                            Log.i(TAG, "no newestHotPatchInfo for apkVersion : " + versionCode);
                            return Observable.empty();
                        }

                        Log.i(TAG, "get newest patch " + newestHotPatchInfo);
                        String hotPatchFileUrl = newestHotPatchInfo.getHotPatchFileUrl();
                        return WbHttpRequest.getInstance().getApiService().downloadFile(hotPatchFileUrl);
                    }
                })
               .subscribe(new SimpleDisObserver<ResponseBody>() {
                   @Override
                   public void onNext(@NonNull ResponseBody responseBody) {
                       try {
                           if(responseBody == null) return;

                           // 下载文件写入到本地
                           FileOutputStream fos = new FileOutputStream(
                                   new File(LoadTinkerManager.PATCH_FILE_PATH));
                           fos.write(responseBody.bytes());
                           fos.close();

                           Log.i(TAG, "download file success");

                           // 使用补丁
                           TinkerInstaller.onReceiveUpgradePatch(
                                   BaseApplicationLike.instance.getApplication(),
                                   LoadTinkerManager.PATCH_FILE_PATH);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {
                       Log.i(TAG, "onError: " + e);
                   }
               });
    }

}
