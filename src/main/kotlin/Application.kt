import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.apache.commons.csv.CSVRecord
import report.StandardOutputReportMaker
import report.WriteToFileReportMaker
import transactions.Transaction
import transactions.csv.retrieveTransactionRecordsFromCsvFile
import transactions.xml.XmlRecord
import transactions.xml.retrieveTransactionRecordsFromXmlFile


fun main() = runBlocking {

    // STEP (1) - read the data from the delivered files (async)

    val csvFilePath = "delivered_files/records.csv"
    val xmlFilePath = "delivered_files/records.xml"

    val csvRecords: Deferred<List<CSVRecord>> = async { retrieveTransactionRecordsFromCsvFile(csvFilePath) }
    val xmlRecords: Deferred<List<XmlRecord>> = async { retrieveTransactionRecordsFromXmlFile(xmlFilePath) }

    // STEP (2) - map the data to one collection of Transaction

    val records = flow {
        csvRecords.await().map { csvRecord ->
            emit(Transaction.mapFrom(csvRecord))
        }
        xmlRecords.await().map { xmlRecord ->
            emit(Transaction.mapFrom(xmlRecord))
        }
    }

    // STEP (3) - validate all transactions, making a seperate list of invalid transactions for
    // each specific validation

    val mutableList = mutableSetOf<Transaction>()
    val recordsWithDuplicatedReference = mutableSetOf<Transaction>()
    val recordsWithIncorrectEndBalance = mutableSetOf<Transaction>()

    records.collect { record ->
        val duplicate = mutableList.filter {
            it.reference == record.reference
        }
        val isDuplicate = duplicate.isNotEmpty()
        if (isDuplicate) {
            recordsWithDuplicatedReference.addAll(duplicate)
            recordsWithDuplicatedReference.add(record)
        }
        mutableList.add(record)

        if (!record.hasCorrectEndBalance()) {
            recordsWithIncorrectEndBalance.add(record)
        }
    }

    // STEP (4) - make reports (async)

    val reportRecordsWithDuplicatedReference = recordsWithDuplicatedReference.map { it.toReportRecord() }
    val reportRecordsWithIncorrectEndBalance = recordsWithIncorrectEndBalance.map { it.toReportRecord() }

    val reportJobs = mutableListOf<Deferred<Unit>>()

    reportJobs.add(
        async {
            StandardOutputReportMaker().makeReport(
                reportRecordsWithDuplicatedReference,
                reportRecordsWithIncorrectEndBalance
            )
        }
    )

    reportJobs.add(
        async {
            WriteToFileReportMaker().makeReport(
                reportRecordsWithDuplicatedReference,
                reportRecordsWithIncorrectEndBalance
            )
        }
    )

    reportJobs.forEach { it.await() }

}