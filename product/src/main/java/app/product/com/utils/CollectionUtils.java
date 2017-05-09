package app.product.com.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utils to operate collections
 *
 * @author chenlong
 */
@SuppressWarnings("unused")
public class CollectionUtils {

  private CollectionUtils() {}

  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  public static <T> boolean isEmpty(T[] array) {
    return array == null || array.length == 0;
  }

  /**
   * Sub the list without exception
   */
  public static <T> List<T> safeSubList(List<T> list, int start, int end) {
    if (start >= list.size()) {
      return Collections.emptyList();
    }
    return list.subList(start, Math.min(end, list.size()));
  }

  /**
   * If list is null, change to empty list
   */
  public static <T> List<T> notNull(List<T> list) {
    if (list == null) {
      return Collections.emptyList();
    }
    return list;
  }

  /**
   * Pick n elements from list randomly
   */
  public static <T> List<T> pickNRandom(List<T> list, int n) {
    List<T> copy = new ArrayList<>(list);
    Collections.shuffle(copy);
    return safeSubList(copy, 0, n);
  }

  /**
   * Convert a list of type T to a list of type Object
   */
  public static <T> List<Object> convertToObjectList(List<T> list) {
    List<Object> result = new ArrayList<>();
    for (T t : list) {
      result.add(t);
    }
    return result;
  }
}
