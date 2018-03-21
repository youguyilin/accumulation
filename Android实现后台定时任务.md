1. CounDownTimer浅析
CounDownTimer(long millisInFuture, long countDownInterval)，但是如果使用不当，常常会报空指针异常？

> 通过源码分析，CounDownTimer的内部实现是采用Handler机制，通过sendMessageDelayed延迟发送一条message到主线程looper中，然后在自身中收到之后判断剩余时间，并发出相关回调，然后再次发送message的方式。由于它存在在Activity或者Fragment被回收时并未调用CounDownTimer的cancel（）方法结束自己，那么CoundownTimer方法中会继续执行，回调onTick(),如果该方法中使用Fragment的变量已经是null，就会出现空指针了。

## Android中定时任务一般有两种实现方式，一种是Java中的Timer类，一种是Android中的Alarm机制。
这两种方法在多数情况下实现 方式类似，但是Timer不适合那些需要长期在后台运行的定时任务，其实为了让手机省电，任何手机都会有休眠策略。如手机长时间未操作进入休眠状态，当进入休眠状态时，可能会导致Timer中的定时任务无法正常运行；而Alarm机制就不会出现这种情况，因为它具有唤醒CPU的功能，但要注意唤醒CPU和唤醒屏幕不是一个概念。

#### Alarm实现
首先通过Context的getSystemService()方法来获取AlarmManager实例，这里需要传入ALARM_SERVICE

接下来使用AlaramManager的set()方法就可以设置一个定时任务，如设置任务在5s之后执行，就可以写成：

``

	long trrigerAtTime = SystemClock.elapsedRealtime()+5*1000;
	manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,trrigerAtTime,pi);

其中set()方法的第一参数指定AlarmManager的工作类型，有四种，分别是ELAPSED_WAKE,ELAPSED_WAKEUP\RTC\RTC_WAKEUP。其中ELAPSED_WAKE是任务触发事件从系统开机时间开始算起，但不会唤醒CPU。ELAPSED_WAKEUP,表示会唤醒CPU，并从系统开机时间开始算起。RTC,表示任务的触发时间从1970.1.1日0点开始算起，不唤醒CPU;RTC_WAKEUP,唤醒CPU.
第三个参数是一个PendingIntent,这里一般会调用getBrodcast()获取一个能够执行广播的PendingIntent,这样任务触发时，广播的onReceive()方法可以得到执行。