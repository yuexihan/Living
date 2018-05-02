package com.tencent.living;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tencent.living.Data.Comment;
import com.tencent.living.Data.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroundFragment extends Fragment {
    private ListView listView;
    private RecordsRefreshListManager refreshListManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.ground_frag_layout, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.id_swipe_ly);
        if (refreshListManager == null)
            refreshListManager = new RecordsRefreshListManager(swipeRefreshLayout, listView);
        return view;
    }
}
