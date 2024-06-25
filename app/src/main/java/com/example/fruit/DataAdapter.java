package com.example.fruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DataItem> dataList;
    private OnItemClickListener listener;

    public DataAdapter(Context context, ArrayList<DataItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem dataItem = dataList.get(position);
        holder.bind(dataItem);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberTextView;
        private TextView dateTextView;
        private TextView timeTextView; // 시간을 표시할 TextView 추가
        private TextView normalAppleTextView;
        private TextView rottenAppleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView); // 시간 TextView 연결
            normalAppleTextView = itemView.findViewById(R.id.normalAppleTextView);
            rottenAppleTextView = itemView.findViewById(R.id.rottenAppleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onClick(dataList.get(position));
                    }
                }
            });
        }

        public void bind(DataItem dataItem) {
            numberTextView.setText(String.valueOf(dataItem.getNumber()));
            String[] dateTimeParts = dataItem.getDate().split("\n"); // 날짜와 시간을 분리

            if (dateTimeParts.length >= 1) {
                dateTextView.setText(dateTimeParts[0]); // 첫 번째 부분은 날짜
            } else {
                dateTextView.setText(""); // 날짜 데이터가 없는 경우 비우기
            }

            if (dateTimeParts.length >= 2) {
                timeTextView.setText(dateTimeParts[1]); // 두 번째 부분은 시간
            } else {
                timeTextView.setText(""); // 시간 데이터가 없는 경우 비우기
            }

            // 파일 이름에 따라서 정상과 불량에 O를 표시하거나 비워둠
            if (dataItem.getFileName().startsWith("normal")) {
                normalAppleTextView.setText("정상");
                rottenAppleTextView.setText("");
            } else if (dataItem.getFileName().startsWith("rotten")) {
                normalAppleTextView.setText("");
                rottenAppleTextView.setText("불량");
            } else {
                // 이 외의 경우에는 둘 다 비워둠
                normalAppleTextView.setText("");
                rottenAppleTextView.setText("");
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(DataItem dataItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
