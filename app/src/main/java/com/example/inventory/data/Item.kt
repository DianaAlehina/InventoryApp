package com.example.inventory.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.text.NumberFormat

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val itemName: String,
    @ColumnInfo(name = "price")
    val itemPrice: Double,
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int,

    @ColumnInfo(name = "providerName")
    val providerName: String,
    @ColumnInfo(name = "providerEmail")
    val providerEmail: String,
    @ColumnInfo(name = "providerPhone")
    val providerPhone: String,

    @ColumnInfo(name = "Record")
    var record: Record = Record.manual
) {
    override fun toString(): String {
        return "${itemName}\n" +
                "₽${itemPrice}\n" +
                "Quantity in Stock: ${quantityInStock}\n" +
                "Provider name: ${providerName}\n" +
                "Provider email: ${providerEmail}\n" +
                "Provider phone: $providerPhone"
    }
}

fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)

enum class Record{
    manual, file
}
