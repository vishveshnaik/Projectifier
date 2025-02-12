package com.princeakash.projectified.view.faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.princeakash.projectified.R
import com.princeakash.projectified.databinding.FragmentAllFaqBinding
import com.princeakash.projectified.viewmodel.FaqViewModel
import com.princeakash.projectified.adapter.FaqAdapter

class FaqActivity : AppCompatActivity(), NewFaqDialogFragment.NewFaqDialogListener {

    lateinit var faqViewModel: FaqViewModel

    private lateinit var binding: FragmentAllFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAllFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

        faqViewModel = ViewModelProvider(this).get(FaqViewModel::class.java)

        subscribeToObservers()

        if(savedInstanceState==null)
            faqViewModel.getAllFaq()

        binding.recyclerViewFaq.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        binding.fab.setOnClickListener{
            //New Dialog Box
            val dialogFragment = NewFaqDialogFragment()
            dialogFragment.show(supportFragmentManager, "NewFaqDialogFragment")
        }
    }

    private fun subscribeToObservers() {
        faqViewModel.responseGetFaq().observe(this, {
            //Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            binding.recyclerViewFaq.adapter = FaqAdapter(it.faqList)
        })
        faqViewModel.responseAddQuestion().observe(this, {
            it?.getContentIfNotHandled()?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })
        faqViewModel.errorString().observe(this, {
            it?.getContentIfNotHandled()?.let{
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDialogPositiveClick(dialogFragment: DialogFragment, question: String) {
        faqViewModel.addQuestion(question)
    }

    override fun onDialogNegativeClick(dialogFragment: DialogFragment) {
        dialogFragment.dismiss()
    }
}