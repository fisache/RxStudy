package fisache.io.rxstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etEdit = (EditText) findViewById(R.id.etEdit);
        TextView etResult = (TextView) findViewById(R.id.tvResult);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(v ->{
            etResult.setText("");
        // 람다 코드 구현
        Observable.just(etEdit.getText().toString())
                .map(dan -> Integer.parseInt(dan))
                .filter(dan -> 1 < dan && dan < 10)
                .flatMap(dan -> Observable.range(1, 9),
                        (dan, row) -> dan + " * " + row + " = " + (dan*row))
                .map(row -> row+"\n")
                .subscribe(
                        etResult::append,
                        e -> Toast.makeText(getBaseContext(), "Not between 2 and 9", Toast.LENGTH_LONG).show()
                );
//            // 람다 사용 안한 코드 구현
//            Observable.just(etEdit.getText().toString())
//                    .map(new Func1<String, Integer>() {
//                        @Override
//                        public Integer call(String dan) {
//                            return Integer.parseInt(dan);
//                        }
//                    })
//                    .filter(new Func1<Integer, Boolean>() {
//                        @Override
//                        public Boolean call(Integer dan) {
//                            if(dan > 1 && dan < 10) {
//                                return true;
//                            }
//                            return false;
//                        }
//                    })
//                    .flatMap(new Func1<Integer, Observable<String>>() {
//                        @Override
//                        public Observable<String> call(Integer dan) {
//                            return Observable.range(1,9)
//                                    .map(new Func1<Integer, String>() {
//                                        @Override
//                                        public String call(Integer row) {
//                                            return dan + " * " + row + " = " + (dan*row);
//                                        }
//                                    });
//                        }
//                    })
//                    .map(new Func1<String, String>() {
//                        @Override
//                        public String call(String row) {
//                            return row+"\n";
//                        }
//                    })
//                    .subscribe(
//                            new Observer<String>() {
//                                @Override
//                                public void onCompleted() {
//                                    Toast.makeText(getBaseContext(), "onCompleted", Toast.LENGTH_LONG).show();
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Toast.makeText(getBaseContext(), "Not between 2 and 9", Toast.LENGTH_LONG).show();
//                                }
//
//                                @Override
//                                public void onNext(String row) {
//                                    etResult.append(row);
//                                }
//                            }
//                    );
        });

    }
}
