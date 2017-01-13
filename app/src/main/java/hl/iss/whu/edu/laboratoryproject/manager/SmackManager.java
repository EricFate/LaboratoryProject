package hl.iss.whu.edu.laboratoryproject.manager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;

/**
 * Created by fate on 2016/12/1.
 */

public class SmackManager {

    private static XMPPTCPConnection connection;
    private static VCard vCard;
    static {
        try {
            XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                    .setHost(Constant.CHAT_HOST)
                    .setPort(Constant.CHAT_PORT)
                    .setXmppDomain(JidCreate.domainBareFrom(Constant.CHAT_DOMAIN))
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
            connection = new XMPPTCPConnection(configuration);
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void login(String name, String password) throws InterruptedException, IOException, SmackException, XMPPException {
        if (!connection.isConnected()){
            connection.connect();
        }
        connection.login(name,password);
        vCard = VCardManager.getInstanceFor(connection).loadVCard();

    }
    public static AbstractXMPPConnection getConnection() {
        return connection;
    }

    public static VCard getVCard(){
        return vCard;
    }
}
