/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addressmodel;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of person to XML
 * 
 * @author ibnahmad
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {
    
    private List<Person> persons;
    
    @XmlElement(name = "person")
    public List<Person> getPersons(){
        return persons;
    }
    
    public void setPersons(List<Person> persons){
        this.persons = persons;
    }
}
