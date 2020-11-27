package com.freeler.demo.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freeler.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 创建操作符
 *
 * @author: xuzeyang
 * @Date: 2020/5/6
 */
public class CreateActivity extends AppCompatActivity {

    private final static String TAG = "CreateActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
        findViewById(R.id.just).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                just();
            }
        });
        findViewById(R.id.fromArray).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromArray();
            }
        });
        findViewById(R.id.fromIterable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromIterable();
            }
        });
        findViewById(R.id.empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empty();
            }
        });
        findViewById(R.id.defer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defer();
            }
        });
        findViewById(R.id.timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer();
            }
        });
        findViewById(R.id.interval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interval();
            }
        });
        findViewById(R.id.intervalRange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervalRange();
            }
        });
        findViewById(R.id.range).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                range();
            }
        });
    }


    /**
     * 完整创建并发送事件
     */
    private void create() {
        // 1. 通过 create() 创建被观察者 Observable 对象
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // 传入参数: ObservableOnSubscribe 接口的实例
            // 当 Observable 被订阅时, ObservableOnSubscribe 的 subscribe() 方法
            // 会自动被调用, 即事件序列就会依照设定依次被触发
            // 即观察者会依次调用对应事件的复写方法从而响应事件
            // 从而实现由被观察者向观察者的事件传递 & 被观察者调用了观察者的回调方法, 即观察者模式

            //2. 再复写的 subscribe() 里定义需要发送的事件
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) {
                // 通过 ObservableEmitter 类对象 产生 & 发送事件
                // ObservableEmitter类介绍
                // a. 定义: 事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件
                // 注: 建议发送事件前检查emitter的isDisposed状态，以便在没有观察者或
                //     观察者与被观察者断开连接时，让Observable停止发射数据
                if (!emitter.isDisposed()) {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                }
                emitter.onComplete();
            } // 至此，一个完整的被观察者对象 Observable 就创建完毕了。
        }).subscribe(new Observer<Integer>() {
            // 3. 通过通过订阅 subscribe 连接观察者和被观察者
            // 4. 创建观察者 & 定义响应事件的行为
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }
            // 默认最先调用复写的 onSubscribe（）

            @Override
            public void onNext(@NonNull Integer value) {
                Log.d(TAG, "接收到了事件" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }

    /**
     * 快速创建并发送事件、10参数以下快速创建
     */
    private void just() {
        // 1. 创建时传入整型1、2、3
        // 在创建后就会发送这些对象，相当于执行了onNext(1)、onNext(2)、onNext(3)
        Observable.just(1, 2, 3)
                // 至此，一个Observable对象创建完毕
                // just方法是对元素判空后, 如果长度大于1, 则调用fromArray方法
                // 2. 通过通过订阅 subscribe 连接观察者和被观察者
                // 3. 创建观察者 & 定义响应事件的行为
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe（）

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });
    }

    /**
     * 快速创建数组并发送事件
     */
    private void fromArray() {
        // 1. 设置需要传入的数组
        Integer[] items = {0, 1, 2, 3, 4};

        // 2. 创建被观察者对象（Observable）时传入数组
        // 在创建后就会将该数组转换成Observable & 发送该对象中的所有数据
        Observable.fromArray(items)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe()

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });

    }

    /**
     * 快速创建集合并发送事件
     */
    private void fromIterable() {
        // 1. 设置一个集合
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        // 2. 通过fromIterable()将集合中的对象 / 数据发送出去
        Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe()

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });


    }

    /**
     * empty:直接通知完成
     * never:不发送任何事件
     * error:直接通知异常
     */
    private void empty() {
        Observable.just(1, 2, 3)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull Integer integer) {
                        if (integer == 2) {
//                            return Observable.never();
                            return Observable.empty();
//                            return Observable.error(new Throwable("这条信息报错啦"));
                        }
                        return Observable.just(integer);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAGTAG", "对Complete事件作出响应");
                    }
                });

    }

    /**
     * 只有当订阅者订阅才创建 Observable，为每个订阅者创建一个新的Observable
     */
    private void defer() {
        // 1. 通过 defer 定义被观察者对象
        // 注: 此时被观察者对象还没创建, Observable.just()还未调用
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() {
                return Observable.just(10, 20);
            }
        });

        // 2. 此时，才会调用 defer() 创建被观察者对象 (Observable)
        observable.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull Integer value) {
                Log.d(TAG, "接收到的整数是" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });

        observable.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull Integer value) {
                Log.d(TAG, "接收到的整数是" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });

    }

    /**
     * 延迟发射数据
     */
    private void timer() {
        // 该例子 = 延迟2s后，发送一个long类型数值
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe()

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 轮询发射数据
     */
    private void interval() {
        // 参数说明：
        // 参数1 = 第1次延迟时间；
        // 参数2 = 间隔时间数字；
        // 参数3 = 时间单位；
        Observable.interval(3, 1, TimeUnit.SECONDS)
                // 该例子发送的事件序列特点：延迟3s后发送事件，每隔1秒产生1个数字（从0开始递增1，无限个）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe()

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 延迟轮询发射指定数量的数据
     */
    private void intervalRange() {
        // 参数说明：
        // 参数1 = 事件序列起始点；
        // 参数2 = 事件数量；
        // 参数3 = 第1次事件延迟发送时间；
        // 参数4 = 间隔时间数字；
        // 参数5 = 时间单位
        Observable.intervalRange(11, 5, 2, 5, TimeUnit.SECONDS)
                // 该例子发送的事件序列特点:
                // 1. 从11开始，一共发送5个事件;
                // 2. 第1次延迟 2s发送，之后每隔 5秒产生1个数字(递增加1)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe()

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });

    }

    /**
     * 无延迟轮询发射指定数量的数据
     */
    private void range() {
        // 参数说明:
        // 参数1 = 事件序列起始点;
        // 参数2 = 事件数量;
        // 注：若设置为负数，则会抛出异常
        Observable.range(3, 4)
                // 该例子发送的事件序列特点: 从3开始发送，每次发送事件递增1,一共发送4个事件
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }
                    // 默认最先调用复写的 onSubscribe()

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });

    }

}
