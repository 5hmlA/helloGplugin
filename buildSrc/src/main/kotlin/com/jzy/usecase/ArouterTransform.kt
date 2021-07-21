package com.jzy.usecase

import org.apache.commons.io.IOUtils
import org.objectweb.asm.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
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
object ArouterTransform {

    const val loadRouterMap = "loadRouterMap"
    const val arouterFilePrefix = "ARouter$$"
    const val logisticsCenterClass = "LogisticsCenter.class"
    var arouterApiJarPath = "LogisticsCenter.class"
    private val routesClassNames = mutableListOf<String>()

    fun holdArouterApiJarPath(path: String) {
        arouterApiJarPath = path
        routesClassNames.clear()
    }

    fun keepRouterClassName(path: String) {
        val className = path.substring(path.indexOf("com"),path.indexOf(".class")).replace("\\",".")
        println(className)
        routesClassNames.add(className)
    }


    fun reWriteLogisticsCenterClass() {
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
                loadRouterMap(inputStream)
            }
            newjarOutputStream.write(IOUtils.toByteArray(inputStream))
            inputStream.close()
            newjarOutputStream.closeEntry()
        }
        newjarOutputStream.close()
        originFile.delete()
        temfile.renameTo(originFile)
    }


    private fun loadRouterMap(inputStream: InputStream) {
        val classReader = ClassReader(inputStream)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        println(">>>> loadRouterMap $routesClassNames $classReader")
        classReader.accept(object : ClassVisitor(Opcodes.ASM9, classWriter) {

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
            ): org.objectweb.asm.MethodVisitor {
                val visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
                if (loadRouterMap == name) {
                    println(" > visitMethod $name ======= ")
                    return LoadRouterMethodVisitor(methodVisitor = visitMethod)
                }
                return visitMethod
            }

        }, ClassReader.SKIP_DEBUG)
    }

}

class LoadRouterMethodVisitor(api: Int = Opcodes.ASM9, methodVisitor: MethodVisitor) : MethodVisitor(api, methodVisitor) {

    override fun visitCode() {
        println("========= visitCode")
        super.visitCode()
    }

    override fun visitInsn(opcode: Int) {
        println("======== visitInsn $opcode ")
//        mv.visitLdcInsn("root")
//        mv.visitMethodInsn(Opcodes.INVOKEDYNAMIC,logisitscCenter,"register","(Ljava/lang/String;)V",false)
        super.visitInsn(opcode)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        super.visitMaxs(maxStack + 4, maxLocals)
    }
}
