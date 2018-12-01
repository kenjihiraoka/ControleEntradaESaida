/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import dao.CadastroDeAcessoDAO;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.CadastroDeAcesso;

/**
 *
 * @author kenji
 */
@FacesConverter(value = "acessoConverter", forClass = CadastroDeAcesso.class)
public class AcessoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            CadastroDeAcessoDAO acessoDAO = new CadastroDeAcessoDAO(javax.persistence.Persistence.createEntityManagerFactory("ControleEntradaSaida2PU"));
            
            return acessoDAO.findCadastroDeAcesso(Integer.valueOf(value));
        }
        
        return null;
    }
    
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null && (value instanceof CadastroDeAcesso)) {
            return String.valueOf(((CadastroDeAcesso) value).getId());
        }
        
        return null;
    }
}
