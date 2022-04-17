package tech.riotcode.kitpvp.util

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.jvm.jvmName

object ClassUtil {

    fun loadClassesFromPackage(packageName: String): List<String> {
        return Reflections(
            ConfigurationBuilder().filterInputsBy(FilterBuilder().includePackage(packageName))
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(SubTypesScanner(false))
        ).allTypes.toList()
    }

    fun isSubclass(kClass: KClass<*>, superClass: KClass<*>): Boolean {
        if (kClass.isOpen)
            return false

        if (superClass.isAbstract || superClass.isOpen) {
            return kClass.allSuperclasses.stream().anyMatch {
                it == superClass || it.jvmName == superClass.jvmName
            }
        }

        return false
    }
}