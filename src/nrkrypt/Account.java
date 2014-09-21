/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrkrypt;

/**
 *
 * @author narko
 */
public class Account {
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

        
}
