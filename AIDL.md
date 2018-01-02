## AIDL
1. AIDL 是Android interface definition Language的英文缩写，--接口定义语言
2. 主要实现发布和调用远程服务，实现跨进程通信
3. 将AIDL 创建并放到对应的src目录中，工程的根目录会自动生成想对应的接口类，再通过bindService（intent，Service，int）方法绑定远程服务，在bindService中的ServiceConnect接口，我们需要覆写改类的onServiceConnected（ComponentName，Ibinder），我们可以通过Ibinder对象，强制转换为AIDL中的接口类，也就是系统产生的代理对象，改代理对象既可以跟我们的进程通信，又可以跟远程的进程通信。
## 子线程更新UI
除了handler和AsyncTask，可以调用Activity对象的runOnUiThread
View.post(Runnable r)