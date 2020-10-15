package com.example.chatvol4at

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

private lateinit var auth: FirebaseAuth
var TAG = "SignInActivity"
var loginModeActive = false
val database = Firebase.database
val users = database.reference.child("users")

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = Firebase.auth

        if (auth.currentUser != null) {
            startActivity(Intent(this, UserList::class.java))
        }
        toggle.setOnClickListener {
            if (loginModeActive) {
                loginModeActive = false
                loginSignUpButton.text = "SIGN UP"
                toggle.text = "Or, LOG IN"
                repeatPasswordEditText.visibility = EditText.VISIBLE
            } else {
                loginModeActive = true
                loginSignUpButton.text = "LOG IN"
                toggle.text = "Or, SIGN UP"
                repeatPasswordEditText.visibility = EditText.GONE
            }
        }



        loginSignUpButton.setOnClickListener {
            loginSignUpUser(emailEditText.text.toString().trim(), passwordEditText.text.toString())
        }
    }

    private fun loginSignUpUser(email: String, password: String) {
        if (loginModeActive) {
            when {
                passwordEditText.text.toString().length < 6 -> {
                    Toast.makeText(
                        this,
                        "Password must be at least 6 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                emailEditText.text.toString() == "" -> {
                    Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                val intent = Intent(this, UserList::class.java)
                                intent.putExtra("userName", nameEditText.text.toString())
                                startActivity(intent)
                                // updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    this, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // updateUI(null)
                                // ...
                            }

                            // ...
                        }
                }
            }

        } else {
            when {
                passwordEditText.text.toString() != repeatPasswordEditText.text.toString() -> {
                    Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                }
                passwordEditText.text.toString().length < 6 -> {
                    Toast.makeText(
                        this,
                        "Password must be at least 6 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                emailEditText.text.toString() == "" -> {
                    Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                createUser(user)
                                val intent = Intent(this, UserList::class.java)
                                intent.putExtra("userName", nameEditText.text.toString())
                                startActivity(intent)
                                // updateUI(user)

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    this, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //  updateUI(null)
                            }

                            // ...
                        }
                }
            }
        }

    }

    private fun createUser(fireBaseUser: FirebaseUser?) {
        var user = User()
        if (fireBaseUser != null) {
            user.id = fireBaseUser.uid
            user.email = fireBaseUser.email.toString()
            user.nickname  = nameEditText.text.toString()
        }
        users.push().setValue(user)
    }

}