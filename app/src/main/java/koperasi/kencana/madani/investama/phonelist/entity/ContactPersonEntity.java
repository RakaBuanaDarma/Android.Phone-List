package koperasi.kencana.madani.investama.phonelist.entity;

import me.zhouzhuo.zzletterssidebar.anotation.Letter;
import me.zhouzhuo.zzletterssidebar.entity.SortModel;

/**
 * Created by zz on 2016/8/15.
 */
public class ContactPersonEntity extends SortModel {

    @Letter(isSortField = true)
    private String personName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
