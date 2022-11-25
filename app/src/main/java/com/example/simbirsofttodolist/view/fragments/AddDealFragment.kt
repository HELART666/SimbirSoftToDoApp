package com.example.simbirsofttodolist.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.simbirsofttodolist.R
import com.example.simbirsofttodolist.viewModel.DealModel
import com.example.simbirsofttodolist.database.Deal
import com.example.simbirsofttodolist.database.DealsDB
import com.example.simbirsofttodolist.databinding.DealaddFragmentBinding
import com.example.simbirsofttodolist.textAndGraphicsUtils.TimeStampConverter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddDealFragment: BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val dataModel: DealModel by activityViewModels()
    private lateinit var binding: DealaddFragmentBinding
    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    private val timeStampConverter = TimeStampConverter()


    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DealaddFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickDate()

        binding.addBtn.setOnClickListener(){
            val name = binding.inputDealName.text
            val desc = binding.inputDescription.text
            val db = DealsDB.getDB(requireActivity())
            if(name.toString().isEmpty()){
                Toast.makeText(requireActivity(), getString(R.string.dealNameError), Toast.LENGTH_SHORT).show()
            } else if(desc.toString().isEmpty()){
                Toast.makeText(requireActivity(), getString(R.string.dealDescriptionError), Toast.LENGTH_SHORT).show()
            } else if(
                timeStampConverter.setFinishDate(
                    savedMonth.toString(),
                    savedDay.toString(),
                    savedYear.toString(),
                    savedHour.toString(),
                    savedMinute.toString(),
                    "00"
                ) < timeStampConverter.setStartDate().toLong()){
                Toast.makeText(requireActivity(), getString(R.string.dateInputError), Toast.LENGTH_SHORT).show()
            }
            else if(name.toString().isNotEmpty() && desc.toString().isNotEmpty() &&
                dateCheck(timeStampConverter.setStartDate().toLong(),
                    timeStampConverter.setFinishDate(
                    savedMonth.toString(),
                    savedDay.toString(),
                    savedYear.toString(),
                    savedHour.toString(),
                    savedMinute.toString(),
                    "00"
                ))
            ){
                val deal = Deal(
                    null,
                    timeStampConverter.setStartDate().toLong(),
                    timeStampConverter.setFinishDate(
                        savedMonth.toString(),
                        savedDay.toString(),
                        savedYear.toString(),
                        savedHour.toString(),
                        savedMinute.toString(),
                        "00"
                    ),
                    name.toString(),
                    desc.toString()
                )
                Thread{
                    db.getDao().insertDeal(deal)
                }.start()

                dataModel.deal.value = deal
                binding.apply {
                    inputDealName.text.clear()
                    inputDescription.text.clear()
                }
            }
        }
    }


    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.DAY_OF_MONTH)
        minute = cal.get(Calendar.MINUTE)
    }


    private fun pickDate(){
        binding.setDateBtn.setOnClickListener(){
            getDateTimeCalendar()
            DatePickerDialog(requireActivity(), this,  year, month, day).show()
        }
    }


    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = p3
        savedMonth = p2 + 1
        savedYear = p1
        getDateTimeCalendar()
        TimePickerDialog(requireActivity(), this, hour, minute, true).show()
    }


    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour = p1
        savedMinute = p2
    }

    private fun dateCheck(dateStart: Long, dateFinish: Long): Boolean {
        return (timeStampConverter.setFinishDate(
            savedMonth.toString(),
            savedDay.toString(),
            savedYear.toString(),
            savedHour.toString(),
            savedMinute.toString(),
            "00"
        ) > timeStampConverter.setStartDate().toLong())
    }

}