package com.simon.microservce.hystrix.usagemode;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Observer;

/**
 * @author simon
 * @date 2019/12/18 10:23
 * @describe 一念花开, 一念花落
 */
public class FailFastObservableCommand extends HystrixObservableCommand<String> {

	private final boolean throwException;

	public FailFastObservableCommand(boolean throwException) {
        super(HystrixCommandGroupKey.Factory.asKey("FailFastObservableGroup"));
		this.throwException = throwException;
	}

	@Override
	protected Observable<String> construct() {
		return null;
	}

    @Override
    protected Observable<String> resumeWithFallback() {
	    if (throwException){
	        return Observable.error(new Throwable("failure from FailFastObservableCommand"));
        }
        return Observable.just("success");
    }

    public static void main(String[] args) {
        Observable<String> observe = new FailFastObservableCommand(true).observe();
        observe.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getCause());
            }

            @Override
            public void onNext(String s) {

            }
        });
    }
}
