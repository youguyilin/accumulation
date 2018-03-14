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

正如上面的两个例子，创建出Observer和Subscriber，再用subscribe()将它们串起来，一次RxJava的基本使用就完成了。然而，在RxJava的默认规则中，事件的发出和消费都是在同一个线程的。所以使用上面的方法，实现出来的只是一个同步的观察者模式。观察者模式本身的目的就是【后台处理，前台回调】的异步机制，因此异步对于RxJava是至关重要的，如果要实现异步，就需要用到RxJava的另一个概念：Scheduler。

## 线程控制--Scheduler
在不指定线程的情况下，RxJava遵循的是线程不变的原则，即：在哪个线程调用subscribe(),就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到Scheduler(调度器)
1. Scheduler的API
在RxJava中，Scheduler——调度器，相当于线程控制器，RxJava通过它来指定每一段代码应该运行在什么样的线程。RxJava已经内置了几个Scheduler:

	* Schedulers.immediate():直接在当前线程运行，相当于不指定线程。这是默认的Scheduler.
	* Schedulers.newThread():总是启用新线程，并在新线程执行操作。
	* Schedulers.io():I/O操作（读写文件、读写数据库、网络信息交互等）所适用的Scheduler.行为模式和newThread()差不多，区别在于io()的内部实现是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下io（）比newThread()更有效率。不要把计算工作放在io()中，可以避免	创建不必要的线程。 
	* Schedulers.computation():计算所适用的Scheduler。这个计算指的是CPU密集型计算，即不会被I/O等操作限制性能能的操作，例如图形的计算。这个Scheduler使用固定的线程池，大小为CPU核数。不要把I/O操作放在computation()中，否则I/O操作的等待时间会浪费CPU。
	* 另外，Android还有一个专用的AndroidSchedulers.mainThread(),它指定的操作将在Android主线程运行。
	

有了这几个Scheduler,就可以使用subscribeOn()和observeOn()两个方法来对线程进行控制了。subscribeOn():指定subscribe()所发生的线程，即Observable.OnSubscribe被激活时所处的线程。或者叫做事件产生的线程。observeOn():指定Subscriber所运行在的线程。或者叫做事件消费的线程。

## 变换
RxJava提供了对事件序列进行变换的支持，这是它的核心功能之一，也是其好用的原因。**所谓的变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列。**

* map():

``

	Observable.just("image/logo.png")//输入类型 String
	.map(new Func1<String, Bitmap>() {
		@Override
		public Bitmap call(String filePath) {
			return getBitmapFromPath(filePath);
		}
	})
	.subscribe(new Action1<Bitmap>() {
		@Override
		public void call(Bitmap bitmap){
			showBitmap(bitmap);
		}
	});

这里的Func1的类，类似于Action1，也是RxJava的一个接口，用于包装含有一个参数的方法。主要区别在于，Func1包装的是有返回值的方法。
可以看到，map()方法，将参数中的String对象转换成一个Bitmap对象后返回，而在经过map()方法后，事件的参数类型也由String转为了Bitmap。这种直接变换对象并返回的，是最常见和最容易理解的变换。
* flatMap():这是一个很有用但非常难理解的变换，首先假设有这么一种需求：假设有一个数据结构【学生】，现在需要打印出一组学生的名字，通常实现方式如下：

``

	Student[] students = ...;
	Subscriber<String> subscriber = new Subscriber<String>() {
		@Override
		public void onNext(String name) {
			Log.d(tag, name);
		}
		...
	};
	Observable.fom(students)
		.map(new Func1<Student, String>() {
			@Override
			public String call(Student student) {
				return student.getName();
			}
		})
		.subscribe(subscriber);

加入还需要再打印出每个学生所修的所有课程的名称呢？（涉及到一对多）,通常可以利用遍历实现：

``

	Studeng[] stidents = ...;
	Subscriber<Student> subscriber = new Subscriber<Student>() {
		@Override
		public void onNext(Student student) {
			List<Course> courses = student.getCourses();
			for(int i = 0;i < courses.size();i++) {
				Course course = courses.get(i);
				Log.d(tag,course.getName());
			}
		}
		...
	};
	Observalble.from(students)
		.subsribe(subscriber);

这个时候就使用到flatMap()了:

``

	Student[] students = ...;
	Subscriber<Course> subscriber =  new Subscriber<Course>(){
		@Override
		public void onNext(Course course) {
			log.d(tag,course.getName());
		}
		...
	};
	Observable.from(students)
		.flatMap(new Func1<Student, Observable<Course>>() {
			@Override
			public Observable<Course> call(Student student) {
				return Observable.from(student.getCourses());
			}
		})
		.subscribe(subscriber);


