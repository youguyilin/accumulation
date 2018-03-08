package com.example.alex.newkotlin.grammer

/**
 * Created by Alex on 2018/3/6.
 */
class KotlinGeneric {
    //泛型即参数化类型，将类型参数化，可以用在类，接口，方法上
    //同java一样，Kotlin也提供泛型，为类型安全提供保证，消除类型强转的烦恼，
    //定义泛型类型变量，可以完整地写明类型参数，如果编译器可以自动推定类型参数，可以省略类型参数

    //类型参数要放在函数名的前面

    //同样Kotlin中也可以使用泛型约束，来设定一个给定参数允许使用的类型
    //上界限定符，
    fun <T: Comparable<T>> sort(list: List<T>){

    }
    //默认的上界是Any?
    //对于多个上界限定，可以使用where子句
    fun <T> copyWhenGenerater(list: List<T>,threadId: T):List<String>
        where T : CharSequence,
              T : Comparable<T> {
        return list.filter { it > threadId }.map { it.toString() }
    }

}