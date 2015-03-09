package ua.test.apps.contactlist;

import java.util.ArrayList;
import java.util.List;

import com.itera.contactlist.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ContactListActivity extends Activity implements OnItemClickListener, View.OnClickListener{
	
	private ListView mContactList;
	private ImageButton mImageButton;
	
	private static final int REQUEST_CONTACT = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		mContactList = (ListView)findViewById(R.id.contact_list);
		mContactList.setAdapter(new ContactAdapter(this, getContacts()));
		mContactList.setOnItemClickListener(this);
		mImageButton = (ImageButton)findViewById(R.id.add_contact);
		mImageButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
		return true;
	}
	
	private class ContactAdapter extends ArrayAdapter<Holder>{
		
		private LayoutInflater mInflater;
		
		public ContactAdapter(Context context, List<Holder> contacts){
			super(context, 0);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			setNotifyOnChange(false);
			for(Holder h : contacts){
				add(h);
			}
			notifyDataSetChanged();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.contact_listitem, null);
			}
			TextView contactName = (TextView) convertView.findViewById(R.id.contact_name);
			TextView phoneNumber = (TextView) convertView.findViewById(R.id.phone_mobile);
			Holder h = getItem(position);
			contactName.setText(h.name);
			phoneNumber.setText(h.phoneNumber);
			return convertView;
		}

	}
	
	private class Holder{
		public String name;
		public String phoneNumber;
	}
	
	private List<Holder> getContacts() {
		List<Holder> result = new ArrayList<Holder>();
	    ContentResolver cr = getContentResolver();
	    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	    Holder h = null;
	    if (cur.getCount() > 0) {
	    while (cur.moveToNext()){
	    	h = new Holder();
	    	String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        String phone = null;
	        	if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	        		Cursor pCur = cr.query(
	        				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
	        					+ " = ?", new String[] { id }, null);
	        		while (pCur.moveToNext()) {
	        	    	 phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        		}
	        		h.phoneNumber = phone;
	        		pCur.close();
	        	} else{
	        		h.phoneNumber = "";
	        	}
	        	h.name = name;
	        	result.add(h);
	    	}
	    }
	    return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Holder h = (Holder)parent.getItemAtPosition(position);
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + h.phoneNumber));
		startActivity(intent);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.add_contact:
				Intent intent = new Intent(this, AddContactActivity.class);
				startActivityForResult(intent, REQUEST_CONTACT);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
       if(requestCode == REQUEST_CONTACT && resultCode == RESULT_OK){
    	   mContactList.setAdapter(new ContactAdapter(this, getContacts()));
       }
    } 

}
