package ca.six.daily

import ca.six.daily.biz.detail.DailyDetailPresenterTest
import ca.six.daily.biz.home.DailyListPresenterTest
import ca.six.daily.utils.DataUtilsTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author hellenxu
 * @date 2017-08-04
 * Copyright 2017 Six. All rights reserved.
 */

@RunWith(Suite::class)
@Suite.SuiteClasses(DailyListPresenterTest::class, DailyDetailPresenterTest::class, DataUtilsTest::class)
class SixDailyTestSuite