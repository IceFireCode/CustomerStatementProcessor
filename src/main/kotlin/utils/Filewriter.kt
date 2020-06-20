package utils

import java.io.File

fun writeFileDirectly(filePath: String, fileName: String, fileContent: String) {
    File(filePath).mkdirs()
    File("$filePath$fileName").writeText(fileContent)
}
