package com.freeler.rxjava.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freeler.rxjava.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * 变换操作符
 *
 * @author: xuzeyang
 * @Date: 2020/5/6
 */
@SuppressLint("CheckResult")
public class MapActivity extends AppCompatActivity {

    private final static String TAG = "MapActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        findViewById(R.id.map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map();
            }
        });
        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });
        findViewById(R.id.flatMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flatMap();
            }
        });
        findViewById(R.id.concatMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concatMap();
            }
        });
        findViewById(R.id.buffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buffer();
            }
        });
        findViewById(R.id.window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window();
            }
        });
        findViewById(R.id.groupBy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupBy();
            }
        });
    }

    /**
     * 事件转换（一个参数）
     * 对 Observable 发送的每一个事件都通过 指定的函数（一个参数） 处理，从而变换成另外一种事件
     * >>>>>>
     * 使用 Map变换操作符 将事件1的参数从 整型1变换成 字符串类型1
     * 使用 Map变换操作符 将事件2的参数从 整型2变换成 字符串类型2
     * 使用 Map变换操作符 将事件3的参数从 整型3变换成 字符串类型3
     * >>>>>>
     */
    private void map() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            // 1. 被观察者发送事件 = 参数为整型 = 1、2、3
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);

            }
            // 2. 使用Map变换操作符中的Function函数对被观察者发送的事件进行统一变换：整型变换成字符串类型
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "使用 Map变换操作符 将事件" + integer + "的参数从 整型" + integer + " 变换成 字符串类型" + integer;
            }
        }).subscribe(new Consumer<String>() {

            // 3. 观察者接收事件时，是接收到变换后的事件 = 字符串类型
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });

    }

    /**
     * 事件转换（两个参数）
     * 对 Observable 发送的每一个事件都通过 指定的函数（两个参数） 处理，从而变换成另外一种事件
     * >>>>>>
     * 聚合值=10
     * 聚合值=200
     * 聚合值=6000
     * >>>>>>
     */
    private void scan() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(10);
                emitter.onNext(20);
                emitter.onNext(30);
            }
        }).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer s) throws Exception {
                // 发射10的时候，value为10
                // 发射20的时候，用保存的value 10运算得到200
                // 发射30的时候，用保存的value 200运算得到6000
                Log.d(TAG, "聚合值=" + s);
            }
        });

    }

    /**
     * 生成新的 Observable，无序
     * 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
     */
    private void flatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }

            // 采用flatMap（）变换操作符
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    /**
     * 生成新的 Observable，有序
     */
    private void concatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }

            // 采用flatMap（）变换操作符
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    /**
     * 分组发射，发射的是事件
     * 定期从 被观察者（Observable）需要发送的事件中 获取一定数量的事件 & 放到缓存区中，最终发送
     * >>>>>>
     * 缓存区里的事件数量 = 3
     * 事件 = 1
     * 事件 = 2
     * 事件 = 3
     * 缓存区里的事件数量 = 3
     * 事件 = 2
     * 事件 = 3
     * 事件 = 4
     * 缓存区里的事件数量 = 3
     * 事件 = 3
     * 事件 = 4
     * 事件 = 5
     * 缓存区里的事件数量 = 2
     * 事件 = 4
     * 事件 = 5
     * 缓存区里的事件数量 = 1
     * 事件 = 5
     * 对Complete事件作出响应
     * >>>>>>
     */
    private void buffer() {
        // 被观察者 需要发送5个数字
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 1) // 设置缓存区大小 & 步长
                // 缓存区大小 = 每次从被观察者中获取的事件数量
                // 步长 = 每次获取新事件的数量
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> stringList) {
                        //
                        Log.d(TAG, " 缓存区里的事件数量 = " + stringList.size());
                        for (Integer value : stringList) {
                            Log.d(TAG, " 事件 = " + value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 分组发射，发射的是Observable
     * 定期将来自 Observable 的数据拆分成一些 Observable 窗口，然后发射这些窗口，而不是每次发射一项
     * >>>>>>
     * onNext
     * accept:1
     * accept:2
     * onNext
     * accept:3
     * accept:4
     * onNext
     * accept:5
     * accept:6
     * onNext
     * accept:7
     * onComplete
     * >>>>>>
     */
    private void window() {
        Observable.range(1, 7)
                .window(2)
                .subscribe(new Consumer<Observable<Integer>>() {
                    @Override
                    public void accept(Observable<Integer> integerObservable) throws Exception {
                        Log.d(TAG, "onNext");
                        integerObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.d(TAG, "accept:" + integer);
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "onError");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /**
     * 分组
     * 将一个 Observable 拆分为一些 Observables 集合，它们中的每一个都发射原始 Observable 的一个子序列
     * >>>>>>
     * onNext
     * accept:1
     * accept:2
     * onNext
     * accept:3
     * accept:4
     * onNext
     * accept:5
     * accept:6
     * onNext
     * accept:7
     * onComplete
     * >>>>>>
     */
    private void groupBy() {
        Observable.range(1, 8)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return (integer % 2 == 0) ? "偶数" : "奇数";
                    }
                }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                final String key = stringIntegerGroupedObservable.getKey();
                stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, key + "包含：" + integer);
                    }
                });
            }
        });
    }
}
