package ru.apliter.evotor_digital_library.ui.fragments

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.book_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.apliter.evotor_digital_library.R
import ru.apliter.evotor_digital_library.ui.MainViewModel
import ru.apliter.evotor_digital_library.ui.rv.BookAdapter
import ru.evotor.framework.device.scanner.event.BarcodeReceivedEvent
import ru.evotor.framework.device.scanner.event.handler.receiver.ScannerBroadcastReceiver
import java.util.*


class BookListFragment : Fragment() {

    private lateinit var bookAdapter: BookAdapter
    private lateinit var rootView: View
    private lateinit var barcodeScanReceiver: BarcodeScanReceiver

    private val viewModel by viewModel<MainViewModel>()

    companion object {
        fun newInstance() = BookListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.book_list_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context).apply {
            initialPrefetchItemCount = 4
            isSmoothScrollbarEnabled = true
            isItemPrefetchEnabled = true
        }

        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
        bookAdapter =
            BookAdapter { bookId: String ->
                openBook(bookId)
            }
        recyclerView.adapter = bookAdapter

        barcodeScanReceiver = BarcodeScanReceiver(this)
    }

    fun openBook(bookId: String) {
        val bookItemFragment = BookItemFragment()
        val arg = Bundle()
        arg.putString("BOOK_ID", bookId)
        bookItemFragment.arguments = arg

        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.container, bookItemFragment)

                .addToBackStack("list")
                .commit()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.startGettingBookList()
        observeViewModel(viewModel, viewLifecycleOwner)
        searchView.setOnQueryTextListener(getSearchViewListener())
        searchView.setOnCloseListener(getCloseBtnListener())
        main.requestFocus()
    }

    private fun getCloseBtnListener(): SearchView.OnCloseListener? {
        return SearchView.OnCloseListener {
            viewModel.startGettingBookList()
            true
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(barcodeScanReceiver)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingBookList()
        val intentFilter = IntentFilter(ScannerBroadcastReceiver.ACTION_BARCODE_RECEIVED)
        activity?.registerReceiver(
            barcodeScanReceiver,
            intentFilter,
            ScannerBroadcastReceiver.SENDER_PERMISSION,
            null
        )
        main.requestFocus()
    }

    private fun observeViewModel(viewModel: MainViewModel, lifeCycleOwner: LifecycleOwner) {
        viewModel.getBooksListObservable()
            .observe(lifeCycleOwner, Observer {
                bookAdapter.submitList(it)
            })

        viewModel.getLoadingObservable()
            .observe(lifeCycleOwner, Observer {
                if (it) {
                    loading_list_pb.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    loading_list_pb.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            })
        viewModel.getBooksListErrorObservable()
            .observe(lifeCycleOwner, Observer {
                Snackbar.make(
                    rootView,
                    getString(R.string.error_message, it::class.java, it.cause?.message),
                    Snackbar.LENGTH_LONG
                ).show()
            })
    }


    private fun getSearchViewListener(): SearchView.OnQueryTextListener? {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchQueryFilter(query.toUpperCase(Locale.ROOT))
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isBlank() || newText.isEmpty()) {
                    viewModel.startGettingBookList()
                    searchView.clearFocus()
                    recyclerView.requestFocus()
                }
                return false
            }
        }
    }

    class BarcodeScanReceiver(private val bookListFragment: BookListFragment) :
        ScannerBroadcastReceiver() {
        override fun handleBarcodeReceivedEvent(context: Context, event: BarcodeReceivedEvent) {
            val viewModel = ViewModelProviders.of(bookListFragment).get(MainViewModel::class.java)
            val book = viewModel.getBookByBarcode(event.barcode)
            book?.let {
                bookListFragment.openBook(it.bookId)
            }
        }
    }

}
