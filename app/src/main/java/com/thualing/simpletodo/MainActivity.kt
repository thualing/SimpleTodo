package com.thualing.simpletodo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        val EDIT_REQUEST_CODE = 20
        val ITEM_TEXT = "itemText"
        val ITEM_POSITION = "itemPosition"

    }

    lateinit var itemsView : ListView
    var items = ArrayList<String>()
    lateinit var itemsAdapter : ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup
        readItems()
        itemsAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, items)
        itemsView = findViewById(R.id.lvItems)
        itemsView.adapter = itemsAdapter

//        items.add("One")
//        items.add("two")
        onDeleteItem(itemsView)
    }

    fun onAddItem(v : View) {
        var itemText = etItem.text.toString()
        itemsAdapter.add(itemText)
        etItem.setText("")
        writeItems()
        Toast.makeText(applicationContext, "Item added to list", Toast.LENGTH_SHORT).show()
    }

    private fun onDeleteItem(v : View) {
        Log.i("MainAtivity", "setting up listener on current view")
        lvItems.setOnItemLongClickListener { parent, view, position, id ->
            Toast.makeText(this, "item deleted: " + items.get(position), Toast.LENGTH_SHORT).show()
            Log.i("MainActivity", "Item removed from list: " + position)
            items.removeAt(position)
            itemsAdapter.notifyDataSetChanged()
            writeItems()
            true
        }

        // setup a regular click listener to edit the item
        lvItems.setOnItemClickListener { parent, view, position, id ->
            // create new activity
            val intent = Intent(this@MainActivity, EditItemActivity::class.java)
            // pass the data being edited
            intent.putExtra(ITEM_TEXT, items.get(position))
            intent.putExtra(ITEM_POSITION, position)
            // display the activity
            startActivityForResult(intent, EDIT_REQUEST_CODE)
        }
    }

    private fun getDateFile(): File {
        return File(filesDir, "todo.txt")
    }

    private fun readItems() {
        try {
            items = ArrayList<String>()
            getDateFile().readLines().forEach {
                items.add(it)
            }
        }
        catch (e : IOException) {
            Log.e("MainActivity" ,"Error Reading File" + e)
            items = ArrayList<String>()
        }
    }

    private fun writeItems() {
        try {
            getDateFile().printWriter().use { out ->
                items.forEach{
                    out.println(it)
                }
            }
        }
        catch (e : IOException) {
            Log.e("MainActivity", "Error writing file" + e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            var updatedItem = data?.extras?.getString(ITEM_TEXT).toString()
            var pos = data?.extras?.getInt(ITEM_POSITION)!!
            items.set(pos, updatedItem)
            itemsAdapter.notifyDataSetChanged()
            writeItems()
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show()
        }
    }
}


