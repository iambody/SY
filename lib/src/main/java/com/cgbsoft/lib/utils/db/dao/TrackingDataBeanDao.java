package com.cgbsoft.lib.utils.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.cgbsoft.lib.base.model.bean.TrackingDataBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRACKING_DATA_BEAN".
*/
public class TrackingDataBeanDao extends AbstractDao<TrackingDataBean, String> {

    public static final String TABLENAME = "TRACKING_DATA_BEAN";

    /**
     * Properties of entity TrackingDataBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property E = new Property(1, String.class, "e", false, "E");
        public final static Property T = new Property(2, Long.class, "t", false, "T");
        public final static Property D = new Property(3, String.class, "d", false, "D");
    }


    public TrackingDataBeanDao(DaoConfig config) {
        super(config);
    }
    
    public TrackingDataBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRACKING_DATA_BEAN\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"E\" TEXT," + // 1: e
                "\"T\" INTEGER," + // 2: t
                "\"D\" TEXT);"); // 3: d
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRACKING_DATA_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TrackingDataBean entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String e = entity.getE();
        if (e != null) {
            stmt.bindString(2, e);
        }
 
        Long t = entity.getT();
        if (t != null) {
            stmt.bindLong(3, t);
        }
 
        String d = entity.getD();
        if (d != null) {
            stmt.bindString(4, d);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TrackingDataBean entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String e = entity.getE();
        if (e != null) {
            stmt.bindString(2, e);
        }
 
        Long t = entity.getT();
        if (t != null) {
            stmt.bindLong(3, t);
        }
 
        String d = entity.getD();
        if (d != null) {
            stmt.bindString(4, d);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public TrackingDataBean readEntity(Cursor cursor, int offset) {
        TrackingDataBean entity = new TrackingDataBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // e
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // t
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // d
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TrackingDataBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setE(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setT(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setD(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(TrackingDataBean entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(TrackingDataBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TrackingDataBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
