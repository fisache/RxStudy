package io.fisache.android_rxbinding.RxBinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

import rx.Observable;

import static io.fisache.android_rxbinding.RxBinding.internal.Preconditions.checkNotNull;

public class RxView {
    @CheckResult @NonNull
    public static Observable<Void> clicks(@NonNull View view) {
        checkNotNull(view, "view == null");
        // Observable 을 만든다 인자로는 Subscriber를 implement한 객체를 가진다
        // Subscriber는 구독될 때 onNext, onCompleted, onError를 호출한다.
        return Observable.create(new ViewClickOnSubscribe(view));
    }
}
