package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (auth.currentUser != null) {
            login()
        }
    }

    fun goClicked(view: View) {
        //check if we can login user
        auth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    login()
                } else {
                    //sign up user
                    auth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FirebaseDatabase.getInstance().reference.child("users").child(task.result?.user?.uid.toString()).child("email").setValue(emailEditText?.text.toString())
                                login()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(baseContext, "Login Failed, Try Again.",Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
    }

    private fun login() {
        //move to next activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}