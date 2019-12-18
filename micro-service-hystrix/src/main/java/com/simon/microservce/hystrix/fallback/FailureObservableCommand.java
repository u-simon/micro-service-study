package com.simon.microservce.hystrix.fallback;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

/**
 * @author simon
 * @Date 2019/12/16 16:20
 * @Describe 一念花开, 一念花落
 */
public class FailureObservableCommand extends HystrixObservableCommand<String> {

    private final String name;

    public FailureObservableCommand(String name){
        super(HystrixCommandGroupKey.Factory.asKey("failure"));
        this.name = name;
    }
    @Override
    protected Observable<String> construct() {
        throw new RuntimeException("his command always fails");
    }

    @Override
    protected Observable<String> resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(" hello failure " + name + " ! ");
                subscriber.onCompleted();
            }
        });
    }
}
