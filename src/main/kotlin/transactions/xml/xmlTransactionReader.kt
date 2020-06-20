package transactions.xml

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Files
import java.nio.file.Paths

fun retrieveTransactionRecordsFromXmlFile(xmlFilePath: String): List<XmlRecord> {
    val mapper = XmlMapper().registerKotlinModule()
    val xmlReader = Files.newBufferedReader(Paths.get(xmlFilePath))
    return mapper.readValue(xmlReader, XmlRecords::class.java).xmlRecords
}