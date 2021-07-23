package com.jzy.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.google.common.collect.ImmutableSet
import com.jzy.usecase.ArouterTransform
import com.jzy.usecase.ITransformCase
import com.jzy.usecase.LoggerTransform
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

    val transformCases = mutableListOf<ITransformCase>(ArouterTransform())
//    val transformCases = mutableListOf<ITransformCase>(LoggerTransform())
//    val transformCases = mutableListOf<ITransformCase>(ActivityTransform(), ArouterTransform())

    override fun getName() = "TransformLearn"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    //    EXTERNAL_LIBRARIES：只有外部库
//    PROJECT：只有项目内容
//    PROJECT_LOCAL_DEPS：只有项目的本地依赖(本地jar)
//    PROVIDED_ONLY：只提供本地或远程依赖项
//    SUB_PROJECTS：只有子项目
//    SUB_PROJECTS_LOCAL_DEPS：只有子项目的本地依赖项(本地jar)
//    TESTED_CODE：由当前变量(包括依赖项)测试的代码
    override fun getScopes(): MutableSet<QualifiedContent.ScopeType> = TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental() = true

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        if (!transformInvocation.isIncremental) {
            //不是增量编译，则清空output目录
            transformInvocation.outputProvider.deleteAll()
        }
//        增量编译，则要检查每个文件的Status，Status分为四种，并且对四种文件的操作不尽相同
//        NOTCHANGED 当前文件不需要处理，甚至复制操作都不用
//        ADDED、CHANGED 正常处理，输出给下一个任务
//        REMOVED 移除outputProvider获取路径对应的文件

        transformCases.onEach { case->
            case.transformStart()
        }

        transformInvocation.inputs.onEach {
            //input 是循环
            //可能出现 direct没有jar多，direct很多jar没有

            println("======================================= directoryInputs ${it.directoryInputs.size}=======")
            it.directoryInputs.onEach { dir ->
//                DirectoryInput：它代表着以源码方式参与项目编译的所有目录结构及其目录下的源码文件，可以借助于它来修改输出文件的目录结构、目标字节码文件。

                //目标文件都被用数字重命名了
                val destDirectory = transformInvocation.outputProvider.getContentLocation(
                    dir.name, dir.contentTypes, dir.scopes,
                    Format.DIRECTORY
                )
                println("..${dir.file}.....")
                println("..${destDirectory}.....")
                FileUtils.copyDirectory(dir.file, destDirectory)
                if (destDirectory.isDirectory) {
                    FileUtils.getAllFiles(destDirectory).onEach { file ->
                        transformCases.onEach { case->
                            case.transformClass(file)
                        }
                    }
                }
//                FileUtils.deletePath(dir.file)
            }

            println("======================================= jarInputs ${it.jarInputs.size} =============")
            it.jarInputs.onEach { jar ->
//                JarInput：它代表着以jar包方式参与项目编译的所有本地jar包或远程jar包，可以借助于它来实现动态添加jar包操作。
                    //这里包括子模块打包的class文件debug\classes.jar
                //目标文件都被用数字重命名了
                val destJarFile = transformInvocation.outputProvider.getContentLocation(
                    jar.name, jar.contentTypes, jar.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(jar.file, destJarFile)
//                FileUtils.deletePath(jar.file)
                transformCases.onEach { case->
                    case.transformJar(jar.file.name, destJarFile)
                }
            }

        }

        transformCases.onEach { case->
            case.transformEnd()
        }


    }


}