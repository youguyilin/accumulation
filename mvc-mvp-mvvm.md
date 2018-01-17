### MVP MVC MVVM
#### MVC
**数据关系**
* View接收用户交互请求
* View将请求转交给Controller
* Controller操作Model进行数据更新
* 数据更新之后，Model通知View更新数据变化
* View更新变化数据
#### 方式
所有的数据都是单向通信

#### 使用时
MVC中的View是可以直接访问model的！因此，View里面会包含Model的信息，不可避免也会包含一些业务逻辑。在MVC模型里面，关注的是MODEL的不变，二同事有多个对Model的不同显示，包括View。所以，在MVC模型里，Model不依赖View，但是View依赖Model的。同时，因为View里面也包含一些业务逻辑，导致View的更改也是比较困难的，至少业务逻辑没法重用

#### MVP
>mvp 与MVC一个重大的区别是:在mvp中View并不直接使用Model，他们之间的通信是通过Presenter来进行的，所有的交互都发生在Presenter的内部，而在MVC中View会直接从Model中独居数据而不是通过Controller
#### 数据关系
1. Model与View完全解耦，修改互不影响
2. 更高效地使用，因为所有的逻辑交互都发生在Presenter内部
3. 一个Presenter可用于多个View，而不需要改变Presenter的逻辑（因为View的变化总是比Model的变化频繁）。
4. 更便于测试，把逻辑放在Presenter中，就可以脱离用户接口来测试逻辑（单元测试）
#### 方式
各部分之间都是双向通信
#### MVC和MVP的关系
项目开发过程中，UI是容易变化的，并且是多样的，一样的数据会有N种显示方式；业务逻辑也是比较容易变化的。为了使得应用具有较大的弹性，我们希望将UI、逻辑（UI的逻辑和业务逻辑）和数据隔离开来，而MVP是一个很好的选择。Presenter代替了Controller，比Controller担当更多的任务，也更加复杂。Presenter处理事件，执行相应的逻辑，这些逻辑映射到Model操作Model，那些处理UI如何工作的代码基本上都位于Presenter。MVC中的Model使用Observer模式进行沟通；MVP中的Presenter和View则使用Mediator模式进行通信；Presenter操作Model则使用Command模式来进行。

#### 基本设计
Modle存储数据，View对Model的表现，Presenter协调两者之间的通信。在MVP中View接收到事件，交给Presenter来处理这些事件。如果要实现的UI比较复杂，而且相关的显示逻辑还跟Model有关系，就可以在View和Presenter之间放置一个Adapter。由这个Adapter来访问Model和View，避免两者之间的关联，而同时，因为Adapter实现了View的接口，从而可以保证与Presenter之间的接口的不变。同时可以保证View和Presenter之间接口的简洁，又不失去UI的灵活性。
#### 使用
MVP的实现会根据View的实现而有一些不同，一部分倾向于在View中放置简单的逻辑。在Presenter放置复杂的逻辑；另一部分倾向于在Presenter中放置全部的逻辑。这两种分别被称为：Passive View 和 Superivising Controller
#### MVVM
MVVP是Modle-View-ViewModle的简写。微软的WPF带来了新的技术体验，如Silverlight、音频、视频、3D、动画......，这导致了软件UI层更加细节化、可定制化。同时，在技术层面，WPF也带来了诸如Bingding、Dependency Property、Routed Events、Command、DataTemplate、ControlTemplate等新特性。MVVM框架的由来便是MVP模式与WPF结合的应用方式时发展演变过来的一种新型架构框架。它立足于原有的MVP框架并且把WPF的新特性糅合进去，以应对客户端呢日益复杂的需求变化。
#### 数据关系
* View接收用户交互请求
* View将请求转交给ViewModel
* ViewModel操作Model数据更新
* Model更新完数据，通知ViewModel数据发生变化
* ViewModel更新View数据
#### 方式
双向绑定，View/modle的变动，自动反映在ViewModel，反之亦然
#### 使用
- 可以兼容当下使用的MVC/MVP框架
- 增加应用的可测试性
- 配合一个绑定机制
#### MVVM
MVVM 模式 和MVC模式一样，主要目的是分离试图（View）和模型（model），有几大优点：
1. 低耦合。View可以独立于Model变化和修改，一个ViewModel可以绑定到不同的View上，当View变化的时候Model可以不变，当Model变化的时候View也可以不变。
2. 可复用性。你可以把一些试图逻辑放在一个ViewModel里面，让很多的View重用这段视图逻辑
3. 独立开发。开发人鱼可以专注于业务逻辑和数据开发，设计人员可以专注于页面设计，生成xml代码
4. 可测试。界面速来是比较难以测试的，而现在测试可以正对ViewModel来写