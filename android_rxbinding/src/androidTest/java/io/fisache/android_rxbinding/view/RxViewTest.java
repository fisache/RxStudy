package io.fisache.android_rxbinding.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.fisache.android_rxbinding.RxBinding.view.RxView;
import io.fisache.android_rxbinding.util.RecordingObserver;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class RxViewTest {
    @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

    private final Context context = InstrumentationRegistry.getContext();
    private final View view = new View(context);

    @Test @UiThreadTest public void clicks() {
        RecordingObserver<Void> o = new RecordingObserver<>();
        Subscription subscription = RxView.clicks(view).subscribe(o);
        o.assertNoMoreEvents();

        view.performClick();
        // RecordingObserver의 takeNext 객체는 OnNext의 value 필드를 반환한다.
        // ViewClickOnSubscribe에서 버튼 클릭 이벤트가 발생하면 OnNext를 호출하는데 이 때 null을 인자로 넘긴다.
        // 테스트 코드에서는 이를 RecordingObserver가 구독하기 때문에 해당 객체의 onNext가 호출된다.
        // 그 객체의 onNext에서는 내부 deque에 내부 클래스 OnNext를 생성해 넣는다.
        // 이 때 OnNext를 생성하면서 전달받은 null을 value 필드에 넣는다.
        // 따라서 takeNext를 이용해 이 OnNext 객체를 얻고 value 필드를 반환했다면 null이 나오는게 맞다.
        assertThat(o.takeNext()).isNull();

        view.performClick();
        assertThat(o.takeNext()).isNull();

        subscription.unsubscribe();

        view.performClick();
        o.assertNoMoreEvents();
    }
}