flatMap()和map()有一个相同点：它是把传入的参数转化之后返回另一个对象。但需要注意的是，和map()不同的是，flatMap()返回的是个Observable对象，并且这个对象并不是被直接发送到了Subscriber的回调方法中。flatMap（）的原理是这样的：1.使用出入的事件对象创建一个Observable对象；2.并不发送这个Observable,而是将它激活，于是它开始发送事件；3.每一个创建出来的Observable发送的事件，都被汇入同一个Observable,而这个Observable负责将这些事件统一交给Subscriber的回调方法。这三个步骤，把事件拆成了两级，通过一组新创建的Observable将初始的对象【铺平】之后通过统一路径发了下去。

扩展：由于可以在嵌套的Observable中添加异步代码，flatMap()也常用于嵌套的异步操作，例如嵌套的网络请求。（Retrofit + RxJava）:

``

	// 返回Observable<String>,在订阅时请求token，并在响应后发送token
	networkClient.token()
		.flatMap(new Func1<String, Observable<Message>>() {
			//返回Observable<Messages>，在订阅的时候请求token，并在响应后发送请求到的消息列表
			return networkClient.message();
			
		})
		.subscribe(new Action1<Messages>() {
			@Override
			public void call(Messages message) {
				//处理显示消息列表
				showMessages(messages);
			}
		})

传统的嵌套请求需要使用嵌套的Callback来实现。而通过flatMap(),可以把嵌套的请求写在一条链中，从而保持程序逻辑的清晰。

* throttleFirst():在每次时间出发后的一定时间间隔内丢弃新的事件，常用作去抖动过滤，例如按钮的点击监听器：
RxView.clickEvents(button)//RxBinding代码

## 变换的原理：lift()
这些变换虽然功能各有不同，单实质上都是**针对事件序列的处理和再发送**。在RxJava的内部，它们都是基于同一个基础的变换方法：
lift(Operator)。首先看lift()的内部实现：

``

	// 注意：这不是lift（）源码，而是将源码中与性能、兼容性、扩展性有关的代码剔除后的核心代码。
	public <R> Observable<R> lift(Operator<? extends R, ? super T> operator) {
		return Observable.create(new OnSubscribe<R>() {
			@Override
			public void call(Subscriber subscriber) {
			Subscriber new Subscriber = operator.call(subscriber);
			newSubscriber.onStart();
			onSubscribe.call(newSubscriber);
		}
		});  	
	}

这段代码很有意思：它生成了一个新的Observable并返回，而且创建新Observable所用的参数OnSubscribe的回调方法call()中的实现竟然看起来和前面讲过的Observable.subscribe()一样，然而它们并不一样，在于第二行onSubscribe.call(subscriber)中的onSubscribe所指代的对象不同：

* subscribe()中这句话的onSubscribe指的是Observable中的onSubscribe对象，这个没问题，但是lift()之后的情况就复杂了。
* 当含有lift()时：
	1. lift()创建了一个Observable后，加上之前的原始Observable,已经有两个Observable;
	2. 而同样的，新Observable里的新OnSubscribe加上之前的原始Observable中的OnSubscribe,也就有两个OnSubscribe;
	3. 当用户调用经过lift（）后的Observable的subscribe（）的时候，使用的是lift（）所返回的新的Observable,于是它所触发的onSubscribe.call(subscriber),于是它所触发的OnSubscribe.call(subscriber),也是用的新Observable中的新OnSubscribe,即在lift（）中生成的那个OnSubscribe;
	4. 而这个新OnSubscribe的call()方法中的onSubscribe,就是指的原始Observable中的原始OnSubscribe,在这个call()方法里，新OnSubscribe利用oprator.call(subscriber)生成了一个新的Subscriber()[operator 就是在这里，通过自己的call()方法将新Subscriber和原始Subscriber进行关联，并插入自己的【变换】代码以实现变换]，然后利用这个新Subscriber向原始Observable进行订阅
	5. 这样实现了lift()过程，有点像**一种代理机制，通过事件拦截和处理实现事件序列的变换**

也可以说：在Observble执行了lift(Operator)方法之后，会返回一个新的Observable，，这个新的Observable会像一个代理一样，负责接收原始的Observable发出的事件，并在处理后发送给Subscriber。

举一个Operator的实现，下面这是一个将事件中的Integer对象转换为String的例子。
``

	observable.lift(new Observable.Operator<String,Integer>() {
		@Override
		public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
			//将事件序列中的Integer对象转换为String对象
			return new Subscriber<Integer>() {
				@Override
				public void onNext(Integer integer) {
					subscribe.onNext("" + integer);
				}
			    @Override
				public void onCompleted() {
					subscriber.onCompleted();
				}
				@Override
				public void onError(Throwable e) {
					subscriber.onError(e);
				}
			};
		}
	});

## compose:对Observable整体的变换
除了lift()之外，Observable还有一个变换方法叫做compose(Transformer)。它和lift（）的区别在于，lift()是针对事件项和事件序列的，而compose()是针对Observable自身进行变换。	