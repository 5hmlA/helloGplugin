package androidx.lifecycle;

/**
 * @author yun.
 * @date 2021/8/17
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
public abstract class JViewModel extends ViewModel {

  @Override
  public <T> T setTagIfAbsent(String key, T newValue) {
    return super.setTagIfAbsent(key, newValue);
  }

  @Override
  public <T> T getTag(String key) {
    return super.getTag(key);
  }

}
