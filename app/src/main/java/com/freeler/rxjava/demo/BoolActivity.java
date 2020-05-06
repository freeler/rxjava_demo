package com.freeler.rxjava.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freeler.rxjava.R;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 条件操作符
 *
 * @author: xuzeyang
 * @Date: 2020/5/6
 */
@SuppressLint("CheckResult")
public class BoolActivity extends AppCompatActivity {

    private final static String TAG = "BoolActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bool);

        findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all();
            }
        });
        findViewById(R.id.takeWhile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeWhile();
            }
        });
        findViewById(R.id.takeUntil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeUntil();
            }
        });
        findViewById(R.id.skipWhile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipWhile();
            }
        });
        findViewById(R.id.skipUntil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipUntil();
            }
        });
        findViewById(R.id.sequenceEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceEqual();
            }
        });
        findViewById(R.id.contains).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains();
            }
        });
        findViewById(R.id.isEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmpty();
            }
        });
        findViewById(R.id.defaultIfEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultIfEmpty();
            }
        });
    }

    /**
     * 判断发送的数据是否全部都满足函数条件
     * >>>>>>
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：3
     * 过滤后接收到的数据：5
     * 过滤后接收到的数据：7
     * 过滤后接收到的数据：9
     * >>>>>>
     */
    private void all() {
        Observable.just(1, "2")
                .all(new Predicate<Serializable>() {
                    @Override
                    public boolean test(Serializable serializable) throws Exception {
                        return serializable instanceof String;
                    }
                })
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "函数条件 onSubscribe");
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.d(TAG, "函数条件：" + aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "函数条件 onError");
                    }
                });
    }

    /**
     * 开始满足函数条件才发射数据，直到不满足则停止发射,后续无论是否满足条件都不再发射
     */
    private void takeWhile() {
        Observable.just(1, 2, 3, 1, 2, 3)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "满足函数条件的数据：" + integer);
                    }
                });
    }

    /**
     * 直到满足函数条件就停止发射,后续无论是否满足条件都不再发射
     * 满足函数条件的数据：1
     * 满足函数条件的数据：2
     * 满足函数条件的数据：3
     */
    private void takeUntil() {
        Observable.just(1, 2, 3, 1, 2, 3)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "满足函数条件的数据：" + integer);
                    }
                });
    }


    /**
     * 丢弃发射的数据直到不满足函数条件才开始发射,后续所有的数据发射出来
     * 满足函数条件的数据：3
     * 满足函数条件的数据：1
     * 满足函数条件的数据：2
     * 满足函数条件的数据：3
     */
    private void skipWhile() {
        Observable.just(1, 2, 3, 1, 2, 3)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer != 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "满足函数条件的数据：" + integer);
                    }
                });
    }

    /**
     * 直到第二个Observable发射数据第一个Observable才会发射，之前的会丢弃掉
     */
    private void skipUntil() {
        Observable.just(1, 2, 3, 1, 2, 3)
                .skipUntil(new ObservableSource<Object>() {
                    @Override
                    public void subscribe(Observer<? super Object> observer) {
                        Observable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d(TAG, "延迟一秒发射");
                            }
                        });
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "满足函数条件的数据：" + integer);
                    }
                });
    }

    /**
     * 判定两个Observables需要发送的数据是否相同
     */
    private void sequenceEqual() {
        Observable.sequenceEqual(Observable.just(1), Observable.just(1))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "是否满足条件：" + aBoolean);
                    }
                });
    }

    /**
     * 判断发送的数据中是否包含指定数据
     */
    private void contains() {
        Observable.just(1, 2, 3, 1, 2, 3)
                .contains(1)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "是否满足条件：" + aBoolean);
                    }
                });
    }

    /**
     * 判断发送的数据是否为空
     */
    private void isEmpty() {
        Observable.empty()
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "是否满足条件：" + aBoolean);
                    }
                });
    }

    /**
     * 如果发送的数据为空，则发射一个默认值
     */
    private void defaultIfEmpty() {
        Observable.empty()
                .defaultIfEmpty(99)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        Log.d(TAG, "满足函数条件的数据：" + object);
                    }
                });
    }


}
