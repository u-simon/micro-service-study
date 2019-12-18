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
      *
      *
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
