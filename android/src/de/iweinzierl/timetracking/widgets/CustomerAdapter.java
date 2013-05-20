package de.iweinzierl.timetracking.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends BaseAdapter {

    public static final int RES_LAYOUT = R.layout.adapter_customer;

    private List<Customer> customers;
    private Context context;

    public CustomerAdapter(Context context) {
        this.context = context;
        customers = new ArrayList<Customer>();
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Object getItem(int position) {
        return position < getCount() ? customers.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(RES_LAYOUT, parent, false);
        setText(view, customers.get(position));

        return view;
    }

    public void setCustomers(List<Customer> customers) {
        if (customers != null) {
            this.customers = customers;
            notifyDataSetChanged();
        }
    }

    public void add(Customer customer) {
        if (customer != null) {
            customers.add(customer);
            notifyDataSetChanged();
        }
    }

    private TextView getTextView(View container, int resId) {
        View child = container.findViewById(resId);
        return child instanceof TextView ? (TextView) child : null;
    }

    private void setText(View container, Customer customer) {
        TextView tv = getTextView(container, R.id.text);
        if (tv != null) {
            tv.setText(customer.getName());
        }
    }
}
