package Server;

import java.io.Serializable;

public class Entry implements Serializable{
    private String key;
    private String value;
    private String op;

    public Entry(String key, String value, String op)
    {
        this.key = key;
        this.value = value;
        this.op = op;
    }

    protected String getOp()
    {
        return op;
    }

    protected String getKey()
    {
        return key;
    }

    protected String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return op+" "+key+" , "+value;
    }
    
}
