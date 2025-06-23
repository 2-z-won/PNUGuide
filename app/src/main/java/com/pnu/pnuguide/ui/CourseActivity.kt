package com.pnu.pnuguide.ui.course

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pnu.pnuguide.R
import com.pnu.pnuguide.MainActivity
import com.pnu.pnuguide.ui.chat.ChatActivity
import com.pnu.pnuguide.ui.stamp.StampActivity
import com.pnu.pnuguide.ui.setupHeader1
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat

class CourseActivity : AppCompatActivity() {
    private var searchQuery: String = ""
    private lateinit var adapter: CourseSearchAdapter
    // List of buildings displayed when searching.
    // Update names, numbers and images here if needed.
    private val baseList = listOf(
        CourseItem("대학본부", "건물번호: 205", imageRes = R.drawable.head),
        CourseItem("조각공원", "생물관 앞 ", imageRes = R.drawable.piece),
        CourseItem("새벽벌 도서관", "건물번호: 420", imageRes = R.drawable.sae_do),
        CourseItem("예원정", "사회관 앞", imageRes = R.drawable.yea),
        CourseItem("금정회관 식당", "건물번호: 419", imageRes = R.drawable.gres),
        CourseItem("경영관", "건물번호: 514", imageRes = R.drawable.busy),
        CourseItem("법학관 모의 법정", "건물번호: 609", imageRes = R.drawable.beopjeong),
        CourseItem("중앙도서관", "건물번호: 510", imageRes = R.drawable.ang),
        CourseItem("음악관", "건물번호: 707", imageRes = R.drawable.music),
        CourseItem("학생회관식당", "건물번호: 708", imageRes = R.drawable.sres),
        CourseItem("경암 체육관", "건물번호: 706", imageRes = R.drawable.p),
        CourseItem("약학관", "건물번호: 503", imageRes = R.drawable.m),
        CourseItem("지질박물관", "건물번호: 414", imageRes = R.drawable.jijil),
        CourseItem("샛벌회관 식당", "건물번호: 415", imageRes = R.drawable.bres),
        CourseItem("박물관", "건물번호: 412", imageRes = R.drawable.museum),
        CourseItem("10.16 기념관", "건물번호: 403", imageRes = R.drawable.tensix),
        CourseItem("건설관", "건물번호: 401", imageRes = R.drawable.a),
        CourseItem("기계관 V-SPACE", "건물번호: 303", imageRes = R.drawable.vs),
        CourseItem("진리의 뜰", "운죽정 뒷편", imageRes = R.drawable.jinli),
        CourseItem("인문관", "건물번호: 306", imageRes = R.drawable.inmun),
        CourseItem("넉넉한터", "건물번호: 203", imageRes = R.drawable.nuck),
        CourseItem("시월 광장", "넉터 앞편", imageRes = R.drawable.ten),
        CourseItem("민주 언덕", "새벽벌 도서관 앞", imageRes = R.drawable.minju),
        CourseItem("미리내 골", "인문관 옆편", imageRes = R.drawable.miri)
    )
    private val displayList = mutableListOf<CourseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_course)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_course)
        toolbar.setupHeader1(this, R.string.title_course)

        findViewById<ImageView>(R.id.rs49bgap0hwg)
            .setImageResource(R.drawable.ic_search)

        findViewById<ImageView>(R.id.image_history_1).setImageResource(R.drawable.sae_do)
        findViewById<ImageView>(R.id.image_history_2).setImageResource(R.drawable.vs)
        findViewById<ImageView>(R.id.image_history_3).setImageResource(R.drawable.beopjeong)

        findViewById<ImageView>(R.id.image_history_1).setImageResource(R.drawable.museum)
        findViewById<ImageView>(R.id.image_history_2).setImageResource(R.drawable.jijil)
        findViewById<ImageView>(R.id.image_history_3).setImageResource(R.drawable.ang)
        findViewById<ImageView>(R.id.image_study_1).setImageResource(R.drawable.nuck)
        findViewById<ImageView>(R.id.image_study_2).setImageResource(R.drawable.cafe)
        findViewById<ImageView>(R.id.image_study_3).setImageResource(R.drawable.jinli)


        val recycler = findViewById<RecyclerView>(R.id.recycler_search)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CourseSearchAdapter()
        recycler.adapter = adapter
        adapter.onItemClick = { item ->
            SpotDetailDialogFragment.newInstance(item)
                .show(supportFragmentManager, "detail")
        }
        displayList.addAll(baseList)
        adapter.submitItems(displayList)

        val cancelBtn: View = findViewById(R.id.btn_cancel_search)
        val scrollView: View = findViewById(R.id.rm27nijip3z)
        val categoryRow: View = findViewById(R.id.r0ynj2pkd7ew9)
        val sectionPopular: View = findViewById(R.id.section_popular)
        val sectionHistory: View = findViewById(R.id.section_history)
        val sectionStudy: View = findViewById(R.id.section_study)

        fun showDetail(title: String, desc: String, image: Int) {
            val item = CourseItem(title, desc, imageRes = image)
            SpotDetailDialogFragment.newInstance(item)
                .show(supportFragmentManager, "detail")
        }

        findViewById<View>(R.id.r4426ybbx5od).setOnClickListener {
            showDetail("새벽벌 도서관", "건물번호: 420", R.drawable.sae_do)
        }
        findViewById<View>(R.id.r0r21uflcziqi).setOnClickListener {
            showDetail("PNU V-SPACE", "건물번호: 303", R.drawable.vs)
        }
        findViewById<View>(R.id.r0augc34kguoi).setOnClickListener {
            showDetail("법학관 모의 법정", "건물번호: 609", R.drawable.beopjeong)
        }
        findViewById<View>(R.id.card_history_1).setOnClickListener {
            showDetail("박물관", "건물 번호: 412", R.drawable.museum)
        }
        findViewById<View>(R.id.card_history_2).setOnClickListener {
            showDetail("지질박물관", "건물 번호: 414", R.drawable.jijil)
        }
        findViewById<View>(R.id.card_history_3).setOnClickListener {
            showDetail("중앙 도서관", "건물 번호: 510", R.drawable.ang)
        }
        findViewById<View>(R.id.card_study_1).setOnClickListener {
            showDetail("넉넉한 터", "건물번호: 203", R.drawable.nuck)
        }
        findViewById<View>(R.id.card_study_2).setOnClickListener {
            showDetail("cafe 운죽정", "건물번호: 202", R.drawable.cafe)
        }
        findViewById<View>(R.id.card_study_3).setOnClickListener {
            showDetail("진리의 뜰", "운죽정 뒷편", R.drawable.jinli)
        }

        fun showSection(target: View) {
            sectionPopular.visibility = if (target == sectionPopular) View.VISIBLE else View.GONE
            sectionHistory.visibility = if (target == sectionHistory) View.VISIBLE else View.GONE
            sectionStudy.visibility = if (target == sectionStudy) View.VISIBLE else View.GONE
        }

        val searchEdit: EditText = findViewById(R.id.r9hg358v9thk)
        searchEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && recycler.visibility != View.VISIBLE) {
                categoryRow.visibility = View.GONE
                sectionPopular.visibility = View.GONE
                sectionHistory.visibility = View.GONE
                sectionStudy.visibility = View.GONE
                scrollView.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                cancelBtn.visibility = View.VISIBLE
            }
        }
        cancelBtn.setOnClickListener {
            recycler.visibility = View.GONE
            categoryRow.visibility = View.VISIBLE
            showSection(sectionPopular)
            scrollView.visibility = View.VISIBLE
            cancelBtn.visibility = View.GONE
            searchEdit.text.clear()
            searchEdit.clearFocus()
            displayList.clear()
            displayList.addAll(baseList)
            adapter.submitItems(displayList)
        }
        searchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                filterList()

                if (s.isNullOrEmpty()) {
                    recycler.visibility = View.GONE
                    categoryRow.visibility = View.VISIBLE
                    showSection(sectionPopular)
                    scrollView.visibility = View.VISIBLE
                    cancelBtn.visibility = View.GONE
                } else if (recycler.visibility != View.VISIBLE) {
                    categoryRow.visibility = View.GONE
                    sectionPopular.visibility = View.GONE
                    sectionHistory.visibility = View.GONE
                    sectionStudy.visibility = View.GONE
                    scrollView.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                    cancelBtn.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (!rv.canScrollVertically(1)) {
                    val more = if (searchQuery.isEmpty()) baseList else baseList.filter {
                        it.title.contains(searchQuery, ignoreCase = true)
                    }
                    val start = displayList.size
                    displayList.addAll(more)
                    adapter.appendItems(more)
                    adapter.notifyItemRangeInserted(start, more.size)
                }
            }
        })

        findViewById<TextView>(R.id.rguxz9lullzn).setOnClickListener {
            showSection(sectionPopular)
        }
        findViewById<TextView>(R.id.r2qlk2kqy8g4).setOnClickListener {
            showSection(sectionHistory)
        }
        findViewById<TextView>(R.id.rygry7veadsj).setOnClickListener {
            showSection(sectionStudy)
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_course -> {
                    true
                }
                R.id.nav_stamp -> {
                    startActivity(Intent(this, StampActivity::class.java))
                    true
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNav.menu.findItem(R.id.nav_course).isChecked = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_toolbar, menu)
        menu.findItem(R.id.action_settings)?.apply {
            icon = ContextCompat.getDrawable(this@CourseActivity, R.drawable.ic_settings)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return true
    }

    private fun filterList() {
        val filtered = if (searchQuery.isEmpty()) baseList else baseList.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }
        displayList.clear()
        displayList.addAll(filtered)
        adapter.submitItems(displayList)
    }
}
