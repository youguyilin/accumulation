package com.example.alex.newkotlin.grammer

/**
 * Created by Alex on 2018/3/13.
 */
class KotlinStatic {
    //Java中使用static标志一个类里的静态方法或属性，可以被这个类使用，而Kotlin使用伴随对象，用companion
    //修饰单例object来实现静态属性或方法
    companion object Test {
        var TEST_NAME:String = "ALEX"
        fun testSomething() {
            println("Hello Alex")
        }
    }
}