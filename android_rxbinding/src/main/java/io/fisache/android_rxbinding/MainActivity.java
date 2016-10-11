package io.fisache.android_rxbinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import io.fisache.android_rxbinding.RxBinding.view.RxView;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn);
        TextView tvText = (TextView) findViewById(R.id.tvText);
        subscription = RxView.clicks(btn)
                .map(aVoid -> "Rx Binding!")
                .subscribe(s -> {
                    tvText.setText(s);
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }
}
