package ru.apliter.evotor_digital_library.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_fragment.*
import ru.apliter.evotor_digital_library.R
import java.text.SimpleDateFormat
import java.util.*

class BookItemFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var rootView: View
    private var bookId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.book_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        observeViewModel(viewModel, this)
        bookId = arguments?.getString("BOOK_ID")

        bookId?.let {
            viewModel.startGettingBook(it)
        } ?: run {
            activity?.onBackPressed()
        }

        book_rent_btn.setOnClickListener {
            rentBook(bookId)
        }
        book_return_btn.setOnClickListener {
            returnBook(bookId)
        }
        book_prolong_btn.setOnClickListener {
            prolongBook(bookId)
        }
    }

    private fun prolongBook(bookId: String?) {
        val rentFragment = BookRentFragment()
        val args = Bundle()
        args.putString("BOOK_ID", bookId)
        rentFragment.arguments = args
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.container, rentFragment)
                .remove(this)
                .commit()
        }
    }

    private fun returnBook(bookId: String?) {
        val returnFragment = BookReturnFragment()
        val args = Bundle()
        args.putString("BOOK_ID", bookId)
        returnFragment.arguments = args
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, returnFragment)
            ?.remove(this)?.commit()
    }

    private fun rentBook(bookId: String?) {
        val rentFragment = BookRentFragment()
        val args = Bundle()
        args.putString("BOOK_ID", bookId)
        rentFragment.arguments = args
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.container, rentFragment)
                .remove(this)
                .commit()
        }
    }

    private fun observeViewModel(viewModel: MainViewModel, lifeCycleOwner: LifecycleOwner) {
        viewModel.getBookObservable()
            .observe(lifeCycleOwner, Observer {
                book_title.text = it.title
                book_about.text = it.preview
                book_author.text = it.author
                setRate(it.rate)

                it.uri?.let { url ->
                    Picasso.get().load(url).into(book_image)
                    book_image.visibility = View.VISIBLE
                }
                it.vacant.let { isVacant ->
                    if (isVacant) {
                        book_rent_btn.visibility = View.VISIBLE
                        rate_layout.visibility = View.VISIBLE
                        label_rate.visibility = View.VISIBLE
                        label_return_date.visibility = View.GONE
                        book_return_date.visibility = View.GONE
                    } else {
                        book_return_date.text = SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.getDefault()
                        ).format(it.returnDate)
                        book_rent_btn.visibility = View.GONE
                        rate_layout.visibility = View.GONE
                        label_rate.visibility = View.GONE
                        book_prolong_btn.visibility = View.VISIBLE
                        book_return_btn.visibility = View.VISIBLE
                        label_return_date.visibility = View.VISIBLE
                        book_return_date.visibility = View.VISIBLE
                    }
                }
            })

        viewModel.getLoadingObservable()
            .observe(lifeCycleOwner, Observer {
                if (it) {
                    book_detail_loading_pb.visibility = View.VISIBLE
                    book_title.visibility = View.GONE
                    book_about.visibility = View.GONE
                    book_author.visibility = View.GONE
                    book_image.visibility = View.GONE
                    label_about.visibility = View.GONE
                    label_author.visibility = View.GONE
                    label_title.visibility = View.GONE
                } else {
                    book_detail_loading_pb.visibility = View.GONE
                    book_title.visibility = View.VISIBLE
                    book_about.visibility = View.VISIBLE
                    book_author.visibility = View.VISIBLE
                    book_image.visibility = View.VISIBLE
                    label_about.visibility = View.VISIBLE
                    label_author.visibility = View.VISIBLE
                    label_title.visibility = View.VISIBLE
                }
            })

        viewModel.getBookErrorObservable()
            .observe(lifeCycleOwner, Observer {
                Snackbar.make(rootView, it.localizedMessage, Snackbar.LENGTH_LONG).show()
            })
    }


    // боль и страдание. Иначе я не придумал =(
    private fun setRate(rate: Int) {
        when (rate) {
            1 -> {
                rate_one.setImageResource(R.drawable.ic_star)
            }
            2 -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
            }
            3 -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                rate_three.setImageResource(R.drawable.ic_star)
            }
            4 -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                rate_three.setImageResource(R.drawable.ic_star)
                rate_four.setImageResource(R.drawable.ic_star)
            }
            5 -> {
                rate_one.setImageResource(R.drawable.ic_star)
                rate_two.setImageResource(R.drawable.ic_star)
                rate_three.setImageResource(R.drawable.ic_star)
                rate_four.setImageResource(R.drawable.ic_star)
                rate_five.setImageResource(R.drawable.ic_star)
            }
        }
    }
}