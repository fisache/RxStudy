package io.fisache.android_rxbinding.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.test.runner.AndroidJUnitRunner;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;

// Android Test를 위해 AndroidJunitRunner를 상속받은 객체
// RxBinding/build.gradle에서 testInstrumentationRunner 이 클래스를 설정한다.
// 그렇게 되면 android test 진행 시 이 테스트 러너를 사용한다.
public class RxBindingTestRunner extends AndroidJUnitRunner {
    // Application을 켜고 끄는 Lock
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onStart() {
        // Application 획득
        Context app = getTargetContext().getApplicationContext();

        String name = RxBindingTestRunner.class.getSimpleName();
        // 키보드를 끈다.
//        KeyguardManager keyguardManager = (KeyguardManager) app.getSystemService(KEYGUARD_SERVICE);
//        keyguardManager.newKeyguardLock(name).disableKeyguard();

//        // 스크린을 켠다.
//        PowerManager power = (PowerManager) app.getSystemService(POWER_SERVICE);
//        wakeLock = power.newWakeLock(FULL_WAKE_LOCK  | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, name);
//        wakeLock.acquire();

        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        wakeLock.release();
    }
}
