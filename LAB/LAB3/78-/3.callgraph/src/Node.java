
public class Node{

    public String name;
    public String type;

    public Node(String name,String type){

        this.name=name;
        this.type=type;
    }

    public Node(){

        this.name="";
        this.type="";
    }

    public Node(Node n){

        this.name=n.name;
        this.type=n.type;
    }
}