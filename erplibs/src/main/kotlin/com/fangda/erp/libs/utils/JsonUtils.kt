/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.libs.utils

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

/**
 * @author yuhb
 * @date 2020/5/22
 */
object JsonUtils {
    private val mapper: ObjectMapper = ObjectMapper()
    fun toJsonString(obj: Any): String {
        return try {
            mapper.writeValueAsString(obj)
        } catch (e: JsonProcessingException) {
            ""
        }
    }

    fun toJsonBytes(obj: Any): ByteArray {
        return try {
            mapper.writeValueAsBytes(obj)
        } catch (e: JsonProcessingException) {
            ByteArray(0)
        }
    }

    fun <T> parseObject(content: String, clazz: Class<T>): T? {
        return try {
            mapper.readValue(content, clazz)
        } catch (e: IOException) {
            return null
        } catch (e: JsonParseException) {
            return null
        } catch (e: JsonMappingException) {
            return null
        }
    }

    fun <T> parseObject(byteArray: ByteArray, clazz: Class<T>): T? {
        return try {
            mapper.readValue(byteArray, clazz)
        } catch (e: IOException) {
            return null
        } catch (e: JsonParseException) {
            return null
        } catch (e: JsonMappingException) {
            return null
        }
    }

    fun <T> parseObject(content: String, typeReference: TypeReference<T>): T? {
        return try {
            mapper.readValue(content, typeReference)
        } catch (e: IOException) {
            return null
        } catch (e: JsonParseException) {
            return null
        } catch (e: JsonMappingException) {
            return null
        }
    }
}