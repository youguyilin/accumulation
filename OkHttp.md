OkHttp是一个高效的HTTP库：
> * 支持SPDY，共享同一个Socket来处理同一个服务器的所有请求
> * 如果SPDY不可用，则通过连接池来减少请求超时
> * 无缝的支持GZIP来减少数据流量
> * 缓存响应数据来减少重复的网络请求
会从很多常用的连接问题中自动回复。如果您的服务器配置了多个IP地址，当第一个IP连接失败的时候，OkHttp会自动尝试下一个IP。OkHttp还处理了dialing服务器问题和SSL握手失败问题。
## 总体设计
Request.Builder--->	Dispatcher(ApplicationInterceptorChain)		-->HttpEngine[Cache]			-->ConnectionPool[Connection]-->[Route] [Platform]					-->Data [Server(Socket)]
通过总体设计流程，可以看到OkHttp主要是通过Dispatcher不断从RequestQueue中取出请求（Call）,根据是否已缓存调用Cache或Network这两类数据获取接口之一，从内存缓存或是服务器取得请求的数据。
## OkHttp中重要的类
1. Route.java
具体的地址是用来连接抽象的远程服务器，当创建一个与客户端的连接时，有以下几种方式：
* HTTP proxy:一个代理服务可用于明确客户端的配置。
* IP address:无论连接到服务器使用代理或者直连，打开socket连接都需要一个IP地址。DNS(域名系统可能会返回多个IP地址去尝试)
* TLS configuration:使用HTTPS连接时，会尝试使用加密串或者TLS的不同版本。
## Platform.java
涉及到具体平台特性
> * Server name indication(SNI):支持Android 2.3+
> * Session Tickets:支持Android 2.3+
> * Android Traffic Stats(Socket Tagging):支持Android 4.0+
> * ALPN（Apllication Layer Protocol Negotiation）:支持Android 5.0+.APIs的具体表现在Android 4.4，但是具体实现是不稳定的。
## 同步与异步的实现
在发起请求时，整个框架主要通过Call来封装每一次请求，同时Call持有OkHttpClient和一份HttpEngine。而每一次的同步或者异步请求都会有Dispatche的参与，不同的是：
> * 同步 
>   Disptcher会在同步执行任务队列中基类当前被执行过的任务Call，同时在当前线程中去执行Call的getResponseWithInterceptorChain()方法，直接获取当前返回数据Response;
> * 异步
	Dispatcher内部实现了懒加载无边界限制的线程池方式，同时该线程池采用了SynchronousQueue这中阻塞队列。SynchronousQueue每个插入操作必须等待另一个线程的移除操作,同样任何一个移除操作等等待另一个线程的插入操作。因此此队列内部其实没有任何一个元素，或者说容量是0，严格说并不是一种容器。由于队列没有容量，因此不能调用peek操作，因此只有移除元素时才有元素。显然这是一种快速传递元素的方式，也就是说在这种情况下元素总是以最快的方式从插入者（生产者）传递给移除者（消费者），这在多任务队列中是最快处理任务的方式。对于高频繁请求的场景，无疑是最适合的。
> 异步执行是通过Call.equeue来执行，在Dispatcher中添加一个封装了Callback的Call的匿名内部类Runnable来执行当前的Call。这里的AsyncCall是Call的匿名内部类。AsyncCall的execute方法仍然会回调到Call的getResponseWithInterceptorChain方法来完成请求，同时将返回数据或者状态通过Callback来完成。
> 