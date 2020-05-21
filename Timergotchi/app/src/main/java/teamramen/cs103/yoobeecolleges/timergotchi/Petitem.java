package teamramen.cs103.yoobeecolleges.timergotchi;

public class Petitem {
    public String name;
    public int image;
    public int id;
    public int health;
    public int affection;

    //0 is food, 1 is accessory
    public int type;

    public Petitem(String name, int image,int type,int id, int health, int affection){
        this.name = name;
        this.image = image;
        this.type = type;
        this.id = id;
        this.health = health;
        this.affection = affection;
    }
}
