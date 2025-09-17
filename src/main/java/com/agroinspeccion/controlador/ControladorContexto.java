package com.agroinspeccion.controlador;

import com.agroinspeccion.dao.AsistenteTecnicoDAO;
import com.agroinspeccion.dao.CultivoDAO;
import com.agroinspeccion.dao.InformeFitosanitarioDAO;
import com.agroinspeccion.dao.InformeProduccionDAO;
import com.agroinspeccion.dao.LoteDAO;
import com.agroinspeccion.dao.LugarProduccionDAO;
import com.agroinspeccion.dao.PlagaDAO;
import com.agroinspeccion.dao.PredioDAO;
import com.agroinspeccion.dao.ProductorDAO;
import com.agroinspeccion.dao.PropietarioDAO;
import com.agroinspeccion.modelo.AsistenteTecnico;
import com.agroinspeccion.modelo.Cultivo;
import com.agroinspeccion.modelo.InformeFitosanitario;
import com.agroinspeccion.modelo.InformeProduccion;
import com.agroinspeccion.modelo.Lote;
import com.agroinspeccion.modelo.LugarProduccion;
import com.agroinspeccion.modelo.Plaga;
import com.agroinspeccion.modelo.Predio;
import com.agroinspeccion.modelo.Productor;
import com.agroinspeccion.modelo.Propietario;

/**
 * Contenedor de controladores de las distintas entidades.
 */
public class ControladorContexto {

    private final ProductorDAO productorDAO = new ProductorDAO();
    private final PropietarioDAO propietarioDAO = new PropietarioDAO();
    private final AsistenteTecnicoDAO asistenteDAO = new AsistenteTecnicoDAO();
    private final LugarProduccionDAO lugarDAO = new LugarProduccionDAO();
    private final PredioDAO predioDAO = new PredioDAO();
    private final CultivoDAO cultivoDAO = new CultivoDAO();
    private final LoteDAO loteDAO = new LoteDAO();
    private final PlagaDAO plagaDAO = new PlagaDAO();
    private final InformeFitosanitarioDAO informeFitoDAO = new InformeFitosanitarioDAO();
    private final InformeProduccionDAO informeProduccionDAO = new InformeProduccionDAO();

    private final BaseControlador<Productor> productorControlador = new BaseControlador<>(productorDAO);
    private final BaseControlador<Propietario> propietarioControlador = new BaseControlador<>(propietarioDAO);
    private final BaseControlador<AsistenteTecnico> asistenteControlador = new BaseControlador<>(asistenteDAO);
    private final BaseControlador<LugarProduccion> lugarControlador = new BaseControlador<>(lugarDAO);
    private final BaseControlador<Predio> predioControlador = new BaseControlador<>(predioDAO);
    private final BaseControlador<Cultivo> cultivoControlador = new BaseControlador<>(cultivoDAO);
    private final BaseControlador<Lote> loteControlador = new BaseControlador<>(loteDAO);
    private final BaseControlador<Plaga> plagaControlador = new BaseControlador<>(plagaDAO);
    private final BaseControlador<InformeFitosanitario> informeFitoControlador = new BaseControlador<>(informeFitoDAO);
    private final BaseControlador<InformeProduccion> informeProduccionControlador = new BaseControlador<>(informeProduccionDAO);

    public ControladorContexto() {
    }

    public BaseControlador<Productor> getProductorControlador() {
        return productorControlador;
    }

    public BaseControlador<Propietario> getPropietarioControlador() {
        return propietarioControlador;
    }

    public BaseControlador<AsistenteTecnico> getAsistenteControlador() {
        return asistenteControlador;
    }

    public BaseControlador<LugarProduccion> getLugarControlador() {
        return lugarControlador;
    }

    public BaseControlador<Predio> getPredioControlador() {
        return predioControlador;
    }

    public BaseControlador<Cultivo> getCultivoControlador() {
        return cultivoControlador;
    }

    public BaseControlador<Lote> getLoteControlador() {
        return loteControlador;
    }

    public BaseControlador<Plaga> getPlagaControlador() {
        return plagaControlador;
    }

    public BaseControlador<InformeFitosanitario> getInformeFitoControlador() {
        return informeFitoControlador;
    }

    public BaseControlador<InformeProduccion> getInformeProduccionControlador() {
        return informeProduccionControlador;
    }

    public InformeFitosanitarioDAO getInformeFitoDAO() {
        return informeFitoDAO;
    }

    public LoteDAO getLoteDAO() {
        return loteDAO;
    }

    public CultivoDAO getCultivoDAO() {
        return cultivoDAO;
    }

    public PredioDAO getPredioDAO() {
        return predioDAO;
    }

    public LugarProduccionDAO getLugarDAO() {
        return lugarDAO;
    }

    public ProductorDAO getProductorDAO() {
        return productorDAO;
    }

    public PropietarioDAO getPropietarioDAO() {
        return propietarioDAO;
    }

    public AsistenteTecnicoDAO getAsistenteDAO() {
        return asistenteDAO;
    }

    public PlagaDAO getPlagaDAO() {
        return plagaDAO;
    }

    public InformeProduccionDAO getInformeProduccionDAO() {
        return informeProduccionDAO;
    }
}
