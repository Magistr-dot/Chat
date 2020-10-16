package com.example.chatvol4at

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

const val RC_IMAGE_PICKER = 123

private lateinit var auth: FirebaseAuth
class ChatActivity : AppCompatActivity() {
    private val storage :FirebaseStorage = FirebaseStorage.getInstance()
    var chatImagesStorage = storage.reference.child("chat_images")
    var userName = ""
    private val database = Firebase.database
    private val myRef = database.reference.child("message")


    private val userRef = database.reference.child("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val intent = intent
        var recipientUserId = intent.getStringExtra("recipientUserId")
        userName = intent?.getStringExtra("userName")?: "Default"





        val list = ArrayList<ChatMessage>()
        val adapter = ChatMessageAdapter(this, R.layout.message, list)
        messageListView.adapter = adapter

        progressBar.visibility = ProgressBar.INVISIBLE

        messageIdText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendMessageButton.isEnabled = s.toString().isNotEmpty()
            }
        })

        sendMessageButton.setOnClickListener {
            val ourMessage = ChatMessage()
            ourMessage.imageUrl = null
            ourMessage.name = userName
            ourMessage.text = messageIdText.text.toString()
            ourMessage.sender = auth.currentUser?.uid.toString()
            ourMessage.recipient = recipientUserId.toString()
            myRef.push().setValue(ourMessage)
            messageIdText.setText("")

        }
        sendPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true)
            startActivityForResult(Intent.createChooser(intent,"Choose an image"),RC_IMAGE_PICKER)

        }
        val userEventListener: ChildEventListener
        userEventListener =  object :ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    if (user.id == FirebaseAuth.getInstance().currentUser?.uid) {
                        userName= user.nickname
                    }
                }
        }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        userRef.addChildEventListener(userEventListener)
    val childEventListener: ChildEventListener
        childEventListener = object :ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessage::class.java)
                if (message != null) {
                    if(message.sender == auth.currentUser?.uid && message.recipient == recipientUserId) {
                        adapter.add(message)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        myRef.addChildEventListener(childEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInf = menuInflater
        menuInf.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.sign_out -> {FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,SignInActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            val imageRef = selectedImageUri?.lastPathSegment?.let { chatImagesStorage.child(it) }

            var uploadTask = selectedImageUri?.let { imageRef?.putFile(it) }

            if (imageRef != null) {
                uploadTask = selectedImageUri.let { imageRef.putFile(it) }
            }

            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef?.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    var chatMessage = ChatMessage()
                    chatMessage.imageUrl = downloadUri.toString()
                    chatMessage.name = userName
                    chatMessage.text = ""
                    myRef.push().setValue(chatMessage)
                } else {
                    // Handle failures
                    // ...
                }
            }

        }
    }
}