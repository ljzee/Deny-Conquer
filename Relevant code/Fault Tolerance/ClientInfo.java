package networking.Shared;

import java.awt.*;
import java.io.Serializable;

public class ClientInfo implements Serializable {
    public Color color;
    public String addr;
    public int connectionID;
;
    public ClientInfo(Color color, String addr, int connectionID){
        this.color = color;
        this.addr = addr;
        this.connectionID = connectionID;
    }
}
