package com.freeler.rxjava.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freeler.rxjava.R;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * 过滤操作符
 *
 * @author: xuzeyang
 * @Date: 2020/5/6
 */
@SuppressLint("CheckResult")
public class FilterActivity extends AppCompatActivity {

    private final static String TAG = "FilterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter();
            }
        });
        findViewById(R.id.ofType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ofType();
            }
        });
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip();
            }
        });
        findViewById(R.id.skipLast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipLast();
            }
        });
        findViewById(R.id.distinct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distinct();
            }
        });
        findViewById(R.id.distinctUntilChanged).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distinctUntilChanged();
            }
        });
        findViewById(R.id.take).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                take();
            }
        });
        findViewById(R.id.takeLast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeLast();
            }
        });
        findViewById(R.id.firstElement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstElement();
            }
        });
        findViewById(R.id.lastElement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastElement();
            }
        });
        findViewById(R.id.elementAt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elementAt();
            }
        });
        findViewById(R.id.elementAtOrError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elementAtOrError();
            }
        });
    }

    /**
     * 过滤操作符
     * >>>>>>
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：3
     * 过滤后接收到的数据：5
     * 过滤后接收到的数据：7
     * 过滤后接收到的数据：9
     * >>>>>>
     */
    private void filter() {
        Observable.range(1, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        // 过滤出奇数
                        return integer % 2 == 1;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 只发射指定类型的事件
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：2
     */
    private void ofType() {
        Observable.just(1, 2, "3")
                .ofType(Integer.class)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 跳过事件的前N项，只发射它之后的事件
     * 过滤后接收到的数据：3
     * 过滤后接收到的数据：4
     * 过滤后接收到的数据：5
     */
    private void skip() {
        Observable.range(1, 5)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }


    /**
     * 跳过事件的最后N项，只发射它之前的事件
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：2
     * 过滤后接收到的数据：3
     */
    private void skipLast() {
        Observable.range(1, 5)
                .skipLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 过滤掉重复的事件
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：2
     */
    private void distinct() {
        Observable.just(1, 2, 1, 2, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 过滤掉重复的事件
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：2
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：2
     */
    private void distinctUntilChanged() {
        Observable.just(1, 2, 1, 2, 2)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }


    /**
     * 跳过事件的前N项，只发射它之后的事件
     * 过滤后接收到的数据：1
     * 过滤后接收到的数据：2
     */
    private void take() {
        Observable.range(1, 5)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 跳过事件的前N项，只发射它之后的事件
     * 过滤后接收到的数据：4
     * 过滤后接收到的数据：5
     */
    private void takeLast() {
        Observable.range(1, 5)
                .takeLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 仅发射第一个的事件
     * 过滤后接收到的数据：1
     */
    private void firstElement() {
        Observable.range(1, 5)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 仅发射最后一个的事件
     * 过滤后接收到的数据：5
     */
    private void lastElement() {
        Observable.range(1, 5)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 指定接收某个元素（通过 索引值 确定），越界给默认值
     * 过滤后接收到的数据：99
     */
    private void elementAt() {
        Observable.range(1, 5)
                .elementAt(6, 99)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                });
    }

    /**
     * 指定接收某个元素（通过 索引值 确定），越界抛出异常
     * 越界抛出了error
     */
    private void elementAtOrError() {
        Observable.range(1, 5)
                .elementAtOrError(6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后接收到的数据：" + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "越界抛出了error");
                    }
                });
    }

}
