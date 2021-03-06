package com.duke.yinyangli.bean.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "shuxiang".
*/
public class ShuXiangDao extends AbstractDao<ShuXiang, Void> {

    public static final String TABLENAME = "shuxiang";

    /**
     * Properties of entity ShuXiang.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Title = new Property(0, String.class, "title", false, "title");
        public final static Property Content = new Property(1, String.class, "content", false, "content");
    }


    public ShuXiangDao(DaoConfig config) {
        super(config);
    }
    
    public ShuXiangDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ShuXiang entity) {
        stmt.clearBindings();
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(1, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ShuXiang entity) {
        stmt.clearBindings();
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(1, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public ShuXiang readEntity(Cursor cursor, int offset) {
        ShuXiang entity = new ShuXiang( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // title
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // content
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ShuXiang entity, int offset) {
        entity.setTitle(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(ShuXiang entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(ShuXiang entity) {
        return null;
    }

    @Override
    public boolean hasKey(ShuXiang entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
