package com.example.woorimanager_payment;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.woorimanager_payment.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String PACKAGE_NAME = MainActivity.class.getPackage().getName();
    private ActivityMainBinding binding;
    private WebView webView;
    private ProgressBar progressBar;
    private long pressedTime = 0;
    private long compareMessageId = 0, firstMessageId = 0;
    private ServerManager serverManager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverManager = new ServerManager();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TedPermission.create().setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                sendRegisterDataToServer();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                sendRegisterDataToServer();
                String[] permissionsList = deniedPermissions.toArray(new String[0]);
                ActivityCompat.requestPermissions(MainActivity.this, permissionsList, 1);
            }
        })      .setPermissions(POST_NOTIFICATIONS, READ_PHONE_STATE, READ_SMS, RECEIVE_SMS)
                .setRationaleTitle("알림").setRationaleMessage("알림 및 SMS 수신을 원하시면 권한을 허용해주세요.")
                .setDeniedTitle("알림").setDeniedMessage("일부 권한이 거부되어 있습니다.\n[설정] > [권한]에서 권한을 허용해주세요.")
                .check();

        progressBar = binding.progressBar;
        webView = binding.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptService(), "android");

        String messageUrl = getIntent().getStringExtra("url");
        if (messageUrl != null) webView.loadUrl(messageUrl);
        else webView.loadUrl(getString(R.string.defalut_url));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.showContextMenu();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
    } // onCreate()

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > pressedTime + 2000) {
            pressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "'뒤로가기' 버튼을 한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
        } else finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        String messageUrl = intent.getStringExtra("url");
        if (messageUrl != null) {
            setContentView(binding.getRoot());
            webView.loadUrl(messageUrl);
        }
    }

    public void sendRegisterDataToServer() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            try {
                serverManager.registerDataToServer(task.getResult(), getPhoneNumber());
            } catch (IllegalArgumentException e) {
                Log.e("Exception", e.getMessage());
            }
        });
    }

    public String getPhoneNumber() {
        if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) == PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, READ_SMS) == PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String phoneNumber = tm.getLine1Number().replace("+", "");
            return !phoneNumber.startsWith("82") && phoneNumber.startsWith("0") ? "82" + phoneNumber.substring(1) : phoneNumber;
        }
        return null;
    }

    public void smsUpdateCheck() {
        if (getPhoneNumber() == null) {
            Toast.makeText(this, "전화번호 정보가 없습니다.\n전화 및 SMS 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean checkFirstMessage = true;
        StringBuilder messageBody = new StringBuilder();
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, "date DESC limit 0, 30");

        while (cursor.moveToNext()) {
            long messageId = cursor.getLong(0); // "_id"
            String address = cursor.getString(2); // "address"
            String body = cursor.getString(5); // "body"
            messageBody.append(String.format("%s@@%s", address, body)).append("||");

            if (checkFirstMessage) {
                firstMessageId = messageId;
                checkFirstMessage = false;
            }
        } cursor.close();

        if (compareMessageId != firstMessageId) {
            compareMessageId = firstMessageId;
            try {
                serverManager.messageToServer(messageBody.toString(), getPhoneNumber());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        }
    }

    // region Inner Class :: JavaScriptInterface
    public class JavaScriptService {
        @JavascriptInterface
        public void smsUpdate() {
            smsUpdateCheck();
        }
    }
    // endregion
}