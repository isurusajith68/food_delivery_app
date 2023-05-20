import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.myapplication.model.Cart

class CartDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        private const val DATABASE_NAME = "cart.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "cart"
        const val COLUMN_ID = "_id"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_PRODUCT_ID = "product_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_QUANTITY = "quantity"
        private const val COLUMN_TOTAL = "total"
        private const val COLUMN_URI = "image"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CART_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_ID INTEGER, " +
                "$COLUMN_PRODUCT_ID INTEGER, " +
                "$COLUMN_NAME INTEGER, " +
                "$COLUMN_PRICE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_QUANTITY INTEGER, " +
                "$COLUMN_TOTAL REAL, " +
                "$COLUMN_URI TEXT );"
        db?.execSQL(CREATE_CART_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_CART_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_CART_TABLE)
        onCreate(db)
    }

    fun addToCart(
        id: Int, userId: Int, productId: Long,
        name:String, price: String, description: String, quantity: Int, total: Double, image: Uri
    ): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_PRODUCT_ID, productId)
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_TOTAL, total)
            put(COLUMN_URI, image.toString())
        }
        val db = writableDatabase
        val added = db.insert(TABLE_NAME, null, values)
        db.close()
        return added != -1L
    }

    fun getCartList(userId: Int): List<Cart> {
        val cartItems = mutableListOf<Cart>()
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USER_ID = $userId"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val itemId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))
            val itemProductId = cursor.getLong(cursor.getColumnIndex(COLUMN_PRODUCT_ID))
            val itemName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val itemPrice = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE))
            val itemDescription = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            val itemQuantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
            val itemTotal = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL))
            val itemUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URI)))
            val cartItem = Cart(id,itemId, itemProductId, itemName ,itemPrice, itemDescription, itemQuantity, itemTotal, itemUri)
            cartItems.add(cartItem)
        }
        cursor.close()
        db.close()
        return cartItems
    }


    fun updateCartItem(cart: Cart) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUANTITY, cart.quantity)
            put(COLUMN_TOTAL, cart.total)
        }

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(cart.id.toString())
        db.update(TABLE_NAME, values, selection, selectionArgs)
        db.close()
    }




}
