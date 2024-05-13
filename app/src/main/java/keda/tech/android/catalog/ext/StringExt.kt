package keda.tech.android.catalog.ext

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.formatCurrency(): String {
    val currency = this.toDouble()
    val formatter = DecimalFormat("###,###,###.##")
    val formatRp = DecimalFormatSymbols()
    formatRp.groupingSeparator = '.'
    formatRp.decimalSeparator = ','
    formatter.decimalFormatSymbols = formatRp
    return "Rp. " + formatter.format(currency)
}