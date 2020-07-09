package com.udacity.gradle.builditbigger.reportPost

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.udacity.gradle.builditbigger.R
import com.udacity.gradle.builditbigger.constants.Constants
import com.udacity.gradle.builditbigger.richTextEditor.RichEditor
import com.udacity.gradle.builditbigger.richTextEditor.RichEditorToolBar

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        val toolBar = findViewById<RichEditorToolBar>(R.id.richEditorToolBar2)
        toolBar.setRichEditor(findViewById(R.id.editor))
        val editor = findViewById<View>(R.id.editor) as RichEditor
        editor.setEditorFontColor("#369F77")
        val ccId = intent.getStringExtra("ccID")
        val postId = intent.getStringExtra("pushId")
        findViewById<View>(R.id.submit_imageButton).setOnClickListener { view: View? ->
            Constants.DATABASE.child("reportedposts/" + ccId + "/" + postId + "/list/" + Constants.UID).setValue(editor.html
            ) { databaseError: DatabaseError?, databaseReference: DatabaseReference? ->
                if (databaseError == null) {
                    Constants.DATABASE.child("reportedposts/$ccId/$postId/num")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val num = dataSnapshot.childrenCount
                                    if (num >= 3) {
                                    } else {
                                        Constants.DATABASE.child("reportedposts/$ccId/$postId/num").setValue(num + 1)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {}
                            })
                }
            }
        }
    }
}