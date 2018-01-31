Dagger2是一个依赖注入框架，是面向对象变成的一种设计模式其目的是降低程序耦合，符合**单一职责原则**，和**开闭原则**，依赖注入有以下几种方式：
* 通过接口注入
* 通过set方法注入
* 通过构造方法注入
* 通过Java注解（Dagger2的实现方式）
引入Dagger2需要配置Android-apt(android annotation process tool)处理注释的工具。
#### Dagger2中的几大注解类
1. Inject **我们可以使用该注解来标注目标类中所依赖的其他类，同样用注解来注解依赖类的构造函数**——这样就可以让目标类中所依赖的其他类与其他类的构造函数之间有了联系。但如果要直接产生关系，还需要一个桥梁来把他们之间连接起来。该桥梁就是Component。
2. Component是一个注解类，使用@Component注解来标注该类，并且该列是接口或抽象类。
	工作方式：Component需要引用到目标类的实例，Component会查找目标类中用Inject注解标注的属性，查找到相应的属性后悔直接查找该属性的实例并把实例进行赋值。因此我们可以给Component叫另外一个名字**注入器**
## 小结：
* 用Inject注解标注目标类中其他类
* 用Inject注解标注其他类的构造函数
* 若其他类还依赖于其他类，则重复进行上面2个步骤
* 调用Component(注入器)的injectXXX(Object)方法开始注入
## Module
项目中使用到了第三方的类库，第三方类库又不能修改，所以根本不可能把Inject注解加入这些类中，这是Inject就失效了。
这是我们可以通过疯长第三方的类库，此时可用Module来管理这些类的封装，**Module其实是一个简单工厂模式，Module里面的方法基本是创建类实例的方法。**
## Component新职责
Component是注入器，它一端连接目标类，另一端连接目标类依赖实例，它把目标类依赖实例注入到目标类中，Module是一个提供类实例的类，所以Module应该是属于Component的实例端的（连接各种目标类依赖实例的端），Compnent的新职责就是管理好Module,Component中的moudules属性可以把Module加入Component,并且可以加入多个Module.
## Provides最终解决第三方类库依赖注入问题
Module中的创建类实例方法用Provides进行标注，Component在搜索到目标类中用Inject注解标注的属性后，Component就会去Module中去查找用Provides标注的对应的创建类实例方法。
Inject、Component、Module、Porvides是Dagger2中最基础最核心的知识点，奠定了dagger2的整个**依赖注入框架**
* Inject 主要是用来标注目标类的依赖和依赖的构造函数
* Component它是一个桥梁，一端是目标类，另一端是目标类所依赖的实例，它也是注入器（Injector）负责把目标类所依赖的实例注入到目标类中，同时它也管理Module
* Module和Provides是为解决第三方类库而生的，Module是一个**简单工厂模式**——包含创建类实例的方法，这些方法用Provides来标注。
## Qualifier（限定符)\Singleton（单例）\Scope（作用域）\SubComponent
创建类实例有2个维度可以创建：
* 通过用Inject注解标注的构造函数来创建（Inject维度）
* 通过**工厂模式**的Module来创建（Module维度）
这两个维度是有优先级之分的，Component会首先从Module维度中查找类实例，若找到就用Module维度创建类实例，并停止查找Inject维度，其次才会从Inject维度查找类实例。即Module维度优先级高于Inject维度
那么会存在，**基于同一个维度条件下，若一个类的实例有多重方法可以创建出来，那么注入器（Component）应该选择哪种方法来创建该类的实例？**这种问题叫做**依赖注入迷失**那么可以给不同的创建类实例的方法用**标识**进行标注，用**标识**就可以对不同的创建类实例的方法进行区分。同时用**要使用的创建类实例方法的标识**对**目标类相应的实例属性**进行标注。这样我们的问题就解决了，提到的标识就是Qualifier注解，当然这种注解需要我们自定义。
Qualifier（限定符）就是解决**依赖注入迷失**问题的。
dagger2在发现**依赖注入迷失**时在编译代码时会报错。
## Scope(作用域)
> Dagger2可以通过自定义Scope注解，来限定**通过Module和Inject方式**创建的类的实例的声明周期能够与目标类的生命周期相同。
## 一个APP钟应该根据什么来划分Component?
* 要有一个全局的Component(可以叫ApplicationComponent),负责管理整个app的全局类实例
* 每个页面对应一个Component,比如一个Activity页面定义一个Component,有些页面之间的依赖的类是一样的，可以用一个Component。
第一个规则很好理解，至于为什么以页面为粒度来划分Component?
* 一个app是由多个页面组成的，从组成app的角度来看一个页面就是一个完整的最小粒度了。
* 一个页面的实现其实是要依赖各种类的，可以理解成一个页面把各种依赖的类组织起来共同实现一个大的功能，每个页面都组织着自己需要依赖的类，一个页面就是一个堆类的组织者。
* **划分粒度不能太小了**如果使用mvp，划分粒度是基于每个页面的m、v、p各自定义Component的，那Component的粒度就太小了，定义了太多的Component，**管理、维护就非常困难**
## Singleton没有创建单例的能力
ApplicationComponent负责管理整个app用到的全局类实例，那不可否认的是这些全局类实例应该都是单例的，那我们怎么才能创建单例呢?
> Module和Provides是为解决第三方类库而生的，Module是一个**简单工厂模式**，Module可以包含创建类实例的方法。
现在Module可以创建**所有类**的实例
> Component会首先从Module维度中查找类实例，若找到就用Module维度，其次才会从Inject维度查找类实例。所以创建类实例级别Module维度要高于Inject维度。
利用以上两点，我们可以定义创建单例：
* 在Module中定义创建全局类实例的方法。
* ApplicationComponent管理Module
* 保证ApplicationComponent只有一个实例
## Singleton的作用：
* 更好的管理ApplicationComponent和Module之间的关系，保证Application和Module是匹配的，若ApplicationComponent和Module的Scope是不一样的，则在编译时报错。
* 代码的可读性
## 组织Component
问题来了，其他Component向要把全局的类实例注入到目标类中该怎么办呢？这就涉及到**类共享实例**的问题，因为Component有管理创建类实例的能力，因此需要很好的组织Component之间的关系：
1. **依赖方式**
一个Component是依赖于一个或多个Component,Component中的dependencies属性就是依赖方式的具体体现
2. **包含方式**
一个Component是包含一个或多个Component的，SubComponent就是包含方式的具体体现。
3. **继承方式**
### Scope真正用武的时候
就在于Component的组织
* 更好的管理Component之间的组织方式，不管是**依赖方式**还是**包含方式**，都有必要用自定义的Scope注解标注这些Component,不一样是为了能更好的体现出Component之间的组织方式。
* 更好的管理Component与Module之间的匹配关系，编译器会检查Component管理的Modules,若发现标注Component的自定义Scope注解与Modules中的标注创建类实例方法的注解不一样，就会报错。
* 可读性提高。