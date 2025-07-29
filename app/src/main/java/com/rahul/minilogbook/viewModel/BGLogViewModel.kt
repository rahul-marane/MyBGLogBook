package com.rahul.minilogbook.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.minilogbook.data.BGLogEntry
import com.rahul.minilogbook.data.BGLogRepository
import com.rahul.minilogbook.data.BGUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BGLogViewModel @Inject constructor(private val bgLogRepository: BGLogRepository) : ViewModel() {

    private val _unit = MutableStateFlow(BGUnit.mg_dL)
    val unit : StateFlow<BGUnit> = _unit

    private val _inputValue = MutableStateFlow("")
    val inputValue: StateFlow<String> = _inputValue

    //var unit by mutableStateOf("mg/dL")
    //var inputValue by mutableStateOf("")

    val entries = bgLogRepository.getAllEntries()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList())

    val average : StateFlow<String> = entries.combine(unit) { list, unit ->
        val values  = list.map {
            if (it.unit == unit)
                it.value
            else convert(it.value, it.unit, unit)
        }
        if (values.isNotEmpty()) "%.2f".format(values.average()) else ""

    }.stateIn(viewModelScope, SharingStarted.Lazily,"")


    fun onUnitChange(u: BGUnit) {
        val oldValue = inputValue.value.toDoubleOrNull()
        if (oldValue != null) {
            val newValue = convert(oldValue, unit.value, u)
            _inputValue.value = "%.2f".format(newValue)
        }
        _unit.value = u
    }

    fun onInputChange(new: String) { _inputValue.value = new }


    fun save () {
        val value = inputValue.value.toDoubleOrNull()
        if (value != null && value >= 0) {
            viewModelScope.launch {
                bgLogRepository.insert(BGLogEntry(value = value, unit = unit.value))
            }
            _inputValue.value = ""
        }
    }




    private fun convert(value: Double, from: BGUnit, to: BGUnit): Double {
        return when {
            from == to -> value
                from == BGUnit.mmol_L -> value * 18.0182
            else -> value / 18.0182
        }
    }
}