package networking.Server;

import networking.Shared.ClientInfo;

import java.awt.*;
import java.util.ArrayList;

public class ServerHelper {
    public static Color getPreassignedColor(String addr, ArrayList<ClientInfo> Infos) {
        for (ClientInfo ci : Infos) {
            System.out.println(ci.addr);
            System.out.println(addr);
            if (ci.addr.equals(addr)) {
                System.out.println(ci.color.toString());
                return new Color(ci.color.getRGB());
            }
        }

        return null;
    }
    public static int getOldConnectionID(String addr, ArrayList<ClientInfo> infos){
        for(ClientInfo ci :infos){
            if (ci.addr.equals(addr)) {
                return ci.connectionID;
            }
        }
        return -1;
    }
}
