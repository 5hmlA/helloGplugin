package com.jzy.usecase

import java.io.File

/**
 * @author yun.
 * @date 2021/7/22
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
interface ITransformCase {
    fun transformStart()
    fun transformJar(jarName: String, destJarFile: File)

    /**
     * 复制后的文件
     * 如果遇到需要依赖其他文件内容的时候
     * 可以保存文件路径，等最后在做修改
     */
    fun transformClass(file: File)
    fun transformEnd()
}