package cn.edu.sjzc.fanyafeng.activity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.sjzc.fanyafeng.servicedemo.R;
import cn.edu.sjzc.fanyafeng.thread.FileDownloadThread;

public class LoginSuccessActivity extends BaseActivity implements
		OnClickListener {
	private Button download, download_two;
	private ProgressBar progressBar, notificationProgress;
	private NotificationManager manager;
	private TextView notificationTitle, notificationPercent, progresstext;
	private int progress, msize;
	private PendingIntent contentIntent;
	// NotificationManager�Ĺ�����ʾ
	/**
	 * NotificationManager��ͼ�꣬һ�㲻Ҫ�ò�ɫ���Ҳ�������ϵͳĬ�ϵ�
	 * ���о���ϵͳ����ͼƬ��int���͵ģ�����Listview��ʾ����ͼƬʱʱ�ǵö���int���� �������������ͼƬҪ����Ϊstring����
	 */
	private int icon_download = android.R.drawable.stat_sys_download;
	private String tickerText = "��ʼ����XXXXXXX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginsuccess);
		initView();
	}

	private void initView() {
		this.download = (Button) LoginSuccessActivity.this
				.findViewById(R.id.download);
		this.download.setOnClickListener(this);

		this.progresstext = (TextView) LoginSuccessActivity.this
				.findViewById(R.id.progresstext);
		this.progressBar = (ProgressBar) LoginSuccessActivity.this
				.findViewById(R.id.progressBar);
		this.notificationTitle = (TextView) LoginSuccessActivity.this
				.findViewById(R.id.notificationTitle);
		this.notificationPercent = (TextView) LoginSuccessActivity.this
				.findViewById(R.id.notificationPercent);
		this.notificationProgress = (ProgressBar) LoginSuccessActivity.this
				.findViewById(R.id.notificationProgress);

		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Notification��Intent���������ת���Activity
		Intent it_notification = new Intent(this, this.getClass());
		it_notification.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		contentIntent = PendingIntent.getActivity(this, 0, it_notification, 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.download:
			doDownload();
			break;

		default:
			break;
		}

	}

	/**
	 * ʹ��Handler����UI������Ϣ
	 */
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// ����Notification
			Notification mNotification = new Notification(icon_download,
					tickerText, System.currentTimeMillis());
			// �趨Notification����ʱ�������������ö����϶�˵�������Զ���
			mNotification.defaults |= Notification.DEFAULT_SOUND;
			// �����Ƿ���
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
			// ָ��Flag,Notification.FLAG_AUTO_CANCEL��ָ������Notification������ȡ������
			// ����һ���Notification�������淶���󻮲���ʧ����QQ
			// mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
			// ֪ͨ���󻮺���ʧ
			mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			// ����RemoteViews����Notification
			RemoteViews contentView = new RemoteViews(getPackageName(),
					R.layout.notification_view_download);
			contentView.setTextViewText(R.id.notificationTitle,
					"Download:����������...");

			progressBar.setProgress(msg.getData().getInt("size"));
			float temp = (float) progressBar.getProgress()
					/ (float) progressBar.getMax();
			int progress = (int) (temp * 100);
			if (progress == 100) {
				// Activity��ʾ
				Toast.makeText(LoginSuccessActivity.this, "������ɣ�",
						Toast.LENGTH_LONG).show();
				// Notification��ʾ
				mNotification.icon = R.drawable.down_success;
				contentView.setTextViewText(R.id.notificationTitle,
						"Download:������ɣ�");

			}
			// notification��ʾ
			contentView.setTextViewText(R.id.notificationPercent, progress
					+ "%");
			contentView.setProgressBar(R.id.notificationProgress, 100,
					progress, false);
			mNotification.contentView = contentView;
			mNotification.contentIntent = contentIntent;
			manager.notify(1, mNotification);
			// activity��ʾ
			progresstext.setText("���ؽ���:" + progress + " %");

		}
	};

	/**
	 * ����׼����������ȡSD��·���������߳�
	 */
	private void doDownload() {

		// ��ȡSD��·��
		String path = Environment.getExternalStorageDirectory()
				+ "/amosdownload/";
		File file = new File(path);
		// ���SD��Ŀ¼�����ڴ���
		if (!file.exists()) {
			file.mkdir();
		}
		// ����progressBar��ʼ��
		progressBar.setProgress(0);

		// ����������Ȱ�URL���ļ�����д������ʵ��Щ������ͨ��HttpHeader��ȡ��
		String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
		String fileName = "baidu_16785426.apk";
		int threadNum = 5;
		String filepath = path + fileName;

		downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
		task.start();
	}

	/**
	 * ���߳��ļ�����
	 * 
	 * 
	 * @2014-8-7
	 */
	class downloadTask extends Thread {
		private String downloadUrl;// �������ӵ�ַ
		private int threadNum;// �������߳���
		private String filePath;// �����ļ�·����ַ
		private int blockSize;// ÿһ���̵߳�������

		public downloadTask(String downloadUrl, int threadNum, String fileptah) {
			this.downloadUrl = downloadUrl;
			this.threadNum = threadNum;
			this.filePath = fileptah;
		}

		@Override
		public void run() {

			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);
				URLConnection conn = url.openConnection();
				// ��ȡ�����ļ��ܴ�С
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("��ȡ�ļ�ʧ��");
					return;
				}
				// ����ProgressBar���ĳ���Ϊ�ļ�Size
				progressBar.setMax(fileSize);

				// ����ÿ���߳����ص����ݳ���
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
						: fileSize / threadNum + 1;

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// �����̣߳��ֱ�����ÿ���߳���Ҫ���صĲ���
					threads[i] = new FileDownloadThread(url, file, blockSize,
							(i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// ��ǰ�����߳���������
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					// ֪ͨhandlerȥ������ͼ���
					Message msg = mHandler.obtainMessage();
					msg.getData().putInt("size", downloadedAllSize);
					mHandler.sendMessage(msg);
					// Log.d(TAG, "current downloadSize:" + downloadedAllSize);
					Thread.sleep(1000);// ��Ϣ1����ٶ�ȡ���ؽ���
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
