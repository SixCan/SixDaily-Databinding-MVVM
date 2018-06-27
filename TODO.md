[基本规范]
1. UI
2. write unit test
3. write espresso test

[Doing]
4. list页， 点击过的变灰色、
5. fix the timeout network error
6. list页做缓存：  几个要注意的点， 一个是rxjava,okhttp缓存response， 二个是判断条件若是今天就不再次请求了， 三个是And7上的存文件，看是否和以前版本有变化 https://github.com/hehonghui/android-tech-frontier/blob/master/issue-37/%E4%BD%BF%E7%94%A8RxJava%E7%BC%93%E5%AD%98Rest%E8%AF%B7%E6%B1%82.md
https://github.com/square/sqlbrite
https://github.com/Kotlin/anko/wiki/Anko-SQLite
http://mobologicplus.com/integration-of-anko-sqlite-database-in-kotlin-android/
http://www.jianshu.com/p/cf1a6037b80e
用数据好复杂， 想改用存文件了。 反正我只在存与取， 根本没有select式的需求。
8. 可能要接入anko-sqlite， 来存储数据
7. 引入LeakCanary， 查看ListPresenter中的rxjava是否有内存泄露
8. list页load more

[bug]
bug: 点击变灰。在复用viewHolder时会混乱

[backlog]
1. multiple module: daily, hot, category, ...,  common
2. biz: copyright, daily, hot, category, about us,
3. detail页滑到底后， 要自动能请求，不要再回list页，多不好的体验
5. refactor the multiple type adapter for rv
6. forbid vertical movement when users switch topics on detail page
7. unit tests
8. integrate tests
9. webview快速滑动时卡顿
10. webview缓存
11. webview加载进度条
12. 缓存新版本的css

[finished]
3. 首页的view holder， 过渡一下在加载的等待时间
2. display correct details when changing topics on detail page.
4. WebView load css files which fetching from remote server
9. cache news details