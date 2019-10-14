package sitetech.NFCcheckPoint.Helpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sitetech.NFCcheckPoint.db.Bus;
import sitetech.NFCcheckPoint.db.BusDao;
import sitetech.NFCcheckPoint.db.Empresa;

public class nfcData {
    private Bus bus;
    private Empresa empresa;
    private Date ultimoCheck;

    public nfcData(Bus _bus) {
        bus = _bus;
        empresa = _bus.getEmpresa();
        ultimoCheck = new Date();
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Date getUltimoCheck() {
        return ultimoCheck;
    }

    public void setUltimoCheck(Date ultimoCheck) {
        this.ultimoCheck = ultimoCheck;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
