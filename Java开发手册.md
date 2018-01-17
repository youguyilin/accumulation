## Java 开发手册
* 类成员与方法访问控制从严
任何类、方法、参数、变量，严控访问范围。过于宽泛的访问范围，不利于模块解耦。
>思考：如果是一个private的方法，想删除就删除，可是一个public的service成员方法或者成员变量，变更、删除时就要考虑是否有其他影响，变量像是自己的小孩，尽量在自己的视线内，变量作用域太大，维护起来就会很困难。
### 集合处理
* 关于hashCode和equals的处理，遵循如下规则：
	1. 只要重写了equals，就必须重写hashCode
	2. 因为Set存储的是不重复的对象，依据hasCode和equals进行判断，所以Set存储的对象必须重写这两个方法
	3. 如果自定义对象作为Map的键，就必须重写hashCode和equals方法，所以我们可以使用String作为key来使用
* ArrayList的subList结果不可墙砖成ArrayList，否则会抛出ClassCastException异常，即java.utill.RandomAccessSubList cannot be cast to java.util.ArraryList;
> 说明：subList返回的是ArrayList的内部类SubList，并不是ArrayList，而是ArrayList的一个试图,对于SubList字列表的所有操作最终会反映到原列表上
* 在subList场景中，高度注意对原集合元素个数的修改，会导致字列表的遍历、增加、删除均会产生ConcurrentModificationException异常
* 使用集合转数组的方法，必须使用集合的toArray(T[] array),传入的是类型完全相同的数组，长度即为list.size();
* 使用工具类Arrays.asList()把数组转换为集合时，不能使用其修改集合相关的方法，它的add/remove/clear方法会抛出UnsupportedOperationException异常
> 说明：asList的返回对象是一个Arrays内部类，并没由实现集合的修改方法。Arrays.asList提现的是适配器模式，只是转换接口，后台的数据仍是数组。
```
	String[] str = new String[]{"you","me"};
	List list = Arrays.asList(str);
第一种情况： list.add("him");运行时异常
第二种情况： str[0] = "she";那么list.get(0)也会随之修改
``` 
* 泛型通配符<? extends T>（上界通配符）来接收返回的数据，此写法泛型集合不能使用add方法，而<? super T>不能使用get方法，作为接口调用赋值时易出现错误。
> 扩展说一下PECS(Producer Extends Consumer Super)原则：
	1. 频繁往外读取内容的，适合用<? extends T>
	2. 经常往里插入的，适合用<? super T>
* 不要在foreach循环里进行元素的remove/add操作，remove元素使用Iterator方式，如果考虑并发操作，需要对Iterator对象加锁。
* 在JDK7版本及以上，Comparator要满足如下三个条件，不然Arrays.sort,Collections.sort会报IllegalArgumentException异常
	1. x,y的比较结果和y，x的比较结果相反
	2. x>y,y>z,则x>z
	3. x = y,则x,z比较结果和y,z比较结果相同