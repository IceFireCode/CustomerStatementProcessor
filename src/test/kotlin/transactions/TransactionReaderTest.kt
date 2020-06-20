package transactions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import transactions.csv.retrieveTransactionRecordsFromCsvFile
import transactions.xml.retrieveTransactionRecordsFromXmlFile
import java.math.BigDecimal
import java.math.RoundingMode

internal class TransactionReaderTest {

    @Test
    fun testIfDataIsReadSuccesfullyFromCsvFile() {
        val records = retrieveTransactionRecordsFromCsvFile("src/test/resources/records.csv")
        assertEquals(10, records.size)
    }

    @Test
    fun testIfDataIsReadSuccesfullyFromXmlFile() {
        val records = retrieveTransactionRecordsFromXmlFile("src/test/resources/records.xml")
        assertEquals(10, records.size)
    }

    @Test
    fun testMappingCsvRecordToTransaction() {
        val record = retrieveTransactionRecordsFromCsvFile("src/test/resources/records.csv")[0]
        val transaction = Transaction.mapFrom(record)

        val expected = Transaction(
            reference = 153096,
            accountNumber = "NL69ABNA0433647324",
            description = "Candy for Daniël Dekker",
            startBalance = BigDecimal(58.57).setScale(2, RoundingMode.HALF_EVEN),
            mutation = BigDecimal(28.42).setScale(2, RoundingMode.HALF_EVEN),
            endBalance = BigDecimal(86.99).setScale(2, RoundingMode.HALF_EVEN)
        )

        assertEquals(expected, transaction)
    }

    @Test
    fun testMappingXmlRecordToTransaction() {
        val record = retrieveTransactionRecordsFromXmlFile("src/test/resources/records.xml")[0]
        val transaction = Transaction.mapFrom(record)

        val expected = Transaction(
            reference = 126928,
            accountNumber = "NL56RABO0149876948",
            description = "Flowers from Rik Dekker",
            startBalance = BigDecimal(47.14).setScale(2, RoundingMode.HALF_EVEN),
            mutation = BigDecimal(-41.71).setScale(2, RoundingMode.HALF_EVEN),
            endBalance = BigDecimal(5.43).setScale(2, RoundingMode.HALF_EVEN)
        )

        assertEquals(expected, transaction)
    }

}