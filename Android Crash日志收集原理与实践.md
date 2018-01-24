通常不需要我们自己研发Crash SDK的任务，市场上已经有很多第三方的Crash SDK可供选择，如腾讯的Bugly\百度的MTJ、友盟SDK等。实现一个Crash SDK包括以下三个方面的工作：
* Crash 的捕获
* Crash 堆栈信息的捕获
* Crash 日志的上报
	Android 底层是基于Linux操作系统构建的，上层是基于Java语言的实现的，上层与底层的通信基于JNI。在Android上面做开发，不可避免的需要跟Java和C/C++打交道。相应的，Crash可能发生在这两层，Java是基于虚拟机运行的，而C/C++更贴近操作系统，这两层的Crash机制差别比较大。
## Java层Crash捕获机制
在Java中异常分为两类：Checked Exception 和UnCheckedException。Checked Exception又称为编译时异常，它是在编译阶段必须处理的，否则编译失败，一般使用try...catch捕获处理。UnCheckedException又称为运行时异常，这种烈性的异常实在编译阶段检测不出来的，只有在程序运行时满足某些条件才会触发。
Java为我们提供了一个全局异常捕获处理器，Android应用在Java层捕获Crash依赖的就是Thread.UncaughtExceptionHandler处理器接口，通常情况下，我们只需要实现这个接口并重写其中的uncaughException方法，在该方法中可以读取Crash的堆栈信息。当然为了使用自定义的UncaughtExceptionHandler,我们还需要对它进行注册，以替换应用默认的异常处理器。通常情况下，手机发生Crash的堆栈信息就足够分析定位出崩溃的原因，但复杂一点的Crash可能仅有的堆栈信息是不够的，我们还需要一些其他信息：
* 线程信息
* SharedPreference信息
* 系统设置