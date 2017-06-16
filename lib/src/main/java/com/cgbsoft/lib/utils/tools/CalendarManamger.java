package com.cgbsoft.lib.utils.tools;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.format.Time;

import com.cgbsoft.lib.base.model.bean.CalendarListBean;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author chenlong on 16/9/20.
 * 系统日历 curd操作
 */
public class CalendarManamger {
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";
    private static String attendeuesesUriString = "content://com.android.calendar/attendees";

    public static long insertSystemCalendar(Context context, CalendarListBean.CalendarBean calendarBean) {
        String calId = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            userCursor.close();
        } else {
            addCalendarAccount(context);
        }
        ContentValues l_event = new ContentValues();
        l_event.put("calendar_id", calId);
        l_event.put("title", calendarBean.getTitle());
        l_event.put("description",  calendarBean.getContent());
        // l_event.put("eventLocation", “”);
        if (!TextUtils.isEmpty(calendarBean.getStartTime()) && calendarBean.getStartTime().contains("-")) {
            l_event.put("dtstart", DataUtil.formatPatternToMilles(calendarBean.getStartTime(), "yyyy-MM-dd HH:mm"));
            l_event.put("dtend", DataUtil.formatPatternToMilles(calendarBean.getEndTime(), "yyyy-MM-dd HH:mm"));
        } else if (!TextUtils.isEmpty(calendarBean.getStartTime())){
            l_event.put("dtstart", Long.parseLong(calendarBean.getStartTime()));
            l_event.put("dtend", Long.parseLong(calendarBean.getEndTime()));
        }

        l_event.put(CalendarContract.Events.EVENT_TIMEZONE, Time.getCurrentTimezone());
//        l_event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
//        l_event.put("allDay", 0);
        l_event.put("eventStatus", 1);
        l_event.put("hasAlarm", 1);
        Uri l_eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            l_eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            l_eventUri = Uri.parse("content://calendar/events");
        }
        Uri l_uri = context.getContentResolver().insert(l_eventUri, l_event);
        long eventId = Long.parseLong(l_uri.getLastPathSegment());
        ContentValues values = new ContentValues();
        values.put("event_id", eventId);
        values.put("minutes", calendarBean.getAlert());
        values.put("method", 1);
        //values.put("minutes", DataUtil.formatPatternToMilles(calendarBean.getEndTime(), CalendarAddActivity.YYYY_MM_DD_MM_HH) + 1000 * 10);
        //context.getContentResolver().insert(l_eventUri, values);
        context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);

//        ContentValues attendeesValues = new ContentValues();
//        attendeesValues.put("event_id", eventId);
//        attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
//        attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
//        attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
//        attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
//        attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
//        context.getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        return eventId;
    }

    /**
     * 更新系统日历事件
     */
    public static void updateCalendar(Context context, CalendarListBean.CalendarBean calendarBean) {
//        updateValues.put("dtstart", DataUtil.formatPatternToMilles(calendarBean.getStartTime()));
//        updateValues.put("dtend", DataUtil.formatPatternToMilles(calendarBean.getEndTime()));
        ContentValues updateValues = new ContentValues();
        updateValues.put("title", calendarBean.getTitle());
        updateValues.put("description", calendarBean.getContent());
        updateValues.put("dtstart", DataUtil.formatPatternToMilles(calendarBean.getStartTime(), "yyyy-MM-dd HH:mm"));
        updateValues.put("dtend", DataUtil.formatPatternToMilles(calendarBean.getEndTime(), "yyyy-MM-dd HH:mm"));
        Uri updateUri = ContentUris.withAppendedId(Uri.parse(calanderEventURL), Long.parseLong(calendarBean.getEventId()));
        context.getContentResolver().update(updateUri, updateValues, null, null);
    }

    /**
     * 初始化系统日历事件帐户
     * @param context
     */
    public static void addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, "yy");
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com");
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "mytt");
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, -9206951);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, "mygmailaddress@gmail.com");
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);
        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "mygmailaddress@gmail.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                .build();
        context.getContentResolver().insert(calendarUri, value);
    }

    public static boolean isExist(Context context, String name,  String content) {
        Cursor managedCursor = null;
        try {
            Uri calendars = Uri.parse(calanderEventURL);
            if (!TextUtils.isEmpty(content)) {
                managedCursor = context.getContentResolver().query(calendars, null,
                        CalendarContract.Events.TITLE.concat("=").concat("? and ").concat(CalendarContract.Events.DESCRIPTION).concat("=?"), new String[]{name, content}, null);
            } else {
                managedCursor = context.getContentResolver().query(calendars, null,
                        CalendarContract.Events.TITLE.concat("=?"), new String[]{name}, null);
            }
            if (managedCursor != null) {
                return managedCursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (managedCursor != null) {
                managedCursor.close();
            }
        }
        return false;
    }
}
