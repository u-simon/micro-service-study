package com.simon.microservce.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * @author simon
 * @date 2019/12/17 15:10
 * @describe 一念花开, 一念花落
 */
public class CommandKey {


    /**
     * 核心配置说明
     *  -> commandKey: 一般同一个监控服务使用相同的CommandKey,目的把HystrixCommand,HystrixCircuitBreaker, HystrixCommandMetrics
     *                 以及其他相关对象关联在一起,形成一个原子组,采用原生接口的话,默认值为类名,采用注解方式的话,默认值为方法名
     *  -> groupKey: 一般情况相同业务功能会使用相同的CommandGroupKey,对CommandKey分组,进行逻辑隔离,相同CommandGroupKey会使用同一
     *              个线程池或者信号量
     *  -> ThreadPoolKey : 物理隔离(相对于GroupKey逻辑隔离),当没有设置ThreadPoolKey的时候,线程池或者信号量的划分按照CommandGroupKey,
     *               当设置了ThreadPoolKey,那么线程池和信号量的划分就按照ThreadPoolKey来处理,相同ThreadPoolKey采用同一个线程池或信号量
     *  -> 命令类
     *      1.Execution:
     *          execution.isolation.strategy : 隔离级别,默认线程
     *          execution.isolation.thread.timeoutInMilliseconds : 线程执行超时时间,默认1000,一般选择服务tp99的时间
     *          (TP指标: TP50：指在一个时间段内（如5分钟），统计该方法每次调用所消耗的时间，并将这些时间按从小到大的顺序进行排序，取第50%的那个值作为TP50 值；
     *           配置此监控指标对应的报警阀值后，需要保证在这个时间段内该方法所有调用的消耗时间至少有50%的值要小于此阀值，否则系统将会报警)
     *      2.circuit Breaker
     *          circuitBreaker.requestVolumeThreshold : 默认20, 一个滑动窗口内"触发断路"要达到的最小访问次数,低于该次数,技术错误率
     *          达到,也不会触发断路操作,用于测试压力时候满足要求
     *          circuitBreaker.errorThresholdPercentage : 默认50(即 50%) 一个窗口内"触发断路"错误率,满足则进入断路状态,快速失效
     *          circuitBreaker.sleepWindowInMilliseconds ：默认5000ms 断路器打开后过多久调用时间服务进行重试
     *      3.ThreadPool线程池
     *          coreSize : 线程池大小,默认10
     *          maxQueueSize : 任务队列大小,使用blockingQueue 默认 -1
     *
     *
     */



    /**
     * 命令名称
     *      默认情况下,命令的名称由类名决定
     *          getClass().getSimpleName()
     *      ,也可以通过HystrixCommand/HystrixObservableCommand的构造显示传入
     *          super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
     *                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld")))
     *       通过定义Setter缓存,可以避免每次构造新的命令时重新构造Setter
     *          Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
     *          .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld))
     */
    public static class CommandName extends HystrixCommand<String>{
        private final String name;
        public CommandName(String name){
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")));
            this.name = name;
        }

        @Override
        protected String run() throws Exception {
            return null;
        }
    }


    /**  命令组
     *      Hystrix提供命令组机制,通过使用相同命令组Key，来统一一组命令的报表/告警/仪表盘/组/库的所有权
     *      默认情况下，Hystrix会让一组命令使用统一的线程池，除非手工指定线程池的划分
     *      HystrixCommandGroupKey是一个接口，并能被实现为枚举类/普通类，但其仍然会保留Factory内部类以构造并intern key 对象
     *      HystrixCommandGroupKey.Factory.asKey("ExampleGroup")
     */

    public static class CommandGroup extends HystrixCommand<String>{

        private final String name ;

        public CommandGroup ( String name ) {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
            this.name = name;
        }
        @Override
        protected String run() throws Exception {
            return null;
        }
    }

     /**
      *  命令线程池
      *      Hystrix使用线程池key来表示一个HystrixThreadPool，以实现监控/监控指标上报/缓存和其他用途中线程池的区分,一个HystrixCommand
      *      与一个通过HystrixThreadPoolKey获得的线程池相关联，该线程池会被注入到这个命令中，如果没有指定线程池Key，命令则默认会和一个
      *      通过HystrixCommandGroupKey创建的线程池相关联，也可以通过HystrixCommand/HystrixObservableCommand的构造器显示传入
      *
      *      super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup))
      *                  .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
      *                  .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool")))
      *    给 HelloWorld 这个 HystrixCommand 分配了一个 HelloWorldPool 的线程池 如果没指定 则默认使用 ExampleGroup 这个 key 代表的线程池
      *
      *    尽量使用 HystrixThreadPoolKey 而不是通过指定另一个HystrixCommandGroupKey 的原因是：多个命令可能从所有权或逻辑功能上来看属于同一个组
      *    ,但某些命令可能需要和其他命令隔离开来
      *
      *   例如
      *         有两个用来访问视频元数据的命令
      *         组名为 “VideoMetadata”
      *         命令A 访问 资源#1
      *         命令B 访问 资源#2
      *         如果 命令A 出现延迟并耗尽其关联的线程池，其不应该影响 命令B 的正常执行，因为它们访问的是不同的后端资源。
      *
      *         因此，我们从逻辑上将这两个命令放在同一个组，但需要将二者隔离开来，通过使用 HystrixThreadPoolKey，我们可以实现这一需求。
      */
    public static class CommandThreadPool extends HystrixCommand<String>{

        private final String name;

        public CommandThreadPool(String name){
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool")));
            this.name = name;
        }
        @Override
        protected String run() throws Exception {
            return null;
        }
    }
}
