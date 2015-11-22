package capstone.barcodereader;

/**
 * Created by meraj0 on 2015-11-21.
 */
public class menu {
    private String category;
    private int id;

    public menu(String category, int iconId){
        this.category  = category;
        this.id = iconId;
    }

    public String getCategory(){
        return category;
    }
    public int getId(){
        return id;
    }
}

