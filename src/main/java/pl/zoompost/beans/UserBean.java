package pl.zoompost.beans;


import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import pl.zoompost.dao.DaoUsers;
import pl.zoompost.models.Users;


/**
 *
 * @author kandif
 */

@Named
@SessionScoped
public class UserBean implements Serializable {

    private String name;
    private boolean zalogowany = false;
    private boolean widocznyformularz = false;
    private DaoUsers du = new DaoUsers();
    private String uprawnienia = "brak";
    private String komunikat = "";
    
    @PostConstruct
    public void init(){
        
    }
    
    public String getKomunikat() {
        return komunikat;
    }

    public void setKomunikat(String komunikat) {
        this.komunikat = komunikat;
    }

    public String getUprawnienia() {
        return uprawnienia;
    }

    public void setUprawnienia(String uprawnienia) {
        this.uprawnienia = uprawnienia;
    }

    public boolean isZalogowany() {
        return zalogowany;
    }

    public void setZalogowany(boolean zalogowany) {
        this.zalogowany = zalogowany;
    }
    private String defaultprzywitanie = "Hello in ZoomPost!";
    private String przywitanie = "Hello in ZoomPost!";

    public void setPrzywitanie(String przywitanie) {
        this.przywitanie = przywitanie;
    }
    private String password;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getPrzywitanie() {
        if (zalogowany) {
            przywitanie = "Hi " + name + " with ZoomPost";
        }

        return przywitanie;
    }

    public String loginacja() {
        if (zalogowany) {
            zalogowany = false;
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            przywitanie = defaultprzywitanie;
        } else {
            if (!widocznyformularz) {
                RequestContext.getCurrentInstance().execute("showform()");
                widocznyformularz = true;
            } else {
                RequestContext.getCurrentInstance().execute("hideform()");
            }
        }
        return "index";
    }

    public void logowanie() {
        if (getName() != null && getPassword() != null) {
            Users trans = new Users();
            trans.setName(getName());trans.setPass(getPassword());
            Users u = (Users)du.login(trans);
            if (u != null) {
                setKomunikat("");
                setName(u.getName());
                setUprawnienia(u.getPermissions());
                zalogowany = true;
                RequestContext.getCurrentInstance().execute("hideform()");
            } else {
                setKomunikat("Hasło lub login jest nieprawidłowy.");
            }
        }

    }
    
    public String dodajpost(){
        if(getUprawnienia().equals("admin") && zalogowany){
            return "AddPost";
        }
        return "";
    }

}
