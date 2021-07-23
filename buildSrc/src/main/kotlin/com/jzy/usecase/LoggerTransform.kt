package com.jzy.usecase

import java.io.File

/**
 * @author yun.
 * @date 2021/7/23
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class LoggerTransform:ITransformCase {
    override fun transformStart() {
        println(" >>>>>>>>> transformStart ")
    }

    override fun transformJar(jarName: String, destJarFile: File) {
        println(" >>>>>>>>> transformJar $jarName ")
    }

    override fun transformClass(file: File) {
        println(" >>>>>>>>> transformClass ${file.path}")
    }

    override fun transformEnd() {
        println(" >>>>>>>>> transformEnd ")
    }
}