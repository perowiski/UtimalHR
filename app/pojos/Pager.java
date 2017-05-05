package pojos;


import java.awt.print.Pageable;

/**
 * Created by peter on 4/27/17.
 */
public class Pager {

    public int page;
    public int limit;
    public int recordSize;

    //eg recordSize = 100,  limit = 5; page = 0
    public Pager(int page, int limit, int totalSize){
        this.page = page;
        this.limit = limit;
        this.recordSize = totalSize;
    }

    public boolean hasNext(){
        return false;
    }

    public boolean hasPrevious(){
        return false;
    }

}
