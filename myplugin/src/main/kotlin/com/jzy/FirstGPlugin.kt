package com.jzy

import com.jzy.extension.GreetingPluginExtension
import com.jzy.task.FirstTask
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author yun.
 * @date 2021/7/19
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class FirstGPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("========= plugin =========")
        val extension = project.extensions.create("greeting", GreetingPluginExtension::class.java)
//        val create = project.extensions.create<GreetingPluginExtension>("greeting")

//        project.tasks.register("first", FirstTask::class.java)
//
//        project.afterEvaluate {
//            println("========== afterEvaluate =========")
//        }


        project.task("hello").doFirst {
            println("================${extension.message.get()}")
        }
    }
}