package io.fisache.android_rxbinding.RxBinding.internal;

public class Preconditions {
    // 전달받는 value(view)는 null이면 안된다
    public static <T> T checkNotNull(T value, String message) {
        if(value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }
}
