package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Create a new user object
            val user = User(name, email, password)

            // Add the user to the database
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_NAME, user.name)
                put(DatabaseHelper.COLUMN_EMAIL, user.email)
                put(DatabaseHelper.COLUMN_PASSWORD, user.password)
            }

            db.insert(DatabaseHelper.TABLE_USERS, null, values)

            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()

            finish()
        }
    }
}