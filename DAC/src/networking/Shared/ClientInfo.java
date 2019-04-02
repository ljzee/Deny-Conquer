package networking.Shared;

import java.awt.*;
import java.io.Serializable;

public class ClientInfo implements Serializable {
    public Color color;
    public String addr;

    public ClientInfo(Color color, String addr){
        this.color = color;
        this.addr = addr;
    }
}
