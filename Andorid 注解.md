Android 应用开发中对注解的使用达到了淋漓尽致的地步，无论是运行时注解，还是编译时注解，亦或是标准注解，都得到了广泛的使用。Support Annotation,各种开源函数库中也使用了注解，如Retrofit使用运行时注解，Dagger2使用编译时注解等。
## 标准注解
Java API中默认定义的注解我们撤职为标准注解。它们定义在java.lang、java.lang.annotation和javax.annotation包中，分为三种使用场景：
1. 编译相关注解——是给编译器使用的 
	* @Override:编译器会检查被注解的方法是否真的重载了一个来自父类的方法，如果没有，编译器会给出错误提示。
	* @Deprecated:修改不被鼓励使用或已被弃用的属性、方法等。
	* @SuppressWarnings:用于包之外的其他声明项中，用来抑制某种类型的警告。
	* @SafeVarargs:用于方法和构造函数，用来断言不定长参数可以安全使用。
	* @Generated:一帮是给代码生成工具使用，用来表示这段代码不是开发者手动编写的。而是工具生成的。
	* @FunctionalInterface:用来修饰接口，表示对应的接口是带单个方法的函数式接口。
2. 资源相关注解
	* @PostConstruct
	* @PreDestroy
	* @Resource
	* @Resources
3. 元注解——用来定义和实现注解的注解
	* @Target：取值是一个ElementType类型的数组，用来制定注解所适用的对象范围。同时支持定义多种类型的注解。如果一个注解没有使用@Target修饰，那么它可以用在除了TYPE_USE和TYPE_PARAMEYER之外的其他类型声明中。
	* @Retention:用来指明注解的访问范围，即在什么级别保留注解：
		* 源码级注解：在定义注解时，使用@Retention(RetentionPolicy.SOURCE)修饰的注解，该类型的注解信息只会保留在.java源码里，经过编译后，注解信息会被丢弃，不会保留在编译好的.class文件中。
		* 编译时注解：在定义注解接口时，使用@Retention(RetentionPolicy.CLASS)修饰的注解，该注解会保留在.java和.Class文件里，执行的时候会被Java虚拟机丢弃，不会加载到虚拟机中。
		* 运行时注解：在定义时，使用@Retention(RetentionPolicy.RUNTIME)修饰的注解，Java虚拟机在运行期也保留注解信息，可以通过反射机制读取注解的信息
		未指定类型时，默认为CLASS类型。
	* Documented:表示被修饰的注解应该被包含在被注解项的文档中。
	* @InHerited:表示该注解可以被子类继承
	* @Repeatable:表示可以在同一项上面应用多次。 