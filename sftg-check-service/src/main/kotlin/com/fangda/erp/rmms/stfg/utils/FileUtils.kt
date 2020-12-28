package com.fangda.erp.rmms.stfg.utils

import org.springframework.http.codec.multipart.FilePart
import java.io.File
import java.nio.file.Files

/**
 * @author yuhb
 * @date 2020/12/25
 */
object FileUtils {
    fun templateSaveFile(part: FilePart): File {
        val tempFile = Files.createTempFile("tmp", part.filename())
        part.transferTo(tempFile)
        return tempFile.toFile()
    }
}