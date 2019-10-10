package me.yohom.amapbase

class Test{
    companion object {
        /** 我是main入口函数 **/
        @JvmStatic
        fun main(args: Array<String>) {
            Sb.printAbc()
            Sb.printAbc()
        }
    }
}

object Sb{
    var xx:String?=null
    fun printAbc(){
        if(xx == null){
            println("xx是空的")
            xx = "哈哈哈"
        } else {
            println(xx)
        }
    }
}