package tech.riotcode.kitpvp.util

import org.bukkit.Bukkit
import java.lang.reflect.Field
import java.lang.reflect.Method

/*
 * Author: Bridge
 */

object MinecraftReflection {

    // Constants
    private val VERSION: String = Bukkit.getServer()::class.java.getPackage().name.split(".")[3]
    //private val MINECRAFT_SERVER: Class<*>? = parseNMSClass("MinecraftServer")
    //private val ROLLING_AVERAGE: Class<*>? = parseNMSClass("MinecraftServer${"$"}RollingAverage")
    //private val MINECRAFT_SERVER_INSTANCE: Any? = fetchMethod(MINECRAFT_SERVER!!, "getServer")!!
    //.invoke(null)
    //private val TPS_ONE: Any? = fetchField(MINECRAFT_SERVER!!, "tps")?.get(MINECRAFT_SERVER_INSTANCE!!)
    //private val GET_AVERAGE_METHOD: Method = fetchMethod(ROLLING_AVERAGE!!, "getAverage")!!

    // MinecraftServer.getServer().tps1.getAverage()

    /*@JvmStatic
    fun findTps(): Double {
        return GET_AVERAGE_METHOD.invoke(TPS_ONE) as Double
    }
     */

    @JvmStatic
    fun parseNMSClass(name: String): Class<*>? {
        return try {
            Class.forName("net.minecraft.server.$VERSION.$name")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun fetchMethod(clazz: Class<*>, methodName: String, vararg parameters: Class<*>?): Method? {
        return try {
            val method = clazz.getDeclaredMethod(methodName, *parameters)
            method.isAccessible = true
            method
        } catch (e: NoSuchMethodException) {
            throw IllegalArgumentException(e)
        }
    }

    @JvmStatic
    fun fetchField(clazz: Class<*>, fieldName: String): Field? {
        return try {
            val field = clazz.getDeclaredField(fieldName)
            if (!field.isAccessible) field.isAccessible = true
            field
        } catch (e: NoSuchFieldException) {
            throw java.lang.IllegalArgumentException(e)
        }
    }

    @JvmStatic
    fun invokeField(field: Field, o: Any?): Any? {
        return try {
            field[o]
        } catch (e: IllegalAccessException) {
            throw java.lang.IllegalArgumentException(e)
        }
    }
}