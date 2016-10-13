package io.fisache.android_rxbinding.util;

import android.util.Log;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import rx.Observer;

import static com.google.common.truth.Truth.assertThat;

// Observer는 구독(subscribe)할 때 인자로 넣는 객체다.
// 보통은 익명클래스로 구현한다. 간혹 onNext가 필요할 때는 Action 클래스로 구현해 call만 쓰는 경우도있다
// 여기서는 테스트를 위해서 구현한다
public final class RecordingObserver<T> implements Observer<T> {
    private final String TAG = "RecordingObserver";

    // onCompleted, onError, onNext 이벤트가 발생했을 때 내부클래스로 구현된 각 객체를 생성해 이 deque에 넣는다
    // 그리고 이벤트가 발생했는지 확인할 때 이 deque에서 빼서 확인한다.
    private final BlockingDeque<Object> events = new LinkedBlockingDeque<>();

    // onCompleted, onError, onNext는 모두 ViewClickOnSubscribe에서 언제 호출되는지 다 명시돼있다.
    // 물론 정확히는 onNext만 호출된다.

    // onCompleted가 수행되면 events에 OnCompleted를 넣는다
    @Override
    public void onCompleted() {
        Log.v(TAG, "onCompleted");
        events.add(new OnCompleted());
    }

    // onError가 수행되면 events에 OnCompleted를 넣는다
    @Override
    public void onError(Throwable e) {
        Log.v(TAG, "onError", e);
        events.add(new OnError(e));
    }

    // onNext가 수행되면 events에 OnCompleted를 넣는다
    @Override
    public void onNext(T t) {
        Log.v(TAG, "onNext" + t);
        events.add(new OnNext(t));
    }

    // takeNext에 의해서 호출된다. takeNext는 테스트 코드에서 버튼이 눌린 후 이벤트가 있는지 확인할 때 호출된다.
    // 전달되는 Class는 OnNext로 events에 OnNext가 있는지 살펴보고 있으면 그걸 호출하고 없으면 exception 처리한다
    private <E> E takeEvent(Class<E> wanted) {
        Object event;
        try {
            // events의 첫번째 객체를 꺼낸다.
            event = events.pollFirst(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(event == null) {
            throw new NoSuchElementException(
                    "No event found while waiting for " + wanted.getSimpleName());
        }
        // 꺼낸 객체가 전달받은 객체와 같은지 확인한다.
        // 전달받은 객체는 OnNext, 꺼낸 객체 역시 OnNext여야 한다
        // click이 되면 ViewClickOnSubscribe에서 OnNext를 호출하고 이걸 구독하면 이 객체의 OnNext가 호출되고
        // onNext는 events에 OnNext 객체를 만들어 추가한다
        // 테스트가 제대로 검증됐다면 참이 된다.
        assertThat(event).isInstanceOf(wanted);
        // 전달받은 객체를 꺼낸 객체로 캐스팅한다. 꺼낸 객체가 OnNext라면 정상적으로 리턴하고 아니라면
        // Cast Exception 을 발생시킨다
        return wanted.cast(event);
    }

    // 테스트 코드에서 버튼을 누른 후 OnNext 이벤트가 있었는지 확인할 때 호출한다
    public T takeNext() {
        OnNext event = takeEvent(OnNext.class);
        return event.value;
    }

    // 테스트 코드에서 호출하진 않지만 에러 이벤트가 있을 때 호출할 것이다
    public Throwable takeError() {
        return takeEvent(OnError.class).throwable;
    }

    // 테스트 코드에서 호출하진 않지만 구독이 완료됐는지 확인할 때 호출한다.
    public void assertOnCompleted() {
        takeEvent(OnCompleted.class);
    }

    // 테스트 코드에서 객체를 생성하고 RxView.clicks를 구독하고 호출한다
    // 아직 버튼 클릭 이벤트가 발생하지 않았기 때문에 events 객체에는 어떠한 객체도 없다.
    public void assertNoMoreEvents() {
        try {
            // 객체가 없어야하는데 객체가 있었다면 Illegal exception을 발생시킨다.
            Object event = takeEvent(Object.class);
            throw new IllegalStateException("Expected no more events but got " + event);
            // NoSuchElementException는 iterator 또는 enum의 끝에서 호출했을 때 발생된다.
            // 여기서는 객체가 없기 때문에 호출된다 하지만 catch에서 에러를 발생시키지 않기 때문에 무시된다.
            // 즉, events 안에 아무것도 없으면 예외가 발생하지 않지만 어떤게 있게되면 예외가 발생한다.
        } catch (NoSuchElementException ignored) {
        }
    }

    // OnCompleted 객체를 정의한다.
    private final class OnCompleted {
        @Override
        public String toString() {
            return "onCompleted";
        }
    }

    private final class OnError {
        private final Throwable throwable;

        private OnError(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public String toString() {
            return "OnError[" + throwable + "]";
        }
    }

    private final class OnNext {
        private final T value;

        private OnNext(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "OnNext[" + value + "]";
        }
    }
}
