package org.jetbrains.research.smtlib

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class SmtLibFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(SmtLibFileType, SmtLibFileType.FILE_EXTENSION)
    }
}
