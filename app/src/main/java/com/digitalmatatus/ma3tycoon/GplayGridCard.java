/*
package com.digitalmatatus.ma3tycoon;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import com.digitalmatatus.ma3tycoon.R;

*/
/**
 * Created by stephineosoro on 23/09/2016.
 *//*


public class GplayGridCard extends Card {


    protected String Name;
    protected String DispatchDate;
    protected String SealNo;
    protected String ProductType;
    protected String Truck;
    protected String TotalNo;
    protected Context cxt;
    protected String Action;
    protected String NameId;


    public GplayGridCard(Context context) {
        super(context, R.layout.inner_content_detail);
        cxt=context;

    }


    public GplayGridCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    protected void init() {

        CardHeader header = new CardHeader(getContext());
        header.setButtonOverflowVisible(true);
        header.setTitle(Name);
        header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                String selected = card.getId();

            }
        });

        addCardHeader(header);

          */
/*  OnCardClickListener clickListener = new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    //Do something
                }
            };

            addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW, clickListener);*//*

         */
/*   setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
//                    Do something
                    String selected= card.getId();
                    Toast.makeText(getBaseContext(), "Item ID is" + selected, Toast.LENGTH_LONG).show();
                   *//*
*/
/* Intent intent =new Intent(getBaseContext(),ProductDetail.class);
                    intent.putExtra("id",selected);
                    intent.putExtra("product_name",card.getTitle());
                    startActivity(intent);*//*
*/
/*
                }
            });*//*



    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.shipper);
        title.setText(Name);

        final TextView Dispatch = (TextView) view.findViewById(R.id.dispatch_date);
        Dispatch.setText(DispatchDate);

        final TextView subtitle = (TextView) view.findViewById(R.id.seal_no);
        subtitle.setText(SealNo);
        final TextView Pt = (TextView) view.findViewById(R.id.product_type);
        Pt.setText(ProductType);
        final TextView truck = (TextView) view.findViewById(R.id.truck);
        truck.setText(Truck);
        final TextView Tn = (TextView) view.findViewById(R.id.total_boxes);
        Tn.setText(TotalNo);
        final TextView action = (TextView) view.findViewById(R.id.action);

        */
/*action.setClickable(true);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Acceptance.class);
                intent.putExtra("seal_no",SealNo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",getCardHeader().getTitle());
                intent.putExtra("TransportId",getId());
                intent.putExtra("NameId",NameId);
                Log.e("transport id is ",getId());
                cxt.startActivity(intent);
            }
        });*//*




    }

}

*/
