package voyage.task.zerodois.app;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NetworkService extends Service {
    private final long interval = 1000 * 3600 * 24; // 24 hours in mili
    private Handler task;
    private NotificationManager notification;
    private int notificationID = 30303;

    private Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                repeat();
            } finally {
                task.postDelayed(run, interval);
            }
        }
    };

    public NetworkService () {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        task = new Handler();
        start();
        return START_STICKY;
    }

    private void repeat() {
        startService(new Intent(this, NetworkLearn.class));
        // Toast.makeText(this, "SERVICO STARTADO", Toast.LENGTH_SHORT).show();
        pushNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void start () {
        run.run();
    }
    void stop () {
        task.removeCallbacks(run);
    }

    private void pushNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_logo_black);
        builder.setContentTitle("Pensando sobre a vida!");
        builder.setContentText("Ajustando e aprendendo com sua rotina");
        notification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.notify(notificationID, builder.build());
    }
}
