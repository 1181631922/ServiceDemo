package cn.edu.sjzc.fanyafeng.service;

import cn.edu.sjzc.fanyafeng.servicedemo.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
	// ����������־
	private static String TAG = "��������";
	// �������ֲ��ű���
	private MediaPlayer mPlayer;
	
	private MyBinder mBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onBind()ִ��");
//		mPlayer.start();
		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate()ִ��");//��һ��ִ��
		//R.raw.xxxx����raw���ܻ������������ƣ���ȻAndroidʶ�𲻳�������
		mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.qimiaozhongdejiyi);
		//��������ѭ������,�����ҿ�����һ��Handler���е�Looper
		mPlayer.setLooping(true);
		super.onCreate();
	}
	
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy()ִ��");//ֹͣ����ʱִ��
		mPlayer.stop();
		super.onDestroy();
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStartCommand()ִ��");//�ڶ���ִ��
		mPlayer.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUnbind()ִ��");
		return super.onUnbind(intent);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		//onStart�������ȸ�����˺�ܣ���������ʹ��
		Log.i(TAG, "onStart()ִ��");//������ִ��
		//start()��������onStartCommand
//		mPlayer.start();
		super.onStart(intent, startId);
	}
	
	public class MyBinder extends Binder{
		MusicService getService(){
			return MusicService.this;
		}
	}

}
