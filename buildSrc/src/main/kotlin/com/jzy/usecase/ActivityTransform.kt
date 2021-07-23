package com.jzy.usecase

import org.objectweb.asm.*
import java.io.File
import java.io.FileOutputStream

/**
 * @author yun.
 * @date 2021/7/22
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class ActivityTransform : ITransformCase {

    override fun transformStart() {
        println("$this >> transformStart ")
    }

    override fun transformJar(jarName: String, destJarFile: File) {
//        println("$this >> transformJar $jarName  ${destJarFile.path}")
    }

    override fun transformClass(file: File) {
        if (file.name.endsWith("Activity.class")) {
            println("== ${file.name} ")

            val classReader = ClassReader(file.inputStream())
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val lifecycleClassVisitor = LifecycleClassVisitor(classVisitor = classWriter)
            classReader.accept(lifecycleClassVisitor, ClassReader.EXPAND_FRAMES)

            val toByteArray = classWriter.toByteArray()
            val fileOutputStream = FileOutputStream(file.path)
            fileOutputStream.write(toByteArray)
            fileOutputStream.close()
        }
    }

    override fun transformEnd() {
        println("$this >> transformEnd ")
    }
}

class LifecycleClassVisitor(api: Int = Opcodes.ASM5, classVisitor: ClassVisitor) : ClassVisitor(api, classVisitor) {

    lateinit var className: String

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name!!
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("LifecycleClassVisitor --- visitMethod $name  $descriptor")
        val visitMethod = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name!!.startsWith("onCreate")) {
            return LifecycleMethodVisitor(methodVisitor = visitMethod, className = className, methodName = name)
        }
        return visitMethod
    }
}

class LifecycleMethodVisitor(
    api: Int = Opcodes.ASM5, methodVisitor: MethodVisitor, val className: String, val methodName:
    String
) :
    MethodVisitor(
        api,
        methodVisitor
    ) {

    override fun visitCode() {
        super.visitCode()
        println("LifecycleMethodVisitor -- visitCode ---")
        mv.visitLdcInsn("TAG")
        mv.visitLdcInsn("$className --> $methodName")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(Opcodes.POP)
    }

}


