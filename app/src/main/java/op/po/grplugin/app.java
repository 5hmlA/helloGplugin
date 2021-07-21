package op.po.grplugin;

import android.app.Application;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author yun.
 * @date 2021/7/21
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
public class app extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ARouter.init(this); // 尽可能早，推荐在Application中初始化
  }
}
