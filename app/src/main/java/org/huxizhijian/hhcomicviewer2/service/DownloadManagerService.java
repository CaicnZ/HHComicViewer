package org.huxizhijian.hhcomicviewer2.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.huxizhijian.hhcomicviewer2.db.ComicCaptureDBHelper;
import org.huxizhijian.hhcomicviewer2.enities.ComicCapture;
import org.huxizhijian.hhcomicviewer2.utils.BaseUtils;
import org.huxizhijian.hhcomicviewer2.utils.Constants;
import org.huxizhijian.hhcomicviewer2.utils.NotificationUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadManagerService extends Service implements DownloadManager.OnMissionFinishedListener {

    private ComicCaptureDBHelper mComicCaptureDBHelper;
    private DownloadManager mDownloadManager;

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_START_RANGE = "ACTION_START_RANGE";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_ALL_START = "ACTION_ALL_START";
    public static final String ACTION_ALL_STOP = "ACTION_ALL_STOP";
    public static final String ACTION_DELETE = "ACTION_DELETE";
    public static final String ACTION_RECEIVER = "ACTION_RECEIVER";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //获取每个Capture的picList
            if (msg.what == 0) {
                InitComicCapture init = new InitComicCapture((ComicCapture) msg.obj, new CallBack() {
                    @Override
                    public void onFinished(ComicCapture comicCapture) {
                        mDownloadManager.startDownload(comicCapture);
                    }
                });
                DownloadManager.sExecutorService.execute(init);
            }
        }
    };

    //广播接收器
    private NotificationChangeReceiver mReceiver;
    private NotificationUtil mNotificationUtil;

    public DownloadManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComicCaptureDBHelper = ComicCaptureDBHelper.getInstance(this);
        mDownloadManager = DownloadManager.getInstance(this);
        mDownloadManager.setOnMissionFinishedListener(this);
        //初始化
        mReceiver = new NotificationChangeReceiver();
        mNotificationUtil = NotificationUtil.getInstance(this);
        //注册广播接收器
        IntentFilter filter = new IntentFilter(ACTION_RECEIVER);
        registerReceiver(mReceiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_START)) {
            //开始下载
            ComicCapture comicCapture = (ComicCapture) intent.getSerializableExtra("comicCapture");

            //查看是否存在数据库中(是否未下载)
            if (mComicCaptureDBHelper.findByCaptureUrl(comicCapture.getCaptureUrl()) == null) {
                comicCapture.setDownloadStatus(Constants.DOWNLOAD_INIT);
                //设置下载目录
                comicCapture.setSavePath(BaseUtils.getDownloadPath(this, comicCapture));
                mComicCaptureDBHelper.add(comicCapture);
                //获取自动分配的ID
                comicCapture = mComicCaptureDBHelper.findByCaptureUrl(comicCapture.getCaptureUrl());
                InitComicCapture init = new InitComicCapture(comicCapture, new CallBack() {
                    @Override
                    public void onFinished(ComicCapture comicCapture) {
                        mComicCaptureDBHelper.update(comicCapture);
                        mDownloadManager.startDownload(comicCapture);
                        //发送广播
                        Intent intentBroadcast = new Intent();
                        intentBroadcast.setAction(DownloadManagerService.ACTION_RECEIVER);
                        intentBroadcast.putExtra("comicCapture", comicCapture);
                        sendBroadcast(intentBroadcast);
                    }
                });
                DownloadManager.sExecutorService.execute(init);
            } else {
                //继续下载
                comicCapture = mComicCaptureDBHelper.findByCaptureUrl(comicCapture.getCaptureUrl());
                if (comicCapture.getDownloadStatus() != Constants.DOWNLOAD_FINISHED) {
                    InitComicCapture init = new InitComicCapture(comicCapture, new CallBack() {
                        @Override
                        public void onFinished(ComicCapture comicCapture) {
                            mDownloadManager.startDownload(comicCapture);
                        }
                    });
                    DownloadManager.sExecutorService.execute(init);
                }
            }
        } else if (intent.getAction().equals(ACTION_START_RANGE)) {
            //继续下载
            ComicCapture comicCapture = (ComicCapture) intent.getSerializableExtra("comicCapture");
            comicCapture = mComicCaptureDBHelper.findByCaptureUrl(comicCapture.getCaptureUrl());
            if (comicCapture.getDownloadStatus() != Constants.DOWNLOAD_FINISHED) {
                InitComicCapture init = new InitComicCapture(comicCapture, new CallBack() {
                    @Override
                    public void onFinished(ComicCapture comicCapture) {
                        mDownloadManager.startDownload(comicCapture);
                    }
                });
                DownloadManager.sExecutorService.execute(init);
            }
        } else if (intent.getAction().equals(ACTION_STOP)) {
            //暂停下载
            ComicCapture comicCapture = (ComicCapture) intent.getSerializableExtra("comicCapture");
            Log.i("DownloadManagerService", "onStartCommand: stop");
            mDownloadManager.setDownloadPause(comicCapture);
        } else if (intent.getAction().equals(ACTION_ALL_START)) {
            //全部开始下载
            List<ComicCapture> unFinishedCaptures = mComicCaptureDBHelper.findUnFinishedCaptures();
            if (unFinishedCaptures != null) {
                Message message = null;
                for (int i = 0; i < unFinishedCaptures.size(); i++) {
                    message = new Message();
                    message.what = 0;
                    message.obj = unFinishedCaptures.get(i);
                    mHandler.sendMessageDelayed(message, 1000 * i);
                }
            }
        } else if (intent.getAction().equals(ACTION_ALL_STOP)) {
            //全部停止下载
            mDownloadManager.stopAllDownload();
        } else if (intent.getAction().equals(ACTION_DELETE)) {
            //删除一个下载任务
            ComicCapture comicCapture = (ComicCapture) intent.getSerializableExtra("comicCapture");
            Log.i("DownloadManagerService", "onStartCommand: delete");
            mDownloadManager.deleteCapture(comicCapture);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadManager.setOnMissionFinishedListener(null);
        mDownloadManager = null;

        //注销广播接收器
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onFinished() {
        //任务全部完成，service停止
        stopSelf();
    }

    public interface CallBack {
        void onFinished(ComicCapture comicCapture);
    }

    class NotificationChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(ACTION_RECEIVER)) return;
            String action = intent.getStringExtra("notification");
            if (action == null) return;
            if (mNotificationUtil == null) return;
            ComicCapture capture = (ComicCapture) intent.getSerializableExtra("comicCapture");
            if (capture == null) return;

            switch (action) {
                case "show":
                    mNotificationUtil.showNotification(DownloadManagerService.this, capture);
                    break;
                case "cancel":
                    mNotificationUtil.cancelNotification(DownloadManagerService.this, capture.getId());
                    break;
                default:
                    break;
            }
        }
    }

    class InitComicCapture extends Thread {
        private ComicCapture mComicCapture;
        private CallBack mCallBack;

        public InitComicCapture(ComicCapture comicCapture, @Nullable CallBack callBack) {
            this.mComicCapture = comicCapture;
            if (callBack != null) {
                this.mCallBack = callBack;
            }
        }

        @Override
        public void run() {
            URL url = null;
            HttpURLConnection conn = null;
            StringBuffer content = null;
            String line = null;
            try {
                url = new URL(mComicCapture.getCaptureUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GB2312"));
                content = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                conn.disconnect();
                mComicCapture.updatePicList(mComicCapture.getCaptureUrl(), content.toString());
                if (mCallBack != null) {
                    mCallBack.onFinished(mComicCapture);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mComicCapture.setDownloadStatus(Constants.DOWNLOAD_ERROR);
                mComicCaptureDBHelper.update(mComicCapture);
            }
        }
    }
}
