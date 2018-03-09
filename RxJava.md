## Rxjava ----a library for composing asynchronous and event-basedprograms using observable sequences for the Java VM即一个在Java VM上使用可观测的序列来组成异步的，基于时间的程序的库。
Rxjava 的优点：简洁，因为在调度过程比较复杂的情况下，异步代码经常会既难写也难被读懂。即随着程序逻辑变得越来越复杂，它依然能够保持简洁。

`` 

	Observable.from(folden)
	.flatMap(new Fun1<file,Observable<File>>(){
		@Override
		public Observable<File>call(File file){
			return Observable.from(file.listFiles());
		}
	})
	.filter(new Fun1<File,Boolean>(){
		@Override
		public Boolean call(File file) {
			return file.getName().endWith(".png")
		}
	})
	.map(new Fun1<File, Bitmap>() {
		@Override
		public Bitmap call(File file) {
			return getBitmapFromFile(file);
		}
	})
	.subsribeOn(Schedulers.io())
	.observerOn(AndroidSchedulers.mainThread())
	.subsribe(new Action1<Bitmap>(){
		@Override
		public void call(Bitmap bitmap) {
			imageCollectorView.addImage(bitmap);
		}
	})

API介绍和原理简析
1. 概念：扩展的观察者模式
RxJava的异步实现，是通过一种扩展的观察者模式来实现的。
观察者模式通常的需求是：A对象（观察者）对B对象（被观察者）的某种变化高度敏感，需要在B变化的一瞬间做出反应。程序的观察者模式和这种真正的
【观察】略有不同，观察者不需要时刻盯着被观察者，而是采用注册或称为Subsribe的方式
Android开发中一个比较典型的例子，就是点击监听器OnClickListener。对于设置OnClickListener来说，View是被观察者，OnClickListener是观察者，二者通过setOnClickListener()方法达成订阅关系。订阅之后用户点击按钮的瞬间，Android Framework就会将点击事件发送给已经注册的OnclickListener。采取这种被动的观察方式，既省去了反复检索状态的资源消耗，也能够得到最高的反馈速度。
而RxJava最为一个工具库，使用的就是通用形式的观察者模式。
## RxJava的观察者模式
RxJava有四个基本概念：Observable(可观察者，即被观察者)、Observer(观察者)、subscribe(订阅)、事件。Observable和Observer通过subsribe(订阅)方法实现订阅关系，从而Observerable可以在需要的时候发出事件来通知Observer

> 与传统观察者模式不同，RxJava的事件回调方法除了普通事件onNext()(相当于onClick()/onEvent())之外，还定义了两个特殊的事件：onCompleted()和onError()
* onCompleted():事件队列完结，RxJava不仅把每个事件单独处理，还会把它们看做一个队列。RxJava规定，当不会再有新的onNext()发出时，需要出发onCompleted()方法作为标志。
* onError():事件队列异常。在事件处理过程中出异常时，onError()会被触发，同时队列自动终止，不允许再有事件发出。
* 在一个正确运行的时间序列中，onCompleted()和onError()有且只有一个，并且是时间序列中的最后一个。

#### 基本实现
RxJava的基本实现主要有三点：

1. 创建Observer
Observer即观察者，它决定时间出发的时候将有怎样的行为。实现方式如下：
``

	Observer<String> observer = new Observer<String>(){
		@Override
		public void onNext(String s) {
			Log.d(tag,"Item: " + s);
		}
		
		@Override
		public void onCompleted() {
			Log.d(tag,"Completed!");
		}

		@Override
		public void onError() {
			Log.d(tag,"Error!");
        }
	}


除此之外，RxJava还内置一个实现了Observer的抽象类：Subscriber。Subscriber对Observer接口进行了一些扩展，但他们的基本使用方式是完全一样的：
``

	Subscriber<String> subscriber = new Subscriber<String>(){
		@Override
		public void onNext(String s) {
			Log.d(tag,"Item: " + s)
		}
		
		@Override
		public void onCompleted() {
			Log.d(tag,"Completed!")
		}

		@Override
		public void onError(Throwalbe e) {
			Log.d(tag,"Error!")
		}		
	}


实际上在RxJava的subscribe过程中，Observer也总是会仙贝转换成一个Subscriber再使用。它们的区别主要有两点：

1. onstart(): 这是Subscriber增加的方法。它会在subscribe刚开始，而事件还未发送之前被调用，可以用于做一些准备工作，例如数据的清零或重置。默认实现为空。当然如果准备工作队员线程有要求，onStart()就不适用了，因为它总是在subsribe所发生的线程被调用，而不能指定线程，要在指定的线程来做准备工作，可以使用doOnSubscribe()方法。
2. unsubsribe():这是Subscriber所实现的另一个接口Subscription的方法，用于取消订阅。调用之后,Subscriber将不再接受事件，通常和isUnsubsribed()一起使用，调用unsubsribe()来解除引用关系，以避免内存泄露的发生。


