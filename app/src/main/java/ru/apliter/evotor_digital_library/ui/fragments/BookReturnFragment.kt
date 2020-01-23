package ru.apliter.evotor_digital_library.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import io.reactivex.Single
import kotlinx.android.synthetic.main.book_return_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.apliter.data.entities.DataBook
import ru.apliter.evotor_digital_library.R
import ru.apliter.evotor_digital_library.R.drawable.ic_error
import ru.apliter.evotor_digital_library.R.drawable.ic_star
import ru.apliter.evotor_digital_library.R.string.*
import ru.apliter.evotor_digital_library.ui.MainViewModel
import java.util.concurrent.TimeUnit

class BookReturnFragment : Fragment(R.layout.book_return_fragment) {
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var book: DataBook
    private var bookId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel(viewModel, viewLifecycleOwner)
        bookId = arguments?.getString("BOOK_ID")
        bookId?.let {
            label_return_status.text = getString(get_book_request)
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
                label_return_status.text = getString(return_book_request)
                viewModel.returnBook()
            })


        viewModel.getBookErrorObservable()
            .observe(lifeCycleOwner, Observer {
                label_return_status.text = getString(error_label)
                label_error.text = it.cause.toString()
                ok_image.setImageResource(ic_error)
                return_progress.visibility = View.GONE
                ok_image.visibility = View.VISIBLE
                waitAndExit(4)
            })

        viewModel.getBookReturnObservable()
            .observe(lifeCycleOwner, Observer {
                if (it == "ok") {
                    return_progress.visibility = View.GONE
                    label_return_status.text = getString(success_return_book)
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
                rate_one.setImageResource(ic_star)
                newRate = 1
            }
            rate_two -> {
                rate_one.setImageResource(ic_star)
                rate_two.setImageResource(ic_star)
                newRate = 2
            }
            rate_three -> {
                rate_one.setImageResource(ic_star)
                rate_two.setImageResource(ic_star)
                rate_three.setImageResource(ic_star)
                newRate = 3
            }
            rate_four -> {
                rate_one.setImageResource(ic_star)
                rate_two.setImageResource(ic_star)
                rate_three.setImageResource(ic_star)
                rate_four.setImageResource(ic_star)
                newRate = 4
            }
            rate_five -> {
                rate_one.setImageResource(ic_star)
                rate_two.setImageResource(ic_star)
                rate_three.setImageResource(ic_star)
                rate_four.setImageResource(ic_star)
                rate_five.setImageResource(ic_star)
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
            fragments.let { fragments ->
                if (fragments.contains(this@BookReturnFragment)) {
                    this.beginTransaction().remove(this@BookReturnFragment).commit()
                    this.popBackStack()
                }
            }
        }
    }
}