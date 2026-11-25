package com.beaconoftheeagle.oceancentinellbusiness;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MainActivity extends Activity {
    private static final String ORIGIN = "https://appassets.androidplatform.net";
    private static final int SAVE = 11, HASH = 12, IMPORT = 13;
    private static final int LIMIT = 2 * 1024 * 1024;
    private WebView web;
    private byte[] pendingExport;
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        web.setBackgroundColor(0xff071022);
        FrameLayout frame = new FrameLayout(this);
        frame.setBackgroundColor(0xff071022);
        frame.addView(web, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(frame);
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            frame.setOnApplyWindowInsetsListener((view, insets) -> {
                android.graphics.Insets safe = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout() | WindowInsets.Type.ime());
                view.setPadding(safe.left, safe.top, safe.right, safe.bottom);
                return WindowInsets.CONSUMED;
            });
        } else {
            frame.setOnApplyWindowInsetsListener((view, insets) -> {
                view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
                return insets.consumeSystemWindowInsets();
            });
        }
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setSupportMultipleWindows(false);
        web.addJavascriptInterface(new DeviceBridge(), "CentinellDevice");
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // The privileged bridge is available only to packaged content.
                return !isLocal(request.getUrl());
            }
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String name = uri.getPath();
                if (!isLocal(uri) || name == null || !name.matches("/assets/[a-zA-Z0-9._-]+")) return missing();
                try {
                    String type = name.endsWith(".png") ? "image/png" : name.endsWith(".js") ? "application/javascript" : name.endsWith(".css") ? "text/css" : "text/html";
                    return new WebResourceResponse(type, "UTF-8", 200, "OK", Collections.singletonMap("Cache-Control", "no-store"), getAssets().open(name.substring(8)));
                } catch (Exception error) { return missing(); }
            }
        });
        web.loadUrl(ORIGIN + "/assets/index.html");
    }
    private boolean isLocal(Uri uri) {
        return "https".equals(uri.getScheme()) && "appassets.androidplatform.net".equals(uri.getHost()) && uri.getPort() == -1;
    }
    private WebResourceResponse missing() {
        return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", Collections.emptyMap(), new ByteArrayInputStream(new byte[0]));
    }
    private void result(String name, JSONObject value) {
        runOnUiThread(() -> { if (!isFinishing() && !isDestroyed()) web.evaluateJavascript("window.CentinellNativeResult(" + JSONObject.quote(name) + "," + value + ")", null); });
    }
    private void failure(String message) {
        JSONObject obj = new JSONObject();
        try { obj.put("message", message); } catch (Exception ignored) { }
        result("error", obj);
    }
    private void choose(int code, String type) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType(type).addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, code);
    }
    private final class DeviceBridge {
        @JavascriptInterface public void openSupport() {
            runOnUiThread(() -> {
                try { startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:centinellsuite@gmail.com?subject=OceanCentinell%20enterprise%20pilot"))); }
                catch (android.content.ActivityNotFoundException error) { failure("No email application is available. Contact centinellsuite@gmail.com."); }
            });
        }
        @JavascriptInterface public void printReport() {
            runOnUiThread(() -> {
                PrintManager manager = (PrintManager) getSystemService(PRINT_SERVICE);
                if (manager != null) manager.print("CENTINELLia case report", web.createPrintDocumentAdapter("CENTINELLia"), new PrintAttributes.Builder().build());
                else failure("Printing is unavailable on this device.");
            });
        }
        @JavascriptInterface public void hashFile() { runOnUiThread(() -> choose(HASH, "*/*")); }
        @JavascriptInterface public void importBackup() { runOnUiThread(() -> choose(IMPORT, "*/*")); }
        @JavascriptInterface public void openPrivacy() {
            runOnUiThread(() -> {
                try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://oceancentinell-ais-gateway-production.up.railway.app/privacy"))); }
                catch (android.content.ActivityNotFoundException error) { failure("No browser is available. Contact centinellsuite@gmail.com for the privacy policy."); }
            });
        }
        @JavascriptInterface public void fetchAIS() {
            worker.execute(() -> {
                javax.net.ssl.HttpsURLConnection connection = null;
                try {
                    connection = (javax.net.ssl.HttpsURLConnection) new java.net.URL("https://oceancentinell-ais-gateway-production.up.railway.app/positions").openConnection();
                    connection.setConnectTimeout(10000); connection.setReadTimeout(10000);
                    connection.setInstanceFollowRedirects(false);
                    if (connection.getResponseCode() != 200) throw new java.io.IOException();
                    try (InputStream input = connection.getInputStream(); ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[8192]; int n;
                        while ((n = input.read(buffer)) != -1) {
                            if (bytes.size() + n > LIMIT) throw new java.io.IOException();
                            bytes.write(buffer, 0, n);
                        }
                        result("ais", new JSONObject(bytes.toString(StandardCharsets.UTF_8.name())));
                    }
                } catch (Exception error) { result("aisError", new JSONObject()); }
                finally { if (connection != null) connection.disconnect(); }
            });
        }
        @JavascriptInterface public void exportText(String filename, String type, String content) {
            if (content.length() > LIMIT) { failure("Export exceeds the 2 MB limit."); return; }
            runOnUiThread(() -> {
                if (pendingExport != null) { failure("Finish the current export first."); return; }
                pendingExport = content.getBytes(StandardCharsets.UTF_8);
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("application/json".equals(type) || "text/csv".equals(type) ? type : "text/plain")
                    .putExtra(Intent.EXTRA_TITLE, filename.replaceAll("[^a-zA-Z0-9._-]", "_"));
                startActivityForResult(intent, SAVE);
            });
        }
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            if (requestCode == SAVE) pendingExport = null;
            result("cancelled", new JSONObject());
            return;
        }
        Uri uri = data.getData();
        byte[] export = pendingExport;
        if (requestCode == SAVE) pendingExport = null;
        worker.execute(() -> {
            try {
                if (requestCode == SAVE) {
                    if (export == null) throw new IllegalStateException();
                    try (OutputStream out = getContentResolver().openOutputStream(uri, "wt")) {
                        if (out == null) throw new IllegalStateException();
                        out.write(export);
                    }
                    result("saved", new JSONObject());
                } else if (requestCode == HASH) {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    long count = 0;
                    try (InputStream input = getContentResolver().openInputStream(uri)) {
                        if (input == null) throw new IllegalStateException();
                        byte[] buffer = new byte[65536]; int n;
                        while ((n = input.read(buffer)) != -1) { digest.update(buffer, 0, n); count += n; }
                    }
                    String name = "Selected file";
                    try (Cursor cursor = getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
                        if (cursor != null && cursor.moveToFirst()) name = cursor.getString(0);
                    }
                    StringBuilder hex = new StringBuilder();
                    for (byte b : digest.digest()) hex.append(String.format(java.util.Locale.ROOT, "%02x", b & 255));
                    result("hash", new JSONObject().put("name", name).put("size", count).put("sha256", hex.toString()));
                } else if (requestCode == IMPORT) {
                    try (InputStream input = getContentResolver().openInputStream(uri); ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                        if (input == null) throw new IllegalStateException();
                        byte[] buffer = new byte[8192]; int n;
                        while ((n = input.read(buffer)) != -1) {
                            if (bytes.size() + n > LIMIT) throw new IllegalArgumentException();
                            bytes.write(buffer, 0, n);
                        }
                        result("import", new JSONObject().put("text", bytes.toString(StandardCharsets.UTF_8.name())));
                    }
                }
            } catch (Exception error) { failure("The file operation could not be completed. Check the selected file and available storage."); }
        });
    }
    @Override protected void onDestroy() {
        worker.shutdown();
        web.removeJavascriptInterface("CentinellDevice");
        web.destroy();
        super.onDestroy();
    }
}
