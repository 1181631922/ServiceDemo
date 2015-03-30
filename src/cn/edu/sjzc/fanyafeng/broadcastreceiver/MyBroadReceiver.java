package cn.edu.sjzc.fanyafeng.broadcastreceiver;

import cn.edu.sjzc.fanyafeng.activity.MainActivity;
import cn.edu.sjzc.fanyafeng.servicedemo.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyBroadReceiver extends BroadcastReceiver {

	private NotificationManager mNotificationManager;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.start")) {

			Log.i("���յ���ϢΪ��", "xuliang's qimiaozhongdejiyi");
			// ��������,ÿ�ε��յ��㲥�����½�һ��������������Ȼ��������յ�ֹͣ�Ĺ㲥ϵͳҲֹͣ����
			// �������֣�������ˣ�ÿ�ζ����½�һ�����������������ε���㲥����ʱ�ͻ�������һ�׸�
			// �ڷֲ�ͬ�������в���
			// ��ô�϶�������ͨ���㲥���ܿ�ʼ����ֹͣ����Service����ʵ��
			MediaPlayer.create(context, R.raw.qimiaozhongdejiyi).start();

		}
		if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.shut")) {
			Log.i("���յ���ϢΪ��", "shut down the music");
			// ֹͣ��������
			MediaPlayer.create(context, R.raw.qimiaozhongdejiyi).stop();
		}

		if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.broad.two")) {
			Log.i("���յ���ϢΪ��", "broad download one");
			// ����ͼƬ
			
		}
		if (intent.getAction().equals("cn.edu.sjzc.fanyafeng.shut.two")) {
			Log.i("���յ���ϢΪ��", "shut the music");
			// ֧�ֶϵ�����

		}
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.i("���յ���ϢΪ��", "ϵͳ�������");
			// ϵͳ�������,����3.0���µİ汾���Լ�⵽�˹㲥��3.1�Ժ��ղ����˹㲥������Ĵ�ҿ��Բ������
			/**
			 * ���������ϵ� Android 3.1��ʼ, ���ڰ�ȫ�ԵĿ���.
			 * �����ڰ�װ��,�û�û��ͨ���Լ��Ĳ�������������Ļ�,��ô��������ղ���android
			 * .intent.action.BOOT_COMPLETED���Intent; �û�ͨ���Լ��Ĳ���������һ�γ����,
			 * receiver��������, �Ӷ��յĵ�android.intent.action.BOOT_COMPLETED Intent.
			 */

			Toast.makeText(context, "ϵͳ�������", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public IBinder peekService(Context myContext, Intent service) {
		// TODO Auto-generated method stub
		//���˲����ϲ�֪������ģ�û��googleԴ��
		return super.peekService(myContext, service);
	}
	
	

}
