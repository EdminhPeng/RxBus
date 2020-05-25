package com.ming.rx;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ming.rxbus.R;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private Button btnPost;

    private Button btnDispose;

    private TextView txtConsumer1;

    private TextView txtConsumer2;

    private int postValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPost = findViewById(R.id.btn_post);
        btnDispose = findViewById(R.id.btn_dispose);
        txtConsumer1 = findViewById(R.id.txt_consumer_1);
        txtConsumer2 = findViewById(R.id.txt_consumer_2);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.post(new RxEvent(++postValue));
            }
        });
        RxBus.with(RxEvent.class)
                .bindLifecycle(this)
                .register(new DisposableConsumer<RxEvent>() {

                    public void accept(RxEvent event) {
                        txtConsumer1.setText(String.valueOf(event.i));
                        dispose();
                    }
                });
        final Disposable disposable = RxBus.with(RxEvent.class)
                .bindLifecycle(this)
                .register(new DisposableConsumer<RxEvent>() {

                    public void accept(RxEvent event) {
                        txtConsumer2.setText(String.valueOf(event.i));
                    }
                });
        btnDispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disposable.dispose();
            }
        });
    }
}
