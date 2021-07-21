package com.jzy.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.jzy.usecase.ArouterTransform
import org.gradle.api.Project

/**
 * @author yun.
 * @date 2021/7/20
 * @des [用户自定义的Transform，会比系统的Transform先执行]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class MyTransform constructor(project: Project) : Transform() {
//    用户自定义的Transform，会比系统的Transform先执行

    override fun getName() = "TransformLearn"

    override fun getInputTypes() = TransformManager.CONTENT_CLASS

    //    EXTERNAL_LIBRARIES：只有外部库
//    PROJECT：只有项目内容
//    PROJECT_LOCAL_DEPS：只有项目的本地依赖(本地jar)
//    PROVIDED_ONLY：只提供本地或远程依赖项
//    SUB_PROJECTS：只有子项目
//    SUB_PROJECTS_LOCAL_DEPS：只有子项目的本地依赖项(本地jar)
//    TESTED_CODE：由当前变量(包括依赖项)测试的代码
    override fun getScopes() = TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental() = true

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)

        transformInvocation.inputs.onEach {
            it.jarInputs.onEach { jar ->
//                JarInput：它代表着以jar包方式参与项目编译的所有本地jar包或远程jar包，可以借助于它来实现动态添加jar包操作。
                val destJarFile = transformInvocation.outputProvider.getContentLocation(
                    jar.name, jar.contentTypes, jar.scopes,
                    Format.JAR
                )
                if (jar.file.name.contains("arouter-api")) {
                    println("..${jar.file}.....  name: ${jar.name} fineName: ${jar.file.name}")
                    ArouterTransform.holdArouterApiJarPath(destJarFile.path)
                }
                FileUtils.copyFile(jar.file, destJarFile)
            }

            it.directoryInputs.onEach { dir ->
//                DirectoryInput：它代表着以源码方式参与项目编译的所有目录结构及其目录下的源码文件，可以借助于它来修改输出文件的目录结构、目标字节码文件。
                val destDirectory = transformInvocation.outputProvider.getContentLocation(
                    dir.name, dir.contentTypes, dir.scopes,
                    Format.DIRECTORY
                )
                println("..${dir.file}.....")
                if (dir.file.isDirectory) {
                    FileUtils.getAllFiles(dir.file).onEach { file ->
                        if (file.name.startsWith(ArouterTransform.arouterFilePrefix)) {
                            ArouterTransform.keepRouterClassName(file.path)
                        }
                    }
                }
                FileUtils.copyDirectory(dir.file, destDirectory)

            }

        }
        //替换ali arouter-api的jar 主要是写入注册代码到LogisticsCenter里面
        ArouterTransform.reWriteLogisticsCenterClass()
    }


}