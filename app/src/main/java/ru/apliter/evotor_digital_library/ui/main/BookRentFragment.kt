package ru.apliter.evotor_digital_library.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Single
import kotlinx.android.synthetic.main.book_rent_fragment.*
import ru.apliter.data.entities.DataBook
import ru.apliter.evotor_digital_library.R
import ru.apliter.evotor_digital_library.R.drawable.*
import ru.apliter.evotor_digital_library.R.string.*
import ru.evotor.devices.commons.DeviceServiceConnector
import java.util.concurrent.TimeUnit

class BookRentFragment : Fragment(R.layout.book_rent_fragment) {
    private lateinit var viewModel: MainViewModel
    private lateinit var book: DataBook
    private var bookId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        observeViewModel(viewModel, this)
        bookId = arguments?.getString("BOOK_ID")
        bookId?.let {
            label_rent_status.text = getString(get_book_request)
            viewModel.startGettingBook(it)
        } ?: run {
            activity?.onBackPressed()
        }
        DeviceServiceConnector.startInitConnections(activity?.applicationContext)
    }

    private fun observeViewModel(viewModel: MainViewModel, lifeCycleOwner: LifecycleOwner) {
        viewModel.getBookErrorObservable()
            .observe(lifeCycleOwner, Observer {
                label_rent_status.text = getString(error_label)
                label_error.text = it.cause.toString()
                ok_image.setImageResource(ic_error)
                rent_progress.visibility = View.GONE
                ok_image.visibility = View.VISIBLE
                waitAndExit(4)
            })

        viewModel.getBookObservable()
            .observe(lifeCycleOwner, Observer {
                label_rent_status.text = getString(rent_book_request)
                book = it
                viewModel.rentBook()
            })

        viewModel.getBookRentObservable()
            .observe(lifeCycleOwner, Observer {
                rent_progress.visibility = View.GONE
                label_rent_status.text = getString(success_rent_book, it)
                ok_image.visibility = View.VISIBLE
                waitAndExit(3)
            })
    }

    @SuppressLint("CheckResult")
    private fun waitAndExit(time: Long) {
        Single.just(time)
            .delay(time, TimeUnit.SECONDS)
            .subscribe({
                activity?.supportFragmentManager?.let { fm ->
                    fm.beginTransaction().remove(this).commit()
                    fm.popBackStack()
                }
            }, {

            })
    }

}