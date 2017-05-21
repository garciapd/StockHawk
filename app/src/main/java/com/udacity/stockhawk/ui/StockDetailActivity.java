package com.udacity.stockhawk.ui;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by danielgarciaperez on 20/05/2017.
 *
 * Created with MPAndroidChart Time Line
 */

public class StockDetailActivity extends AppCompatActivity{

    @BindView(R.id.chart1)
    LineChart chart;

    @BindView(R.id.symbol)
    TextView symbol;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.change)
    TextView change;

    private final int QUERY_TOKEN = 13;
    private final String HISTORY_SEPARATOR = "\\r?\\n";
    private final String HISTORY_VALUE_SEPARATOR = ", ";
    private final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        ButterKnife.bind(this);

        chart.getDescription().setText(getString(R.string.stock_detail_chart_description));
        chart.getDescription().setTextColor(ContextCompat.getColor(this, R.color.white));
        chart.getLegend().setEnabled(false);
        chart.setBorderColor(ContextCompat.getColor(this, R.color.white));
        chart.setNoDataText(getString(R.string.stock_detail_no_data));
        chart.setNoDataTextColor(ContextCompat.getColor(this, R.color.white));
        chart.getXAxis().setTextColor(ContextCompat.getColor(this, R.color.white));
        chart.getAxisLeft().setTextColor(ContextCompat.getColor(this, R.color.white));
        chart.getAxisRight().setTextColor(ContextCompat.getColor(this, R.color.white));

        final Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            AsyncQueryHandler asyncQueryHandler =
                    new AsyncQueryHandler(getContentResolver()) {

                        @Override
                        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                            if (null != cursor && cursor.moveToFirst()) {
                                symbol.setText(cursor.getString(Contract.Quote.POSITION_SYMBOL));
                                price.setText(Formatter.dollarFormat().format(cursor.getFloat(Contract.Quote.POSITION_PRICE)));

                                float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                                float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

                                if (rawAbsoluteChange > 0) {
                                    change.setBackgroundResource(R.drawable.percent_change_pill_green);
                                } else {
                                    change.setBackgroundResource(R.drawable.percent_change_pill_red);
                                }

                                String changeString = Formatter.dollarFormatWithPlus().format(rawAbsoluteChange);
                                String percentage = Formatter.percentageFormat().format(percentageChange / 100);

                                if (PrefUtils.getDisplayMode(StockDetailActivity.this)
                                        .equals(getString(R.string.pref_display_mode_absolute_key))) {
                                    change.setText(changeString);
                                } else {
                                    change.setText(percentage);
                                }

                                setQuoteHistory(cursor.getString(Contract.Quote.POSITION_HISTORY));
                                chart.invalidate();
                                chart.setVisibility(View.VISIBLE);
                            } else {
                                chart.setVisibility(View.GONE);
                            }
                        }
                    };
            asyncQueryHandler.startQuery(QUERY_TOKEN, null, intent.getData(), null, null, null, null);
        }

    }

    private void setQuoteHistory(String history) {
        if(history == null || history.length()==0) return;
        List<String> historyPoints = Arrays.asList(history.split(HISTORY_SEPARATOR));
        if(historyPoints != null) {
            if (getResources().getConfiguration().getLayoutDirection() != View.LAYOUT_DIRECTION_RTL) {
                Collections.reverse(historyPoints);
            }

            final List<String> xAxisData = getXAxisData(historyPoints);
            final List<Entry> yAxisData = getYAxisData(historyPoints);
            final LineDataSet dataSet = new LineDataSet(yAxisData, null);
            final LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xAxisData.get((int) value);
                }
            });

        }
    }

    private List<Entry> getYAxisData(@NonNull final List<String> historyPoints) {
        final List<Entry> entries = new ArrayList<>();
        for(int i=0; i < historyPoints.size(); i++){
            String historyPoint = historyPoints.get(i);
            final String[] datePricePair = historyPoint.split(HISTORY_VALUE_SEPARATOR);
            float stock = Float.parseFloat(datePricePair[1]);
            entries.add(new Entry(i, stock));
        }
        return entries;
    }

    private List<String> getXAxisData(@NonNull final List<String> historyPoints) {
        final List<String> xAxisData = new ArrayList<>();
        for (final String historyPoint : historyPoints) {
            final long millisecons = Long.parseLong(historyPoint.split(HISTORY_VALUE_SEPARATOR)[0]);
            final Date date = new Date(millisecons);
            xAxisData.add(new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(date));
        }
        return xAxisData;
    }
}