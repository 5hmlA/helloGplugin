package com.jzy.usecase

import org.apache.commons.io.IOUtils
import org.objectweb.asm.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * @author yun.
 * @date 2021/7/21
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */

const val loadRouterMap = "loadRouterMap"
const val arouterFilePrefix = "ARouter$$"
const val logisticsCenterClass = "LogisticsCenter.class"
private val routesClassNames = mutableListOf<String>()

class ArouterTransform : ITransformCase {

        val regex = Regex("/|\\\\")
    private var arouterApiJarPath = "LogisticsCenter.class"

    private fun holdArouterApiJarPath(path: String) {
        arouterApiJarPath = path
    }

    private fun keepRouterClassName(path: String) {
        val className = path.substring(path.indexOf("com"), path.indexOf(".class")).replace(regex, ".")
        routesClassNames.add(className)
    }


    private fun reWriteLogisticsCenterClass() {
        val temfile = File("$arouterApiJarPath.temp")
        if (temfile.exists()) {
            temfile.delete()
        }
        val newjarOutputStream = JarOutputStream(FileOutputStream(temfile))
        val originFile = File(arouterApiJarPath)
        val jarFile = JarFile(originFile)
        jarFile.entries().toList().onEach {
            newjarOutputStream.putNextEntry(ZipEntry(it.name))
            val inputStream = jarFile.getInputStream(it)
            if (it.name.endsWith(logisticsCenterClass)) {
                println("fond logisticsCenterClass -->> ${it.name}")
                //插入代码
                val newBye = LogisticsCenterVisitor(inputStream)
                newjarOutputStream.write(newBye)
            } else {
                newjarOutputStream.write(IOUtils.toByteArray(inputStream))
            }
            inputStream.close()
            newjarOutputStream.closeEntry()
        }
        newjarOutputStream.close()
        jarFile.close()
        originFile.delete()

        println("${temfile.path}  ${temfile.renameTo(originFile)}")
    }


    private fun LogisticsCenterVisitor(inputStream: InputStream): ByteArray {
        val classReader = ClassReader(inputStream)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        println(">>>> loadRouterMap $routesClassNames $classReader")
        classReader.accept(object : ClassVisitor(Opcodes.ASM5, classWriter) {

            override fun visit(
                version: Int,
                access: Int,
                name: String?,
                signature: String?,
                superName: String?,
                interfaces: Array<out String>?
            ) {
                super.visit(version, access, name, signature, superName, interfaces)
            }

            override fun visitMethod(
                access: Int,
                name: String?,
                descriptor: String?,
                signature: String?,
                exceptions: Array<out String>?
            ): MethodVisitor {
                val visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
                if (loadRouterMap == name) {
                    println(" > visitMethod $name ======= ")
                    return LoadRouterMethodVisitor(methodVisitor = visitMethod)
                }
                return visitMethod
            }

        }, ClassReader.SKIP_DEBUG)
        return classWriter.toByteArray()
    }

    override fun transformStart() {
        routesClassNames.clear()
    }

    override fun transformJar(jarName: String, destJarFile: File) {
        if (jarName.contains("arouter-api")) {
            println("..${destJarFile}.....  $jarName: ${destJarFile.name} ")
            holdArouterApiJarPath(destJarFile.path)
            val jarFile = JarFile(destJarFile)
            jarFile.entries().toList().onEach {
                if (it.name.contains(arouterFilePrefix)) {
                    keepRouterClassName(it.name)
                }
            }
            jarFile.close()
        }
        if (jarName.contains("class")) {
            val jarFile = JarFile(destJarFile)
            jarFile.entries().toList().onEach {
                if (it.name.contains(arouterFilePrefix)) {
                    keepRouterClassName(it.name)
                }
            }
            jarFile.close()
        }
    }

    override fun transformClass(file: File) {
        if (file.name.startsWith(arouterFilePrefix)) {
            keepRouterClassName(file.path)
        }
    }


    override fun transformEnd() {
        println("$this  >>>  transformEnd ")
        //修改jar
        reWriteLogisticsCenterClass()
    }

}

class LoadRouterMethodVisitor(api: Int = Opcodes.ASM5, methodVisitor: MethodVisitor) : MethodVisitor(api, methodVisitor) {

    val logisitscCenter = "com/alibaba/android/arouter/core/LogisticsCenter"

    override fun visitInsn(opcode: Int) {
        if (opcode == Opcodes.RETURN) {
            mv.visitLdcInsn("TAG")
            mv.visitLdcInsn("$logisitscCenter --> visitInsn")
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false)
            mv.visitInsn(Opcodes.POP)
            println("======== visitInsn $opcode  $routesClassNames")
            routesClassNames.onEach {
                mv.visitLdcInsn(it)
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, logisitscCenter, "register", "(Ljava/lang/String;)V", false)
            }
        }
        super.visitInsn(opcode)
    }

}
