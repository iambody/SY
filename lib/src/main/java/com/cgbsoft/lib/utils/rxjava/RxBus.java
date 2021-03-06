package com.cgbsoft.lib.utils.rxjava;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static volatile RxBus instance;
    public static boolean DEBUG = false;


    public static synchronized RxBus get() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    public void clear() {
        for (Object obj : subjectMapper.keySet()) {
            List<Subject> subjects = subjectMapper.get(obj);
            if (null != subjects) {
                subjects.clear();
            }
        }
        subjectMapper.clear();
    }

    public boolean isHasObservable(Object tag) {
        if (subjectMapper.size() > 0) {
            return subjectMapper.get(tag) != null;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = Collections.synchronizedList(new ArrayList<>());
            subjectMapper.put(tag, subjectList);
        }

        final Subject<T, T> subject;
        subjectList.add(subject = new SerializedSubject<>(PublishSubject.create()));
        if (DEBUG) Log.d(TAG, "[register]subjectMapper: " + subjectMapper);
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
            }
            if (!observable.subscribe().isUnsubscribed()) {
                observable.subscribe().unsubscribe();
            }
        }

        if (DEBUG) Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper);
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        if (subjectMapper == null) {
            return;
        }
        List<Subject> subjectList = subjectMapper.get(tag);

        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                if (subject != null) {
                    subject.onNext(content);
                }
            }
        }
        if (DEBUG) Log.d(TAG, "[send]subjectMapper: " + subjectMapper);
    }


    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }
}
