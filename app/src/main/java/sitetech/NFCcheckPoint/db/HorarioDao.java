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
 * DAO for table "HORARIO".
*/
public class HorarioDao extends AbstractDao<Horario, Long> {

    public static final String TABLENAME = "HORARIO";

    /**
     * Properties of entity Horario.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nombre = new Property(1, String.class, "nombre", false, "NOMBRE");
        public final static Property HoraDesde = new Property(2, String.class, "horaDesde", false, "HORA_DESDE");
        public final static Property HoraHasta = new Property(3, String.class, "horaHasta", false, "HORA_HASTA");
        public final static Property MaxMinutos = new Property(4, Integer.class, "maxMinutos", false, "MAX_MINUTOS");
        public final static Property TiempoNormal = new Property(5, String.class, "tiempoNormal", false, "TIEMPO_NORMAL");
        public final static Property HoraFestivoDesde = new Property(6, String.class, "horaFestivoDesde", false, "HORA_FESTIVO_DESDE");
        public final static Property HoraFestivoHasta = new Property(7, String.class, "horaFestivoHasta", false, "HORA_FESTIVO_HASTA");
        public final static Property TiempoDiaFestivo = new Property(8, String.class, "tiempoDiaFestivo", false, "TIEMPO_DIA_FESTIVO");
        public final static Property MaxMinutosFestivo = new Property(9, Integer.class, "maxMinutosFestivo", false, "MAX_MINUTOS_FESTIVO");
        public final static Property HoraFinSemanaDesde = new Property(10, String.class, "horaFinSemanaDesde", false, "HORA_FIN_SEMANA_DESDE");
        public final static Property HoraFinSemanaHasta = new Property(11, String.class, "horaFinSemanaHasta", false, "HORA_FIN_SEMANA_HASTA");
        public final static Property TiempoFinSemana = new Property(12, String.class, "tiempoFinSemana", false, "TIEMPO_FIN_SEMANA");
        public final static Property MaxMinutosFinSemana = new Property(13, Integer.class, "maxMinutosFinSemana", false, "MAX_MINUTOS_FIN_SEMANA");
        public final static Property Eliminado = new Property(14, boolean.class, "eliminado", false, "ELIMINADO");
    }


    public HorarioDao(DaoConfig config) {
        super(config);
    }
    
    public HorarioDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HORARIO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOMBRE\" TEXT," + // 1: nombre
                "\"HORA_DESDE\" TEXT," + // 2: horaDesde
                "\"HORA_HASTA\" TEXT," + // 3: horaHasta
                "\"MAX_MINUTOS\" INTEGER," + // 4: maxMinutos
                "\"TIEMPO_NORMAL\" TEXT," + // 5: tiempoNormal
                "\"HORA_FESTIVO_DESDE\" TEXT," + // 6: horaFestivoDesde
                "\"HORA_FESTIVO_HASTA\" TEXT," + // 7: horaFestivoHasta
                "\"TIEMPO_DIA_FESTIVO\" TEXT," + // 8: tiempoDiaFestivo
                "\"MAX_MINUTOS_FESTIVO\" INTEGER," + // 9: maxMinutosFestivo
                "\"HORA_FIN_SEMANA_DESDE\" TEXT," + // 10: horaFinSemanaDesde
                "\"HORA_FIN_SEMANA_HASTA\" TEXT," + // 11: horaFinSemanaHasta
                "\"TIEMPO_FIN_SEMANA\" TEXT," + // 12: tiempoFinSemana
                "\"MAX_MINUTOS_FIN_SEMANA\" INTEGER," + // 13: maxMinutosFinSemana
                "\"ELIMINADO\" INTEGER NOT NULL );"); // 14: eliminado
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HORARIO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Horario entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(2, nombre);
        }
 
        String horaDesde = entity.getHoraDesde();
        if (horaDesde != null) {
            stmt.bindString(3, horaDesde);
        }
 
        String horaHasta = entity.getHoraHasta();
        if (horaHasta != null) {
            stmt.bindString(4, horaHasta);
        }
 
        Integer maxMinutos = entity.getMaxMinutos();
        if (maxMinutos != null) {
            stmt.bindLong(5, maxMinutos);
        }
 
        String tiempoNormal = entity.getTiempoNormal();
        if (tiempoNormal != null) {
            stmt.bindString(6, tiempoNormal);
        }
 
        String horaFestivoDesde = entity.getHoraFestivoDesde();
        if (horaFestivoDesde != null) {
            stmt.bindString(7, horaFestivoDesde);
        }
 
        String horaFestivoHasta = entity.getHoraFestivoHasta();
        if (horaFestivoHasta != null) {
            stmt.bindString(8, horaFestivoHasta);
        }
 
        String tiempoDiaFestivo = entity.getTiempoDiaFestivo();
        if (tiempoDiaFestivo != null) {
            stmt.bindString(9, tiempoDiaFestivo);
        }
 
        Integer maxMinutosFestivo = entity.getMaxMinutosFestivo();
        if (maxMinutosFestivo != null) {
            stmt.bindLong(10, maxMinutosFestivo);
        }
 
        String horaFinSemanaDesde = entity.getHoraFinSemanaDesde();
        if (horaFinSemanaDesde != null) {
            stmt.bindString(11, horaFinSemanaDesde);
        }
 
        String horaFinSemanaHasta = entity.getHoraFinSemanaHasta();
        if (horaFinSemanaHasta != null) {
            stmt.bindString(12, horaFinSemanaHasta);
        }
 
        String tiempoFinSemana = entity.getTiempoFinSemana();
        if (tiempoFinSemana != null) {
            stmt.bindString(13, tiempoFinSemana);
        }
 
        Integer maxMinutosFinSemana = entity.getMaxMinutosFinSemana();
        if (maxMinutosFinSemana != null) {
            stmt.bindLong(14, maxMinutosFinSemana);
        }
        stmt.bindLong(15, entity.getEliminado() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Horario entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(2, nombre);
        }
 
        String horaDesde = entity.getHoraDesde();
        if (horaDesde != null) {
            stmt.bindString(3, horaDesde);
        }
 
        String horaHasta = entity.getHoraHasta();
        if (horaHasta != null) {
            stmt.bindString(4, horaHasta);
        }
 
        Integer maxMinutos = entity.getMaxMinutos();
        if (maxMinutos != null) {
            stmt.bindLong(5, maxMinutos);
        }
 
        String tiempoNormal = entity.getTiempoNormal();
        if (tiempoNormal != null) {
            stmt.bindString(6, tiempoNormal);
        }
 
        String horaFestivoDesde = entity.getHoraFestivoDesde();
        if (horaFestivoDesde != null) {
            stmt.bindString(7, horaFestivoDesde);
        }
 
        String horaFestivoHasta = entity.getHoraFestivoHasta();
        if (horaFestivoHasta != null) {
            stmt.bindString(8, horaFestivoHasta);
        }
 
        String tiempoDiaFestivo = entity.getTiempoDiaFestivo();
        if (tiempoDiaFestivo != null) {
            stmt.bindString(9, tiempoDiaFestivo);
        }
 
        Integer maxMinutosFestivo = entity.getMaxMinutosFestivo();
        if (maxMinutosFestivo != null) {
            stmt.bindLong(10, maxMinutosFestivo);
        }
 
        String horaFinSemanaDesde = entity.getHoraFinSemanaDesde();
        if (horaFinSemanaDesde != null) {
            stmt.bindString(11, horaFinSemanaDesde);
        }
 
        String horaFinSemanaHasta = entity.getHoraFinSemanaHasta();
        if (horaFinSemanaHasta != null) {
            stmt.bindString(12, horaFinSemanaHasta);
        }
 
        String tiempoFinSemana = entity.getTiempoFinSemana();
        if (tiempoFinSemana != null) {
            stmt.bindString(13, tiempoFinSemana);
        }
 
        Integer maxMinutosFinSemana = entity.getMaxMinutosFinSemana();
        if (maxMinutosFinSemana != null) {
            stmt.bindLong(14, maxMinutosFinSemana);
        }
        stmt.bindLong(15, entity.getEliminado() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Horario readEntity(Cursor cursor, int offset) {
        Horario entity = new Horario( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nombre
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // horaDesde
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // horaHasta
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // maxMinutos
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // tiempoNormal
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // horaFestivoDesde
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // horaFestivoHasta
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // tiempoDiaFestivo
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // maxMinutosFestivo
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // horaFinSemanaDesde
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // horaFinSemanaHasta
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // tiempoFinSemana
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // maxMinutosFinSemana
            cursor.getShort(offset + 14) != 0 // eliminado
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Horario entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHoraDesde(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHoraHasta(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMaxMinutos(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setTiempoNormal(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHoraFestivoDesde(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setHoraFestivoHasta(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTiempoDiaFestivo(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setMaxMinutosFestivo(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setHoraFinSemanaDesde(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setHoraFinSemanaHasta(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setTiempoFinSemana(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setMaxMinutosFinSemana(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setEliminado(cursor.getShort(offset + 14) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Horario entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Horario entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Horario entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
