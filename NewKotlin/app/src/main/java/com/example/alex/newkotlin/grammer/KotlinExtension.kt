package com.example.alex.newkotlin.grammer

import com.example.alex.newkotlin.operational.OperationalCharacter

/**
 * Created by Alex on 2018/3/5.
 */
class KotlinExtension{
    //kotlin中所有类都隐式继承自Any类，它是所有类的超类，对于没有超类型声明的类是默认超类
    //Any默认提供了三个函数

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    //继承符号 （：）无需其他关键字
    //Any不是java.lang.Object,
    //如果一个类要被继承，使用open关键字进行修饰
    //如果子类有主构造函数，则基类必须在主构造函数中立即初始化
    //如果子类没有主构造函数，则子类必须在一个二级函数中使用super关键字初始化基类，或者在代理另一个构造函数
    //初始化基类时可以调用基类不同的构造方法

    /********************************* Kotlin 重写 ****************************************/
    //在基类中，fun函数默认默认为final修饰，不能被子类重写，如果允许子类重写该函数，那么就要使用open关键字来
    //修饰它，子类重写使用override关键词
    //如果在父类或接口中都有同一个方法，那么需要使用super关键字选择性的调用父类的实现。

    interface B{
        fun f(){ println("B")} //接口中可以有实现，子类继承时使用override关键字
    }

    open class A{
       open fun f(){
            println("A")}
    }

    class C:A(),B{
        override  fun f() {
            super<A>.f()
//            super<B>.f()
        }
    }
    /********************************* Kotlin 属性重写 ****************************************/
    //属性重写使用override关键字，属性必须具有兼容类型，每一个声明都可以通过初始化代码块或getter方法重写
    //我们可以用一个var属性重写一个val属性，但是反过来不行，因为val属性本省定义了getter方法，重写为var属性会在衍生
    //类中额外声明一个setter
    //可以在主构造函数中使用override作为主构造函数的一部分
}