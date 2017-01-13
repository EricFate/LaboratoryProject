package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jxmpp.jid.Jid;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Request;
import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/12/25.
 */

public class RecyclerRequestAdapter extends BaseRecyclerViewAdapter<Request,RecyclerRequestAdapter.MyViewHolder> {

    public RecyclerRequestAdapter(ArrayList<Request> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_request, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Request request = data.get(position);
        final AbstractXMPPConnection connection = SmackManager.getConnection();
        final Roster roster = Roster.getInstanceFor(connection);
        final Jid from = request.getFrom();
        final String fromString = from.getLocalpartOrNull().toString();
        View.OnClickListener acceptListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Presence presenceRes = new Presence(Presence.Type.subscribed);
                presenceRes.setTo(from);
                try {
                    connection.sendStanza(presenceRes);
                    roster.createEntry(from.asBareJid(), fromString,new String[]{"同学"});
                    remove(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(UiUtils.getContext()).setMessage("失败"+e).show();
                }
            }
        };
        View.OnClickListener rejectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Presence presenceRes = new Presence(Presence.Type.unsubscribe);
                presenceRes.setTo(from);
                try {
                    connection.sendStanza(presenceRes);
                    remove(request);
                } catch (Exception e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(UiUtils.getContext()).setMessage("失败"+e).show();
                }
            }
        };
        View.OnClickListener confirmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(request);
            }
        };

        holder.btAccept.setOnClickListener(acceptListener);
        holder.btReject.setOnClickListener(rejectListener);
        holder.btConfirm.setOnClickListener(confirmListener);

        switch (request.getType()){
            case Request.TYPE_SUBSCRIBE:
                holder.tvInfo.setText(fromString+"向你发送了好友请求");
                holder.btConfirm.setVisibility(View.GONE);
                break;
            case Request.TYPE_SUBSCRIBED:
                holder.tvInfo.setText(fromString+"接受了你的好友请求");
                holder.btAccept.setVisibility(View.GONE);
                holder.btReject.setVisibility(View.GONE);
                break;
            case Request.TYPE_UNSUBSCRIBE:
                holder.tvInfo.setText(fromString+"拒绝了你的好友请求");
                holder.btAccept.setVisibility(View.GONE);
                holder.btReject.setVisibility(View.GONE);
                break;
        }
    }

    public void remove(Request request) {
        data.remove(request);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvInfo;
        Button btAccept;
        Button btReject;
        Button btConfirm;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvInfo = ButterKnife.findById(itemView, R.id.tv_info);
            btAccept = ButterKnife.findById(itemView,R.id.bt_accept);
            btReject  = ButterKnife.findById(itemView,R.id.bt_reject);
            btConfirm = ButterKnife.findById(itemView,R.id.bt_confirm);
        }
    }
}
