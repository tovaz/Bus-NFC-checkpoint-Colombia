package sitetech.NFCcheckPoint.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REGISTRO__TURNO".
*/
public class Registro_TurnoDao extends AbstractDao<Registro_Turno, Long> {

    public static final String TABLENAME = "REGISTRO__TURNO";

    /**
     * Properties of entity Registro_Turno.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Fecha = new Property(1, java.util.Date.class, "fecha", false, "FECHA");
        public final static Property Eliminado = new Property(2, Boolean.class, "eliminado", false, "ELIMINADO");
        public final static Property MinAtrazado = new Property(3, String.class, "minAtrazado", false, "MIN_ATRAZADO");
        public final static Property MinAdelantado = new Property(4, String.class, "minAdelantado", false, "MIN_ADELANTADO");
        public final static Property Justificacion = new Property(5, String.class, "justificacion", false, "JUSTIFICACION");
        public final static Property Despacho = new Property(6, String.class, "despacho", false, "DESPACHO");
        public final static Property PuntoControl = new Property(7, String.class, "puntoControl", false, "PUNTO_CONTROL");
        public final static Property ExtraString = new Property(8, String.class, "extraString", false, "EXTRA_STRING");
        public final static Property ExtraInt = new Property(9, Integer.class, "extraInt", false, "EXTRA_INT");
        public final static Property BusId = new Property(10, long.class, "busId", false, "BUS_ID");
        public final static Property RutaId = new Property(11, long.class, "rutaId", false, "RUTA_ID");
        public final static Property TurnoId = new Property(12, long.class, "turnoId", false, "TURNO_ID");
        public final static Property UserId = new Property(13, long.class, "userId", false, "USER_ID");
        public final static Property PuntoId = new Property(14, long.class, "puntoId", false, "PUNTO_ID");
    }

    private DaoSession daoSession;


    public Registro_TurnoDao(DaoConfig config) {
        super(config);
    }
    
    public Registro_TurnoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REGISTRO__TURNO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FECHA\" INTEGER," + // 1: fecha
                "\"ELIMINADO\" INTEGER," + // 2: eliminado
                "\"MIN_ATRAZADO\" TEXT," + // 3: minAtrazado
                "\"MIN_ADELANTADO\" TEXT," + // 4: minAdelantado
                "\"JUSTIFICACION\" TEXT," + // 5: justificacion
                "\"DESPACHO\" TEXT," + // 6: despacho
                "\"PUNTO_CONTROL\" TEXT," + // 7: puntoControl
                "\"EXTRA_STRING\" TEXT," + // 8: extraString
                "\"EXTRA_INT\" INTEGER," + // 9: extraInt
                "\"BUS_ID\" INTEGER NOT NULL ," + // 10: busId
                "\"RUTA_ID\" INTEGER NOT NULL ," + // 11: rutaId
                "\"TURNO_ID\" INTEGER NOT NULL ," + // 12: turnoId
                "\"USER_ID\" INTEGER NOT NULL ," + // 13: userId
                "\"PUNTO_ID\" INTEGER NOT NULL );"); // 14: puntoId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REGISTRO__TURNO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Registro_Turno entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date fecha = entity.getFecha();
        if (fecha != null) {
            stmt.bindLong(2, fecha.getTime());
        }
 
        Boolean eliminado = entity.getEliminado();
        if (eliminado != null) {
            stmt.bindLong(3, eliminado ? 1L: 0L);
        }
 
        String minAtrazado = entity.getMinAtrazado();
        if (minAtrazado != null) {
            stmt.bindString(4, minAtrazado);
        }
 
        String minAdelantado = entity.getMinAdelantado();
        if (minAdelantado != null) {
            stmt.bindString(5, minAdelantado);
        }
 
        String justificacion = entity.getJustificacion();
        if (justificacion != null) {
            stmt.bindString(6, justificacion);
        }
 
        String despacho = entity.getDespacho();
        if (despacho != null) {
            stmt.bindString(7, despacho);
        }
 
        String puntoControl = entity.getPuntoControl();
        if (puntoControl != null) {
            stmt.bindString(8, puntoControl);
        }
 
        String extraString = entity.getExtraString();
        if (extraString != null) {
            stmt.bindString(9, extraString);
        }
 
        Integer extraInt = entity.getExtraInt();
        if (extraInt != null) {
            stmt.bindLong(10, extraInt);
        }
        stmt.bindLong(11, entity.getBusId());
        stmt.bindLong(12, entity.getRutaId());
        stmt.bindLong(13, entity.getTurnoId());
        stmt.bindLong(14, entity.getUserId());
        stmt.bindLong(15, entity.getPuntoId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Registro_Turno entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date fecha = entity.getFecha();
        if (fecha != null) {
            stmt.bindLong(2, fecha.getTime());
        }
 
        Boolean eliminado = entity.getEliminado();
        if (eliminado != null) {
            stmt.bindLong(3, eliminado ? 1L: 0L);
        }
 
        String minAtrazado = entity.getMinAtrazado();
        if (minAtrazado != null) {
            stmt.bindString(4, minAtrazado);
        }
 
        String minAdelantado = entity.getMinAdelantado();
        if (minAdelantado != null) {
            stmt.bindString(5, minAdelantado);
        }
 
        String justificacion = entity.getJustificacion();
        if (justificacion != null) {
            stmt.bindString(6, justificacion);
        }
 
        String despacho = entity.getDespacho();
        if (despacho != null) {
            stmt.bindString(7, despacho);
        }
 
        String puntoControl = entity.getPuntoControl();
        if (puntoControl != null) {
            stmt.bindString(8, puntoControl);
        }
 
        String extraString = entity.getExtraString();
        if (extraString != null) {
            stmt.bindString(9, extraString);
        }
 
        Integer extraInt = entity.getExtraInt();
        if (extraInt != null) {
            stmt.bindLong(10, extraInt);
        }
        stmt.bindLong(11, entity.getBusId());
        stmt.bindLong(12, entity.getRutaId());
        stmt.bindLong(13, entity.getTurnoId());
        stmt.bindLong(14, entity.getUserId());
        stmt.bindLong(15, entity.getPuntoId());
    }

    @Override
    protected final void attachEntity(Registro_Turno entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Registro_Turno readEntity(Cursor cursor, int offset) {
        Registro_Turno entity = new Registro_Turno( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // fecha
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // eliminado
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // minAtrazado
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // minAdelantado
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // justificacion
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // despacho
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // puntoControl
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // extraString
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // extraInt
            cursor.getLong(offset + 10), // busId
            cursor.getLong(offset + 11), // rutaId
            cursor.getLong(offset + 12), // turnoId
            cursor.getLong(offset + 13), // userId
            cursor.getLong(offset + 14) // puntoId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Registro_Turno entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFecha(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setEliminado(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setMinAtrazado(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMinAdelantado(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setJustificacion(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDespacho(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPuntoControl(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setExtraString(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setExtraInt(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setBusId(cursor.getLong(offset + 10));
        entity.setRutaId(cursor.getLong(offset + 11));
        entity.setTurnoId(cursor.getLong(offset + 12));
        entity.setUserId(cursor.getLong(offset + 13));
        entity.setPuntoId(cursor.getLong(offset + 14));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Registro_Turno entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Registro_Turno entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Registro_Turno entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getBusDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getRutaDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getTurnoDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getUsuarioDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T4", daoSession.getPuntoDao().getAllColumns());
            builder.append(" FROM REGISTRO__TURNO T");
            builder.append(" LEFT JOIN BUS T0 ON T.\"BUS_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN RUTA T1 ON T.\"RUTA_ID\"=T1.\"_id\"");
            builder.append(" LEFT JOIN TURNO T2 ON T.\"TURNO_ID\"=T2.\"_id\"");
            builder.append(" LEFT JOIN USUARIO T3 ON T.\"USER_ID\"=T3.\"_id\"");
            builder.append(" LEFT JOIN PUNTO T4 ON T.\"PUNTO_ID\"=T4.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Registro_Turno loadCurrentDeep(Cursor cursor, boolean lock) {
        Registro_Turno entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Bus bus = loadCurrentOther(daoSession.getBusDao(), cursor, offset);
         if(bus != null) {
            entity.setBus(bus);
        }
        offset += daoSession.getBusDao().getAllColumns().length;

        Ruta ruta = loadCurrentOther(daoSession.getRutaDao(), cursor, offset);
         if(ruta != null) {
            entity.setRuta(ruta);
        }
        offset += daoSession.getRutaDao().getAllColumns().length;

        Turno turno = loadCurrentOther(daoSession.getTurnoDao(), cursor, offset);
         if(turno != null) {
            entity.setTurno(turno);
        }
        offset += daoSession.getTurnoDao().getAllColumns().length;

        Usuario usuario = loadCurrentOther(daoSession.getUsuarioDao(), cursor, offset);
         if(usuario != null) {
            entity.setUsuario(usuario);
        }
        offset += daoSession.getUsuarioDao().getAllColumns().length;

        Punto punto = loadCurrentOther(daoSession.getPuntoDao(), cursor, offset);
         if(punto != null) {
            entity.setPunto(punto);
        }

        return entity;    
    }

    public Registro_Turno loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Registro_Turno> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Registro_Turno> list = new ArrayList<Registro_Turno>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Registro_Turno> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Registro_Turno> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
