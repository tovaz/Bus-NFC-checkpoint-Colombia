package sitetech.NFCcheckPoint.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PUNTO".
*/
public class PuntoDao extends AbstractDao<Punto, Long> {

    public static final String TABLENAME = "PUNTO";

    /**
     * Properties of entity Punto.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nombre = new Property(1, String.class, "nombre", false, "NOMBRE");
        public final static Property Eliminada = new Property(2, boolean.class, "eliminada", false, "ELIMINADA");
    }


    public PuntoDao(DaoConfig config) {
        super(config);
    }
    
    public PuntoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PUNTO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOMBRE\" TEXT NOT NULL ," + // 1: nombre
                "\"ELIMINADA\" INTEGER NOT NULL );"); // 2: eliminada
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PUNTO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Punto entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNombre());
        stmt.bindLong(3, entity.getEliminada() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Punto entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNombre());
        stmt.bindLong(3, entity.getEliminada() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Punto readEntity(Cursor cursor, int offset) {
        Punto entity = new Punto( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // nombre
            cursor.getShort(offset + 2) != 0 // eliminada
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Punto entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.getString(offset + 1));
        entity.setEliminada(cursor.getShort(offset + 2) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Punto entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Punto entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Punto entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
