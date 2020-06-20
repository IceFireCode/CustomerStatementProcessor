package report

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.writeFileDirectly
import java.util.*

const val FILE_PATH_TO_REPORTS = "reports/"

interface ReportMaker {
    suspend fun makeReport(
        recordsWithDuplicatedReference: List<ReportRecord>,
        recordsWithIncorrectEndBalance: List<ReportRecord>
    )
}

data class ReportRecord(
    val reference: Int,
    val description: String
) {
    fun toPrettyString() = "transactions.Transaction $reference, description: \"$description\""
}

class WriteToFileReportMaker : ReportMaker {

    override suspend fun makeReport(
        recordsWithDuplicatedReference: List<ReportRecord>,
        recordsWithIncorrectEndBalance: List<ReportRecord>
    ) = withContext(Dispatchers.IO) {

        val fileName = "Transaction Validation ${Date()}"

        var reportText = "\n\tTRANSACTIONS WITH DUPLICATE REFERENCE\n"
        reportText += "\t------------------------------\n"
        reportText += if (recordsWithDuplicatedReference.isEmpty()) {
            "\tAll transactions have a unique reference\n"
        } else ""
        recordsWithDuplicatedReference.forEach {
            reportText += "\t${it.toPrettyString()}\n"
        }

        reportText += "\n\tTRANSACTIONS WITH INVALID END BALANCE\n"
        reportText += "\t------------------------------\n"
        reportText += if (recordsWithIncorrectEndBalance.isEmpty()) {
            "\tAll transactions have a valid end balancee\n"
        } else ""
        recordsWithIncorrectEndBalance.forEach {
            reportText += "\t${it.toPrettyString()}\n"
        }

        writeFileDirectly(FILE_PATH_TO_REPORTS, fileName, reportText)

    }

}

class StandardOutputReportMaker : ReportMaker {

    override suspend fun makeReport(
        recordsWithDuplicatedReference: List<ReportRecord>,
        recordsWithIncorrectEndBalance: List<ReportRecord>
    ) {

        println("TRANSACTIONS WITH DUPLICATE REFERENCE")
        println("------------------------------")
        if (recordsWithDuplicatedReference.isEmpty()) {
            println("All records have a unique reference")
        }
        recordsWithDuplicatedReference.forEach {
            println(it)
        }
        println()

        println("TRANSACTIONS WITH INVALID END BALANCE")
        println("------------------------------")
        if (recordsWithIncorrectEndBalance.isEmpty()) {
            println("All transactions have a valid end balance")
        }
        recordsWithIncorrectEndBalance.forEach {
            println(it)
        }

    }

}