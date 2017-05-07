package hl.iss.whu.edu.laboratoryproject;

import android.content.Intent;
import android.os.Looper;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;

import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testRxjava(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(2);
        Flowable.fromIterable(integers).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                System.out.println("filter:" + Thread.currentThread().getName());
                return integer >= 2;
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe:" + Thread.currentThread().getName());
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + Thread.currentThread().getName());
                        System.out.println(integer);
                    }
                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError:" + Thread.currentThread().getName());
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("onComplete:" + Thread.currentThread().getName());
                    }
                });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}