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
 * DAO for table "USUARIO".
*/
public class UsuarioDao extends AbstractDao<Usuario, Long> {

    public static final String TABLENAME = "USUARIO";

    /**
     * Properties of entity Usuario.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Cedula = new Property(1, String.class, "cedula", false, "CEDULA");
        public final static Property Nombre = new Property(2, String.class, "nombre", false, "NOMBRE");
        public final static Property Password = new Property(3, String.class, "password", false, "PASSWORD");
        public final static Property Telefono = new Property(4, String.class, "telefono", false, "TELEFONO");
        public final static Property Rol = new Property(5, String.class, "rol", false, "ROL");
        public final static Property Activo = new Property(6, boolean.class, "activo", false, "ACTIVO");
        public final static Property Eliminado = new Property(7, boolean.class, "eliminado", false, "ELIMINADO");
    }


    public UsuarioDao(DaoConfig config) {
        super(config);
    }
    
    public UsuarioDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USUARIO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CEDULA\" TEXT," + // 1: cedula
                "\"NOMBRE\" TEXT NOT NULL ," + // 2: nombre
                "\"PASSWORD\" TEXT," + // 3: password
                "\"TELEFONO\" TEXT," + // 4: telefono
                "\"ROL\" TEXT NOT NULL ," + // 5: rol
                "\"ACTIVO\" INTEGER NOT NULL ," + // 6: activo
                "\"ELIMINADO\" INTEGER NOT NULL );"); // 7: eliminado
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USUARIO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Usuario entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String cedula = entity.getCedula();
        if (cedula != null) {
            stmt.bindString(2, cedula);
        }
        stmt.bindString(3, entity.getNombre());
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(4, password);
        }
 
        String telefono = entity.getTelefono();
        if (telefono != null) {
            stmt.bindString(5, telefono);
        }
        stmt.bindString(6, entity.getRol());
        stmt.bindLong(7, entity.getActivo() ? 1L: 0L);
        stmt.bindLong(8, entity.getEliminado() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Usuario entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String cedula = entity.getCedula();
        if (cedula != null) {
            stmt.bindString(2, cedula);
        }
        stmt.bindString(3, entity.getNombre());
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(4, password);
        }
 
        String telefono = entity.getTelefono();
        if (telefono != null) {
            stmt.bindString(5, telefono);
        }
        stmt.bindString(6, entity.getRol());
        stmt.bindLong(7, entity.getActivo() ? 1L: 0L);
        stmt.bindLong(8, entity.getEliminado() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Usuario readEntity(Cursor cursor, int offset) {
        Usuario entity = new Usuario( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cedula
            cursor.getString(offset + 2), // nombre
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // password
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // telefono
            cursor.getString(offset + 5), // rol
            cursor.getShort(offset + 6) != 0, // activo
            cursor.getShort(offset + 7) != 0 // eliminado
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Usuario entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCedula(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNombre(cursor.getString(offset + 2));
        entity.setPassword(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTelefono(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRol(cursor.getString(offset + 5));
        entity.setActivo(cursor.getShort(offset + 6) != 0);
        entity.setEliminado(cursor.getShort(offset + 7) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Usuario entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Usuario entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Usuario entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
