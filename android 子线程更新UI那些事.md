### Android 子线程更新UI的问题
	android 单线程模型：Android UI 操作不是线程安全的，并且UI的更新必须在UI线程执行
    如果其他线程想访问UI线程，有以下4中方式
<pre><code>
Activity.runOnUiThread(Runnable)
View.post(Runnable)
View.postDelayed(Runnalbe. long)
Handler
</code></pre>

	当一个程序第一次启动的时候，Android会同事启动一个对应的主线程，这个主线程就是UI线程，也就是ActivityThread.Ui线程主要负责处理与UI相关的时间，如用户的按键点击、用户触摸屏幕，以及屏幕绘图等。系统不会为每个组件单独创建一个线程，在同一个进程里的UI组件都会在UI线程里实例化，系统对每个UI组件的调用都从UI线程分发出去。所以，响应系统回调的方法永远都是在UI线程里运行，如响应用户动作的onKeyDown()回调。
   
   