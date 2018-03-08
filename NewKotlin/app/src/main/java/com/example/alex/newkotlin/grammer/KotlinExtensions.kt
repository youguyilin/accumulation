package com.example.alex.newkotlin.grammer

/**
 * Created by Alex on 2018/3/6.
 */
class KotlinExtensions {

    //扩展属性:只能在Kotlin文件或类中，且不可以有初始化器

    val <T> List<T>.lastIndex: Int
        get() = size -1



    //Kotlin可以对一个类的方法或属性进行扩展且不需要使用继承或者Decorator模式
    //扩展是一种静态行为，对被扩展的类的本身不会造成任何影响
    fun testOne(){

    }

    fun KotlinExtensions.testOne(){

    }

    //receiverType:表示函数的接收者，也就是函数扩展的对象
    //functionName:扩展函数的名称，
    //params:扩展函数的参数可以为null
    //扩展函数是静态解析的，并不是接收者类型的虚拟成员，在调用扩展函数时，具体调用的是哪一个函数，由调用函数的对象表达式来决定，
    //而不是动态的类型决定的
    //通过c:C指定类型
    //如果扩展函数和成员函数一致，则优先使用成员函数

    //扩展一个空对象
    //在扩展函数内，可以通过this判断接收者是否为null，这样，即使接收者为Null，也可以调用扩展函数，
    fun Any?.toString():String{
        if(this == null) return "null"
        //空检测之后“this”会自动转换为非空类型，toString()解析为Any类的成员函数
        return toString()
    }

    //Kotlin 伴生对象的扩展使用“类名.”调用


}