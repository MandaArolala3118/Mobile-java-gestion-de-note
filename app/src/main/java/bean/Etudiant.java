package bean;

public class Etudiant {
    private int number;
    private String name;
    private double avarage;

    public Etudiant(int number, String name, double avarage){
        this.number = number;
        this.name = name;
        this.avarage = avarage;
    }

    public int getNumber(){
        return number;
    }
    public void setNumber(int number){
        this.number = number;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public double getAvarage(){
        return avarage;
    }
    public void setAvarage(double avarage){
        this.avarage = avarage;
    }
    public String getObs(){
        if(avarage < 5)
            return "exclus";
        if(avarage >= 10)
            return "admis";
        else
            return "redoublant";
    }
}
