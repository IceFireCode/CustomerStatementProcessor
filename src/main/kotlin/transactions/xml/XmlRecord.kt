package transactions.xml

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

@JsonRootName("records")
data class XmlRecords(

    @set:JsonProperty("record")
    @JacksonXmlElementWrapper(useWrapping = false)
    var xmlRecords: List<XmlRecord> = ArrayList()
)

@JsonRootName("record")
data class XmlRecord(

    @set:JsonProperty("reference")
    var reference: String,

    @set:JsonProperty("accountNumber")
    var accountNumber: String? = null,

    @set:JsonProperty("description")
    var description: String? = null,

    @set:JsonProperty("startBalance")
    var startBalance: String? = null,

    @set:JsonProperty("mutation")
    var mutation: String? = null,

    @set:JsonProperty("endBalance")
    var endBalance: String? = null
)