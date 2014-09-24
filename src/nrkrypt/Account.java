/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrkrypt;

import javax.xml.bind.annotation.*;

/**
 *
 * @author narko
 */

@XmlRootElement(name="account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account {
    
    @XmlAttribute(name="id")
    private String account;
    private String name;
    private String email;
    private String nick;
    private String question;
    private String answer;
    private String password;
    private String other;

    public Account() {
    }
    
    public Account(String account, String name, String email, String nick, String question, String answer, String password, String other) {
        this.account = account;
        this.name = name;
        this.email = email;
        this.nick = nick;
        this.question = question;
        this.answer = answer;
        this.password = password;
        this.other = other;
    }
    

    //@XmlAttribute(name="id")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }
//    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
//    @XmlElement
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }
//    @XmlElement
    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getQuestion() {
        return question;
    }
//    @XmlElement
    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }
//    @XmlElement
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPassword() {
        return password;
    }
//    @XmlElement
    public void setPassword(String password) {
        this.password = password;
    }

    public String getOther() {
        return other;
    }
//    @XmlElement
    public void setOther(String other) {
        this.other = other;
    }

        
}
