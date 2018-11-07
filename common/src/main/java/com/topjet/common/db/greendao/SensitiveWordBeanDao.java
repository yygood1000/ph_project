package com.topjet.common.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.topjet.common.db.bean.SensitiveWordBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SENSITIVE_WORD_BEAN".
*/
public class SensitiveWordBeanDao extends AbstractDao<SensitiveWordBean, Long> {

    public static final String TABLENAME = "SENSITIVE_WORD_BEAN";

    /**
     * Properties of entity SensitiveWordBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Word_id = new Property(1, String.class, "word_id", false, "WORD_ID");
        public final static Property Word_name = new Property(2, String.class, "word_name", false, "WORD_NAME");
        public final static Property Word_count = new Property(3, int.class, "word_count", false, "WORD_COUNT");
    }


    public SensitiveWordBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SensitiveWordBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SENSITIVE_WORD_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"WORD_ID\" TEXT," + // 1: word_id
                "\"WORD_NAME\" TEXT," + // 2: word_name
                "\"WORD_COUNT\" INTEGER NOT NULL );"); // 3: word_count
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SENSITIVE_WORD_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SensitiveWordBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String word_id = entity.getWord_id();
        if (word_id != null) {
            stmt.bindString(2, word_id);
        }
 
        String word_name = entity.getWord_name();
        if (word_name != null) {
            stmt.bindString(3, word_name);
        }
        stmt.bindLong(4, entity.getWord_count());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SensitiveWordBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String word_id = entity.getWord_id();
        if (word_id != null) {
            stmt.bindString(2, word_id);
        }
 
        String word_name = entity.getWord_name();
        if (word_name != null) {
            stmt.bindString(3, word_name);
        }
        stmt.bindLong(4, entity.getWord_count());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SensitiveWordBean readEntity(Cursor cursor, int offset) {
        SensitiveWordBean entity = new SensitiveWordBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // word_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // word_name
            cursor.getInt(offset + 3) // word_count
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SensitiveWordBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWord_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setWord_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setWord_count(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SensitiveWordBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SensitiveWordBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SensitiveWordBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
