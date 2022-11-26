package com.example.simbirsofttodolist.view.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.utils.calendar
import com.applandeo.materialcalendarview.utils.midnightCalendar
import com.example.simbirsofttodolist.viewModel.DealModel
import com.example.simbirsofttodolist.R
import com.example.simbirsofttodolist.view.adapters.SwipeToDeleteCallback
import com.example.simbirsofttodolist.database.Deal
import com.example.simbirsofttodolist.database.DealsDB
import com.example.simbirsofttodolist.databinding.ActivityMainBinding
import java.time.LocalDateTime
import com.example.simbirsofttodolist.textAndGraphicsUtils.TimeStampConverter
import com.example.simbirsofttodolist.view.fragments.AddDealFragment
import com.example.simbirsofttodolist.view.adapters.DealAdapter
import java.util.*


class MainActivity : AppCompatActivity() {
    val timeStampConverter = TimeStampConverter()
    private lateinit var bindingClass: ActivityMainBinding

    private val dealModel: DealModel by viewModels()
    private val bottomSheetDialogFragment = AddDealFragment()
    private val adapter = DealAdapter()
    private val notes = mutableMapOf<EventDay, String>()
    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(bindingClass.root)
        val db = DealsDB.getDB(this)
        var clickDay = Calendar.getInstance().timeInMillis / 1000

        if(adapter.itemCount != 0){
            bindingClass.apply {
                setGoneImage()
            }
        } else{
            bindingClass.apply {
                setVisibleImage()
            }
        }

        dealModel.deal.observe(this) {
            val time =
                android
                    .text
                    .format
                    .DateFormat
                    .format("dd-MM-yyyy", it.dateFinish * 1000L).toString().split('-')
            if(time[0] == LocalDateTime.now().dayOfMonth.toString()){
                adapter.clearList()
                db.getDao().getDealsForDate(
                    timeStampConverter.setNullFinishDate(clickDay),
                    timeStampConverter.getDate(clickDay)
                ).asLiveData()
                    .observe(this@MainActivity){ list ->
                        checkItems()
                        if(list.isNotEmpty()){
                            list.forEach {
                                setGoneImage()
                                adapter.addAllDeals(Deal(it.id, it.dateStart, it.dateFinish, it.name, it.description))
                            }
                        } else{
                            bindingClass.apply {
                                setVisibleImage()
                            }
                        }
                    }
            }
            //Toast.makeText(this, "${timeStampConverter.setNullFinishDate(clickDay)}", Toast.LENGTH_LONG).show()
            //Toast.makeText(this, "${timeStampConverter.getDate(clickDay)}", Toast.LENGTH_LONG).show()

            checkItems()
        }

        checkItems()

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val currentDealPos = adapter.getDealId(position)
                //adapter.clearList()
                Thread{
                    db.getDao().deleteDeal(currentDealPos)
                }.start()
                adapter.removeItem(position)
                if(adapter.itemCount == 0){
                    setVisibleImage()
                    checkItems()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(bindingClass.rcView)


        bindingClass.apply {
            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            rcView.adapter = adapter


            plusBtn.setOnClickListener(){
                bottomSheetDialogFragment.show(supportFragmentManager, "BottomSheetDialog")
            }

            calendarView.setOnDayClickListener(object : OnDayClickListener {
                override fun onDayClick(eventDay: EventDay) {
                    clickDay = eventDay.calendar.timeInMillis / 1000

                adapter.clearList()
                db.getDao().getDealsForDate(
                    clickDay,
                    timeStampConverter.getDate(clickDay)
                ).asLiveData()
                    .observe(this@MainActivity){ list ->
                        checkItems()
                        if(list.isNotEmpty()){
                            list.forEach {
                                setGoneImage()
                                adapter.addAllDeals(Deal(it.id, it.dateStart, it.dateFinish, it.name, it.description))
                            }
                        } else{
                            bindingClass.apply {
                                setVisibleImage()
                            }
                        }
                }
            }
            })
        }
    }
    private fun checkItems(){
        val db = DealsDB.getDB(this)
        db.getDao().getAllDeals().asLiveData().observe(this){
            notes.clear()
            it.forEach{ list ->
                val calendar = midnightCalendar
                calendar.timeInMillis = list.dateFinish * 1000
                val note = ""
                val eventDay = EventDay(calendar, R.drawable.icons)
                notes[eventDay] = note
            }
            bindingClass.calendarView.setEvents(notes.keys.toList())
        }
    }
    private fun setVisibleImage(){
        bindingClass.apply {
            rcView.visibility = View.GONE
            listEmptyImage.visibility = View.VISIBLE
            listEmptyText.visibility = View.VISIBLE
        }
    }
    private fun setGoneImage(){
        bindingClass.apply {
            rcView.visibility = View.VISIBLE
            listEmptyImage.visibility = View.GONE
            listEmptyText.visibility = View.GONE
        }
    }
}