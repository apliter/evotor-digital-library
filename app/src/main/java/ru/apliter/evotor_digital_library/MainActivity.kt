package ru.apliter.evotor_digital_library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.apliter.evotor_digital_library.ui.main.BookListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BookListFragment.newInstance())
                .commitNow()
        }
    }

    // Да, так можно - особенность задачи =) Да, возможно выстрел в ногу - если добавиться еще какой-нибудь диалог \ дополнительный шаг, то это придется убрать =)
    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, BookListFragment.newInstance())
            .commitNow()
    }
}
