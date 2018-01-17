经典的Builder模式主要有四个参与者：
* Product：被构造的复杂对象，ConcreteBuilder用来床架该对象的内部便是，并定义它的装配过程。
* Builder：抽象接口，用来定义创建Product对象的各个组成部件的操作。
* ConcreteBuilder:Builder接口的具体实现，可以定义多个，是时机构建Product对象的地方，同时会提供一个返回Product的接口。
* Director: Builder接口的构造者和使用者。
## Builder模式的变种
经典的Builder模式重点在于抽象出对象创建的步骤，并通过调用不同的具体实现从而得到不同的结果，而变种Builder模式的目的在于减少对象创建过程中引入的多个重载构造函数、可选参数以及setters过渡使用导致的不必要的复杂性。如一个对象中有可选参数和必选参数，
* 我们将必选参数属性值声明为final，因此必须在构造函数中对这些属性进行赋值，否则编译不通过。
* 由于属性值有必选和可选之分，也就是说构造函数需要提供可以选择忽略可选参数的方式。
#### 变种Builder模式，改造User类后：
* User类的构造函数是私有的，者意味着调用者不能直接实例化这个类。
* User类是不可变得，所有必选的属性值都是final的并且在构造函数中设置；同时对属性值只提供getter函数。
* UserBuilder使用Fluent Interface惯用法，使得类的使用者代码可读性更佳。
* UserBuilder的构造函数只接收必选的属性值作为参数，并且只有必选的属性值设置为final，以此保证他们在构造函数中设置。
同时，Builder模式可以自动化生成，在Android Studio中，可以通过安装InnerBuilder的插件来简化Builder模式的创建过程。
很多开源函数库都是用了Builder模式，如AlertDialog,图片函数库Glid、Picasso、ImageLoader,网络请求框架OkHttp的请求封装。