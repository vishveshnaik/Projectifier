package com.princeakash.projectified.view.faq

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.princeakash.projectified.R

class NewFaqDialogFragment: DialogFragment() {
    var editText: EditText? = null
    lateinit var listener: NewFaqDialogListener
    val TAG = "NewFaqDialogFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as NewFaqDialogListener
        }catch (e: ClassCastException){
            Log.d(TAG, "onAttach: NewFaqDialogListener must be implemented")
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_new_faq, null)
        editText = view.findViewById(R.id.editTextNewFaq)
        builder
                .setTitle("Submit New Question")
                .setView(view)
                .setPositiveButton("Submit", DialogInterface.OnClickListener { _,_ ->
                    if(editText!!.text.isNullOrEmpty()){
                        editText!!.error = "Enter a valid question"
                        return@OnClickListener
                    }
                    listener.onDialogPositiveClick(this, question = editText!!.text.toString())
                })
                .setNegativeButton("Cancel", { _, _ -> listener.onDialogNegativeClick(this) })
        return builder.create()
    }

    interface NewFaqDialogListener{
        fun onDialogPositiveClick(dialogFragment: DialogFragment, question: String)
        fun onDialogNegativeClick(dialogFragment: DialogFragment)
    }
}