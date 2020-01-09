package ru.apliter.evotor_digital_library.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Single
import kotlinx.android.synthetic.main.book_return_fragment.*
import ru.apliter.data.entities.DataBook
import ru.apliter.evotor_digital_library.R
import java.util.concurrent.TimeUnit

class BookReturnFragment : Fragment(R.layout.book_return_fragment) {
    private lateinit var viewModel: MainViewModel
    private lateinit var book: DataBook
    private var bookId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        observeViewModel(viewModel, this)
        bookId = arguments?.getString("BOOK_ID")
        bookId?.let {
            label_return_status.text = "Запрашиваю книгу"
            viewModel.startGettingBook(it)
        } ?: run {
            activity?.onBackPressed()
        }

        rate_one.setOnClickListener(getRateClickListener())
        rate_two.setOnClickListener(getRateClickListener())
        rate_three.setOnClickListener(getRateClickListener())
        rate_four.setOnClickListener(getRateClickListener())
        rate_five.setOnClickListener(getRateClickListener())

    }


    private fun observeViewModel(viewModel: MainViewModel, lifeCycleOwner: LifecycleOwner) {
        viewModel.getBookObservable()
            .observe(lifeCycleOwner, Observer {
                book = it
                label_return_status.text = "Возвращаю книгу"
                viewModel.returnBook()
            })


        viewModel.getBookErrorObservable()
            .observe(lifeCycleOwner, Observer {
                label_return_status.text = "Ошибка!"
                label_error.text = it.cause.toString()
                ok_image.setImageResource(R.drawable.ic_error)
                return_progress.visibility = View.GONE
                ok_image.visibility = View.VISIBLE
                waitAndExit(4)
            })

        viewModel.getBookReturnObservable()
            .observe(lifeCycleOwner, Observer {
                if (it == "ok") {
                    return_progress.visibility = View.GONE
                    label_return_status.text =
                        "Спасибо, что вернули книгу.\n Оцените, понравилась\nли она вам?"
                    ok_image.visibility = View.VISIBLE
                    rate_layout.visibility = View.VISIBLE
                    waitAndExit(15)
                }
            })
    }

    @SuppressLint("CheckResult")
    private fun waitAndExit(time: Long) {
        Single.just(time)
            .delay(time, TimeUnit.SECONDS)
            .subscribe({
                exit()
            }, {

            })
    }

    private fun getRateClickListener() = View.OnClickListener {
        var newRate = 0
        when (it) {
            rate_one -> {
                rate_one.setImageResource(R.drawable.ic_star)
                newRate = 1
            }
            rate_two -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                newRate = 2
            }
            rate_three -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                rate_three.setImageResource(R.drawable.ic_star)
                newRate = 3
            }
            rate_four -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                rate_three.setImageResource(R.drawable.ic_star)
                rate_four.setImageResource(R.drawable.ic_star)
                newRate = 4
            }
            rate_five -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                rate_three.setImageResource(R.drawable.ic_star)
                rate_four.setImageResource(R.drawable.ic_star)
                rate_five.setImageResource(R.drawable.ic_star)
                newRate = 5
            }
        }
        if (newRate != 0) {
            viewModel.setRate(newRate)
        }
        waitAndExit(1)
    }

    private fun exit() {
        activity?.supportFragmentManager?.apply {
            fragments.let {fragments->
                if (fragments.contains(this@BookReturnFragment)) {
                    this.beginTransaction().remove(this@BookReturnFragment).commit()
                    this.popBackStack()
                }
            }
        }
    }
}