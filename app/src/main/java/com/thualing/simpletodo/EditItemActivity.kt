package com.thualing.simpletodo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.thualing.simpletodo.MainActivity.Companion.ITEM_POSITION
import com.thualing.simpletodo.MainActivity.Companion.ITEM_TEXT
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AppCompatActivity() {

    var position = 0
    var etItemText : EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)


        supportActionBar?.title = "Set Item"
        etItemText = edItem
        etItemText?.setText(intent.getStringExtra(ITEM_TEXT))
        position = intent.getIntExtra(ITEM_POSITION, 0)
    }

    fun onSaveItem(v : View) {
        val i = Intent()
        i.putExtra(ITEM_TEXT, etItemText?.text.toString())
        i.putExtra(ITEM_POSITION, position)
        setResult(Activity.RESULT_OK, i)
        finish()
    }
}
