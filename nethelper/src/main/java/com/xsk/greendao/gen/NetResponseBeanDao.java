package com.xsk.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zx.xsk.nethelper.dbbeans.NetResponseBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NET_RESPONSE_BEAN".
*/
public class NetResponseBeanDao extends AbstractDao<NetResponseBean, Long> {

    public static final String TABLENAME = "NET_RESPONSE_BEAN";

    /**
     * Properties of entity NetResponseBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Key = new Property(1, String.class, "key", false, "KEY");
        public final static Property ResponseContent = new Property(2, String.class, "responseContent", false, "RESPONSE_CONTENT");
        public final static Property UpdateTime = new Property(3, long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property Overtime = new Property(4, long.class, "overtime", false, "OVERTIME");
    }


    public NetResponseBeanDao(DaoConfig config) {
        super(config);
    }
    
    public NetResponseBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NET_RESPONSE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"KEY\" TEXT," + // 1: key
                "\"RESPONSE_CONTENT\" TEXT," + // 2: responseContent
                "\"UPDATE_TIME\" INTEGER NOT NULL ," + // 3: updateTime
                "\"OVERTIME\" INTEGER NOT NULL );"); // 4: overtime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NET_RESPONSE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NetResponseBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(2, key);
        }
 
        String responseContent = entity.getResponseContent();
        if (responseContent != null) {
            stmt.bindString(3, responseContent);
        }
        stmt.bindLong(4, entity.getUpdateTime());
        stmt.bindLong(5, entity.getOvertime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NetResponseBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(2, key);
        }
 
        String responseContent = entity.getResponseContent();
        if (responseContent != null) {
            stmt.bindString(3, responseContent);
        }
        stmt.bindLong(4, entity.getUpdateTime());
        stmt.bindLong(5, entity.getOvertime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public NetResponseBean readEntity(Cursor cursor, int offset) {
        NetResponseBean entity = new NetResponseBean( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // responseContent
            cursor.getLong(offset + 3), // updateTime
            cursor.getLong(offset + 4) // overtime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NetResponseBean entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setResponseContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUpdateTime(cursor.getLong(offset + 3));
        entity.setOvertime(cursor.getLong(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NetResponseBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NetResponseBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NetResponseBean entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
