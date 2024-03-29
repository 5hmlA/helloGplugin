package com.jzy.usecase

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.jzy.transform.MyTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState
import java.util.*

/**
 * @author yun.
 * @date 2021/7/20
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class AndroidCase:Plugin<Project> {
    override fun apply(project: Project) {
        //监听每个task的执行
//        project.gradle.addListener(object : TaskExecutionListener {
//            override fun beforeExecute(p0: Task) {
//                println("*********** beforeExecute ${p0.path} **************")
//            }
//
//            override fun afterExecute(p0: Task, p1: TaskState) {
//                println("*********** afterExecute ${p0.path} **************")
//            }
//        })

        val properties = Properties()
        //gradle.properties是否存在
        if(project.rootProject.file("gradle.properties").exists()){
            //gradle.properties文件->输入流
            properties.load(project.rootProject.file("gradle.properties").inputStream())
            "false".toBoolean()
            println("read data from gradle.properties > ${properties.getProperty("android.useAndroidX", "false")}")
        }

        println("${project.name}  ${project.plugins.hasPlugin("com.android.application")}")
        println("${project.name}  ${project.plugins.hasPlugin(AppPlugin::class.java)}")
        println("${project.name}  ${project.plugins.hasPlugin(com.android.build.gradle.internal.plugins.AppPlugin::class.java)}")

        //[DefaultTaskExecutionRequest{args=[assembleDebug],projectPath='null'}]
        //[DefaultTaskExecutionRequest{args=[clean, assembleRelease],projectPath='null'}]

        println(project.gradle.startParameter.taskRequests)
        val taskRequests = project.gradle.startParameter.taskRequests
        if (taskRequests.size > 0) {
            val args = taskRequests[0].args
            if (args.size > 0) {
                val predicate: (String) -> Boolean = { it.toLowerCase().contains("release") }
                if (args.any(predicate)) {
                    println("=================== release 启用ARouter自动注册 =====================")
                    val android = project.extensions.findByType(AppExtension::class.java)
                    println("project name: ${project.name}  $android  ${android?.transforms}")
                    android?.registerTransform(MyTransform(project))
                }
            }
        }

//        val android = project.extensions.findByType(BaseAppModuleExtension::class.java)
//        val android = project.extensions.findByType(AppExtension::class.java)
//        android?.registerTransform(MyTransform(project))
//        println("project name: ${project.name}  $android  ${android?.transforms}")
//        val libraryExtension = project.extensions.findByType(LibraryExtension::class.java)
//        libraryExtension?.registerTransform(MyTransform(project))
    }
}