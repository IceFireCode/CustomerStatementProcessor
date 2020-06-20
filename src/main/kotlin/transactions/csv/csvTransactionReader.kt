package transactions.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

fun retrieveTransactionRecordsFromCsvFile(csvFilePath: String): List<CSVRecord> {
    val csvReader = Files.newBufferedReader(Paths.get(csvFilePath), Charset.forName("ISO-8859-1"))

    return CSVParser(
        csvReader,
        CSVFormat.DEFAULT
            .withSkipHeaderRecord()
            .withHeader("Reference", "Account Number", "Description", "Start Balance", "Mutation", "End Balance")
    ).toList()
}