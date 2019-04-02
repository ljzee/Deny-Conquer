package networking.Server;

import networking.Shared.ClientInfo;

import java.awt.*;
import java.util.ArrayList;

public class ServerHelper {
    public static Color getPreassignedColor(String addr, ArrayList<ClientInfo> Infos) {
        for (ClientInfo ci : Infos) {
            System.out.println(ci);
            if (ci.addr.equals(addr)) {
                return new Color(ci.color.getRGB());
            }
        }

        return null;
    }
}
