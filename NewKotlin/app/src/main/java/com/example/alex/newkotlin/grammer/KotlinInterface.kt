package com.example.alex.newkotlin.grammer

/**
 * Created by Alex on 2018/3/5.
 */
interface KotlinInterface {
    //Kotlin接口同java类似，使用Interface关键字，允许有默认的实现
    //接口的实现同样使用（：）
    //接口中的属性必须是抽象的，不允许初始化，接口不会保存属性值，实现接口时，必须重写属性
    var name: String //name ，抽象的 ，实现类必须使用override关键字初始化
    //由于接口时多继承的，同样也会出现，同一个方法，多个实现的问题，这时我们可以使用super<>指明调用具体类的方法
}