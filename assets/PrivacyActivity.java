package com.godot.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import com.godot.game.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PrivacyActivity extends Activity {

    private static final String PREFS_NAME = "PlayerPrefs";
    private static final String KEY_PRIVACY_ACCEPTED = "PrivacyAccepted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 非中文环境直接进入主页面
        if (!isSystemLanguageChinese(this)) {
            startMainActivity();
            return;
        }
        // 已同意隐私政策直接进入
        if (isPrivacyAccepted()) {
            startMainActivity();
            return;
        }
        // 显示隐私政策对话框
        showPrivacyDialog();
    }

    // 检测系统语言是否为中文
    public static boolean isSystemLanguageChinese(Context context) {
        Configuration config = context.getResources().getConfiguration();
        String languageCode = config.getLocales().get(0).getLanguage();
        return languageCode.startsWith("zh");
    }

    /**
     * 显示隐私政策对话框（从res/raw读取内容）
     */
    private void showPrivacyDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy, null);

        WebView webView = dialogView.findViewById(R.id.dialog_webview);
        String privacyHtml = readPrivacyHtmlFromRaw();  // 从res/raw读取
        webView.loadDataWithBaseURL(null, privacyHtml, "text/html", "UTF-8", null);

        Button negativeButton = dialogView.findViewById(R.id.dialog_negative_button);
        Button positiveButton = dialogView.findViewById(R.id.dialog_positive_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        // 取消按钮：退出应用
        negativeButton.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        // 同意按钮：保存状态并进入主页面
        positiveButton.setOnClickListener(v -> {
            dialog.dismiss();
            setPrivacyAccepted();
            startMainActivity();
        });

        dialog.show();
    }

    /**
     * 从res/raw目录读取privacy_content.html（方案2核心）
     */
    private String readPrivacyHtmlFromRaw() {
        StringBuilder content = new StringBuilder();
        try {
            // 读取res/raw/privacy_content.html（R.raw.privacy_content是自动生成的资源ID）
            InputStream is = getResources().openRawResource(R.raw.privacy_content);
            BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)  // 强制UTF-8编码
            );
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");  // 保留换行符
            }
            br.close();
            is.close();
            Log.d("PrivacyActivity", "隐私政策HTML读取成功，长度：" + content.length());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PrivacyActivity", "隐私政策HTML读取失败：" + e.getMessage());
            return "隐私政策加载失败，请重试（错误：" + e.getMessage() + "）";
        }
        return content.toString();
    }

    /**
     * 启动主活动
     */
    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClassName(this, "com.godot.game.GodotApp");
        startActivity(intent);
        finish();
    }

    /**
     * 保存隐私政策同意状态
     */
    private void setPrivacyAccepted() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(KEY_PRIVACY_ACCEPTED, true);
        editor.apply();
    }

    /**
     * 检查是否已同意隐私政策
     */
    private boolean isPrivacyAccepted() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_PRIVACY_ACCEPTED, false);
    }
}