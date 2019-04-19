package edu.sjsu.silversnugmobileapp.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import edu.sjsu.silversnugmobileapp.R;

/**
 * Created by ashwini on 3/14/19.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private ClickListener clickListener;
    private boolean justClick = false;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && !justClick) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
                if(justClick){
                    justClick =false;
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if(e.getAction() == MotionEvent.ACTION_UP ) {

            for (int numChildren = 0; numChildren <= rv.getChildCount(); numChildren++) {
                View _temp = rv.getChildAt(numChildren);
                if (_temp != null) {
                    Rect parentBounds = new Rect();
                    _temp.getHitRect(parentBounds);
                    if (parentBounds.contains((int) e.getX(), (int) e.getY())) {
                        View child1 = _temp.findViewById(R.id.editPill);
                        if (child1 instanceof ImageButton) {
                            Rect childBounds = new Rect();
                            child1.getHitRect(childBounds);
                            Context parent = child.getContext();
                                childBounds.top = childBounds.top + parentBounds.top;
                                childBounds.bottom = parentBounds.top + childBounds.bottom;
                            if (childBounds.contains((int) e.getX(), (int) e.getY())) {

                                if (parent instanceof UIListner) {
                                    ((UIListner) parent).editFunction(numChildren);
                                    justClick = true;
                                    return false;

                                }

                            }

                        }
                    }
                }
            }
        }
            if ( child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
                return true;
            }


        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
         gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e);
    }
}
