 package pl.paullettuce.swipelayout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_test.*

class ListTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_test)

        val dummyItems = listOf(Any(),Any(),Any(),Any(),Any(),Any(),Any(),Any(),Any(),Any(),Any())
        val adapter = SwipeViewsAdapter(dummyItems)
        recview.layoutManager = LLM(this)
        recview.adapter = adapter

        recview.requestDisallowInterceptTouchEvent(true)
    }
}

class LLM(context: Context): LinearLayoutManager(context) {
//    override fun canScrollHorizontally(): Boolean {
//        return false
//    }
//
//    override fun canScrollVertically(): Boolean {
//        return false
//    }
}

class SwipeViewsAdapter(
    val items: List<Any>
): RecyclerView.Adapter<SwipeViewsAdapter.SwipeViewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return SwipeViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SwipeViewViewHolder, position: Int) {
        holder.bind()
    }

    inner class SwipeViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {}
    }
}