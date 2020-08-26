package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChooseUserActivity : AppCompatActivity() {

    var chooseUserListView: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    var keys: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        chooseUserListView = findViewById(R.id.chooseUserListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        chooseUserListView?.adapter = adapter

        FirebaseDatabase.getInstance().reference.child("users").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val email = snapshot.child("email").value as String
                emails.add(email)
                keys.add(snapshot.key!!)
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}

        })

        chooseUserListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val snapMap: Map<String, String> = mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email!!,
                                                     "imagename" to intent.getStringExtra("imagename"),
                                                     "imageurl" to intent.getStringExtra("imageurl"),
                                                     "message" to intent.getStringExtra("message"))

            FirebaseDatabase.getInstance().reference.child("users").child(keys[position]).child("snaps").push().setValue(snapMap)

            val intent = Intent(this, SnapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}