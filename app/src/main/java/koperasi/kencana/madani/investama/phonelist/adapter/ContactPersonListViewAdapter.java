package koperasi.kencana.madani.investama.phonelist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.xeoh.android.texthighlighter.TextHighlighter;
import java.util.List;
import koperasi.kencana.madani.investama.phonelist.R;
import koperasi.kencana.madani.investama.phonelist.entity.ContactPersonEntity;
import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by zz on 2016/8/15.
 */
public class ContactPersonListViewAdapter extends BaseSortListViewAdapter<ContactPersonEntity, ContactPersonListViewAdapter.ViewHolder> {

    private TextHighlighter textHighlighter;

    public ContactPersonListViewAdapter(Context ctx, List<ContactPersonEntity> datas, TextHighlighter textHighlighter) {
        super(ctx, datas);
        this.textHighlighter = textHighlighter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.contact_list_item;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvName = (TextView) view.findViewById(R.id.list_item_tv_name);
        viewHolder.tvHP = (TextView) view.findViewById(R.id.list_item_tv_hp);

        return viewHolder;
    }

    @Override
    public void bindValues(ViewHolder viewHolder, int position) {
        this.textHighlighter.addTarget(viewHolder.tvName);
        this.textHighlighter.addTarget(viewHolder.tvHP);

        String detail = mDatas.get(position).getPersonName();
        String[] detail_s = detail.split("@HP");
        String detail_r = detail.replace("@HP"+detail_s[detail_s.length - 1], "");

        viewHolder.tvName.setText(detail_r);
        viewHolder.tvHP.setText(detail_s[detail_s.length - 1].replace("-", ""));
        this.textHighlighter.invalidate(TextHighlighter.CASE_INSENSITIVE_MATCHER);
    }

    public static class ViewHolder extends BaseViewHolder {
        protected TextView tvName;
        protected TextView tvHP;
    }

}
