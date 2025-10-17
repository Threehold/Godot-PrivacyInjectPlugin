# PrivacyInjectPlugin for Godot
A Godot plugin that automatically injects Android privacy policy page files (layouts, Java, HTML) and modifies AndroidManifest, helping developers quickly integrate a mandatory privacy page for app store compliance.
一款godot的安卓导出插件。可以增加隐私页。需要自行调整privacy_content.html到自己需要的内容。或者修改java文件定向到真实地址。

## 功能说明 | Feature Overview
- **Automatic File Injection**: Auto-copies privacy page resources (layout `dialog_privacy.xml`, logic `PrivacyActivity.java`, content `privacy_content.html`, button styles `drawable/`) to Android build directory.
- **AndroidManifest Modification**: Injects privacy activity configuration and `tools:replace` attribute to resolve manifest merge conflicts.
- **One-Click Integration**: No manual Android Studio operation required; all changes are applied during Godot export.


## 安装步骤 | Installation Steps
1. Download the plugin from [GitHub Releases](https://github.com/Threehold/Godot-PrivacyInjectPlugin) or clone the repo.
2. Extract the `PrivacyInjectPlugin` folder to your Godot project's `addons/` directory.
3. Open Godot, go to **Project > Project Settings > Plugins**.
4. Find "PrivacyInjectPlugin" and toggle **Enable** to `On`.


## 关键配置 | Critical Configuration
After enabling the plugin, you **must** modify the Android export preset to avoid runtime conflicts.

### 中文界面操作 | Chinese UI Steps
1. 打开 **项目 > 导出**（Godot 顶部菜单栏）。
2. 在导出窗口中，选择你的 **Android 导出预设**（如“Android 调试版”或“Android 发布版”）。
3. 向下滚动找到 **“高级设置”** 部分（折叠时点击展开）。
4. 在“应用”或“启动器”分组下，找到选项 **“显示在APP库中”**。
5. 将其设置为 **“false”**（默认通常为“true”）。
6. 点击 **“保存”** 应用预设修改。

### English UI Steps
1. Open **Project > Export** (Godot top menu bar).
2. In the export window, select your **Android export preset** (e.g., "Android Debug" / "Android Release").
3. Scroll down to the **Advanced Settings** section (expand if collapsed).
4. Under the "Application" or "Launcher" group, find **"Show in app library"**.
5. Set it to **"False"** (default: "True").
6. Click **"Save"** to apply changes.

![Export Setting Screenshot](https://github.com/Threehold/Godot-PrivacyInjectPlugin/blob/main/images/export-setting.png)  
*Location of "Show in app library" in Godot's Android export preset*


## 为什么需要此配置 | Why This Setting Is Required
When "Show in app library" is `True`, Android treats the app as a **library app** (for code sharing), which conflicts with the plugin’s privacy page logic (the privacy activity needs to be the main launch entry). Setting it to `False` ensures the app is recognized as a standalone application, allowing the privacy page to load on startup.


## 使用说明 | Usage Guide
1. **Customize Privacy Content**: Edit `addons/PrivacyInjectPlugin/assets/privacy_content.html` to update your app’s privacy policy text, links to user agreements, or third-party SDK information.
2. **Export APK**: Go to **Project > Export > Select Android Preset > Click "Export Project"**.
3. **Test the Privacy Page**: Install the exported APK; the privacy page will pop up on first launch (only for Chinese system languages by default; English systems skip to the main app).


## 故障排除 | Troubleshooting
| 问题 | Solution |
|------|----------|
| Privacy page not showing | 1. Check if "Show in app library" is set to `False`; 2. Verify all files are injected (see `android/build/res/` and `android/build/src/`). |
| AndroidManifest merge error | Ensure the plugin’s `_inject_privacy_activity_to_all_manifests` function runs (check export logs for "Manifest injected successfully"). |
| Button styles not loading | Confirm `btn_gray_round.xml` and `btn_blue_round.xml` exist in `android/build/res/drawable/`. |


## 许可证 | License
This plugin is licensed under the **MIT License** - see the [LICENSE](https://github.com/Threehold/Godot-PrivacyInjectPlugin/blob/main/LICENSE) file for details.


## 贡献 | Contributions
Feel free to submit issues or pull requests to improve the plugin (e.g., support for Godot 4.x, multi-language privacy pages).
