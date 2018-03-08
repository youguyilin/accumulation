package com.example.alex.newkotlin.grammer

import android.gesture.GestureUtils
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ReplacementTransformationMethod
import java.util.ArrayList

/**
 * Created by Alex on 2018/2/28.
 */
class BasicGrammer(var name: String) : AppCompatActivity() {

    private val bar: Int = 1

    var v = "成员属性"

    /**
     * 内部类，访问外部类成员和函数
     */
    inner class testInnerClass{
        fun foo() = bar //获取外部类成员
        fun innerTest(){
            var o = this@BasicGrammer //获取外部类对象，从而获取外部类成员变量
            println("内部类可以获取外部类成员，例如${o.v}")
        }

    }

    constructor(name: String, age: Int) : this(name) {
        //初始化操作
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun grammerRole() {

    }

    //kotlin类型检查和自动类型转换
    //kotlin中使用is运算符来检查数据类型和类型转换
    //同时!is表示相反的意思
    fun getStringLength(n: Any): Int? {
        if (n is String)
            return n.length//这里会自~动将n转换为字符串类型
        return null

    }

    /**
     * for循环和while循环
     */
    fun testFor() {
        var arr = arrayOf(1, 2, 3, 4, 5, 6)
        for (i in arr.indices) { //通过索引循环
            println(arr[i])
        }

        //直接使用数组中的对象循环
        for (num in arr) {
            println(num)
        }
    }

    fun testWhile() {
        var i = 0;
        while (i < 10) {
            print(" " + i)
            i++
        }
    }

    /********************************* 可空类型****************************************/
    fun nullableTest() {
        //在变量类型后面加上问号，代表该变量是可空变量
        var name: String? = "张三"
        name = null//可以把null赋值给变量
        var person: String = "tom"
//        person= null //代码报错，不可将null赋值给可空变量
    }

    //函数返回值为可空的例子
    /**
     * 函数返回值为T?表示返回值可为空
     */
    fun paseInt(s: String?): Int? {
        if (s == null || s == "")
            return null
        return Integer.parseInt(s)
    }

    /********************************* 条件表达式 ****************************************/
    // 常规条件表达式
    fun max(a: Int, b: Int): Int {
        if (a > b)
            return a
        else
            return b
    }

    //Kotlin 简写的条件表达式
    fun max2(a: Int, b: Int) = if (a > b) a else b

    /********************************* 字符串模版 ****************************************/
    //字符串模版的用法
    fun stringTemple(args: Array<String>) {
        if (args.size > 0)
            println("args[0] = ${args[0]}")
    }

    fun main(args: Array<String>) {
        var arr = arrayOf("hello", "world")
        stringTemple(arr)
    }

    /********************************* 常量和变量 ****************************************/
    //使用val声明一个常量（只读，不可修改），使用var声明一个变量
    fun test() {
        //使用val声明一个常量（只读），声明常量时必须初始化
        val a: Int = 1 //显示指定常量类型
        val b = 2 //自动推断类型
        val c: Int //声明一个不初始化的常量，必须显示指定类型
//        b = 3 //常量值不可修改

        //使用var 声明变量,变量的值可以修改
        var year: Int = 2016 //显示指定变量的类型
        var month = 5 //自动推断变量的类型
        var day: Int //声明一个不初始化的变量，必须显示指定类型
        month = 6 //变量值可以修改
    }

    /********************************* when 表达式****************************************/
    fun testCase(obj: Any) {
        when (obj) {
            is String -> {
                print("this is string")
            }
            is Int -> {
                print("this is integer")
            }
            else -> {
                print("unknown value")
            }
        }
    }
    /********************************* ranges 的使用 ****************************************/
    //使用in操作符检查一个数是否在某个范围内
    /**
     * 判断分数是否大于等于90，小于等于100
     */
    fun isGood(score: Int) {
        if (score in 90..100)//ranges是闭区间
            println("very good")
        else
            println("not so good")
    }

    //使用step指定步 长
    fun stepTest() {
        for (i in 1..4 step 2) println(i) //输出“13”
        for (i in 4 downTo 1 step 2) println(i)//输出42
    }

    //使用until函数排除结束元素
    fun untilTest() {
        for (i in 1 until 10) {
            //[1,10)排除了10
            println(i)
        }
    }


    /**
     * 检查索引是否越界
     * 检查index是否在数组arr的索引范围内
     */
    fun checkIndex(index: Int, arr: Array<Int>) {
        if (index in 0..arr.lastIndex)
            println("index in bounds")
        else
            println("index out of bounds")
    }

    //通过in操作符 遍历一个范围
    /********************************* vararg 可变长参数函数 ****************************************/
    fun vars(vararg v: Int) {
        for (vt in v) {
            print(vt)
        }
    }

    /********************************* lambda(匿名函数) 的使用 ****************************************/
    fun mainTest(args: Array<String>) {
        val sumLambd: (Int, Int) -> Int = { x, y -> x + y }
        println(sumLambd(1, 2))
    }

    /********************************* 比较两个数字 == 表示比较值 ===表示比较对象 ****************************************/
    //kotlin中没有基础数据类型，只有封装的数字类型，定义的每一个变量，Kotlin其实帮我们封装了一个对象，这样可以保证不会出现空指针
    //所以在比较数字的时候就有比较数值和对象的区别
    fun matchNumber() {
        val a: Int = 1000
        println(a === a)//true 值相等，对象地址相等

        //通过装箱，创建两个不同的对象
        val boxedA: Int? = a
        val anotherBoxedA: Int? = a
        println(boxedA === anotherBoxedA)//false 值相等，对象地址不一样
        println(boxedA == anotherBoxedA)//true,值相等
    }

    /********************************* 类型转换 ****************************************/
    //每种数据类型都有下面的这些方法，可以转化为其他的类型
    /**
     * toByte():Byte
     * toShort():Short
     * toInt():Int
     * toLong:Long
     * toFloat:Float
     * toDouble():Double
     * toChar():char
     */
    /********************************* 位操作符 ****************************************/
    /**
     * shl(bits) - 左移位（<<）
     * shr(bits) - 右移位（>>）
     * ushr(bits) - 无符号右移位（>>>）
     * and（bits）- 与
     * or(bits) - 或
     * xor(bits) - 异或
     * inv（） - 反向
     */
    //和Java不一样，Kotlin中的Char不能和数字直接操作，Char必须是单引号引起来的

    //在Kotlin中任何表达式都可以用标签（lable）来标记，格式为标志符后跟@标签，返回指定的函数位置
    /********************************* Kotlin 类和对象 ****************************************/
    //kotlin类中可以包含，构造函数，初始化代码块，函数，属性，内部类，对象声明
    //getter和setter方法
    /**
     * var <propertyName>[:PropertyType] [= Property_initializer]
     *      [<getter>]
     *      [<setter>]
     */
    //类中主构造器中不能包含任何代码，初始化代码可以放在初始化代码段中，初始化代码段用init关键字作为前缀
    /**
     * class Person constructor(firstName: String){
     *     init{
     *          System.out.println("FirstName is $firstName")
     *     }
     *     }
     * 类中可以有二级构造函数，需要加前缀constructor
     * 如果类中有主构造函数，那么每一个次构造函数都要直接或者间接的通过另一个次构造函数代理主构造函数，在同一个类中通过this关键字来实现
     * 如果一个非抽象类没有声明构造函数（主构造函数，或者次构造函数），那么他会生成一个没有参数的构造函数，默认的构造函数为public
     * 如果不想有公有的构造函数，就需要自己声明一个空的私有构造函数
     * class BasicGrammer private constructor(){}。
     *
     **/
    /********************************* Kotlin 抽象类 ****************************************/
    /**
     * 抽象是面向对象基本特征之一，类本身或类中的部分成员可以声明为abstract，抽象的成员在类中不存在具体的实现，
     * 无需对抽象类或抽象成员使用open注解，
     *
     *
     */
    /********************************* Kotlin 嵌套类 ****************************************/
    //我们可以把类嵌套在其他类中。
    // 调用方式：外部类.内部类().方法().
    /********************************* Kotlin 内部类 ****************************************/
    //内部类使用inner来表示
    //内部类会保留外部类的引用，所以内部类可以访问外部类的成员属性和方法。
    //通常在内部类中使用this@label来制定外部作用域，lable指代this的来源
    /********************************* Kotlin 匿名内部类 ****************************************/
    //采用对象表达式的方式创建
    interface  TestInterface{
        fun testA()
    }

    class TestAnonymousInnerClass{
        var v  = "成员属性"
        fun setInterface(test:TestInterface){
            test.testA()
        }
    }

    fun testMain(args: Array<String>){
        var test = TestAnonymousInnerClass()
        /**
         * 采用对象表达式的方式创建接口对象，即匿名内部类的实例
         **/
        test.setInterface(object :TestInterface{
            override fun testA() {
                println("对象表达式方式创建匿名内部类")
            }
        })
    }
    /********************************* Kotlin 类的修饰符 ****************************************/
    //类的修饰符包括classModifier(类的属性修饰符)和(_accessModifier_)访问权限修饰符
    /**
     * classModifier:
     *  abstract //抽象类
     *  final //类不可继承，默认属性
     *  enum //枚举类
     *  open //类可继承 默认为final
     *  annotation //注解类
     * accessModifier:
     *  private //同一个文件中可见
     *  protected //同一个文件或者子类中可见
     *  public //所有调用的地方都可见
     *  internal //同一个模块可见
     *
     */
}