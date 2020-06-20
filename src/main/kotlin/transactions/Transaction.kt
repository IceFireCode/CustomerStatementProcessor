package transactions

import org.apache.commons.csv.CSVRecord
import report.ReportRecord
import transactions.xml.XmlRecord
import java.math.BigDecimal

data class Transaction(
    val reference: Int,
    val accountNumber: String,
    val description: String,
    val startBalance: BigDecimal,
    val mutation: BigDecimal,
    val endBalance: BigDecimal
) {
    fun hasCorrectEndBalance(): Boolean = startBalance + mutation == endBalance

    fun toReportRecord() = ReportRecord(
        reference = this.reference,
        description = this.description
    )

    companion object Mapper {
        fun mapFrom(csvRecord: CSVRecord) = Transaction(
            reference = csvRecord.get("Reference").toInt(),
            accountNumber = csvRecord.get("Account Number"),
            description = csvRecord.get("Description"),
            startBalance = csvRecord.get("Start Balance").toBigDecimal(),
            mutation = csvRecord.get("Mutation").toBigDecimal(),
            endBalance = csvRecord.get("End Balance").toBigDecimal()
        )

        fun mapFrom(xmlRecord: XmlRecord) = Transaction(
            reference = xmlRecord.reference.toInt(),
            accountNumber = xmlRecord.accountNumber!!,
            description = xmlRecord.description!!,
            startBalance = xmlRecord.startBalance!!.toBigDecimal(),
            mutation = xmlRecord.mutation!!.toBigDecimal(),
            endBalance = xmlRecord.endBalance!!.toBigDecimal()
        )
    }
}