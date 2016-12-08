package hl.iss.whu.edu.laboratoryproject.manager;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
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
    static {
        try {
            XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                    .setHost(Constant.CHAT_HOST)
//                    .setCompressionEnabled(false)
                    .setPort(Constant.CHAT_PORT)
                    .setXmppDomain(JidCreate.domainBareFrom(Constant.CHAT_DOMAIN))
                    .build();
            connection = new XMPPTCPConnection(configuration);
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void login(String name, String password) throws InterruptedException, IOException, SmackException, XMPPException {
        connection.login(name,password);
    }
    public static AbstractXMPPConnection getConnection()
    {
        return connection;
    }


}
