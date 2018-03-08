package com.example.alex.newkotlin.grammer

/**
 * Created by Alex on 2018/3/7.
 */
class KotlinEnum {
    //枚举类最基本的用法是实现一个类型安全的枚举
    //枚举常量用逗号分隔，每个常量都是一个对象

    enum class Color{
        RED,BLACK,BLUE,GREEN,WHITE
    }

    //每个枚举都是枚举类的实例，它们可以被初始化
    enum class InitColor(var rgb: Int){
        RED(0XFF0000),
        GREEN(0X00FF00),
        BLUE(0X0000FF)
    }

    //使用枚举常量，Kotlin中枚举类具有合成方法，允许遍历定义的枚举常量，并通过其名称获取枚举常量
    //EnumClass.valueOF(value: String): EnumClass
    //EnumClass.values():Array<EnumClass>以数组的形式，返回枚举值
    //获取枚举类相关信息方法
    //val name:String //获取枚举类名称
    //val ordinal:Int //获取枚举值在所有枚举数组中的顺序
    //Kotlin 1.1以后，可使用enumValues<T>()和enumValueOf<T>()函数以泛型的方式获取枚举类中的常量

}