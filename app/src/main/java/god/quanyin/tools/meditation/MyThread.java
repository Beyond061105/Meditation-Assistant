package god.quanyin.tools.meditation;

import androidx.appcompat.app.AppCompatActivity;

class MyThread extends Thread {
    private boolean running=false;
    MyCallback cb;
    AppCompatActivity context;
    MyThread(AppCompatActivity context, MyCallback cb) {
        this.context = context;
        this.cb = cb;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while(running) {
            try {
                Thread.sleep(1000);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cb.callBack();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}