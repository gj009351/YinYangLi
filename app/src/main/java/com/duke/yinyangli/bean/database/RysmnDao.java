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
 * DAO for table "rysmn".
*/
public class RysmnDao extends AbstractDao<Rysmn, Long> {

    public static final String TABLENAME = "rysmn";

    /**
     * Properties of entity Rysmn.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Siceng = new Property(1, String.class, "siceng", false, "siceng");
        public final static Property Mingmi = new Property(2, String.class, "mingmi", false, "mingmi");
    }


    public RysmnDao(DaoConfig config) {
        super(config);
    }
    
    public RysmnDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Rysmn entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String siceng = entity.getSiceng();
        if (siceng != null) {
            stmt.bindString(2, siceng);
        }
 
        String mingmi = entity.getMingmi();
        if (mingmi != null) {
            stmt.bindString(3, mingmi);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Rysmn entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String siceng = entity.getSiceng();
        if (siceng != null) {
            stmt.bindString(2, siceng);
        }
 
        String mingmi = entity.getMingmi();
        if (mingmi != null) {
            stmt.bindString(3, mingmi);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Rysmn readEntity(Cursor cursor, int offset) {
        Rysmn entity = new Rysmn( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // siceng
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // mingmi
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Rysmn entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSiceng(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMingmi(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Rysmn entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Rysmn entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Rysmn entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}