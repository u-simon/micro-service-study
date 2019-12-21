package com.simon.microservce.hystrix.pattern;

import java.util.LinkedList;
import java.util.List;

/**
 * @author simon
 * @date 2019/12/20 19:50
 * @describe 一念花开, 一念花落
 */
public class ObserverPattern {


    /**
     * 观察者
     */
    interface Observer{
        void doEvent();
    }


    /**
     * 被观察者
     */
    static class Observable{
        List<Observer> observerList = new LinkedList<>();


        Observable register(Observer observer){
            observerList.add(observer);
            return this;
        }


        Observable unregister(Observer observer){
            observerList.remove(observer);
            return this;
        }

        void publisher(){
            for (Observer observer : observerList) {
                observer.doEvent();
            }
        }
    }

}
