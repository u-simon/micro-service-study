package com.simon.microservce.hystrix.command;

import com.netflix.hystrix.*;

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
     * Hystrix容错
     *  Hystrix容错主要是通过添加容许延迟和容错方法,帮助控制这些分布式服务之间的交互,还通过隔离服务之间的访问点,阻止他们之间的级联故障
     *  以及提供回退选项来实现这一点,从而提高系统的整体弹性,Hystrix主要提供了以下几种容错方法:
     *      -> 资源隔离
     *      -> 熔断
     *      -> 降级
     *   资源隔离
     *      -> 资源隔离主要是针对线程的隔离,Hystrix提供了两种线程隔离方式:线程池和信号量
     *      线程隔离-线程池
     *          -> Hystrix通过命令模式对发送请求的对象和执行请求的对象进行解耦,将不同类型的业务请求封装为对应的命令请求,通过将发送请求与执行请求
     *             的线程分离,可有效防止发生级联故障,当线程池或请求队列饱和时,Hystrix将拒绝服务,使得请求线程可以快速失败,从而避免依赖问题扩散
     *          -> 优点
     *              1.保护应用程序以免受来自依赖故障的影响,指定依赖现场池饱和不会影响应用程序的其余部分
     *              2.当引入新客户端lib时,即使发生问题,也是在本lib中,并不会影响到其他内容
     *              3.当依赖从故障恢复正常时,应用程序会立即恢复正常的性能
     *              4.当以能用程序一些配置参数错误时,线程池的运行状况会很快检测到这一点,同时可以通过动态属性进行实时纠正错误的参数配置
     *              5.如果服务的性能有变化,需要实时调整,比如增加或减少超时时间,更爱重试次数,可以通过线程池指标动态属性修改,而不会影响到其他调用请求
     *              6.除了隔离优势外,Hystrix拥有专门的线程池可提供内置的并发功能,使得可以在同步调用之上构建异步门面(外观模式),为异步编程提供了支持
     *              注意: 尽管线程池提供了线程隔离,我们的客户端底层代码也必须要有超时设置或响应线程中断,不能无限制的阻塞以至线程池一直饱和
     *          -> 缺点
     *              1.线程池的主要缺点就是增加了计算开销,每个命令的执行都在单独的线程完成,增加了排队、调度和上下文切换的开销,因此,要使用Hystrix,就必须
     *              接受他带来的开销,以换取他所提供的好处
     *         通常情况下,线程池引入的开销足够小,不会有重大的成本或性能影响,但对于一些访问延迟极低的服务,如只依赖内存缓存,线程池引入的开销就比较明显了,
     *         这时候使用线程池隔离技术就不适合了,我们需要考虑更轻量级的方式,如信号量
     *
     *      线程隔离-信号量
     *          -> 当依赖延迟极低的服务时,线程池隔离技术引入的开销超过了他带来的好处,这时候可以使用信号量隔离技术来代替,通过设置信号量来限制对任何给定
     *              依赖的并发调用量
     *          -> 使用线程池时,发送请求的线程和执行依赖服务的线程不是同一个,而使用信号量时，发送请求的线程和执行依赖服务的线程是同一个,都是发起请求
     *              的线程
     *           由于Hystrix默认使用线程池做线程隔离,使用信号量隔离需要显示地将属性execution.isolation.strategy设置为 ExecutionIsolationStrategy.SEMAPHORE
     *           ,同时配置信号量个数为10,客户端需向依赖服务发起请求时,需要获取一个信号量才能真正发起调用,由于信号量的数量有限,当并发请求量超过信号量个数时,后续的
     *           请求都会直接拒绝,进入fallback流程
     *           线程隔离主要是通过控制并发请求量,防止请求线程大面积阻塞,从而达到限流和防止雪崩的目的
     *  熔断
     *      配置
     *          1.circuitBreaker.enabled 是否启动熔断器,默认是true
     *          2.circuitBreaker.forceOpen 熔断器强制打开,始终保持打开状态,不关注熔断开关的实际状态,默认值false
     *          3.circuitBreaker.forceClosed 熔断器强制关闭,始终保持关闭状态,不关注熔断开关的实际状态,默认值为false
     *                              此外不必关注熔断器实际状态，也就是说熔断器仍然会维护统计数据和开关状态，只是不生效而已
     *          4.circuitBreaker.errorThresholdPercentage 错误率,默认是50%,例如一段时间内(10s)有100个请求,其中有54个异常或者超时,那么这段时间内的错误率是54%
     *                              大于了默认值50%,这种情况下会触发熔断器打开
     *          5.circuitBreaker.requestVolumeThreshold 默认值20,含义是一段时间内至少有20个请求才进行errorThresholdPercentage计算,比如一段时间有了19个请求,
     *                              且请求全部失败了,错误率是100%,但熔断器不会打开,总请求数不满足20
     *          6.circuitBreaker.sleepWindowInMilliseconds 半开状态试探睡眠时间,默认是5000ms,比如当熔断器开启5000ms之后,会尝试放过去一部分流量进行试探,确定
     *                              依赖服务是否恢复
     *
     */

    /**
     * 信号量示例
     */
    public class QueryByOrderIdCommandSemaphore extends HystrixCommand<Integer> {

        private String name;
        public QueryByOrderIdCommandSemaphore(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("orderService"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("queryByOrderId"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withCircuitBreakerRequestVolumeThreshold(10)////至少有10个请求，熔断器才进行错误率的计算
                            .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                            .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                            .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)));//最大并发请求量
            this.name = name;
        }

        @Override
        protected Integer run() {
            //实际执行
            return 0;
        }

        @Override
        protected Integer getFallback() {
            return -1;
        }
    }

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
