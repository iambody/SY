package com.cgbsoft.lib.utils.tools;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author chenlong
 */
public class RegexUtil {

  /**
   * 匹配邮箱
   */
  public static final String EMAIL = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";

  public static final String CHINESE_NAME = "[\\u4E00-\\u9FA5]{1,7}(?:\\u00B7[\\u4E00-\\u9FA5]{1,7}){0,2}";

  public static final String PINYIN = "[A-Za-z]{0,20}";

  public static final String PHONE_NUMBER = "^13[0-9]{9}$|^14[0-9]{9}$|^15[0-9]{9}$|^17[0-9]{9}$|^18[0-9]{9}$";

  public static final String ID_NUMBER = "^(\\d{17})([0-9]|X|x)$";

  public static final String URL = "(http:|https:|)//[A-Za-z0-9\\\\._?%&+=/#-]*";

  public static boolean match(String s, String patternText) {
    if (TextUtils.isEmpty(s)) {
      return false;
    }
    Matcher matcher = getMatcher(s, patternText);
    return matcher.matches();
  }

  public static Matcher getMatcher(@NonNull String s, String patternText) {
    Pattern pattern = Pattern.compile(patternText);
    return pattern.matcher(s);
  }
}
