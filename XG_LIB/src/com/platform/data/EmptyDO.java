package com.platform.data;


import com.droideek.entry.data.Entry;

/**
 * Created by Droideek on 2016/1/4.
 */
public class EmptyDO extends MsgTO {
    public static final int TYPE_DEFAULT = 0x0;
    public static final int TYPE_COUPON= 0x1;
    public static final int TYPE_ADDRESS= 0x2;
    public static final int TYPE_CART= 0x3;
    public static final int TYPE_ORDER= 0x4;
    public static final int TYPE_ORDER_PAY= 0x5;
    public static final int TYPE_SHIPPING= 0x6;
    public static final int TYPE_SEARCH= 0x7;
    public static final int TYPE_SEARCH_RESULT= 0x8;
    public static final int TYPE_CS= 0x9;
    public static final int TYPE_BILL_QUERY= 0x10;
    public static final int TYPE_DISTRIBUTE_QUERY= 0x11;
    public static final int TYPE_SHOP= 0x12;
    public static final int TYPE_SHOP_CUSTOMER= 0x13;
    public static final int TYPE_XG_COIN= 0x14;
    public static final int TYPE_WITHDRAW_HISTORY= 0x15;
    public static final int TYPE_MY_GROUP= 0x16;
    public static final int TYPE_ERROR_INFO= 0x17;

    public static Entry mContent;

    public int type = TYPE_DEFAULT;

    public EmptyDO() {
        super();
    }

    public EmptyDO(int type) {
        super();
        this.type = type;
    }

    public EmptyDO(int type, String msg){
        super(msg);
        this.type = type;
    }

    public EmptyDO(int type, Entry data) {
        super();
        this.type = type;
        this.mContent = data;
    }

}
