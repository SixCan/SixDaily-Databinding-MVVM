package ca.six.daily.biz.detail

import android.os.Build
import ca.six.daily.BuildConfig
import ca.six.daily.core.BaseApp
import ca.six.daily.core.network.HttpEngine
import ca.six.daily.data.DailyDetailResponse
import ca.six.daily.utils.writeToCacheFile
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * @author hellenxu
 * @date 2017-08-03
 * Copyright 2017 Six. All rights reserved.
 */

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.LOLLIPOP)
        , application = BaseApp::class)
class DailyDetailPresenterTest {
    @Mock lateinit var view : IDailyDetailView
    val presenter: DailyDetailPresenter by lazy {
        DailyDetailPresenter(view)
    }
    private val articleId = 9520758L
    private val fileName = "news_9520758.json"

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        val immediate = object : Scheduler(){
            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run()})
            }

            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit) // change delay into 0 to make sure no waiting
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @After fun tearDown() {
        val dir = BaseApp.app.cacheDir
        val file = File(dir, fileName)
        file.delete()

        HttpEngine.isMock = false
        HttpEngine.mockJson = ""
    }

    private fun prepareMockData () {
        HttpEngine.isMock = true
        HttpEngine.mockJson = "{\"body\":\"<div class=\\\"main-wrap content-wrap\\\">\\n<div class=\\\"headline\\\">\\n\\n<div class=\\\"img-place-holder\\\"><\\/div>\\n\\n\\n\\n<\\/div>\\n\\n<div class=\\\"content-inner\\\">\\n\\n\\n\\n\\n<div class=\\\"question\\\">\\n<h2 class=\\\"question-title\\\">有哪些色彩丰富但又不落俗套的建筑？<\\/h2>\\n\\n<div class=\\\"answer\\\">\\n\\n<div class=\\\"meta\\\">\\n<img class=\\\"avatar\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/v2-ed5400a57d320077e8d289631be65e27_is.jpg\\\">\\n<span class=\\\"author\\\">夕广，<\\/span><span class=\\\"bio\\\">A little bit about Architecture<\\/span>\\n<\\/div>\\n\\n<div class=\\\"content\\\">\\n<p>太喜欢这道题了，刚好自己也想总结一下这个问题（没想到内容这么多）！大概归纳了几个方面吧，从传统建筑宗教性建筑，现代主义大师柯布西耶，以及新材料运用的角度，提供了一下色彩丰富但又不落俗套的建筑例子。以及大家注意审题啊，问的是色彩丰富，不是色彩鲜艳啊！本文多图，杀流量。<\\/p>\\r\\n<p>这几十年建筑圈子流行起了一种&ldquo;性冷淡&rdquo;的风，现代主义风格建筑的崛起，极大地受到包豪斯和 De Stijl 等等影响，更加推行简洁而有力的作品，当代来看以日本大师为例，混凝土或是木头成为建筑的主要材料，营造相对宁静和深远的意境，同样，欧美现代主义大师代表很多作品也倾向于白色和混凝土，过多色彩的表达反而被视为的建筑。<\\/p>\\r\\n<p>大概归了一下类，<\\/p>\\r\\n<p>1. 传统建筑及宗教建筑<\\/p>\\r\\n<p>中国传统建筑常运用丰富的色彩等级关系，这样的色彩体系极大程度上象征了礼制观念，《中国传统建筑装饰、环境、色彩研究》中提到，<\\/p>\\r\\n<blockquote>通过对紫禁城的建筑色彩进行量化分析发现，不仅具有色相上的对比，还包括自上而下的&ldquo;明度渐变&rdquo;和&ldquo;互补强度渐次降低&rdquo;的特征。<\\/blockquote>\\r\\n<p>网上找了两张图，大家感受一下，图侵删。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-3038964c32e469139f20fcac6d52a2af_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-11bf93f2fc2897056bdb3d0f0a32b151_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>其中我们看到的宫殿的色彩大多都遵循&ldquo;五行五色五方&rdquo;的规则观念，&ldquo;五行对应五方&rdquo;观念实则是来源于董仲舒的《春秋繁露》，其中记载：<\\/p>\\r\\n<blockquote>&ldquo;左青龙（木），右白虎（金），前朱雀（火），后玄武（水），中央后土&rdquo;<\\/blockquote>\\r\\n<p>而再对应上颜色便是：金 - 白色，木 - 青色，水 - 黑色，火 - 赤色，土 - 金色。总结起来就是下图这个东西（从网上找的图）<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-3a723550dbb4b3a6352cd0de0bee319d_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>黄色：土，中央，王权，紫禁城就一个很好的，利用色彩来强调等级制度的例子，象征着皇权，帝王，乾清宫作为内廷正殿，被黄色的屋顶覆盖，其地位一目了然，颇有气魄和震慑力，btw，从大的 scale 来看，北京的屋顶瓦片颜色也有一定的规则，皇家帝王为黄色，亲王贵族为绿色（除了共恭王府，协和医院的古建筑也是绿色的瓦片），而我们所见到的胡同中的平常人家瓦片颜色则为灰色。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-1fcb51fba8b8a4729d0756d5b182b15a_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来源于网络，图侵删）乾清宫屋顶<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-d1a0bf96eebfad8aa005ba15db8da045_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来源于网络，图侵删）恭王府绿色屋瓦<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-113e2d539de860240a5e35d39db3de27_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来源于网络，图侵删）微杂院改造，标准营造，灰色瓦片融合入胡同<\\/p>\\r\\n<p>黑色：水，北方，玄武，&ldquo;藏&rdquo;，紫禁城中文渊阁，座北面南，大面积的使用了黑砖墙，黑瓦片，文渊阁也是紫禁城中最大的藏书阁。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-59d408e3e29579a608ace78a387068ca_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来源于网络，图侵删）文渊阁黑色砖瓦和屋顶<\\/p>\\r\\n<p>还有，赤色：火，南方，朱雀。青色：木，东方，青龙。白色：金，西方，白虎，不一一举例子啦~<\\/p>\\r\\n<p>参考资料：三三古建 - 从紫禁城看古代建筑的&ldquo;五行五方五色四象&rdquo;<\\/p>\\r\\n<p>除了中国传统建筑，其他的国家传统风格建筑也具有很浓烈的色彩<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic1.zhimg.com\\/70\\/v2-9775cada792ecc6acfac535e7c81e918_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来源于网络，图侵删）泰国，金碧辉煌，五颜六色<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-65850dd345249060f627b2ef4c7d2dd9_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来自网络，图侵删）俄罗斯教堂，这个颜色并非寻常的拜占庭风格（一般来讲没有这么多颜色），考过建筑史的人都知道考试的重点是：拜占庭穹窿顶，穹顶支撑方柱结构（你们一定画过这个图），主体建筑与小部件的关系。<\\/p>\\r\\n<p>2. 柯布西耶和现代主义<\\/p>\\r\\n<p>柯布西耶在《纯粹主义》中将色彩按照特性和创作的需求进行分类，首先是&ldquo;色调颜色&rdquo;，这些颜色具有强大的控制能力，并且能够和谐的搭配在一起，可以用来表达体量，是构成画面不可或缺的颜色，并且占据大量篇幅，比如：土黄色，深红色，褐色，白色，群青等。其次，&ldquo;活跃颜色&rdquo;，顾名思义便是跳跃，第一眼能在画面中浮现的颜色，也有着很强的不稳定性。第三层次被称为&ldquo;过度颜色&rdquo;，如大红，翠绿，画面中湖水的颜色，并不起主要作用。<\\/p>\\r\\n<p>参考资料：<a href=\\\"http:\\/\\/www.artdesign.org.cn\\/?p=22418\\\">柯布西耶的色卡 - 艺术与设计<\\/a><\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic1.zhimg.com\\/70\\/v2-a90b3f9b760716d45e75f607a067eb90_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>魏森霍夫早餐室及楼梯<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-8dee724854b7e9d7e7aead8d1061ec1b_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>魏森霍夫起居室<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-76ae6c23843be0870da8a2cedd330d21_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>魏森霍夫屋顶平台<\\/p>\\r\\n<p>当然，De Style 对柯布西耶也有很深的影响，最著名的马赛公寓<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-9ff3992da98f3228efb93e1808bd0932_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>马赛公寓 Unit&eacute; d&rsquo;Habitation, Marseille, France, 1947-52<\\/p>\\r\\n<p>3. 建筑材料和技术实现的色彩<\\/p>\\r\\n<p>Frank Gery, 一个 cynical 的大师，建筑运用了极多的色彩和材料，极具表现力，但是其风格饱受争议，举个例子音乐体验博物馆，西雅图。<\\/p>\\r\\n<p>大家感受一下全景，城市轨道交通是从里面穿过的。我是很服气了。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic1.zhimg.com\\/70\\/v2-65f95c5a52a2bbcc808466ea12a356cc_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-3884a6c02062a6bc7d0446c427515482_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic1.zhimg.com\\/70\\/v2-bac8b4eb5d467832387ac044d254b17c_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>（图片来自网络，图侵删）运用的材质结合太阳光的照射，赋予了这个建筑独一无二的色彩。<\\/p>\\r\\n<p>另一个特殊材质运用的我很喜欢的例子就是香奈儿阿姆斯特丹的门店，位于 P.C. Hooftstraat 大街，就是大家口中的香奈儿水晶宫，基友去年去了这儿，说是水晶砖碎了不少，效果其实没有那么梦幻，这是 MVRDV 的项目。<\\/p>\\r\\n<p>运用水晶砖砌成的玻璃幕墙，很有渐变的感觉，阳光的照射，蓝天白云的映衬，让这面墙散发出万丈光芒，而当夜幕降临之时，灯光亮起来了，这面墙又变得金碧辉煌。十足的现代感。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-bddc17d7862521cdab14cfce56f8a04b_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-abe80f166d3be5d05efaad970e3cde2b_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>MVRDV 还有很多色彩丰富的项目，就是有的比较.....，放几个比较好看的。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic1.zhimg.com\\/70\\/v2-3769f1c4a80a2a45f99aa3d0b7943180_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>WoZoCo 老年公寓，木材质贴以彩色有机玻璃作为阳台，玻璃材质更加通透。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-abe41c200899f6a5dfb7c6a221baa14f_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>市集公寓（ Market Hall ）<\\/p>\\r\\n<p>还有前面答主提到的 Jean Nouvel，很详细了。<\\/p>\\r\\n<hr \\/>\\r\\n<p>欢迎关注微信公众号：枕侧记<\\/p>\\r\\n<p>如需转载，请私信，谢谢<\\/p>\\n<\\/div>\\n<\\/div>\\n\\n<div class=\\\"answer\\\">\\n\\n<div class=\\\"meta\\\">\\n<img class=\\\"avatar\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/d89292d85dd756217e64b856aa04c2f2_is.jpg\\\">\\n<span class=\\\"author\\\">赵德住，<\\/span><span class=\\\"bio\\\">公众号：赵德住<\\/span>\\n<\\/div>\\n\\n<div class=\\\"content\\\">\\n<p>来自柏林的事务所 Sauerbruch Hutton 很符合这个题目，他们的标志性语言，赖以成名的绝技之一，就是特别丰富的色彩。可 ke 贵 pa 的是，他们已经一直这样坚持了 20 多年了，技巧越来越娴熟，很多作品都令人印象深刻。<\\/p>\\r\\n<p>先介绍一个小型的：<\\/p>\\r\\n<p><strong>Feuer- und Polizeiwache Berlin， 警察局和消防站，2004<\\/strong><\\/p>\\r\\n<p>这是一个 19 世纪老建筑的改建&amp;加建，建筑师通过打印上颜色的半透明玻璃材质面板，将两个代表建筑的功能的标志性颜色巧妙的融合在一起 （德国消防局的颜色是红色，警察局的颜色是绿色，可惜后来欧盟警察统一了蓝色作为其标识色，那是后话了）<\\/p>\\r\\n<p>同样材质的半透明的玻璃百叶窗也隐藏了办公室的窗户，也同时起到了遮阳的效果。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-c82c3518d67f224e80e95b1d6bc50c3e_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>一边是红色的消防站，底层的磨砂黑色和上部分高亮的彩色形成反差。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-7a644e8dd9b50ba093284a5000d8fe86_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>一边是绿色的警察局，玻璃面板这边主要是和一百多年的老建筑形成对比<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-57234888753ba78e98f2e6b5d59089db_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>转角部分的玻璃面板也弯成弧形<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-ec2fa68735e700efb848a1d1c295fb79_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>总的来说，看似简单的创意，要达到好的表达效果对工艺还是又一定要求的<\\/p>\\r\\n<p>说到工艺复杂，就再介绍一个搞纲的作品：<\\/p>\\r\\n<p><strong>Haus K in M&uuml;nchen, K 住宅，2012<\\/strong><\\/p>\\r\\n<p>由于是别墅住宅区，这栋建筑虽然颜色丰富，却又相对低调安静。外墙使用了小色块分三层表达了从深红到浅灰的渐变。远看像是普通的砖墙砌体，却又有着细腻的颜色变化。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-1915ebbde33e9f44e118b34ececaeae5_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>内院才采用了一些体量的变化和弧形的外墙。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-53bbc526769e3d9522c23833cb5f9117_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>越近看，越能看到细节，反光并且突起的色块在不同光线和阴影下变化丰富。<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-fb03d8c20adb498aa3cf7a6d874afd77_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>最后亮点来了，使用的居然是一种特别设计的砖块，分别上釉之后再烧制<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-9ee83d84b33008ecb2b41095d5980a76_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>不过说起色彩丰富又比较低调，我也很喜欢他们另外一个作品：<\\/p>\\r\\n<p><strong>Munich RE M&uuml;nchen， 慕尼黑 RE， 2012<\\/strong><\\/p>\\r\\n<p>这是一个 80 年代旧楼的改造项目。先看这栋建筑原始的样子，估计大部分人可能直接就拆了重建了。Sauerbruch Hutton 改建了这个项目，改建后达到新的节能标准的同时，他们还希望高速路上往来的车辆经过建筑的时候能观察到色彩变化的面板。可是体积这么庞大又单调的建筑，怎么样才能随着车辆的运动产生颜色的变化呢？<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-1192a6b87bb913ac5f50e62932759992_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>先是把旧面板完全拆除，只保留承重结构部分<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-c018455cac120177e69fa8d8e22c9366_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>再用圆锯一点点修整，然后挂上新的面板。新面板在横向和纵向都有不同的厚度变化，主面是黑色，只有侧面是有颜色的<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-c28ba9562ea12ebbc4fd995ee11f8cb2_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>于是面板上就有节奏了，同时由于在不同的朝向上只能看到一种颜色，随着人沿着面板移动，就会看到不同颜色的变化，真是很有意思的想法！<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic3.zhimg.com\\/70\\/v2-21d44a05569cdd24c92d5bd0a8e4822e_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>角落里能看到不同颜色的朝向<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-797fd5ff0bb21b84bf1096d43aeca7b1_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>沿着建筑走的话，随着观察角度的不同，有时候是蓝色的<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic2.zhimg.com\\/70\\/v2-8d0034472fd23ffaefa7a4e841436039_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>有时候是红色的<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic1.zhimg.com\\/70\\/v2-e23e9e78037eff16c742e1bfa5ec9638_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>有的时候又是黄色的<\\/p>\\r\\n<p><img class=\\\"content-image\\\" src=\\\"http:\\/\\/pic4.zhimg.com\\/70\\/v2-06d56805175ad2b47157bd797349663f_b.jpg\\\" alt=\\\"\\\" \\/><\\/p>\\r\\n<p>我在想，改建完成之后，会不会时不时有人绕着这个建筑转圈呢&hellip;&hellip;<\\/p>\\r\\n<p>总之，色彩丰富又不落俗套，我觉得他们的建筑应该能算上一份吧<\\/p>\\r\\n<p>欢迎讨论，谢谢<\\/p>\\n<\\/div>\\n<\\/div>\\n\\n\\n<div class=\\\"view-more\\\"><a href=\\\"http:\\/\\/www.zhihu.com\\/question\\/62094123\\\">查看知乎讨论<span class=\\\"js-question-holder\\\"><\\/span><\\/a><\\/div>\\n\\n<\\/div>\\n\\n\\n\\n\\n\\n<div class=\\\"question\\\">\\n<h2 class=\\\"question-title\\\"><\\/h2>\\n\\n<div class=\\\"answer\\\">\\n\\n<div class=\\\"content\\\">\\n<p>更多讨论，查看<span class=\\\"s1\\\">&nbsp;<\\/span>知乎圆桌<span class=\\\"s1\\\">&nbsp;&middot;&nbsp;<\\/span><a href=\\\"https:\\/\\/www.zhihu.com\\/roundtable\\/jianzhu?utm_campaign=official_account&amp;utm_source=zhihudaily&amp;utm_medium=link&amp;utm_content=roundtable\\\">看见建筑之美<\\/a><\\/p>\\n<\\/div>\\n<\\/div>\\n\\n\\n<\\/div>\\n\\n\\n<\\/div>\\n<\\/div>\",\"image_source\":\"Yestone.com 版权图片库\",\"title\":\"谁说只有走「性冷淡」风的建筑看起来才高级？\",\"image\":\"https:\\/\\/pic4.zhimg.com\\/v2-4d1cbbbffb0ba2b9982e59f94f5b8a5f.jpg\",\"share_url\":\"http:\\/\\/daily.zhihu.com\\/story\\/9520758\",\"js\":[],\"ga_prefix\":\"071311\",\"images\":[\"https:\\/\\/pic3.zhimg.com\\/v2-492c1f9d550f7e367839ac5221654096.jpg\"],\"type\":0,\"id\":9520758,\"css\":[\"http:\\/\\/news-at.zhihu.com\\/css\\/news_qa.auto.css?v=4b3e3\"]}"
    }

    @Test fun testGetDetails_whenNoCache_sendNetworkRequest() {
        prepareMockData()
        presenter.getDetails(articleId)
        val expectedResponse = DailyDetailResponse(HttpEngine.mockJson)

        verify(view).updateDetails(expectedResponse)
        assertFalse(presenter.isCached)
    }

    @Test fun testGetDetails_whenHasCache_loadCachedData() {
        prepareMockData()
        writeToCacheFile(HttpEngine.mockJson, fileName)
        presenter.getDetails(articleId)
        val expectedResponse = DailyDetailResponse(HttpEngine.mockJson)

        verify(view).updateDetails(expectedResponse)
        assertTrue(presenter.isCached)
    }
}