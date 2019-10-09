package sitetech.greendaogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

import java.util.function.LongPredicate;

public class daoGenerator {
    private Entity user, empresa, bus, ruta, horario, horarioPorRuta, turno, registroTurno, diaFestivo;

    public  static void main(String[] args) {
        Schema schema = new Schema(1, "sitetech.NFCcheckPoint.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        daoGenerator dg = new daoGenerator();
        dg.addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTables(final Schema schema) {
        userEntity(schema);
        empresaEntity(schema);
        busEntity(schema);
        rutaEntity(schema);
        horarioEntity(schema);
        horariosPorRutaEntity(schema);
        turnoEntity(schema);
        registroEntity(schema);
        diasFestivosEntity(schema);
    }

    // This is use to describe the colums of your table
    private Entity userEntity(final Schema schema) {
        user = schema.addEntity("Usuario");

        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("cedula");
        user.addStringProperty("nombre").notNull();
        user.addStringProperty("password");
        user.addStringProperty("telefono");
        user.addStringProperty("rol").notNull();
        user.addBooleanProperty("activo").notNull();
        user.addBooleanProperty("eliminado").notNull();

        return user;
    }

    private Entity busEntity(final Schema schema) {
        bus = schema.addEntity("Bus");

        bus.addIdProperty().primaryKey().autoincrement();
        bus.addStringProperty("placa").notNull();
        bus.addStringProperty("interno");
        bus.addLongProperty("recorridos");
        bus.addBooleanProperty("eliminado").notNull();
        Property empresaId = bus.addLongProperty("empresaId").notNull().getProperty();

        bus.addToOne(empresa, empresaId);

        return bus;
    }

    private Entity empresaEntity(final Schema schema) {
        empresa = schema.addEntity("Empresa");

        empresa.addIdProperty().primaryKey().autoincrement();
        empresa.addStringProperty("nombre").notNull();
        empresa.addStringProperty("telefono");
        empresa.addBooleanProperty("eliminado").notNull();
        return empresa;
    }

    private Entity horarioEntity(final Schema schema) {
        horario = schema.addEntity("Horario"); //hora de inicio
        horario.addIdProperty().primaryKey().autoincrement();
        horario.addStringProperty("nombre");
        horario.addStringProperty("horaDesde");
        horario.addStringProperty("horaHasta");
        horario.addIntProperty("maxMinutos");

        horario.addStringProperty("horaFestivoDesde");
        horario.addStringProperty("horaFestivoHasta");
        horario.addIntProperty("maxMinutosFestivo");

        horario.addStringProperty("horaFinSemanaDesde"); //hora no en uso
        horario.addStringProperty("horaFinSemanaHasta"); //hora
        horario.addIntProperty("maxMinutosFinSemana"); // hora

        horario.addBooleanProperty("eliminado").notNull();
        return horario;
    }

    private Entity rutaEntity(final Schema schema) {
        ruta = schema.addEntity("Ruta");

        ruta.addIdProperty().primaryKey().autoincrement();
        ruta.addStringProperty("nombre").notNull();
        ruta.addBooleanProperty("eliminada").notNull();
        return ruta;
    }

    private Entity horariosPorRutaEntity(final Schema schema){ // TABLA INTERMEDIA PARA GUARDAR INFO RELACIONADA
        horarioPorRuta = schema.addEntity("horarioPorRuta");
        horarioPorRuta.addIdProperty().primaryKey().autoincrement();

        Property rutaId = horarioPorRuta.addLongProperty("rutaId").notNull().getProperty();
        Property horarioId = horarioPorRuta.addLongProperty("horarioId").notNull().getProperty();

        horarioPorRuta.addToOne(horario, horarioId);
        horarioPorRuta.addToOne(ruta, rutaId);
        horarioPorRuta.addBooleanProperty("eliminado").notNull();

        return horarioPorRuta;
    }

    private Entity diasFestivosEntity(final Schema schema){ // TABLA INTERMEDIA PARA GUARDAR INFO RELACIONADA
        diaFestivo = schema.addEntity("DiaFestivo");
        diaFestivo.addIdProperty().primaryKey().autoincrement();

        diaFestivo.addStringProperty("nombre").getProperty();
        diaFestivo.addDateProperty("dia").notNull().getProperty();
        diaFestivo.addBooleanProperty("eliminado").notNull();

        return diaFestivo;
    }

    private Entity turnoEntity(final Schema schema){
        turno = schema.addEntity("Turno");

        turno.addIdProperty().primaryKey().autoincrement();
        turno.addDateProperty("fechaCreacion").notNull();
        turno.addDateProperty("fechaCierre");
        turno.addLongProperty("totalBuses");
        turno.addBooleanProperty("eliminada").notNull();
        Property operadorCierre = turno.addLongProperty("operadorCierre").notNull().getProperty();

        turno.addToOne(user, operadorCierre);

        return turno;
    }

    private Entity registroEntity(final Schema schema){
        registroTurno = schema.addEntity("Registro_Turno");

        registroTurno.addIdProperty().primaryKey().autoincrement();
        registroTurno.addDateProperty("fecha");
        registroTurno.addBooleanProperty("eliminado");
        registroTurno.addIntProperty("minAtrazado");
        registroTurno.addIntProperty("minAdelantado");
        registroTurno.addStringProperty("justificacion");
        registroTurno.addStringProperty("despacho");
        Property busId = registroTurno.addLongProperty("busId").notNull().getProperty();
        Property rutaId = registroTurno.addLongProperty("rutaId").notNull().getProperty();
        Property turnoId = registroTurno.addLongProperty("turnoId").notNull().getProperty();
        Property userId = registroTurno.addLongProperty("userId").notNull().getProperty();

        registroTurno.addToOne(bus, busId);
        registroTurno.addToOne(ruta, rutaId);
        registroTurno.addToOne(turno, turnoId);
        registroTurno.addToOne(user, userId);
        return registroTurno;
    }
}
