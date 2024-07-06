package god.quanyin.tools.meditation;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import god.quanyin.tools.meditation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity/* implements MyCallback*/{
    private final String TAG = "TAG_" + getClass().getSimpleName();

    private AppBarConfiguration appBarConfiguration;
private ActivityMainBinding binding;

    ImageButton btn_light;
    ImageButton btn_sound;
    Button btn_reset;

    TextView textLightTogo;
    TextView textSoundTogo;
    TextView textLightRatioTogo, textTotalLight;
    TextView textSoundRatioTogo, textTotalSound;
    TextView textDetail;
    TextView textLabelTotal, textLabelRatioTogo, textLabelQuotaTogo;
    long totalTime = 150L;

    boolean flag_ffAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate()");

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot()); //setContentView(R.layout.activity_main);
//        binding.toolbar.setBackgroundColor((Color.parseColor("#80000000")));
//        setSupportActionBar(binding.toolbar);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.YELLOW);
        setSupportActionBar(toolbar);
binding = ActivityMainBinding.inflate(getLayoutInflater());
setContentView(binding.getRoot()); //setContentView(R.layout.activity_main);
binding.toolbar.setBackgroundColor((Color.parseColor("#80000000")));
setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //String datetime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                        //applytime.setText(datetime);  //取得選定的時間指定給時間編輯框
                    }
                }, hourOfDay, minute,true).show();
            }
        });
        btn_reset = findViewById(R.id.btn_reset);
        btn_light = findViewById(R.id.btn_light);
        btn_sound = findViewById(R.id.btn_sound);

        //觀光、觀音時間顯示
        textTotalLight = findViewById(R.id.textTotalLight);
        textTotalSound = findViewById(R.id.textTotalSound);
        textLightRatioTogo = findViewById(R.id.textLightRatioTogo);
        textSoundRatioTogo = findViewById(R.id.textSoundRatioTogo);
        textLightTogo = findViewById(R.id.textLightTogo);
        textSoundTogo = findViewById(R.id.textSoundTogo);
        //打坐明細
        textDetail = findViewById(R.id.textDetail);
        textDetail.setMovementMethod(new ScrollingMovementMethod());

        textLabelTotal = findViewById(R.id.textLabelTotal);
        textLabelRatioTogo = findViewById(R.id.textLabelRatioTogo);
        textLabelQuotaTogo = findViewById(R.id.textLabelQuotaTogo);

        btn_light.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"hit LIGHT");
                if( btn_sound.isEnabled() ) {
                    btn_sound.setEnabled(false);btn_sound.setImageAlpha(0x3F);
                    btn_reset.setEnabled(false);
                    Log.d(TAG,"Before LIGHT setStart(): actionName="+actionName+", currentActionType="+currentActionType+", param:"+ACTION_TYPE_LIGHT);
                    setStart(ACTION_TYPE_LIGHT);
                    Log.d(TAG,"After LIGHT setStart(): actionName="+actionName+", currentActionType="+currentActionType);
                }else{
                    btn_sound.setEnabled(true);btn_sound.setImageAlpha(0xFF);
                    btn_reset.setEnabled(true);
                    Log.d(TAG,"Before LIGHT setEnd(): actionName="+actionName+", currentActionType="+currentActionType);
                    setEnd();
                    Log.d(TAG,"After LIGHT setEnd(): actionName="+actionName+", currentActionType="+currentActionType);
                }
            }
        });

        btn_sound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"hit SOUND");
                if( btn_light.isEnabled() ) {
                    btn_light.setEnabled(false);btn_light.setImageAlpha(0x3F);
                    btn_reset.setEnabled(false);
                    Log.d(TAG,"Before SOUND setStart(): actionName="+actionName+", currentActionType="+currentActionType+", param:"+ACTION_TYPE_SOUND);
                    setStart(ACTION_TYPE_SOUND);
                    Log.d(TAG,"After SOUND setStart(): actionName="+actionName+", currentActionType="+currentActionType);
                }else{
                    btn_light.setEnabled(true);btn_light.setImageAlpha(0xFF);
                    btn_reset.setEnabled(true);
                    Log.d(TAG,"Before SOUND setEnd(): actionName="+actionName+", currentActionType="+currentActionType);
                    setEnd();
                    Log.d(TAG,"After SOUND setEnd(): actionName="+actionName+", currentActionType="+currentActionType);
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG,"hit RESET");
                btn_light.setEnabled(true);btn_light.setImageAlpha(0xFF);
                btn_sound.setEnabled(true);btn_sound.setImageAlpha(0xFF);
                totalTimeLight = totalTimeSound = 0L;
                doViewsInit();

                textDetail.setText("");
                actionName = "";
                startTime = 0l;
                currentActionType = ACTION_TYPE_NONE;
                flag_ffAlert = false;
                hadAlertLight = hadAlertSound = false;
                saveButton(BTN_NONE);
                saveSetting();
            }
        });

        textDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "onClick textDetail");
                if (textDetail.getAlpha() == 1.0f) {
                    textDetail.setAlpha(0.0f);
                } else {
                    textDetail.setAlpha(1.0f);
                }
            }
        });

        doViewsInit();

        setLongClickToChangeBGColor(textLightTogo);
        setLongClickToChangeBGColor(textSoundTogo);
        setLongClickToChangeBGColor(textLightRatioTogo);
        setLongClickToChangeBGColor(textTotalLight);
        setLongClickToChangeBGColor(textSoundRatioTogo);
        setLongClickToChangeBGColor(textTotalSound);
        setLongClickToChangeBGColor(textDetail);

        setLongClickToChangeBGColor(textLabelTotal);
        setLongClickToChangeBGColor(textLabelRatioTogo);
        setLongClickToChangeBGColor(textLabelQuotaTogo);

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        if( sharedPreferences.contains("isCustomized") == false ) {
            saveSetting();
        }else{
            restoreSetting();
        }

        if (Build.VERSION.SDK_INT>=31) {
            if( vibratorManager == null ) {
                vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager.getDefaultVibrator();
            }
        }
        else {
            if( vibrator == null ) {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }
        }
    }

    private boolean isRunning; //flag for time kicks
    private Thread timerThread;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                // Update TextViews
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkComplete();
                    }
                });

                // Sleep for 1 second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "Time kick stopped.");
        }
    };

    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed()");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("確定要離開?")
                .setCancelable(false)
                .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause()");

        saveSetting();
    }

    private void restoreSetting(){
        Log.d(TAG,"restoreSetting()");
        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        textLightTogo.setText(sharedPreferences.getString("textLightTogo",""));
        textSoundTogo.setText(sharedPreferences.getString("textSoundTogo",""));
        textLightRatioTogo.setText(sharedPreferences.getString("textLightRatioTogo",""));
        textTotalLight.setText(sharedPreferences.getString("textTotalLight",""));
        textSoundRatioTogo.setText(sharedPreferences.getString("textSoundRatioTogo",""));
        textTotalSound.setText(sharedPreferences.getString("textTotalSound",""));
        textDetail.setText(sharedPreferences.getString("textDetail",""));
        textDetail.setAlpha(sharedPreferences.getFloat("textDetail_Alpha",1.0f));
        textLightTogo.setBackgroundColor(sharedPreferences.getInt("textLightTogo_Color",0));
        textSoundTogo.setBackgroundColor(sharedPreferences.getInt("textSoundTogo_Color",0));
        textLightRatioTogo.setBackgroundColor(sharedPreferences.getInt("textLightRatioTogo_Color",0));
        textTotalLight.setBackgroundColor(sharedPreferences.getInt("textTotalLight_Color",0));
        textSoundRatioTogo.setBackgroundColor(sharedPreferences.getInt("textSoundRatioTogo_Color",0));
        textTotalSound.setBackgroundColor(sharedPreferences.getInt("textTotalSound_Color",0));
        textDetail.setBackgroundColor(sharedPreferences.getInt("textDetail_Color",0));
        textLabelTotal.setBackgroundColor(sharedPreferences.getInt("textLabelTotal_Color",0));
        textLabelRatioTogo.setBackgroundColor(sharedPreferences.getInt("textLabelRatioTogo_Color",0));
        textLabelQuotaTogo.setBackgroundColor(sharedPreferences.getInt("textLabelQuotaTogo_Color",0));
        totalTime = sharedPreferences.getLong("totalTime",150*60*1000L);
        allocateTime(totalTime);
        totalTimeLight = sharedPreferences.getLong("totalTimeLight",0L);
        totalTimeSound = sharedPreferences.getLong("totalTimeSound",0L);

        Log.d(TAG, "@restoreSetting --> sharedPreferences.getInt(\"btn_hit\",BTN_NONE):"+sharedPreferences.getInt("btn_hit",BTN_NONE));
        switch( sharedPreferences.getInt("btn_hit",BTN_NONE) ){
            case BTN_NONE:
                //do nothing
                startTime = 0l;
                break;
            case BTN_LIGHT:
                btn_sound.setEnabled(false);btn_sound.setImageAlpha(0x3F);
                btn_reset.setEnabled(false);
                startTime = sharedPreferences.getLong("startTime",0L);
                setStart(BTN_LIGHT, startTime);
                break;
            case BTN_SOUND:
                btn_light.setEnabled(false);btn_light.setImageAlpha(0x3F);
                btn_reset.setEnabled(false);
                startTime = sharedPreferences.getLong("startTime",0L);
                setStart(BTN_SOUND, startTime);
                break;
        }
        hadAlertLight = sharedPreferences.getBoolean("hadAlertLight",false);
        hadAlertSound = sharedPreferences.getBoolean("hadAlertSound",false);

        actionName = sharedPreferences.getString("actionName","");
        Log.d(TAG, "@restoreSetting after --> btn_light.isEnabled():"+btn_light.isEnabled() + ",btn_sound.isEnabled():"+btn_sound.isEnabled());

    }

    private void saveButton(int theButton){
        Log.d(TAG,"saveButton(), theButton="+theButton);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("btn_hit",theButton);
        editor.commit();
    }

    private void saveSetting(){
        Log.d(TAG,"saveSetting()");
        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isCustomized",true); //app已啟用標記
        //標籤顏色
        editor.putInt("textLabelTotal_Color",getBackgroundColor(textLabelTotal));
        editor.putInt("textLabelRatioTogo_Color",getBackgroundColor(textLabelRatioTogo));
        editor.putInt("textLabelQuotaTogo_Color",getBackgroundColor(textLabelQuotaTogo));
        //累計時間Total
        editor.putInt("textTotalLight_Color",getBackgroundColor(textTotalLight));
        editor.putInt("textTotalSound_Color",getBackgroundColor(textTotalSound));
        //按比例尚有多少時間才能完成
        editor.putInt("textLightRatioTogo_Color",getBackgroundColor(textLightRatioTogo));
        editor.putInt("textSoundRatioTogo_Color",getBackgroundColor(textSoundRatioTogo));
        //尚有多少時間才能完成
        editor.putInt("textLightTogo_Color",getBackgroundColor(textLightTogo));
        editor.putInt("textSoundTogo_Color",getBackgroundColor(textSoundTogo));
        //明細
        editor.putInt("textDetail_Color",getBackgroundColor(textDetail));

        //數字紀錄
        editor.putString("textTotalLight",""+textTotalLight.getText());
        editor.putString("textTotalSound",""+textTotalSound.getText());
        editor.putString("textLightRatioTogo",""+textLightRatioTogo.getText());
        editor.putString("textSoundRatioTogo",""+textSoundRatioTogo.getText());
        editor.putString("textLightTogo",""+textLightTogo.getText());
        editor.putString("textSoundTogo",""+textSoundTogo.getText());
        editor.putString("textDetail",""+textDetail.getText());
        editor.putFloat("textDetail_Alpha", textDetail.getAlpha());
        //累計
        editor.putLong("totalTimeLight", totalTimeLight);
        editor.putLong("totalTimeSound", totalTimeSound);
        //預設或自設總觀音觀光時數
        editor.putLong("totalTime", totalTime);

        //狀態
        Log.d(TAG, "@saveSetting --> btn_light.isEnabled():"+btn_light.isEnabled() + ",btn_sound.isEnabled():"+btn_sound.isEnabled());
        if( btn_sound.isEnabled() && !btn_light.isEnabled() ) {
            //editor.putInt("btn_hit",BTN_SOUND);
            saveButton(BTN_SOUND);
            editor.putLong("startTime", startTime);
        }else{
            if( btn_light.isEnabled() && !btn_sound.isEnabled() ) {
                //editor.putInt("btn_hit",BTN_LIGHT);
                saveButton(BTN_LIGHT);
                editor.putLong("startTime", startTime);
            }else{
                saveButton(BTN_NONE);
            }
        }
        editor.putBoolean("hadAlertLight", hadAlertLight);
        editor.putBoolean("hadAlertSound", hadAlertSound);

        editor.putString("actionName",actionName);

        editor.commit();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG,"onPostResume()");

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG,"onResume()");

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        if( sharedPreferences.contains("textLightTogo") == false ) {
            return;
        }
        restoreSetting();
    }

    private boolean counterIsRunning;

    private static Vibrator vibrator;
    private static VibratorManager vibratorManager;
    private static final long[] VIBRATE_PATTERN_0 = {500, 500};
    private static final long[] VIBRATE_PATTERN_1 = {100, 100};

    private void doCompleteAlert(int patternNo, int repeat){
        Log.d(TAG, "doCompleteAlert() flag_ffAlert="+flag_ffAlert);


        if (Build.VERSION.SDK_INT >= 26) {
            Log.d(TAG, "Build.VERSION.SDK_INT >= 26, which is "+Build.VERSION.SDK_INT);
            vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN_0,repeat));
        }
        else {
            Log.d(TAG, "Build.VERSION.SDK_INT OLD.");
            vibrator.vibrate(VIBRATE_PATTERN_0,repeat);
        }

        Log.d(TAG, "doVibrate() --> show alert.");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提醒服務");
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setMessage("停止震動：")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vibrator.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shortVibrate(){
        vibrator.vibrate(VIBRATE_PATTERN_1,-1);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN_1,-1));
        }
        else {
            Log.d(TAG, "Build.VERSION.SDK_INT OLD.");
            vibrator.vibrate(VIBRATE_PATTERN_1,-1);
        }
    }

    private int getBackgroundColor(View view) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            if (Build.VERSION.SDK_INT >= 11) {
                return colorDrawable.getColor();
            }
            try {
                Field field = colorDrawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(colorDrawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void setLongClickToChangeBGColor(View view){
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentColor = getBackgroundColor(v);
                Log.d(TAG, "currentColor="+currentColor);
                ColorPickerDialogBuilder
                        .with(MainActivity.this)
                        .setTitle("挑顏色")
                        .initialColor(currentColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                Log.d(TAG, "Color pick is "+Integer.toHexString(selectedColor));
                                v.setBackgroundColor(selectedColor);
                            }
                        })
                        .setPositiveButton("確定", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                //changeBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                v.setBackgroundColor(currentColor);
                            }
                        })
                        .build()
                        .show();
                return  false;
            }
        });
    }

    private void doViewsInit(){
        btn_light.setEnabled(true);
        btn_sound.setEnabled(true);
        btn_reset.setEnabled(true);
        //textDetail.setVisibility(View.VISIBLE);
        //textDetail.setAlpha(1.0f);

        textLightTogo.setText(getMM(TOTAL_LIGHT));
        textSoundTogo.setText(getMM(TOTAL_SOUND));
        textLightRatioTogo.setText("0");
        textSoundRatioTogo.setText("0");
        textTotalLight.setText("0");
        textTotalSound.setText("0");

    }

    private static int currentActionType = 1;
    private String actionName = "";
    private static long startTime, endTime;
    private int getAction(){ return currentActionType; }

    private void setStart(int action, long _startTime) {
        currentActionType = action;
        if( ACTION_TYPE_LIGHT == currentActionType ){
            this.actionName = "觀光";
        }else{
            if( ACTION_TYPE_SOUND == currentActionType ) {
                this.actionName = "觀音";
            }else{
                currentActionType = ACTION_TYPE_NONE;
                return;
            }
        }
        startTime = _startTime;

        isRunning = true;
        if( timerThread == null ) {
            timerThread = new Thread(timerRunnable);
            timerThread.start();
        }
    }

    private void setStart(int action){
        shortVibrate();
        setStart(action, System.currentTimeMillis());
    }

    //觀光完成後的震動提示只執行一次的標記。
    private static boolean hadAlertLight;

    //觀音完成後的震動提示只執行一次的標記。
    private static boolean hadAlertSound;

    private void checkComplete(){
        Log.d(TAG,"checkComplete(), currentActionType="+getAction()+", flag_ffAlert="+flag_ffAlert+" , hadAlertLight="+hadAlertLight+" , hadAlertSound="+hadAlertSound);
        Log.d(TAG, "startTime="+startTime);

        if( startTime==0l ){
            Log.d(TAG,"Not Start yet!");
            return;
        }

        endTime = System.currentTimeMillis();
        long timeDiff = (endTime - startTime);
        long calTotal;
        String remainMM;

        Log.d(TAG, "活動已進行="+getHHMM(timeDiff)+", TOTAL_LIGHT="+TOTAL_LIGHT+", TOTAL_SOUND="+TOTAL_SOUND);

        if( ACTION_TYPE_LIGHT == currentActionType ){
            calTotal = totalTimeLight + timeDiff;
            Log.d(TAG, "calTotal="+calTotal/1000+", totalTimeLight="+(totalTimeLight/1000)+", timeDiff="+(timeDiff/1000));
            Log.d(TAG, "Time remain:"+getHHMM(TOTAL_LIGHT - calTotal));
            { // redundant code , TODO in the future.
                //觀光時間超過規定就一律顯示'0'。
                remainMM = getHHMM(TOTAL_LIGHT - calTotal < 0L ? 0L : (TOTAL_LIGHT - calTotal));
                Log.d(TAG, "距離完成還有="+remainMM);
                textLightTogo.setText(remainMM);
                textTotalLight.setText(getHHMM(calTotal));
            }
            //觀光時間已達時數。
            if( !hadAlertLight && TOTAL_LIGHT - calTotal <= 0L ){
                //20230304
                Log.d(TAG,"fullfillAlert(), LIGHT flag_ffAlert="+flag_ffAlert);
                doCompleteAlert(0, 0);
                hadAlertLight = true;
            }
        }else{
            if( ACTION_TYPE_SOUND == currentActionType ) {
                calTotal = totalTimeSound + timeDiff;
                Log.d(TAG, "calTotal="+calTotal+", totalTimeSound="+(totalTimeSound/1000)+", timeDiff="+(timeDiff/1000));
                { // redundant code , TODO in the future.
                    //觀音時間超過規定就一律顯示'0'。
                    remainMM = getHHMM(TOTAL_SOUND - calTotal < 0L ? 0L : (TOTAL_SOUND - calTotal));
                    Log.d(TAG, "距離完成還有="+remainMM);
                    textSoundTogo.setText(remainMM);
                    textTotalSound.setText(getHHMM(calTotal));
                }

                //觀音時間已達時數。
                if( !hadAlertSound && TOTAL_SOUND - calTotal <= 0L ){
                    Log.d(TAG,"fullfillAlert(), SOUND flag_ffAlert="+flag_ffAlert);
                    doCompleteAlert(0, 0);
                    hadAlertSound = true;
                }
            }
        }
    }

    private static final int BTN_NONE = 1;
    private static final int BTN_LIGHT = 2;
    private static final int BTN_SOUND = 3;

    private final static int ACTION_TYPE_NONE  = 1;
    private final static int ACTION_TYPE_LIGHT = 2;
    private final static int ACTION_TYPE_SOUND = 3;

    private static long TOTAL_LIGHT = 5400000, TOTAL_SOUND = 3600000;
    private long totalTimeLight, totalTimeSound;

    private void setEnd(){
        Log.d(TAG,"setEnd()");

        isRunning = false; // stop time kicks.
        timerThread = null;

        vibrator.cancel();
        flag_ffAlert = false;

        endTime = System.currentTimeMillis();
        long timeDiff = (endTime - startTime);
        //long calTotal;
        String remainMM;

        if( ACTION_TYPE_LIGHT == currentActionType ){
            actionName = "觀光";
            totalTimeLight += timeDiff;
            //觀光時間超過規定就一律顯示'0'。
            remainMM = getHHMM(TOTAL_LIGHT - totalTimeLight < 0L ? 0L : (TOTAL_LIGHT - totalTimeLight));
            textLightTogo.setText(remainMM);
            textTotalLight.setText(getHHMM(totalTimeLight));
        }else{
            if( ACTION_TYPE_SOUND == currentActionType ) {
                actionName = "觀音";
                totalTimeSound += timeDiff;
                //觀音時間超過規定就一律顯示'0'。
                remainMM = getHHMM(TOTAL_SOUND - totalTimeSound < 0L ? 0L : (TOTAL_SOUND - totalTimeSound));
                textSoundTogo.setText(remainMM);
                textTotalSound.setText(getHHMM(totalTimeSound));
            }else{
                actionName = "";
                return;
            }
        }
        SimpleDateFormat df = new SimpleDateFormat(" MM-dd HH:mm:ss");
        String formattedDate = df.format(Calendar.getInstance().getTime());

        String diffHHMM  = getHHMM(timeDiff);
        String showRecord = "【"+formattedDate + "】 本次 " + actionName + " "+diffHHMM + "\n" + textDetail.getText();
                //+ "\n" +"累計 " + actionName + diffTotal + "\n\n" + textDetail.getText();
        textDetail.setText(showRecord);

        //Log.d(TAG, showRecord);
        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("btn_hit",0);
        currentActionType = ACTION_TYPE_NONE;
        startTime = 0l;
    }

    private String getHHMM(long timeDiff){
        int secound, minute, hour;
        secound = (int)timeDiff / 1000;
        minute = (int)secound / 60;
        hour = (int)minute / 60;
        long seconds = secound % 60;
        long minutes = minute % 60;
        long hours   = hour;
        return hours + "時 " + minutes + "分 " + seconds + "秒";
    }

    private String getMM(long timeDiff){
        int second, minute;
        second = (int)timeDiff / 1000;
        minute = (int)second / 60;
        return minute + "分鐘 ";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected()");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("請輸入總分鐘數：");
            builder.setIcon(R.mipmap.ic_launcher);

            final EditText editText = new EditText(MainActivity.this); //final一個editText
            builder.setView(editText);
            editText.setText(""+(TOTAL_LIGHT+TOTAL_SOUND)/1000/60);

            builder.setMessage("請輸入數字：")
                    .setCancelable(false)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int inputMinutes = 150;
                            try{
                                inputMinutes = Integer.parseInt(editText.getText().toString().trim());
                            }catch(Exception e){ e.printStackTrace(); }
                            Log.d(TAG,"inputMinutes = "+inputMinutes);

                            totalTime = inputMinutes;
                            TOTAL_LIGHT  = (long)(inputMinutes * 60 * 1000 * (3/5f));
                            TOTAL_SOUND  = inputMinutes * 60 * 1000 - TOTAL_LIGHT;

                            String remainMM;
                            //觀光時間超過規定就一律顯示'0'。
                            remainMM = getHHMM(TOTAL_LIGHT - totalTimeLight <= 0L ? 0L : (TOTAL_LIGHT - totalTimeLight));
                            textLightTogo.setText(remainMM);
                            //觀音時間超過規定就一律顯示'0'。
                            remainMM = getHHMM(TOTAL_SOUND - totalTimeSound <= 0L ? 0L : (TOTAL_SOUND - totalTimeSound));
                            textSoundTogo.setText(remainMM);
                            flag_ffAlert = hadAlertSound = hadAlertLight = false;
                            Log.d(TAG,"checkComplete(), currentActionType="+currentActionType+", flag_ffAlert="+flag_ffAlert+" , hadAlertLight="+hadAlertLight+" , hadAlertSound="+hadAlertSound);
                            saveSetting();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            editText.requestFocus();
            editText.selectAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void allocateTime(long totalTime){
        TOTAL_LIGHT  = (long)(totalTime * 60 * 1000 * (3/5f));
        TOTAL_SOUND  = totalTime * 60 * 1000 - TOTAL_LIGHT;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}