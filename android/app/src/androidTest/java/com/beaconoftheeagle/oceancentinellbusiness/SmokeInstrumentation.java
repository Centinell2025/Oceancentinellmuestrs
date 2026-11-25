package com.beaconoftheeagle.oceancentinellbusiness;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/** Runs the packaged Android interface on an emulator and captures actual app screens. */
public final class SmokeInstrumentation extends Instrumentation {
    private Activity activity;
    private WebView web;
    @Override public void onCreate(Bundle arguments) { super.onCreate(arguments); start(); }
    @Override public void onStart() {
        Bundle result = new Bundle();
        try {
            Intent intent = new Intent(getTargetContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity = startActivitySync(intent);
            runOnMainSync(() -> web = findWeb(activity.getWindow().getDecorView()));
            if (web == null) throw new AssertionError("No packaged WebView");
            await("document.readyState === 'complete' && !!document.getElementById('enterDemo')");
            capture("01-welcome.png");
            js("document.getElementById('enterDemo').click(); true");
            await("document.getElementById('main').innerText.includes('Maritime Operations Center')");
            capture("02-command-overview.png");
            js("location.hash='fleet'; true");
            await("!!document.getElementById('fleet-search')");
            js("document.getElementById('fleet-search').value='Harbor'; document.getElementById('fleet-search').dispatchEvent(new Event('input',{bubbles:true}));true");
            await("document.getElementById('fleet-table').innerText.includes('Harbor Sentinel') && !document.getElementById('fleet-table').innerText.includes('Atlantic Pioneer')");
            capture("03-vessel-register.png");
            js("location.hash='map'; true");
            await("document.querySelectorAll('.map-marker').length===3");
            capture("04-fleet-map.png");
            js("document.querySelector('[data-ais-action=start]').click(); true");
            await("document.getElementById('ais-service').innerText.includes('Provider key required') || document.getElementById('ais-service').innerText.includes('Receiving AIS') || document.getElementById('ais-service').innerText.includes('Waiting for AIS')");
            js("document.querySelector('[data-ais-action=stop]').click();true");
            js("location.hash='maintenance';true");
            await("!!document.querySelector('[data-task]')");
            js("var task=document.querySelector('[data-task]');task.value='Completed';task.dispatchEvent(new Event('change',{bubbles:true}));true");
            await("JSON.parse(localStorage.getItem('oceancentinell-business-demo-v1')).tasks[0].status==='Completed'");
            result.putString("stream", "PASS: packaged app loaded, fleet filtering, map, native AIS gateway and local maintenance persistence.\n");
            result.putString("result", "PASS");
            finish(Activity.RESULT_OK, result);
        } catch (Throwable error) {
            try { capture("00-failure.png"); } catch (Exception ignored) {}
            result.putString("stream", "FAIL: " + error + "\n");
            result.putString("result", "FAIL");
            finish(Activity.RESULT_CANCELED, result);
        }
    }
    private WebView findWeb(View view) {
        if (view instanceof WebView) return (WebView) view;
        if (view instanceof ViewGroup) for (int i=0;i<((ViewGroup)view).getChildCount();i++) {
            WebView found=findWeb(((ViewGroup)view).getChildAt(i)); if (found!=null) return found;
        }
        return null;
    }
    private String js(String source) throws Exception {
        CountDownLatch done=new CountDownLatch(1); AtomicReference<String> value=new AtomicReference<>();
        runOnMainSync(() -> web.evaluateJavascript(source, v -> {value.set(v);done.countDown();}));
        if(!done.await(30,TimeUnit.SECONDS))throw new AssertionError("WebView evaluation timed out: "+source);
        return value.get();
    }
    private void await(String condition) throws Exception {
        for(int i=0;i<60;i++){if("true".equals(js(condition)))return;Thread.sleep(500);}
        throw new AssertionError("Condition failed: "+condition);
    }
    private void capture(String name) throws Exception {
        waitForIdleSync(); Thread.sleep(400);
        Bitmap bitmap=getUiAutomation().takeScreenshot();
        if(bitmap==null)throw new AssertionError("Screenshot unavailable");
        File folder=new File(getContext().getExternalFilesDir(null),"screenshots");folder.mkdirs();
        try(FileOutputStream out=new FileOutputStream(new File(folder,name))){bitmap.compress(Bitmap.CompressFormat.PNG,100,out);}
        bitmap.recycle();
    }
}
