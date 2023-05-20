package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (!isValidEmail(email)) {
                emailEditText.error = "Invalid email address"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            // Check if the user is an admin
            if (email == "admin@example.com" && password == "admin123") {
                // Start the admin activity
                val intent = Intent(this, AdminHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Look up the user in the database
                val dbHelper = DatabaseHelper(this)
                val db = dbHelper.readableDatabase

                val projection = arrayOf(DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME)
                val selection = "${DatabaseHelper.COLUMN_EMAIL} = ? AND ${DatabaseHelper.COLUMN_PASSWORD} = ?"
                val selectionArgs = arrayOf(email, password)

                val cursor = db.query(
                    DatabaseHelper.TABLE_USERS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
                )

                // Check if the user was found
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    val userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                    val userName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))

                    // Start the user activity
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("userName", userName)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }

                cursor.close()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun onRegisterButtonClicked(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}