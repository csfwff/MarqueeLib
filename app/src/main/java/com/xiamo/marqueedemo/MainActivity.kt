package com.xiamo.marqueedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import com.xiamo.marqueelib.gong.SimpleMF


import android.view.View
import android.widget.TextView
import com.xiamo.marqueelib.g.MarqueeViewGong
import com.xiamo.marqueelib.gong.MarqueeFactory
import com.xiamo.marqueelib.gong.SimpleMarqueeViewGong
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        marqueeViewXin.start()

        val message = "月色寒 独怜谁家青石板  檐角珠光续了又断 恰满成一盏  她说相识是缘  他言别离时难  一夜燃尽在渔火阑珊"
        marqueeViewSun.startWithText(message, R.anim.anim_right_in, R.anim.anim_left_out)

        val datas: List<String?> = listOf(
            "长安雨，一夜落秋意",
            "路千里，朔风吹客衣",
            "江船夜雨听笛，倚晚晴",
            "平沙漠漠兮愁无际",
            "长安堤，垂杨送别离",
            "千山月，一片伤心碧",
            "长门又误佳期，声清凄",
            "朱颜染尘兮梦中语"
        )
//SimpleMarqueeView<T>，SimpleMF<T>：泛型T指定其填充的数据类型，比如String，Spanned等
        val marqueeFactory: SimpleMF<String?> = SimpleMF<String?>(this)
        marqueeFactory.data = datas
        (simpleMarqueeView as MarqueeViewGong<TextView, String>).setMarqueeFactory(marqueeFactory as MarqueeFactory<TextView, String>)
        simpleMarqueeView.startFlipping()
    }
}