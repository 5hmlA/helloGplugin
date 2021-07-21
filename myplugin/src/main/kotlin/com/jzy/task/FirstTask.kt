package com.jzy.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author yun.
 * @date 2021/7/19
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class FirstTask : DefaultTask() {

    @TaskAction
    fun output(){
        println("this is my custom task output")
    }
}