###### 创建Observerable
Observerable即被观察者，它决定什么时候触发，以及触发怎样的事件。
``

	Observerable observable = Observable.create(new Observalbe.OnSubscribe<String>() {
		@Override
		public void call(Subscriber<? super String> subscriber) {
			subscriber.onNext("Hello");
			subscriber.onNext("Hi");
			subscriber.onNext("Alex");
			subscriber.onCompleted();	

		}
	}) 

这里传入一个OnSubscribe对象作为参数。OnSubscribe会被存储在返回的Observable对象中，它的作用相当于一个计划表，当Observable被订阅的时候，OnSubscribe的call()方法会自动被调用，事件序列就会依照设定一次触发。这样由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式。
create()方法是RxJava最基本的创造事件序列的方法，同时基于这个方法，RxJava还提供了一些方法用来快捷创建事件队列，如：

* just(T...)将传入的参数依次发送出来。
``

	Observable observalbe = Observalbe.just("Hello","Hi","Alex");
    //将会依次调用：
	//onNext("Hello")
	//onNext("Hi")
	//onNext("Alex")
	//onCompleted()

* from(T[])/from(Iterable<? extends T>):将传入的数组或Iterable拆分成具体对象后，依次发送出来。
`` 

	String[] words = {"Hello","Hi","Alex"};
	Observable observable = Observable.from(wrode);

##### Subscribe(订阅)
创建了Observable和Observer之后，再调用subscribe（）方法将它们联系起来，整条链子就可以工作了，代码如下：
``

	observalbe.subscribe(observer);
	//或者:
	observable.subscribe(subscriber);

Observable.subscribe(Subscriber)的内部实现是这样的（核心代码）：
``

	public Subscription subscribe(Subscriber subscriber) {
		subscriber.onStart();
		onSubscribe.call(subscriber);
		return subscriber;
	}

可见subscriber()做了三件事：

1. 调用Subscriber.onStart()。
2. 调用调用Observable中的OnSubscribe.call(Subscriber)。在这里，事件发送的逻辑开始运行。从而可见在RxJava中，Observable并不是在创建的时候就立即开始发送事件，而是在它被订阅的时候，即当subscribe（）方法执行的时候。 
3. 将传入的Subscriber作为subscription返回。这是为了方便unsubscribe();

除了subscribe(Observer)和subscribe(Subscriber),subscribe()还支持不完整定义的回调，RxJava会自动根据定义创建出Subscriber.
``

	Action1<String> onNextAction  = new Action1<String>() {
		//onNext()
		@Override
		public void call(String s) {
			Log.d(tag,s);
		}	
	};
	Action1<Throwable> onErrorAction = new Action1<Throwable>() {
		//onError
		@Override
		public void call(Throwable throwable){
			//Error handling
		}

	};
	Action0 onCompletedAction  = new Action0() {
		//onCompleted()
		@override
		public void call() {
			Log.d(tag,"completed");
		}
	}
	observable.subscribe(onNextAction);
	observalbe.subscribe(onNextActiion,onErrorAction);
	obServable.subscribe(onNextAction,onErrorAction,onCompleteAction);

其中Action1和Action0.Action0是RxJava的一个借口，它只有一个方法call()。这个方法是无参无返回值的，由于，onCompleted()方法也是无参无返回值的，因此Action0可以被当成一个包装对象，将onComplete()的内容打包起来将自己作为一个参数传入subscribe()以实现不完整定义的回调。Action1同理表示单参无返回值的包装方法。

虽然Action0和Action1在API中使用最广泛，但RxJava是提供了多个ActionX形式的接口（例如Action2,Action3），它们可以被用以包装不同的无返回值的方法。

### 举两个例子
* 打印字符串数组
将字符串数组names中的所有字符串一次打印出来：

``

	String[] names = ...;
	Observable.from(name)
	.subscribe(new Action1<String>() {
		@Override
		public void call(String name) {
			Log.d(tag,name);
		}
	})

* 由id取得图片并显示
由指定的一个drawable文件id，drawableRes取得图片，并显示在ImageView中，并在出现异常的时候打印Toast报错：

``

	int drawableRes = ..;
	ImageView imageView = ..;
	Observable.create(new OnSubscribe<Drawable>() {
		@Override
		public void (Subscribe<? super Drawable> subscriber) {
			Drawable drawable =  getTheme().getDrawable(drawableRes);
			subscriber.onNext(drawable);
			subscriber.onCompleted();
		}
	}).subscribe(new Observer<Drawable>() {
		@Override
		public void onNext(Drawable drawable){
			imageView.setImageDrawable(drawable);
		}
	
		@Override
		public void onCompleted(){
			
		}

		@Override
		public void onError(Throwable e) {
			Toast.makeText(context,"Error!",Toast.LENGTH_SHORT).show();
		}
	})


