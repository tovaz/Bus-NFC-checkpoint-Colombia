package sitetech.NFCcheckPoint.Helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;

public class nfcData {
    private Bus bus;
    private Date horaSalida;

    public nfcData(Bus _bus) {
        bus = _bus;
        horaSalida = new Date();
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }
}
